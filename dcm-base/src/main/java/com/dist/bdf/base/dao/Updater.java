package com.dist.bdf.base.dao;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 更新对象类
 * <p>
 * 提供三种更新模式：MAX, MIN, MIDDLE
 * </p>
 * <ul>
 * <li>MIDDLE：默认模式。除了null外，都更新。exclude和include例外。</li>
 * <li>MAX：最大化更新模式。所有字段都更新（包括null）。exclude例外。</li>
 * <li>MIN：最小化更新模式。所有字段都不更新。include例外。</li>
 * </ul>
 * 
 * @author 李其云
 * @version: v1.0 2013-7-22 下午5:07:22
 * @param <T>
 *            要更新的类型
 */
public class Updater<T> {
    private T bean;
    /**
     * 包含的属性
     */
    private List<String> includeProperties = new ArrayList<String>();
    /**
     * 排除的属性
     */
    private List<String> excludeProperties = new ArrayList<String>();
    /**
     * 更新模式
     */
    private UpdateMode mode = UpdateMode.MIDDLE;

    // private static final Logger log = LoggerFactory.getLogger(Updater.class);

    /**
     * 更新模式类别
     * <p>
     * MAX 除了excludeProperties中包括的属性而外全部更新<br>
     * MIN 只更新includeProperties中包括的属性<br>
     * MIDDLE 当更新值为null且属性包含在includeProperties中时进行更新，
     * 或当更新值不为null且属性不包含在excludeProperties中时进行更新
     * </p>
     * 
     * @author ShenYuTing
     */
    public static enum UpdateMode {
        MAX, MIN, MIDDLE
    }

    /**
     * 构造器
     * 
     * @param bean
     *            待更新对象
     */
    public Updater(T bean) {
        this.bean = bean;
    }

    /**
     * 
     * 构造器
     * 
     * @param bean
     *            待更新对象
     * @param mode
     *            更新模式
     */
    public Updater(T bean, UpdateMode mode) {
        this.bean = bean;
        this.mode = mode;
    }

    /**
     * 设置更新模式
     * 
     * @param mode
     *            更新模式
     * @return 更新对象自身
     */
    public Updater<T> setUpdateMode(UpdateMode mode) {
        this.mode = mode;
        return this;
    }

    /**
     * 必须更新的字段
     * 
     * @param property
     *            字段名
     * @return 更新对象自身
     */
    public Updater<T> include(String property) {
        includeProperties.add(property);
        return this;
    }

    /**
     * 不更新的字段
     * 
     * @param property
     *            字段名
     * @return 更新对象自身
     */
    public Updater<T> exclude(String property) {
        excludeProperties.add(property);
        return this;
    }

    /**
     * 判断指定字段是否更新
     * 
     * @param name
     *            要判断的字段
     * @param value
     *            字段值。用于检查是否为NULL
     * @return true表示是已经更新,false表示没有更新
     */
    public boolean isUpdate(String name, Object value) {
        if (this.mode == UpdateMode.MAX) {
            return !excludeProperties.contains(name);
        } else if (this.mode == UpdateMode.MIN) {
            return includeProperties.contains(name);
        } else if (this.mode == UpdateMode.MIDDLE) {
            if (value != null) {
                return !excludeProperties.contains(name);
            } else {
                return includeProperties.contains(name);
            }
        }
        return true;
    }

    /**
     * 获取泛型的对象
     * 
     * @return 泛型的对象
     */
    public T getBean() {
        return bean;
    }

    /**
     * 获取排除的属性
     * 
     * @return 排除的属性列表
     */
    public List<String> getExcludeProperties() {
        return excludeProperties;
    }

    /**
     * 获取包含的属性
     * 
     * @return 包含的属性列表
     */
    public List<String> getIncludeProperties() {
        return includeProperties;
    }
}
