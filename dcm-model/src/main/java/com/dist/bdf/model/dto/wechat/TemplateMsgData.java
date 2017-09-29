package com.dist.bdf.model.dto.wechat;

import java.io.Serializable;
/**
 * 模板消息数据
 * @author weifj
 *
 */
public class TemplateMsgData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 值
	 */
	private String value;  
	/**
	 * 颜色码
	 */
    private String color;  
  
    public String getValue() {  
        return value;  
    }  
  
    public void setValue(String value) {  
        this.value = value;  
    }  
  
    public String getColor() {  
        return color;  
    }  
  
    public void setColor(String color) {  
        this.color = color;  
    }

	@Override
	public String toString() {
		return "TemplateMsgData [value=" + value + ", color=" + color + "]";
	}  
    
}
