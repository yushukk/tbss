package org.erik.common.interceptor.interceptor;

import org.aopalliance.intercept.MethodInvocation;

/**
 * 通用拦截器接口
 * <p/>
 * Author: liyd
 * Date: 17-12-4 下午6:07
 * version $Id: LoginLogFacadeImplTest.java, v 0.1 Exp $
 */
public interface NormalInterceptor {

    /**
     * 执行拦截器业务
     *
     * @param invocation 拦截器参数
     * @return 方法执行结果
     */
    public Object proceed(MethodInvocation invocation);

}
