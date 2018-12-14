package com.ioter.warehouse.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.ioter.warehouse.ui.activity.BaseActivity;


public class IoterDialog extends Dialog
{
    protected BaseActivity mBaseActivity;

    public IoterDialog(Context context)
    {
        super(context);
        if (context instanceof BaseActivity)
        {
            mBaseActivity = (BaseActivity) context;
        }
    }

    public IoterDialog(Context context, int theme)
    {
        super(context, theme);
        if (context instanceof BaseActivity)
        {
            mBaseActivity = (BaseActivity) context;
        }
    }

    public IoterDialog(Context context, boolean cancelable, OnCancelListener cancelListener)
    {
        super(context, cancelable, cancelListener);
        if (context instanceof BaseActivity)
        {
            mBaseActivity = (BaseActivity) context;
        }
    }

    @Override
    public void setContentView(int layoutResID)
    {
        super.setContentView(layoutResID);
    }

    @Override
    public void setContentView(View view, LayoutParams params)
    {
        super.setContentView(view, params);
    }
}