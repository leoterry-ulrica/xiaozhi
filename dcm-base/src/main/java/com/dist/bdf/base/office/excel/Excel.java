package com.dist.bdf.base.office.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Goofy
 * Excel注解，用以生成Excel表格文件
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.TYPE})
public @interface Excel {

	/**
	 * 列名
	 * @return
	 */
    String name() default "";
     
    /**
     * 宽度
     * @return
     */
    int width() default 20;
 
    /**
     * 忽略该字段
     * @return
     */
    boolean skip() default false;
    
    /**
     * 日期格式
     * @return
     */
    String dateFormat() default "yyyy-MM-dd HH:mm:ss";
}
