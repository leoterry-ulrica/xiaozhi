package com.dist.bdf.model.dto.system.user;

/**
 * 用户dto
 * @author weifj
 * @version 1.0，2016/05/06，创建简单用户DTO
 */
public class UserSimpleDTO extends BaseUserDTO{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 头像
     */
    protected String avatar;

    /**
     * 性别
     */
    protected String sex;

	
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
}
