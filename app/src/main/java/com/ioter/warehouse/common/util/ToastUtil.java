package com.ioter.warehouse.common.util;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.ioter.warehouse.AppApplication;


/**
 * @author chenc
 * 
 *         全局的toast显示的工具类
 */
public class ToastUtil
{
    private static Toast mToast;

    /**
     * 取消mToast引用对象
     */
    public static void destroy()
    {
        mToast = null;
    }

    /**
     * 默认toast的显示
     * 
     * @param resId
     *            提示内容的资源id
     */
    public static void toast(int resId)
    {
        toastNow(AppApplication.getApplication().getResources().getText(resId), Toast.LENGTH_SHORT);
    }
    
    /**
     * 默认toast的显示
     * 
     * @param content
     *            提示内容
     */
    public static void toast(CharSequence content)
    {
        toastNow(content, Toast.LENGTH_SHORT);
    }
    
    public static void toastLong(String content)
    {
        toast(content, Toast.LENGTH_LONG);
    }
    
    /**
     * 指令返回结果的toast提示
     * 
     * @param content
     *            提示内容
     * @param status
     *            错误状态字
     */
    public static void toast(String content, int status)
    {
        if (content != null && content.trim().length() > 0)
        {
            toastNow(content, Toast.LENGTH_SHORT);
        }
    }
    
    private static void toastNow(final CharSequence content, int duration)
    {
        if (Looper.myLooper() == Looper.getMainLooper())// 主线程
        {
            if (mToast != null)
            {
                mToast.cancel();
            }
            mToast = Toast.makeText(AppApplication.getApplication(), content, duration);
            mToast.show();
            return;
        }
        new Handler(Looper.getMainLooper()).post(new Runnable()
        {
            @Override
            public void run()
            {
                if (mToast != null)
                {
                    mToast.cancel();
                }
                mToast = Toast.makeText(AppApplication.getApplication(), content, Toast.LENGTH_SHORT);
                mToast.show();                    
            }
        });
    }
}