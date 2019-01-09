package com.ioter.warehouse.ui.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ioter.warehouse.R;

public class AutoListView extends ListView {

	private LayoutInflater inflater;
	private View footer;
	private TextView noData;
	private TextView loadFull;
	private TextView more;
	private ProgressBar loading;

	private boolean isLoading;// 判断是否正在加载
	private boolean isLoadFull;
	private int pageSize = 10;

	private OnLoadListener onLoadListener;

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public AutoListView(Context context) {
		super(context);
		initView(context);
	}

	public AutoListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public AutoListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}


	// 初始化组件
	private void initView(Context context) {
		inflater = LayoutInflater.from(context);
		footer = inflater.inflate(R.layout.listview_footer, null);
		loadFull = (TextView) footer.findViewById(R.id.loadFull);
		noData = (TextView) footer.findViewById(R.id.noData);
		more = (TextView) footer.findViewById(R.id.more);
		loading = (ProgressBar) footer.findViewById(R.id.loading);

		this.addFooterView(footer);
		this.setOnScrollListener(new OnScrollListener() {
            /**
             *监听着ListView的滑动状态改变。
             * 官方的有三种状态SCROLL_STATE_TOUCH_SCROLL、SCROLL_STATE_FLING、SCROLL_STATE_IDLE：
             * SCROLL_STATE_TOUCH_SCROLL：手指正拖着ListView滑动
             * SCROLL_STATE_FLING：ListView正自由滑动
             * SCROLL_STATE_IDLE：ListView滑动后静止
             * */
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				ifNeedLoad(view, scrollState);
			}

            /**
             * firstVisibleItem: 表示在屏幕中第一条显示的数据在adapter中的位置
             * visibleItemCount：则表示屏幕中最后一条数据在adapter中的数据，
             * totalItemCount则是在adapter中的总条数
             * */
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (onLoadListener != null) {
					onLoadListener.onAfterScroll(firstVisibleItem);
				}
			}
		});
	}

	public void onLoad() {
		if (onLoadListener != null) {
			onLoadListener.onLoad();
		}
	}
	// 用于加载更多结束后的回调
	public void onLoadComplete() {
		isLoading = false;
	}

	// 根据listview滑动的状态判断是否需要加载更多
	private void ifNeedLoad(AbsListView view, int scrollState) {
		try {
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
					&& !isLoading
					&& view.getLastVisiblePosition() == view
					.getPositionForView(footer) && !isLoadFull) {
				onLoad();
				isLoading = true;
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 这个方法是根据结果的大小来决定footer显示的。
	 * <p>
	 * 这里假定每次请求的条数为10。如果请求到了10条。则认为还有数据。
	 * 如过结果不足10条，则认为数据已经全部加载，这时footer显示已经全部加载
	 * </p>
	 * 
	 * @param resultSize
	 */
	public void setResultSize(int resultSize) {
		if (resultSize == 0) {
			isLoadFull = true;
			loadFull.setVisibility(View.GONE);
			loading.setVisibility(View.GONE);
			more.setVisibility(View.GONE);
			noData.setVisibility(View.VISIBLE);
		} else if (resultSize > 0 && resultSize < pageSize) {
			isLoadFull = true;
			loadFull.setVisibility(View.VISIBLE);
			loading.setVisibility(View.GONE);
			more.setVisibility(View.GONE);
			noData.setVisibility(View.GONE);
		} else if (resultSize == pageSize) {
			isLoadFull = false;
			loadFull.setVisibility(View.GONE);
			loading.setVisibility(View.VISIBLE);
			more.setVisibility(View.VISIBLE);
			noData.setVisibility(View.GONE);
		}

	}

    // 加载更多监听
    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

	/*
	 * 定义加载更多接口
	 */
	public interface OnLoadListener {
		public void onLoad();
		public void onAfterScroll(int firstVisibleItem);
	}

}
