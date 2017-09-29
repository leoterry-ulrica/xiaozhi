package com.dist.bdf.common.constants;

/**
 * 针对流程操作类型
 * @author weifj
 *
 */
public final class ActionType {

	/**
	 * 针对task的操作
	 * @author weifj
	 *
	 */
	class TaskOp {
		
		/**
		 * 添加
		 */
		public static final long Add = 1L;

		/**
		 * 删除
		 */
		public static final long Delete = -1L;
		
		/**
		 * 发送
		 */
		public static final long Send = 2L;
		
		/**
		 * 修改更新
		 */
		public static final long Update = 3L;
		
	}
	
	/**
	 * 针对文档的操作
	 * @author weifj
	 *
	 */
	class FileOp {
		
		/**
		 * 添加文档
		 */
		public static final long Add = 1L;
		/**
		 * 删除文档
		 */
		public static final long Delete = -1L;
		/**
		 * 添加文档版本
		 */
		public static final long AddVersion = 2L;
		/**
		 * 删除文档版本
		 */
		public static final long DeleteVersion = 3L;
		
	}
	
}
