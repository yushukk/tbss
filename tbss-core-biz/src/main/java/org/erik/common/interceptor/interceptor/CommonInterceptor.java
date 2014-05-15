package org.erik.common.interceptor.interceptor;

import org.aopalliance.intercept.MethodInvocation;
import org.erik.common.GlobalException;

/**
 * 普通拦截器
 * <p/>
 * Author: liyd
 * Date: 17-12-4 下午8:24
 * version $Id: LoginLogFacadeImplTest.java, v 0.1 Exp $
 */
public class CommonInterceptor implements NormalInterceptor {

    /**
     * 执行拦截器业务
     *
     * @param invocation 拦截器参数
     * @return 方法执行结果
     */
    @Override
    public Object proceed(MethodInvocation invocation) {
        try {

            return invocation.proceed();
        } catch (GlobalException exception) {
            throw exception;
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}
