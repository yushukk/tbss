/**
 * Yolema.com Inc.
 * Copyright (c) 2011-2012 All Rights Reserved.
 */
package org.erik.common.result;

/**
 * tbss系统返回结果基础类
 * 
 * @author liyd
 * @version $Id: TbssResult.java, v 0.1 2012-5-16 下午2:19:34 liyd Exp $
 */
public class TbssResult extends GlobalResult {

    /** serialVersionUID */
    private static final long serialVersionUID = -5705823626887196334L;

    /**
     * 默认构造函数
     */
    public TbssResult() {
        super();
    }

    /**
     * 构造函数
     *
     * @param success 是否成功标识符
     */
    public TbssResult(boolean success) {
        super(success);
    }

    /**
     * 构造函数
     *
     * @param success 是否成功标识符
     * @param resultMsg 处理结果信息
     */
    public TbssResult(boolean success, String resultMsg) {
        super(success, resultMsg);
    }

}
