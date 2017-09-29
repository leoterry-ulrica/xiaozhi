package com.dist.bdf.base.exception;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dist.bdf.base.result.Result;
import com.dist.bdf.base.utils.ExceptionUtil;

/**
 * 该拦截器用于对控制层的异常进行拦截捕获，并且将异常信息封装放入model或者与原有的返回值进行拼接.
 *
 * <p>
 * 大大的减少用户在controller手工进行异常捕获的工作量。
 * 如果该捕获方式以及信息的处理方式不符合用户的需求，用户依然可以自己在controller进行捕获，不会造成影响。
 *
 * @author ShenYuTing
 * @version 1.1, 2013-12-2
 * @version 1.2, 2015.12.1 修复判断条件的错误：本意是Controller使用@HandleException时需要处理，但是逻辑上却是不使用时进行处理。去掉了对@HandleException的判断，即针对所有使用了@ResponseBody注解的方法
 */
public class ControllerExceptionHandlerInterceptor implements MethodInterceptor {
	protected final Logger logger = (Logger) LoggerFactory.getLogger(getClass());

	/**
	 * 对于拦截的方法进行异常处理。 <br>
	 * 若该方法包含@ResponseBody并且不含WithoutCatch注解,则进行处理。
	 *
	 * @param mi 通过该参数可以获取到被拦截的方法的各种参数及值
	 * @return 根据异常类型返回处理执行结果（由Result构造的json串）,当异常为BusinessException时，表示业务逻辑错误，返回的status="fail"，否则返回的status="error"，表示系统异常或者代码错误。
	 */
	public Object invoke(MethodInvocation mi) throws Throwable {
		//只有方法的注解包含了@ResponseBody时才进行处理
		if (mi.getMethod().isAnnotationPresent(ResponseBody.class)) {
			try {
				// 调用被拦截的方法，如果该方法抛出异常，则会被catch
				return mi.proceed();
			} catch (BusinessException e) {
				return new Result(Result.FAIL, e.getMessage()).toJsonString();
			} catch (Exception e) {
				logger.error(ExceptionUtil.getFullMessage(e));
				return new Result(Result.ERROR, e.getMessage()).toJsonString();
			}
		}
		return mi.proceed();
	}

}
