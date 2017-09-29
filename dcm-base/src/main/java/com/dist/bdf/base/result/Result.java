package com.dist.bdf.base.result;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.dist.bdf.base.utils.JSONUtil;

import net.sf.json.util.CycleDetectionStrategy;



/**
 * 通用业务信息传递机制类，定义了业务处理后的结果状态以及用来传递数据的对象
 * @author heshun
 * @version V1.0
 * @created 2014-08-13
 */
/*@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)*/

public class Result implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 业务状态：成功
     */
    public static final String SUCCESS = "success";
    /**
     * 业务状态：失败 因为业务逻辑错误导致操作失败，如邮箱已存在，年龄不满足条件等。
     */
    public static final String FAIL = "fail";
    /**
     * 业务状态：错误 因为一些不可预计的、系统级的错误导致的操作失败，如数据库断电，服务器内存溢出等。
     */
    public static final String ERROR = "error";

    /**
     * 业务状态，若业务状态是成功/失败/错误，则采用ReturnValue.SUCCESS、FAILED、ERROR。若非这3种状态，请自行定义。
     */
    private String status;
    /**
     * 业务信息，包含从业务异常中提取出的业务错误信息，也可以是自定义的业务信息，以及业务对象、业务对象集合等。
     */
    private Object data;
    /**
     * 消息，可以用于存放提示内容
     */
    private String message;
    /**
     * 状态编码
     */
    private Integer code;

    /**
     * 公用无参的构造器
     */
    public Result() {
        super();
    }

    /**
     * 公用带Object参数的构造器
     * 
     * @param data
     *            业务信息
     */
    public Result(Object data) {
        super();
        this.data = data;
    }

    /**
     * 公用带status参数的构造器
     * 
     * @param status
     *            业务状态
     */
    public Result(String status) {
        super();
        this.status = status;
    }

    /**
     * 公用带参构造器
     * 
     * @param status
     *            业务状态
     * @param data
     *            业务信息
     */
    public Result(String status, Object data) {
        super();
        this.status = status;
        this.data = data;
    }

    public Result(String status, Object data, String message) {
        super();
        this.status = status;
        this.data = data;
        this.message = message;
    }
    public Result(String status, Object data, String message, Integer code) {
        super();
        this.status = status;
        this.data = data;
        this.message = message;
        this.code = code;
    }
    /**
     * 将业务状态和业务信息转换成JSON字符串，业务状态的key为：status，业务信息的key为：data
     * <p>
     * 该字符串返回客户端后采用
     * eval()或其他方法将该字符串转换成JSON对象，采用该JSON对象.status、JSON对象.data即可获取业务状态和业务信息。
     * 
     * @return 封装好的JSON字符串
     */
    public String toJsonString() {
        return JSONUtil.toJSONStringExcludeLazy(this);
    }
    /**
     * 将业务状态和业务信息转换成JSON字符串，业务状态的key为：status，业务信息的key为：data
     * <p>
     * 该字符串返回客户端后采用
     * eval()或其他方法将该字符串转换成JSON对象，采用该JSON对象.status、JSON对象.data即可获取业务状态和业务信息。
     * @param cycleDetectionStrategy 转换成json的级别设置
     * @return 封装好的JSON字符串
     */
    public String toJsonString(CycleDetectionStrategy cycleDetectionStrategy) {
        return JSONUtil.toJSONString(this, cycleDetectionStrategy);
    }
    
    /**
     * 获取业务状态<br/>
     * 如果状态为系统定义的成功/失败/错误，则直接调用ReturnValue.SUCCESS、FAILED、ERROR得到状态值。<br/>
     * 如果状态为用户传入的自定义值，则直接返回该值<br/>
     * 
     * @return 业务状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置业务状态<br/>
     * 可传入系统定义的三种状态值(成功/失败/错误),调用方式：ReturnValue.SUCCESS、FAILED、ERROR<br/>
     * 也可自定义状态值<br/>
     * 
     * @param status
     *            业务状态
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取业务信息,业务信息类型：<br>
     * 1.从业务异常中提取出的业务错误信息。<br>
     * 2.自定义的业务信息。<br>
     * 3.业务对象、业务对象集合。<br>
     * 
     * @return 业务信息
     */
    public Object getData() {
        return data;
    }

    /**
     * 设置业务信息,业务信息类型：<br>
     * 1.从业务异常中提取出的业务错误信息。<br>
     * 2.自定义的业务信息。<br>
     * 3.业务对象、业务对象集合。<br>
     * 
     * @param data
     *            业务信息
     */
    public void setData(Object data) {
        this.data = data;
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 获取 #{bare_field_comment} 
	 * @return the code
	 */
	public Integer getCode() {
		return code;
	}

	/**
	 * 设置 #{bare_field_comment}
	 * @param code the code to set
	 */
	public void setCode(Integer code) {
		this.code = code;
	}

}
