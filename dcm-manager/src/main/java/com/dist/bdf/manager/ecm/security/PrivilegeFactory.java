
package com.dist.bdf.manager.ecm.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.HashMap;

import com.filenet.api.constants.AccessRight;

/**
 * 权限类型
 * @author weifj
 * @version 1.0，2016/02/29，weifj，创建权限类型字典
 * @version 1.1，2016/03/04，weifj，整理扩展权限和CE本身支持的权限
 * @version 1.2，2016/03/05，weifj，添加方法：getCodes，获取权限编码
 * @version 1.3,2016/04/28，weifj，更改类名为：PrivilegeFactory，权限工厂类
 * 
 * （READ | READ_ACL | VIEW_CONTENT）只读是只可以阅览（不能下载和上传新版本）
 * （Priv_Download）下载是可以阅览和下载（不能上传新版本（MAJOR_VERSION））
 * （MAJOR_VERSION）编辑是可以预览（READ | READ_ACL | VIEW_CONTENT）、下载（Priv_Download）和上传（MAJOR_VERSION）
 * 
 */
public enum PrivilegeFactory {

	Priv_UI_Enabled("Priv_UI_Enabled", 0x100000000L, "界面可用"),
	Priv_UI_Disabled("Priv_UI_Disabled", 0x200000000L, "界面不可用"),
	Priv_Search("Priv_Search", 0x400000000L, "搜索"),
	Priv_Download("Priv_Download", 0x800000000L, "下载"),
	Priv_Print("Priv_Print", 0x1000000000L, "打印"),
	Priv_Share("Priv_Share", 0x2000000000L, "共享"),
	Priv_Remove("Priv_Remove", 0x4000000000L, "逻辑删除"),
	Priv_MemberAssignment("Priv_MemberAssignment", 0x8000000000L, "人员管理"),
	Priv_ProjectManagement("Priv_ProjectManagement", 0x10000000000L, "项目管理"),
	Priv_ProjectWork("Priv_ProjectWork", 0x20000000000L, "项目工作"),
	Priv_Forward("Priv_Forward", 0x40000000000L, "前移"),
	Priv_Backward("Priv_Backward", 0x80000000000L, "前移"),
	Priv_HigherLevel("Priv_HigherLevel", 0x100000000000L, "上级"),
	Priv_LowerLevel("Priv_LowerLevel", 0x200000000000L, "下级"),
	Priv_SameLevel("Priv_SameLevel", 0x400000000000L, "同级"),
	Priv_Move("Priv_Move", 0x800000000000L, "移动"),
	Priv_Upload("Priv_Upload", 0x1000000000000L, "上传"),
	Priv_Statistics("Priv_Statistics", 0x2000000000000L, "统计"),
	Priv_Rename("Priv_Rename", 0x4000000000000L, "重命名"),
	Priv_None("Priv_None",  Long.valueOf(AccessRight.NONE_AS_INT), "空权限"),
	
	// 济南扩展权限
	PRIV_EDIT_INS("PRIV_EDIT_INS", null, "编辑企业信息"),
	PRIV_MGNT_ORG("PRIV_MGNT_ORG", null, "管理组织机构"),
	PRIV_MGNT_USER("PRIV_MGNT_USER", null, "管理用户信息"),
	PRIV_GRANT_USER("PRIV_GRANT_USER", null, "分配用户权限"),
	PRIV_CREATE_PRJ("PRIV_CREATE_PRJ", null, "创建项目"),
	PRIV_TASK_SUMMARY("PRIV_TASK_SUMMARY", null, "查看任务汇总"),
	PRIV_VIEW_PRJMIN("PRIV_VIEW_PRJMIN", null, "查看项目基本信息"),
	PRIV_HOT("PRIV_HOT", null, "设置热门项目"),
	PRIV_CREATE_TEAM("PRIV_CREATE_TEAM", null, "创建团队"),
	PRIV_PUBLISH_TASK("PRIV_PUBLISH_TASK", null, "发布任务"),
	PRIV_EDIT_TASK("PRIV_EDIT_TASK", null, "编辑任务"),
	PRIV_DELETE_TASK("PRIV_DELETE_TASK", null, "删除任务"),
	PRIV_JOIN_TASK("PRIV_JOIN_TASK", null, "参与任务");


	/**
	 * 权限编码
	 */
	private String code;
	/**
	 * 权限值
	 */
	private Long mask;
	/**
	 * 描述
	 */
	private String desc;
	
	private PrivilegeFactory(String code, Long mask, String desc){
		this.code = code;
		this.mask = mask;
		this.desc = desc;
	}
	/**
	 * 权限编码与值的映射
	 */
	private static Map<String, Long> PrivcodeToMaskMap = new HashMap<String, Long>(){
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		{
			put(PrivilegeFactory.Priv_UI_Enabled.code, PrivilegeFactory.Priv_UI_Enabled.mask);
			put(PrivilegeFactory.Priv_UI_Disabled.code, PrivilegeFactory.Priv_UI_Disabled.mask);
			put(PrivilegeFactory.Priv_Search.code, PrivilegeFactory.Priv_Search.mask);
			put(PrivilegeFactory.Priv_Download.code, PrivilegeFactory.Priv_Download.mask);
			put(PrivilegeFactory.Priv_Print.code, PrivilegeFactory.Priv_Print.mask);
			put(PrivilegeFactory.Priv_Share.code, PrivilegeFactory.Priv_Share.mask);
			put(PrivilegeFactory.Priv_Remove.code, PrivilegeFactory.Priv_Remove.mask);
			put(PrivilegeFactory.Priv_MemberAssignment.code, PrivilegeFactory.Priv_MemberAssignment.mask);
			put(PrivilegeFactory.Priv_ProjectManagement.code, PrivilegeFactory.Priv_ProjectManagement.mask);
			put(PrivilegeFactory.Priv_ProjectWork.code, PrivilegeFactory.Priv_ProjectWork.mask);

			put("READ", (long) AccessRight.READ_AS_INT | (long) AccessRight.READ_ACL_AS_INT | (long) AccessRight.VIEW_CONTENT_AS_INT);
			put("WRITE", (long) AccessRight.WRITE_AS_INT);
			put("RESERVED12", (long) AccessRight.RESERVED12_AS_INT);
			put("RESERVED13", (long) AccessRight.RESERVED13_AS_INT);
			put("VIEW_CONTENT", (long) AccessRight.VIEW_CONTENT_AS_INT | (long) AccessRight.READ_AS_INT | (long) AccessRight.READ_ACL_AS_INT );
			put("LINK", (long) AccessRight.LINK_AS_INT);
			put("PUBLISH", (long) AccessRight.PUBLISH_AS_INT);
			put("CREATE_INSTANCE", (long) AccessRight.CREATE_INSTANCE_AS_INT);
			put("CHANGE_STATE", (long) AccessRight.CHANGE_STATE_AS_INT);
			put("MINOR_VERSION", (long) AccessRight.MINOR_VERSION_AS_INT);
			put("MAJOR_VERSION", (long) AccessRight.MAJOR_VERSION_AS_INT | (long) AccessRight.READ_AS_INT | (long) AccessRight.READ_ACL_AS_INT | (long) AccessRight.VIEW_CONTENT_AS_INT | PrivilegeFactory.Priv_Download.mask);
			put("DELETE", (long) AccessRight.DELETE_AS_INT);
			put("READ_ACL", (long) AccessRight.READ_ACL_AS_INT | (long) AccessRight.READ_AS_INT | (long) AccessRight.VIEW_CONTENT_AS_INT);
			put("WRITE_ACL", (long) AccessRight.WRITE_ACL_AS_INT);
			put("WRITE_OWNER", (long) AccessRight.WRITE_OWNER_AS_INT);
			put("UNLINK", (long) AccessRight.UNLINK_AS_INT);
			put("CREATE_CHILD", (long) AccessRight.CREATE_CHILD_AS_INT);
			
			put(PrivilegeFactory.Priv_UI_Disabled.code, PrivilegeFactory.Priv_UI_Disabled.mask);
			put(PrivilegeFactory.Priv_UI_Enabled.code, PrivilegeFactory.Priv_UI_Enabled.mask);
			put(PrivilegeFactory.Priv_Download.code, PrivilegeFactory.Priv_Download.mask);
			put(PrivilegeFactory.Priv_Print.code, PrivilegeFactory.Priv_Print.mask);
			put(PrivilegeFactory.Priv_Search.code, PrivilegeFactory.Priv_Search.mask);
			put(PrivilegeFactory.Priv_Remove.code, PrivilegeFactory.Priv_Remove.mask);
			put(PrivilegeFactory.Priv_MemberAssignment.code, PrivilegeFactory.Priv_MemberAssignment.mask);
			put(PrivilegeFactory.Priv_Share.code, PrivilegeFactory.Priv_Share.mask);
			put(PrivilegeFactory.Priv_ProjectManagement.code, PrivilegeFactory.Priv_ProjectManagement.mask);
			put(PrivilegeFactory.Priv_ProjectWork.code, PrivilegeFactory.Priv_ProjectWork.mask);
			
			put(PrivilegeFactory.Priv_Forward.code, PrivilegeFactory.Priv_Forward.mask);
			put(PrivilegeFactory.Priv_Backward.code, PrivilegeFactory.Priv_Backward.mask);
			put(PrivilegeFactory.Priv_HigherLevel.code, PrivilegeFactory.Priv_HigherLevel.mask);
			put(PrivilegeFactory.Priv_LowerLevel.code, PrivilegeFactory.Priv_LowerLevel.mask);
			put(PrivilegeFactory.Priv_SameLevel.code, PrivilegeFactory.Priv_SameLevel.mask);
			put(PrivilegeFactory.Priv_Move.code, PrivilegeFactory.Priv_Move.mask);
			put(PrivilegeFactory.Priv_Upload.code, PrivilegeFactory.Priv_Upload.mask);
			put(PrivilegeFactory.Priv_Statistics.code, PrivilegeFactory.Priv_Statistics.mask);
			put(PrivilegeFactory.Priv_Rename.code, PrivilegeFactory.Priv_Rename.mask);
		}
	};
	
	/**
	 * 权限值与编码映射
	 */
    private static Map<Long, String> PrivmaskToCodeMap = new HashMap<Long, String>(){
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put(PrivilegeFactory.Priv_UI_Enabled.mask, PrivilegeFactory.Priv_UI_Enabled.code);
			put(PrivilegeFactory.Priv_UI_Disabled.mask, PrivilegeFactory.Priv_UI_Disabled.code);
			put(PrivilegeFactory.Priv_Search.mask, PrivilegeFactory.Priv_Search.code);
			put(PrivilegeFactory.Priv_Download.mask, PrivilegeFactory.Priv_Download.code);
			put(PrivilegeFactory.Priv_Print.mask, PrivilegeFactory.Priv_Print.code);
			put(PrivilegeFactory.Priv_Share.mask, PrivilegeFactory.Priv_Share.code);
			put(PrivilegeFactory.Priv_Remove.mask, PrivilegeFactory.Priv_Remove.code);
			put(PrivilegeFactory.Priv_MemberAssignment.mask, PrivilegeFactory.Priv_MemberAssignment.code);
			put(PrivilegeFactory.Priv_ProjectManagement.mask, PrivilegeFactory.Priv_ProjectManagement.code);
			put(PrivilegeFactory.Priv_ProjectWork.mask, PrivilegeFactory.Priv_ProjectWork.code);

			put((long) AccessRight.READ_AS_INT, "READ");
			put((long) AccessRight.WRITE_AS_INT, "WRITE_");
			put((long) AccessRight.RESERVED12_AS_INT, "RESERVED12");
			put((long) AccessRight.RESERVED13_AS_INT, "RESERVED13");
			put((long) AccessRight.VIEW_CONTENT_AS_INT, "VIEW_CONTENT");
			put((long) AccessRight.LINK_AS_INT, "LINK");
			put((long) AccessRight.PUBLISH_AS_INT, "PUBLISH");
			put((long) AccessRight.CREATE_INSTANCE_AS_INT, "CREATE_INSTANCE");
			put((long) AccessRight.CHANGE_STATE_AS_INT, "CHANGE_STATE");
			put((long) AccessRight.MINOR_VERSION_AS_INT, "MINOR_VERSION");
			put((long) AccessRight.MAJOR_VERSION_AS_INT, "MAJOR_VERSION");
			put((long) AccessRight.DELETE_AS_INT, "DELETE");
			put((long) AccessRight.READ_ACL_AS_INT, "READ_ACL");
			put((long) AccessRight.WRITE_ACL_AS_INT, "WRITE_ACL");
			put((long) AccessRight.WRITE_OWNER_AS_INT, "WRITE_OWNER");
			put((long) AccessRight.UNLINK_AS_INT, "UNLINK");
			put((long) AccessRight.CREATE_CHILD_AS_INT, "CREATE_CHILD");
	
			put(PrivilegeFactory.Priv_UI_Disabled.mask, PrivilegeFactory.Priv_UI_Disabled.code);
			put(PrivilegeFactory.Priv_UI_Enabled.mask, PrivilegeFactory.Priv_UI_Enabled.code);
			put(PrivilegeFactory.Priv_Download.mask, PrivilegeFactory.Priv_Download.code);
			put(PrivilegeFactory.Priv_Print.mask, PrivilegeFactory.Priv_Print.code);
			put(PrivilegeFactory.Priv_Search.mask, PrivilegeFactory.Priv_Search.code);
			put(PrivilegeFactory.Priv_Remove.mask, PrivilegeFactory.Priv_Remove.code);
			put(PrivilegeFactory.Priv_MemberAssignment.mask, PrivilegeFactory.Priv_MemberAssignment.code);
			put(PrivilegeFactory.Priv_Share.mask, PrivilegeFactory.Priv_Share.code);
			put(PrivilegeFactory.Priv_ProjectManagement.mask, PrivilegeFactory.Priv_ProjectManagement.code);
			put(PrivilegeFactory.Priv_ProjectWork.mask, PrivilegeFactory.Priv_ProjectWork.code);
			put(PrivilegeFactory.Priv_Forward.mask, PrivilegeFactory.Priv_Forward.code);
			put(PrivilegeFactory.Priv_Backward.mask, PrivilegeFactory.Priv_Backward.code);
			put(PrivilegeFactory.Priv_HigherLevel.mask, PrivilegeFactory.Priv_HigherLevel.code);
			put(PrivilegeFactory.Priv_LowerLevel.mask, PrivilegeFactory.Priv_LowerLevel.code);
			put(PrivilegeFactory.Priv_SameLevel.mask, PrivilegeFactory.Priv_SameLevel.code);
			put(PrivilegeFactory.Priv_Move.mask, PrivilegeFactory.Priv_Move.code);
			put(PrivilegeFactory.Priv_Upload.mask, PrivilegeFactory.Priv_Upload.code);
			put(PrivilegeFactory.Priv_Statistics.mask, PrivilegeFactory.Priv_Statistics.code);
			put(PrivilegeFactory.Priv_Rename.mask, PrivilegeFactory.Priv_Rename.code);
		}
	};

	/**
	 * 基础权限，支持CE的权限类别
	 */
	public static final long BASICS = 
			AccessRight.READ_AS_INT | 
			AccessRight.WRITE_AS_INT | 
			AccessRight.RESERVED12_AS_INT | 
			AccessRight.RESERVED13_AS_INT |
			AccessRight.VIEW_CONTENT_AS_INT | 
			AccessRight.LINK_AS_INT | 
			AccessRight.PUBLISH_AS_INT | 
			AccessRight.CREATE_INSTANCE_AS_INT | 
			AccessRight.CHANGE_STATE_AS_INT |
			AccessRight.MINOR_VERSION_AS_INT | 
			AccessRight.MAJOR_VERSION_AS_INT | 
			AccessRight.DELETE_AS_INT | 
			AccessRight.READ_ACL_AS_INT | 
			AccessRight.WRITE_ACL_AS_INT |
			AccessRight.WRITE_OWNER_AS_INT | 
			AccessRight.UNLINK_AS_INT | 
			AccessRight.CREATE_CHILD_AS_INT;
	/**
	 * 扩展权限，最多只能有19位可用，即总可用位数为基础权限32+扩展权限19=51，原因是在前端js中Number的精确值最多只有51位？54？
	 * 目前系统扩展了8个，后续可以继续扩展
	 */
	public static final long EXTENDS = 
			PrivilegeFactory.Priv_UI_Disabled.mask | 
			PrivilegeFactory.Priv_UI_Enabled.mask | 
			PrivilegeFactory.Priv_Download.mask | 
			PrivilegeFactory.Priv_Print .mask| 
			PrivilegeFactory.Priv_Search.mask | 
			PrivilegeFactory.Priv_Remove.mask | 
			PrivilegeFactory.Priv_MemberAssignment.mask | 
			PrivilegeFactory.Priv_Share.mask |
			PrivilegeFactory.Priv_ProjectManagement.mask |
			PrivilegeFactory.Priv_ProjectWork.mask |
			PrivilegeFactory.Priv_Forward.mask |
			PrivilegeFactory.Priv_Backward.mask |
			PrivilegeFactory.Priv_HigherLevel.mask |
			PrivilegeFactory.Priv_LowerLevel.mask |
			PrivilegeFactory.Priv_SameLevel.mask |
			PrivilegeFactory.Priv_Move.mask |
			PrivilegeFactory.Priv_Upload.mask |
			PrivilegeFactory.Priv_Statistics.mask |
			PrivilegeFactory.Priv_Rename.mask;

	/**
	 * 系统所支持的所有权限
	 */
	public static final long All_PRIVS = BASICS | EXTENDS;
	/**
	 * 获取权限编码
	 * @param masks
	 * @return
	 */
	public static List<String> getCodes(Long masks) {
	
		List<String> codes = new ArrayList<String>();
		for(Entry<Long, String> entry : PrivmaskToCodeMap.entrySet()) {
			if((entry.getKey() & masks) == (long)entry.getKey()) codes.add(entry.getValue());
		}

		// 扩展权限
		/*if((PrivilegeFactory.Priv_Download & masks) == Priv_Download) codes.add("Priv_Download");
		if((PrivilegeFactory.Priv_MemberAssignment & masks) == Priv_MemberAssignment) codes.add("Priv_MemberAssignment");
		if((PrivilegeFactory.Priv_Print & masks) == Priv_Print) codes.add("Priv_Print");
		if((PrivilegeFactory.Priv_Remove & masks) == Priv_Remove) codes.add("Priv_Remove");
		if((PrivilegeFactory.Priv_Search & masks) == Priv_Search) codes.add("Priv_Search");
		if((PrivilegeFactory.Priv_Share & masks) == Priv_Share) codes.add("Priv_Share");
		if((PrivilegeFactory.Priv_UI_Disabled & masks) == Priv_UI_Disabled) codes.add("Priv_UI_Disabled");
		if((PrivilegeFactory.Priv_UI_Enabled & masks) == Priv_UI_Enabled) codes.add("Priv_UI_Enabled");
		if((PrivilegeFactory.Priv_ProjectManagement & masks) == Priv_ProjectManagement) codes.add("Priv_ProjectManagement");
		if((PrivilegeFactory.Priv_ProjectWork & masks) == Priv_ProjectWork) codes.add("Priv_ProjectWork");*/
		
		// CE权限
		/*if((AccessRight.READ_AS_INT & masks) == AccessRight.READ_AS_INT) codes.add("READ");
		if((AccessRight.WRITE_AS_INT & masks) == AccessRight.WRITE_AS_INT) codes.add("WRITE");
		if((AccessRight.RESERVED12_AS_INT & masks) == AccessRight.RESERVED12_AS_INT) codes.add("RESERVED12");
		if((AccessRight.RESERVED13_AS_INT & masks) == AccessRight.RESERVED13_AS_INT) codes.add("RESERVED13");
		if((AccessRight.VIEW_CONTENT_AS_INT & masks) == AccessRight.VIEW_CONTENT_AS_INT) codes.add("VIEW_CONTENT");
		if((AccessRight.LINK_AS_INT & masks) == AccessRight.LINK_AS_INT) codes.add("LINK");
		if((AccessRight.PUBLISH_AS_INT & masks) == AccessRight.PUBLISH_AS_INT) codes.add("PUBLISH");
		if((AccessRight.CREATE_INSTANCE_AS_INT & masks) == AccessRight.CREATE_INSTANCE_AS_INT) codes.add("CREATE_INSTANCE");
		if((AccessRight.CHANGE_STATE_AS_INT & masks) == AccessRight.CHANGE_STATE_AS_INT) codes.add("CHANGE_STATE");
		if((AccessRight.MINOR_VERSION_AS_INT & masks) == AccessRight.MINOR_VERSION_AS_INT) codes.add("MINOR_VERSION");
		if((AccessRight.MAJOR_VERSION_AS_INT & masks) == AccessRight.MAJOR_VERSION_AS_INT) codes.add("MAJOR_VERSION");
		if((AccessRight.DELETE_AS_INT & masks) == AccessRight.DELETE_AS_INT) codes.add("DELETE");
		if((AccessRight.READ_ACL_AS_INT & masks) == AccessRight.READ_ACL_AS_INT) codes.add("READ_ACL");
		if((AccessRight.WRITE_ACL_AS_INT & masks) == AccessRight.WRITE_ACL_AS_INT) codes.add("WRITE_ACL");
		if((AccessRight.WRITE_OWNER_AS_INT & masks) == AccessRight.WRITE_OWNER_AS_INT) codes.add("WRITE_OWNER");
		if((AccessRight.UNLINK_AS_INT & masks) == AccessRight.UNLINK_AS_INT) codes.add("UNLINK");
		if((AccessRight.CREATE_CHILD_AS_INT & masks) == AccessRight.CREATE_CHILD_AS_INT) codes.add("CREATE_CHILD");*/
		
		
		return codes;
	}
	/**
	 * 根据权限编码获取对应的mask值
	 * @param codes
	 * @return
	 */
	public static long getMasks(String[] codes) { 
		
		long masks = Priv_None.mask;
	    for(String code : codes) {
			
			if(PrivcodeToMaskMap.containsKey(code)) {
				
				masks |= PrivcodeToMaskMap.get(code);
			}
		}
		
	    return masks;
	    
	}
	/**
	 * 检查是否有【查看内容】的权限
	 * @param masks
	 * @return
	 */
	public static boolean haveViewContent(Long masks) {
		
		return ((AccessRight.VIEW_CONTENT_AS_INT & masks) == AccessRight.VIEW_CONTENT_AS_INT);
	}
	/**
	 * 检查是否有【下载】的权限
	 * @param masks
	 * @return
	 */
	public static boolean haveDownload(Long masks) {
		
		return ((PrivilegeFactory.Priv_Download.mask & masks) == PrivilegeFactory.Priv_Download.mask);
	}
	/**
	 * 获取共享权限值
	 * @return
	 */
	public static long getSharePrivMasks(){
		return PrivilegeFactory.Priv_Print.mask | PrivilegeFactory.Priv_Download.mask | AccessRight.VIEW_CONTENT_AS_INT;
	}
	
	public static void main(String[]args){
		
		String code = "MAJOR_VERSION";
		String[] sp = code.split(",");
		System.out.println(sp);
	/*	List<String> codes = PrivilegeFactory.getCodes(34359738368L | 549755813888L);
		System.out.println(codes);*/
	}
	/**
	 * 获取 编码 
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 设置 编码
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取 mask值 
	 * @return the mask
	 */
	public Long getMask() {
		return mask;
	}
	/**
	 * 设置 mask值
	 * @param mask the mask to set
	 */
	public void setMask(Long mask) {
		this.mask = mask;
	}
	/**
	 * 获取 描述
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * 设置 描述
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	/**
	 * 获取拥有者权限编码
	 * @return
	 */
	public static String[] getOwnerPrivCodes() {
		return new String[]{"DELETE", "VIEW_CONTENT", "MAJOR_VERSION", Priv_Share.getCode(), Priv_Rename.getCode(), Priv_Download.getCode(), Priv_Move.getCode()};
	}
}
