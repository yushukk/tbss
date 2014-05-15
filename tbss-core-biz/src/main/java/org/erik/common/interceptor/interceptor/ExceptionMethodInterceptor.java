/**
 * Yolema.com Inc.
 * Copyright (c) 2011-2012 All Rights Reserved.
 */
package org.erik.common.interceptor.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.erik.common.TbssException;
import org.erik.common.result.GenericsResult;
import org.erik.common.result.TbssResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 
 * @author liyd
 * @version $Id: ExceptionMethodInterceptor.java, v 0.1 2012-8-10 上午10:00:32 liyd Exp $
 */
public class ExceptionMethodInterceptor implements MethodInterceptor {

    /** 日志对象 */
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionMethodInterceptor.class);

    /** 事务模板 */
    private TransactionTemplate transactionTemplate;

    /** 
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    public Object invoke(final MethodInvocation invocation) throws Throwable {

        return transactionTemplate.execute(new TransactionCallback<Object>() {

            public Object doInTransaction(TransactionStatus status) {

                //被拦截的类
                String targetClass = invocation.getThis().getClass().getName();

                //被拦截方法
                String targetMethod = invocation.getMethod().getName();

                //当前时间毫秒数
                Long now = System.currentTimeMillis();

                LOG.info("开始执行业务层方法[class=" + targetClass + ",method=" + targetMethod + "]");

                Object result = null;

                try {
                    //此处调用业务方法
                    result = invocation.proceed();

                } catch (TbssException tbssException) {

                    status.setRollbackOnly();

                    //该异常为已知的处理异常，因此只输出简化处理过的结果码和结果信息
                    LOG.warn("出现已知异常，异常信息[resultCode=" + tbssException.getResultCode()
                             + ",resultMsg=" + tbssException.getResultMsg() + "]");

                    //设置返回结果
                    result = getResult(invocation, tbssException);

                } catch (Throwable e) {
                    status.setRollbackOnly();
                    LOG.error("发现未知异常，异常信息", e);

                    //设置返回结果
                    result = getResult(invocation, null);
                } finally {

                    LOG.info("业务层方法执行完毕[class=" + targetClass + ",method=" + targetMethod + "]，耗时["
                             + (System.currentTimeMillis() - now) + "ms]");
                }

                return result;
            }

        });

    }

    /**
     * 设置并返回业务方法的返回结果
     * 
     * @param invocation spring拦截业务对象
     * @param tbssException 已知异常信息
     * @return
     */
    @SuppressWarnings("rawtypes")
    private Object getResult(MethodInvocation invocation, TbssException tbssException) {

        try {

            if (tbssException == null) {
                tbssException = new TbssException();
            }
            //方法返回类型
            Class<?> returnClass = invocation.getMethod().getReturnType();

            if (TbssResult.class.equals(returnClass) || GenericsResult.class.equals(returnClass)) {
                GenericsResult result = new GenericsResult(false);
                result.setResultCode(tbssException.getResultCode());
                result.setResultMsg(tbssException.getResultMsg());
                return result;
            }
            if ("void".equals(returnClass.getName())) {
                return null;
            } else {

                //初始化一个业务方法的返回类型
                Object obj = returnClass.newInstance();

                //如果是指定的业务返回类型，设置返回结果，否则不处理
                if (obj instanceof TbssResult) {
                    ((TbssResult) obj).setSuccess(false);
                    ((TbssResult) obj).setResultCode(tbssException.getResultCode());
                    ((TbssResult) obj).setResultMsg(tbssException.getResultMsg());
                    return obj;
                }
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

    /**
     * Setter method for property <tt>transactionTemplate</tt>.
     * 
     * @param transactionTemplate value to be assigned to property transactionTemplate
     */
    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

}
