package org.erik.common.interceptor.interceptor;

import org.aopalliance.intercept.MethodInvocation;
import org.erik.common.GlobalException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 事务拦截器
 * <p/>
 * Author: liyd
 * Date: 17-12-4 下午7:38
 * version $Id: LoginLogFacadeImplTest.java, v 0.1 Exp $
 */
public class TransactionInterceptor implements NormalInterceptor {

    /** 事务模板 */
    private TransactionTemplate transactionTemplate;

    /**
     * 执行拦截器业务
     *
     * @param invocation 拦截器参数
     * @return 方法执行结果
     */
    @Override
    public Object proceed(final MethodInvocation invocation) {

        return transactionTemplate.execute(new TransactionCallback<Object>() {

            public Object doInTransaction(TransactionStatus status) {

                Object result = null;

                try {
                    //此处调用业务方法
                    result = invocation.proceed();

                } catch (GlobalException globalException) {
                    //回滚事务
                    status.setRollbackOnly();
                    throw globalException;

                } catch (Throwable e) {
                    //回滚事务
                    status.setRollbackOnly();
                    throw new RuntimeException(e);
                }

                return result;
            }
        });
    }

    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }
}
