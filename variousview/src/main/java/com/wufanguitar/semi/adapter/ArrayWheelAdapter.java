package com.wufanguitar.semi.adapter;

import java.util.List;

/**
 * @Author: Frank Wu
 * @Email: wu.fanguitar@163.com
 * @Description: 简单数组的滚动适配器
 */

public class ArrayWheelAdapter<T> implements WheelAdapter {
	public static final int DEFAULT_LENGTH = 4;
	private List<T> mItems;

	public ArrayWheelAdapter(List<T> items) {
		this.mItems = items;
	}

	@Override
	public Object getItem(int index) {
		if (index >= 0 && index < mItems.size()) {
			return mItems.get(index);
		}
		return "";
	}

	@Override
	public int getItemsCount() {
		return mItems.size();
	}

	@Override
	public int indexOf(Object o){
		return mItems.indexOf(o);
	}
}
