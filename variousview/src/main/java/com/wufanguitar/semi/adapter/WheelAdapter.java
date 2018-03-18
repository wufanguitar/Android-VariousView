package com.wufanguitar.semi.adapter;

/**
 * @Author: Frank Wu
 * @Email: wu.fanguitar@163.com
 * @Description: WheelView适配器
 */

public interface WheelAdapter<T> {
	/**
	 * 获取 items 的数量
	 */
	int getItemsCount();
	
	/**
	 * 通过给定位置获取 item 对象
	 */
	T getItem(int index);
	
	/**
     * 获取指定 item 对象的位置
     */
	int indexOf(T o);
}
