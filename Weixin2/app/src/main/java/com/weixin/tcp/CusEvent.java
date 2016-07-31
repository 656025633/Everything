package com.weixin.tcp;

import java.util.EventObject;

/**
 * Created by zhao on 2016/7/25 0025.
 */
public class CusEvent extends EventObject {
    private static final long serialVersionUID = 1L;
    private Object source;//事件源

    public CusEvent(Object source){
        super(source);
        this.source = source;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }
}