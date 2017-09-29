package com.dist.bdf.common.constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dist.bdf.base.exception.BusinessException;

public final class ResourceConstants {

	/**
	 * 
	 * 资源类型
	 * @author weifj
	 * @version 1.0，2016/01/04，weifj，创建资源类型字典
	 * @version 1.1，2016/03/05，weifj
	 *     1. 从包（com.dist.bdf.dcm.auth.constants）迁移到包（com.dist.bdf.core.common.constants）；
	 *     2. 修改Res_File值为Document和Res_Folder值为Folder；
	 *
	 */
	public final static class ResourceType {

		private final static Logger logger = LoggerFactory.getLogger(ResourceType.class);
		
		/**
		 * 首页
		 */
		public static final String RES_UI_HOMEPAGE = "Res_UI_HomePage";
		/**
		 * 院包
		 */
		public static final String RES_PCK_INSTITUTE = "Res_Pck_Institute";
		/**
		 * 所包
		 */
		public static final String RES_PCK_DEPARTMENT = "Res_Pck_Department";
		/**
		 * 项目包
		 */
		public static final String RES_PCK_PROJECT = "Res_Pck_Project";
		/**
		 * BPM项目包
		 */
		public static final String RES_PCK_PROJECT_BPM = "Res_Pck_Project_BPM";
		/**
		 * 个人包
		 */
		public static final String RES_PCK_PERSON = "Res_Pck_Person";
		/**
		 * 组包
		 */
		public static final String RES_PCK_GROUP = "Res_Pck_Group";
		/**
		 * 院空间
		 */
		public static final String RES_SPACE_INSTITUTE = "Res_Space_Institute";
		/**
		 * 所空间
		 */
		public static final String RES_SPACE_DEPARTMENT = "Res_Space_Department";
		/**
		 * 项目
		 */
		public static final String RES_SPACE_PROJECT = "Res_Space_Project";
		/**
		 * 讨论组
		 */
		public static final String RES_SPACE_DISCUSSION = "Res_Space_Discussion";
		/**
		 * 个人
		 */
		public static final String RES_SPACE_PERSON = "Res_Space_Person";
		/**
		 * 文件
		 */
		public static final String RES_FILE = "Document";
		/**
		 * 文件夹
		 */
		public static final String RES_FOLDER = "Folder";
		/**
		 * 微作
		 */
		public static final String RES_WZ = "Document_WZ";
		/**
		 * 脑图
		 */
		public static final String RES_BRAINMAP = "Res_BrainMap";
		/**
		 * cad模板
		 */
		public static final String RES_CAD_TEMPLATE = "Res_Cad_Template";
		/**
		 * 系统资源
		 */
		public static final String RES_System = "Res_System";
		/**
		 * 根据客户端传过来的资源类型映射到服务端资源编码
		 * @param clientResType
		 * @return
		 */
		public static String getResTypeCode(int clientResType ) {
			
			String resType = "";
			switch (clientResType) {
			case 1:
				resType = ResourceType.RES_PCK_PERSON;
				break;
			case 2:
				resType = ResourceType.RES_PCK_PROJECT;
				break;
			case 3:
				resType = ResourceType.RES_PCK_DEPARTMENT;
				break;
			case 4:
				resType = ResourceType.RES_PCK_INSTITUTE;
				break;
			default:
				logger.warn("传入的资源类型int="+clientResType+"，在模型中没有找到对应的类型。");
				break;
			}
			return resType;
		}
		/**
		 * 根据服务端资源编码映射成客户端所需的资源类型
		 * @param resTypeCode
		 * @return
		 */
	   public static int getClientResType(String  resTypeCode){
			
			int clientResType = -1;
			switch (resTypeCode) {
			case ResourceType.RES_PCK_PERSON:
				clientResType = 1;
				break;
			case ResourceType.RES_PCK_PROJECT:
				clientResType = 2;
				break;
			case ResourceType.RES_PCK_DEPARTMENT:
				clientResType = 3;
				break;
			case ResourceType.RES_PCK_INSTITUTE:
				clientResType = 4;
				break;
			default:
				logger.warn("传入的服务端资源类型String=[{}]，在没有找到对应的类型代码。", resTypeCode);
				throw new BusinessException("传入的服务端资源类型String=[{"+resTypeCode+"}]，在没有找到对应的类型代码。");
				
				//break;
			}
			
			return clientResType;
		}
	}
	
	/**
	 * @author weifj
	 * @version 1.0，2016/02/18，weifj，创建资源类型状态的字典
	 */
	public final class ResourceStatus {

		/**
		 * 资源类型状态：在办
		 */
		public static final long ACTIVE = 1L;
		/**
		 * 资源类型状态：归档
		 */
		public static final long ARCHIVE = 0L;
	}
}
