package com.ioter.warehouse.ui.adapter;

import android.view.View;

/**
 *
 *         列表中的每一项的数据处理类，可在takeData()中异步下载数据
 */
public abstract class ItemData
{
	/**
	 * 此数据对象的唯一值，必须赋值
	 */
	public abstract String getUniqueKey();

	/**
	 * 下载数据，成功后调用notifyChange()更新列表
	 */
	public abstract void takeData();

	/**
	 * 更新列表中对应布局的显示
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void notifyChanged()
	{
		if (mConvertView != null && mConvertView.isShown())
		{
			ItemLayout layout = (ItemLayout) mConvertView.getTag();
			if (layout.mData == this)// 当前显示的convertView的数据是否是此对象
			{
				layout.mIsSetView = true;
				layout.setView(layout.mPosition, layout.mData);
			}
		}
	}

	private View mConvertView;

	final void setConvertView(View convertView)
	{
		mConvertView = convertView;
	}
}