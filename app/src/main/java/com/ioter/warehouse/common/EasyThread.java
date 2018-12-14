package com.ioter.warehouse.common;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.ioter.warehouse.AppApplication;


/**
 * @author chenc
 *         <p>
 *         按顺序执行线程中和主线程中的操作
 */
public abstract class EasyThread
{
    /**
     * 在线程中的操作
     */
    protected abstract Object doInWork() throws Exception;

    /**
     * 在主线程中的操作
     *
     * @param resultStatus 执行状态:RESULT_OK表示正常、RESULT_NO_NET表示无网络、RESULT_EXCEPTION表示异常、
     *                     RESULT_DESTROY表示操作已被取消
     * @param obj          （doInWork）的返回值
     */
    protected abstract void doInUI(int resultStatus, Object obj);

    public static final int RESULT_OK = 0;// 执行正常
    public static final int RESULT_NO_NET = -1;// 无网络
    public static final int RESULT_EXCEPTION = -2;// 执行异常
    public static final int RESULT_DESTROY = -3;// 操作已被取消

    private boolean mIsDestroy;
    private Handler mHandler;

    /**
     * 销毁，取消操作时执行
     */
    public void destroy()
    {
        mIsDestroy = true;
    }

    /**
     * 按顺序执行方法：（doInWork）、（doInUI）
     */
    public void start()
    {
        AppApplication.getExecutorService().submit(new Runnable()
        {
            public void run()
            {
                try
                {
                    if (mIsDestroy)
                    {
                        postUI(RESULT_DESTROY, null);
                        return;
                    }
                    Object obj = doInWork();
                    if (mIsDestroy)
                    {
                        postUI(RESULT_DESTROY, null);
                        return;
                    }
                    postUI(RESULT_OK, obj);
                } catch (Exception e) // 执行异常的情况
                {
                    if (mIsDestroy)
                    {
                        postUI(RESULT_DESTROY, null);
                        return;
                    }
                    postUI(RESULT_EXCEPTION, null);
                }
            }
        });
    }

    /**
     * 更新UI界面
     *
     * @param resultStatus 执行状态
     * @param obj
     */
    private void postUI(final int resultStatus, final Object obj)
    {
        if (mHandler == null)
        {
            mHandler = new Handler(Looper.getMainLooper());
        }
        mHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    if (mIsDestroy)
                    {
                        doInUI(RESULT_DESTROY, null);
                        return;
                    }
                    doInUI(resultStatus, obj);
                } catch (Exception e)
                {
                    Log.d("EasyThread", "postUI:" + resultStatus + e);
                }
            }
        });
    }
}