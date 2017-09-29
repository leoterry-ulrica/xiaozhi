package com.dist.bdf.base.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 反射工具类
 * @author 何顺
 * @create 2014年11月20日
 */
public class ReflectionUtil {

    public static List<MethodBean> getMethods(Object obj) {
        if (obj == null)
            return null;
        List<MethodBean> list = new ArrayList<MethodBean>();
        Class<? extends Object> cs = obj.getClass();
        Method[] methods = cs.getDeclaredMethods();
        for (Method md : methods) {
            list.add(new MethodBean(md));
        }
        return list.size() == 0 ? null : list;
    }

    /**
     * 枚举对象的属性和值
     * @param obj
     * @return
     */
    public static Map<String, Object> getFieldsMap(Object obj) {
        if (obj == null)
            return null;
        HashMap<String, Object> map = new HashMap<String, Object>();
        Class<? extends Object> cs = obj.getClass();
        Field[] fileds = cs.getDeclaredFields();
        for (Field fd : fileds) {
            //允许访问private成员
            fd.setAccessible(true);
            try {
                map.put(fd.getName(), fd.get(obj));
            } catch (Exception e) {
                e.printStackTrace();
                map.remove(fd.getName());
            }
        }
        return map.size() == 0 ? null : map;
    }

    /** 
     * 循环向上转型, 获取对象的 DeclaredField 
     * @param object : 子类对象 
     * @param fieldName : 父类中的属性名 
     * @return 父类中的属性对象 
     */

    public static Field getDeclaredField(Object object, String fieldName) {
        Field field = null;

        Class<?> clazz = object.getClass();

        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                return field;
            } catch (Exception e) {
                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。  
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了  

            }
        }

        return null;
    }

    /**
     * 获取所有属性
     * @param obj
     * @return
     */
    public static void getFieldList(Object obj, List<Field> fields) {
        if (obj == null) {
            return;
        }
        Class<?> clazz = obj.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                Field[] fields2 = clazz.getDeclaredFields();
                for (Field field : fields2) {
                    fields.add(field);
                }
            } catch (Exception e) {
                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。  
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了  

            }
        }
    }

}

/**
 * 对象--方法名--参数类型--返回值类型
 * @author Administrator
 */
class MethodBean {
    //对象--方法名--参数类型--返回值类型
    private String name;
    private String parameterType;
    private String returnType;

    public MethodBean(String name, String parameterType, String returnType) {
        this.name = name;
        this.parameterType = parameterType;
        this.returnType = returnType;
    }

    public MethodBean(Method md) {
        this.name = md.getName();
        this.returnType = md.getReturnType().getName();
        this.parameterType = getParameter(md.getParameterTypes());
    }

    private <T> String getParameter(Class<?>[] cls_arr) {
        String result = "";
        for (Class<?> xs : cls_arr) {
            if (result != "")
                result = result + "," + xs.getSimpleName();
            else
                result = result + xs.getName();
        }
        return result == "" ? null : result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    @Override
    public String toString() {
        return "MethodBean [name=" + name + ", parameterType=" + parameterType + ", returnType=" + returnType + "]";
    }

}

class Person {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Person(String name, int age) {
        super();
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "[" + this.name + "  " + this.age + "]";
    }

    private String name;
    private int age;
}
