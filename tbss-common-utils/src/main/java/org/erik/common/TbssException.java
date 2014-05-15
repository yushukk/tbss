/**
 * Yolema.com Inc.
 * Copyright (c) 2011-2012 All Rights Reserved.
 */
package org.erik.common;

/**
 * tbss系统自定义exception
 * 
 * @author liyd
 * @version $Id: TbssException.java, v 0.1 2012-4-6 上午11:30:46 liyd Exp $
 */
public class TbssException extends GlobalException {

    /** serialVersionUID */
    private static final long serialVersionUID = 1068543011536534296L;

    /**
     * 创建一个<code>TbssException</code>对象
     */
    public TbssException() {
        super();
    }

    /**
     * 创建一个<code>TbssException</code>对象
     * 
     * @param resultMsg   异常结果码
     */
    public TbssException(String resultMsg) {
        super(resultMsg);
    }

    /**
     * 创建一个<code>TbssException</code>对象
     * 
     * @param resultCode     异常结果码  
     * @param resultMsg      异常结果信息
     */
    public TbssException(String resultCode, String resultMsg) {
        super(resultCode, resultMsg);
    }

    /**
     * 创建一个<code>TbssException</code>
     * 
     * @param cause      异常原因
     */
    public TbssException(Throwable cause) {
        super(cause);
    }

}
