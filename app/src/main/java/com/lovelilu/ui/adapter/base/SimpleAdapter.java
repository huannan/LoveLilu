package com.lovelilu.ui.adapter.base;

import android.content.Context;

import java.util.List;

/**
 * 一个简易的adapter
 */

public abstract class SimpleAdapter<T> extends BaseAdapter<T, BaseViewHolder> {

	public SimpleAdapter(Context context, int layoutResId) {
		super(context, layoutResId);
	}

	public SimpleAdapter(Context context, int layoutResId, List<T> datas) {
		super(context, layoutResId, datas);
	}


}
