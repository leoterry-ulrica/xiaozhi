package com.dist.bdf.base.entity;

import com.dist.bdf.base.dto.BaseDTO;
import com.dist.bdf.base.utils.Convert2Date;

/**
 * 通用DTO
 * 
 * @author 何顺
 * @create 2014年12月2日
 */
public class GeneralDTO extends BaseDTO {


    /**
     * 创建日期
     */
    @Convert2Date
    private String createDate;

    /**
     * 修改日期
     */
    @Convert2Date
    private String modifyDate;

    /**
        * 创建者名字
        */
    private String creatorName;
    /**
     * 修改者名字
     */
    private String modifierName;

	/**
	 * 用户的ID
	 */
	private String userId;
	/**
	 * 用户的姓名
	 */
	private String userName;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCreateDate() {
		return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getModifierName() {
        return modifierName;
    }

    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
    }
    
    

}
