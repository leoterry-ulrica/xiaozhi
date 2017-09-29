package com.dist.bdf.model.dto.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 类描述：    用户标识需要记录的方法节点，将会记录该方法的执行时间，调用者，访问IP  <br />
 * 使用该注解时，需要说明该方法节点的名称。
 * @创建人：沈宇汀   
 * @创建时间：2014-12-31 上午9:26:30
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationPoint {
	String name() default "";
}
