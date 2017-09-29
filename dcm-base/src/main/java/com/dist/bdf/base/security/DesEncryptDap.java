
package com.dist.bdf.base.security;

import java.lang.reflect.Method;

import com.dist.bdf.base.security.DesEncryptBase64.InnerDesEncryptAdapter;

/**
 * dap加密解密算法，不能实现序列化接口，否则破坏单例模式
 * @author weifj
 * @version 1.0，2016/03/23，weifj，集成dap加密解密接口
 * @version 1.1，2016/03/30，weifj，添加属性adapter的类型：volatile
 */
public class DesEncryptDap implements DesEncryptAdapter {

	
    private DesEncryptDap() {
		
	}
	
	 /**
    * 获取适配器实例
    * @return
    */
   public static DesEncryptAdapter getInstance(){
   	
   	return InnerDesEncryptAdapter.INSTANCE;
   }
   
   static class InnerDesEncryptAdapter {
   	
   	private final static DesEncryptAdapter INSTANCE = new DesEncryptDap();
   }
    /**
     * （不同的写法）
     * @return
     */
    /*public static synchronized DesEncryptAdapter getInstance(){
    	
    	if(null == adapter){
    		adapter = new DesEncryptDap();
    	}
    	return adapter;
    }*/
    
	@Override
	public String encrypt(String text) {

		if(null == text){
			return text;
		}
		 String returnValue = "";
			try {
				Object instance = Class.forName("JDesEncrypt").newInstance();
				Class clazz = instance.getClass();
	           
				Method method = clazz.getMethod("EncryptCipher", String.class);
	            returnValue = (String) method.invoke(instance, text);
			  
			} catch (Exception ex) {
				// TODO Auto-generated catch block
				System.out.println("密码加密失败：");
				ex.printStackTrace();
			} 
			return returnValue;
	}

	@Override
	public String decrypt(String text) {

		if(null == text){
			return text;
		}
		 String returnValue = "";
			try {
				Object instance = Class.forName("JDesEncrypt").newInstance();
				Class clazz = instance.getClass();
	           
				Method method = clazz.getMethod("DecryptCipher", String.class);
	            returnValue = (String) method.invoke(instance, text);
			  
			} catch (Exception ex) {
	
				System.out.println("密码解密失败：");
				ex.printStackTrace();
			} 
			return returnValue;
	}

}
