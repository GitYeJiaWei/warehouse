package com.ioter.warehouse.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;

/**
 *
 *         列表中的每一项的布局处理类，禁止在该类中异步更新view
 * 
 * @param <E>
 *            ItemData的子类
 */
public abstract class ItemLayout<E extends ItemData> implements OnClickListener, OnLongClickListener
{
	/**
	 * 初始化布局
	 * 
	 * @see 布局为空时执行此方法
	 * @see View view = inflater.inflate(R.layout.XX, viewGroup, false);
	 * @see imageView = (ImageView) view.findViewById(R.id.XX);
	 * @see return view;
	 */
	public abstract View initView(LayoutInflater inflater, ViewGroup viewGroup);

	/**
	 * 设置View的默认数据，防止显示错乱
	 * 
	 * @see 每次getView时执行此方法
	 */
	public abstract void setDefaultView(int position, E data);

	/**
	 * 设置View的data数据
	 * 
	 * @see 每次getView时执行此方法
	 */
	public abstract void setView(int position, E data);

	/**
	 * 点击事件处理
	 * 
	 * @see 在initView方法里执行view.setOnClickListener(this);
	 * @see 在此方法里执行switch(view.getId())响应不同的点击事件
	 */
	public abstract void onClick(View v, E data);
	
	public void onLongClick(View v, E data)
	{
	}

	/**
	 * 获取当前布局对应的itemData
	 * 
	 * @return
	 */
	public E getCurrentData()
	{
	    return mData;
	}
	
	/** @hide 子类勿用 */
	int mViewType;
	/** @hide 子类勿用 */
	int mPosition;
	/** @hide 子类勿用 */
	E mData;
	/** @hide 子类勿用 */
	boolean mIsSetView;

	@Override
	public final void onClick(View v)
	{
		onClick(v, mData);
	}
	
	@Override
	public final boolean onLongClick(View v)
	{
	    onLongClick(v, mData);
	    return true;
	}
}