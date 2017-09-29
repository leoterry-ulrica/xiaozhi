
package com.dist.bdf.manager.ecm.security.constants;

/**
 * @author weifj
 * @version 1.0，2016/03/02，weifj，创建对应CE继承深度的类型
 */
public final class InheritableDepth {
 /**
	  0 - This object only (no inheritance).（仅此对象）
	  *     1 - This object and immediate children only.（此对象和直接子代） Other positive values greater than 1 indicate the allowed depth of inheritance (for example, 2 means this object, its immediate children and grandchildren only can inherit the permission).
	  *    -1 - This object and all children (infinite levels deep).（此对象和所有子代）
	  *    -2 - All children (infinite levels deep) but not the object itself.（所有子代，但不是此对象）
	  *    -3 - Immediate children only but not this object. （直接子代，但不是此对象）
	  *    Other negative values less than -3 indicate the allowed depth of inheritance (for example, -4 means only immediate children and grandchildren of the object inherit the permission, but not the object itself).
	  */
	/**
	 * 0 - This object only (no inheritance).（仅此对象）
	 */
	public static int Object_Only = 0;
	/**
	 * 1 - This object and immediate children only.（此对象和直接子代）
	 *  Other positive values greater than 1 indicate the allowed depth of inheritance (for example, 2 means this object, its immediate children and grandchildren only can inherit the permission).
	 */
	public static int Object_And_ImmediateChildren = 1;
	/**
	 * -1 - This object and all children (infinite levels deep).（此对象和所有子代）
	 */
	public static int Object_And_AllChildren = -1;
	/**
	 *  -2 - All children (infinite levels deep) but not the object itself.（所有子代，但不是此对象）
	 */
	public static int AllChildren_Not_Object = -2;
	/**
	 *   -3 - Immediate children only but not this object. （直接子代，但不是此对象）
	 *    Other negative values less than -3 indicate the allowed depth of inheritance (for example, -4 means only immediate children and grandchildren of the object inherit the permission, but not the object itself).
	 */
	public static int ImmediateChildren_Not_Object = -3;
}
