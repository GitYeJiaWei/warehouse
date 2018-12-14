package com.ioter.warehouse.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * @param <E> ItemData的子类
 * @see 添加列表数据：updateData(未处理的数据集合);
 * @see 清除列表数据：clearData();
 * @see 快速滚动处理：listView.setOnScrollListener(adapter);
 */
public abstract class EpcAdapter<E extends ItemData> extends BaseAdapter implements OnScrollListener
{
    /**
     * 对列表的数据集合进行排序等的操作
     */
    public abstract void sortDataList(ArrayList<E> dataList);

    /**
     * 获取布局的处理对象
     *
     * @see return new ItemLayout的子类();
     */
    public abstract ItemLayout<E> getItemLayout(E data);

    /**
     * 获取多个Layout布局的总数
     */
    public int getViewTypeCount()
    {
        return 1;
    }

    /**
     * 多个Layout布局中，获取和data关联的类型，必须从0开始依次累加
     */
    public int getItemViewType(int position, E data)
    {
        return 0;
    }

    public void reFresh(Collection<? extends E> collection)
    {
        if (collection == null || collection.size() < 1)
        {
            return;
        }
        ArrayList<E> temp = new ArrayList<E>(collection);
        temp.addAll(mDataList);
        mUniqueMap.clear();
        boolean hasChanged = false;
        for (E data : collection)
        {
            if (data != null)
            {
                String key = data.getUniqueKey();
                if (key != null)
                {
                    E oldData = mUniqueMap.put(key, data);
                    if (oldData != data)
                    {
                        hasChanged = true;
                    }
                }
            }
        }
        if (hasChanged)
        {
            mDataList.clear();
            Collection<E> values = mUniqueMap.values();
            for (E data : values)
            {
                mDataList.add(data);
            }
            sortDataList(mDataList);
            notifyDataSetChanged();
        }

    }

    public void updateData(E data)
    {
        if (data != null)
        {
            String key = data.getUniqueKey();
            if (key != null)
            {
                E oldData = mUniqueMap.put(key, data);
                if (oldData != data)
                {
                    refreshData();
                }
            }
        }
    }

    /**
     * 给列表添加数据集合，根据每项数据的唯一值getUniqueKey()排除重复
     */
    public void updateData(Collection<? extends E> collection)
    {
        if (collection == null || collection.size() < 1)
        {
            return;
        }
        boolean hasChanged = false;
        for (E data : collection)
        {
            if (data != null)
            {
                String key = data.getUniqueKey();
                if (key != null)
                {
                    E oldData = mUniqueMap.put(key, data);
                    if (oldData != data)
                    {
                        hasChanged = true;
                    }
                }
            }
        }
        if (hasChanged)
        {
            refreshData();
        }
    }

    protected void refreshData()
    {
        mDataList.clear();
        mDataList.addAll(mUniqueMap.values());
        sortDataList(mDataList);
        notifyDataSetChanged();
    }

    /**
     * 强制给列表添加数据集合，不排除重复，不清除原有数据
     *
     * @param collection
     */
    public void addDataByForce(Collection<? extends E> collection)
    {
        if (collection == null || collection.size() < 1)
        {
            return;
        }
        mDataList.addAll(collection);
        sortDataList(mDataList);
        notifyDataSetChanged();
    }

    /**
     * 清空列表的所有数据
     */
    public void clearData()
    {
        mUniqueMap.clear();
        mDataList.clear();
        notifyDataSetChanged();
    }

    /**
     * 移除某个数据
     */
    public void removeData(E data)
    {
        if (data != null)
        {
            mUniqueMap.remove(data.getUniqueKey());
            mDataList.remove(data);
            notifyDataSetChanged();
        }
    }

    /**
     * 批量移除
     */
    public void removeData(Collection<? extends E> collection)
    {
        if (collection == null || collection.size() < 1)
        {
            return;
        }
        for (E data : collection)
        {
            if (data != null)
            {
                mUniqueMap.remove(data.getUniqueKey());
                mDataList.remove(data);
            }
        }
        notifyDataSetChanged();
    }

    public void sortDataList()
    {
        if (mDataList.size() > 0)
        {
            sortDataList(mDataList);
            notifyDataSetChanged();
        }
    }

    public ArrayList<E> getDataList()
    {
        return mDataList;
    }

    public E getData(String uniqueKey)
    {
        return mUniqueMap.get(uniqueKey);
    }

    private LayoutInflater mLayoutInflater;
    protected HashMap<String, E> mUniqueMap;
    protected ArrayList<E> mDataList;
    private boolean mIsScroll, mIsFling;

    public EpcAdapter(LayoutInflater layoutInflater)
    {
        mLayoutInflater = layoutInflater;
        mUniqueMap = new HashMap<String, E>();
        mDataList = new ArrayList<E>();
    }

    @Override
    public final int getCount()
    {
        return mDataList.size();
    }

    @Override
    public final E getItem(int position)
    {
        return mDataList.get(position);
    }

    @Override
    public final long getItemId(int position)
    {
        return position;
    }

    @Override
    public final int getItemViewType(int position)
    {
        return getItemViewType(position, mDataList.get(position));
    }

    @SuppressWarnings("unchecked")
    @Override
    public final View getView(int position, View convertView, ViewGroup parent)
    {
        E data = mDataList.get(position);
        int viewType = getItemViewType(position, data);
        ItemLayout<E> layout;
        if (convertView == null || (layout = (ItemLayout<E>) convertView.getTag()).mViewType != viewType)
        {
            layout = getItemLayout(data);
            layout.mViewType = viewType;
            convertView = layout.initView(mLayoutInflater, parent);
            convertView.setTag(layout);
        }
        data.setConvertView(convertView);
        layout.mPosition = position;
        layout.mData = data;
        layout.mIsSetView = !mIsFling;
        layout.setDefaultView(position, data);
        if (layout.mIsSetView)
        {
            layout.setView(position, data);
            data.takeData();
        }
        return convertView;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onScrollStateChanged(AbsListView listView, int scrollState)
    {
        switch (scrollState)
        {
            case SCROLL_STATE_IDLE:
                mIsScroll = false;
                mIsFling = false;
                for (int i = 0; i < listView.getChildCount(); i++)
                {
                    if (mIsScroll || mIsFling)
                    {
                        break;
                    }
                    try
                    {
                        View convertView = listView.getChildAt(i);
                        if (convertView != null)
                        {
                            ItemLayout<E> layout = (ItemLayout<E>) convertView.getTag();
                            if (!layout.mIsSetView)
                            {
                                layout.mIsSetView = true;
                                layout.setView(layout.mPosition, layout.mData);
                                layout.mData.takeData();
                            }
                        }
                    } catch (Exception e)
                    {
                    }
                }
                break;
            case SCROLL_STATE_TOUCH_SCROLL:
                mIsScroll = true;
                break;
            case SCROLL_STATE_FLING:
                mIsFling = true;
                break;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
    }

    public void destroy()
    {
        mUniqueMap.clear();
        mDataList.clear();
    }
}