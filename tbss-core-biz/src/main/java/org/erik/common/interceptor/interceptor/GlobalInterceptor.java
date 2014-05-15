package org.erik.common.interceptor.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.erik.common.GlobalException;
import org.erik.common.result.GenericsResult;
import org.erik.common.result.GlobalResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统全局拦截器
 * <p/>
 * Author: liyd
 * Date: 17-12-4 下午5:12
 * version $Id: LoginLogFacadeImplTest.java, v 0.1 Exp $
 */
public class GlobalInterceptor implements MethodInterceptor {

    /** 日志对象 */
    private static final Logger            LOG                 = LoggerFactory
                                                                   .getLogger(GlobalInterceptor.class);

    /** 默认拦截器key */
    private static final String            DEFAULT_INTERCEPTOR = "default_interceptor";

    /** 拦截器工厂 */
    private Map<String, NormalInterceptor> interceptorFactory  = new HashMap<String, NormalInterceptor>();

    /**
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    public Object invoke(final MethodInvocation invocation) throws Throwable {

        //被拦截的类
        String targetClass = invocation.getThis().getClass().getName();

        //被拦截方法
        String targetMethod = invocation.getMethod().getName();

        //当前时间毫秒数
        Long now = System.currentTimeMillis();

        LOG.info("开始执行业务层方法[class={},method={}]", targetClass, targetMethod);

        //返回结果
        Object result = null;

        //获取拦截器
        NormalInterceptor interceptor = getInterceptor(targetMethod);

        try {
            //此处调用业务方法
            result = interceptor.proceed(invocation);

        } catch (GlobalException globalException) {

            //该异常为已知的处理异常，因此只输出简化处理过的结果码和结果信息
            LOG.warn("出现已知异常，异常信息[resultCode={},resultMsg={}]", globalException.getResultCode(),
                globalException.getResultMsg());

            //设置返回结果
            result = getResult(invocation, globalException);

        } catch (Throwable e) {

            LOG.error("发现未知异常，异常信息", e);

            //设置返回结果
            result = getResult(invocation, null);
        }

        LOG.info("业务层方法执行完毕[class={},method={}]，耗时[{}ms]", new Object[] { targetClass,
                targetMethod, System.currentTimeMillis() - now });

        return result;
    }

    /**
     * 根据方法获取拦截器
     *
     * @param methodName 方法名
     * @return 拦截器
     */
    private NormalInterceptor getInterceptor(String methodName) {

        //根据方法名获取拦截器
        for (Map.Entry<String, NormalInterceptor> entry : interceptorFactory.entrySet()) {

            String regex = StringUtils.replace(entry.getKey(), "*", ".*");

            //方法名是否以指定名称开头
            if (methodName.matches(regex)) {

                //获取拦截器
                return entry.getValue();
            }
        }
        return interceptorFactory.get(DEFAULT_INTERCEPTOR);
    }

    /**
     * 设置并返回业务方法的返回结果
     *
     * @param invocation spring拦截业务对象
     * @param globalException 已知异常信息
     * @return 返回结果
     */
    private Object getResult(MethodInvocation invocation, GlobalException globalException) {

        try {

            if (globalException == null) {
                globalException = new GlobalException();
            }
            //方法返回类型
            Class<?> returnClass = invocation.getMethod().getReturnType();

            //无返回值
            if (StringUtils.equals("void", returnClass.getName())) {

                return null;
            }

            //预订规范的结果
            if (GlobalResult.class.equals(returnClass) || GenericsResult.class.equals(returnClass)) {
                GenericsResult<Object> result = new GenericsResult<Object>(false);
                result.setResultCode(globalException.getResultCode());
                result.setResultMsg(globalException.getResultMsg());
                return result;
            }

            //初始化一个业务方法的返回类型
            Object obj = returnClass.newInstance();

            //如果是继承了规范的返回结果类，设置返回结果，否则不处理
            if (obj instanceof GlobalResult) {
                ((GlobalResult) obj).setSuccess(false);
                ((GlobalResult) obj).setResultCode(globalException.getResultCode());
                ((GlobalResult) obj).setResultMsg(globalException.getResultMsg());
                return obj;
            }

        } catch (InstantiationException e) {
            LOG.error("初始化业务方法返回值失败", e);
        } catch (IllegalAccessException e) {
            LOG.error("利用反射初始化业务方法返回值失败", e);
        } catch (Exception e) {
            LOG.error("业务异常拦截器出现错误", e);
        }

        return null;
    }

    public void setInterceptorFactory(Map<String, NormalInterceptor> interceptorFactory) {
        this.interceptorFactory = interceptorFactory;
    }
}
