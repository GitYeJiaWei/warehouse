package com.ioter.warehouse.ui.dialog;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


import com.ioter.warehouse.R;

import java.lang.ref.WeakReference;

public class BaseDialog extends IoterDialog
{
    /** 确定按钮和取消按钮的对话框 */
    public static final int POSITIVE_AND_NEGATIVE = 1;

    private Context mContext;
    private TextView mHintTv;// 第一行提示内容
    private Button mConfrimBtn;// 确定按键
    private Button mCancelBtn;// 取消按键
    private int MAX_LIMTNUMBER = 50;
    
    /**
     * function:有提示对话框包含确定，或含有取消按钮，输入框的对话框的构造函数
     *            ：参数0表示只有确定按钮的对话框，参数1表示有确定和取消按钮的对话框，参数3表示有输入框的对话框
     */
    public BaseDialog(Context context, int type)
    {
        this(context, type, R.style.BaseDialogStyle);
    }

    public BaseDialog(Context context, int type, int dialogStyle)
    {
        super(context, dialogStyle);
        WeakReference<Context> wr = new WeakReference<Context>(mContext);
        this.mContext = wr.get();
        // 忽略系统字体设置
        setDialogType(type);
    }

    /**
     * function:用于有list或下拉列表的dialog的构造函数 需要时自行添加
     */

    /**
     * @param type
     */
    private void setDialogType(int type)
    {
        switch (type)
        {

        case POSITIVE_AND_NEGATIVE:
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            setContentView(R.layout.dialog_positive_and_negative);
            mHintTv = (TextView) findViewById(R.id.dialog_hint_tv);
            mConfrimBtn = (Button) findViewById(R.id.dialog_ok_btn);
            mCancelBtn = (Button) findViewById(R.id.dialog_cancel_btn);
            setComfirmBtnOnclick();
            setCancleBtnOnclick();
            show();
            break;
        default:
            break;
        }
    }

    public void setMAX_LIMTNUMBER(int mAX_LIMTNUMBER)
    {
        MAX_LIMTNUMBER = mAX_LIMTNUMBER;
    }

    private void setCancleBtnOnclick()
    {
        if (mCancelBtn == null)
        {
            return;
        }
        mCancelBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dismiss();
            }
        });
    }

    private void setComfirmBtnOnclick()
    {
        if (mConfrimBtn == null)
        {
            return;
        }
        mConfrimBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dismiss();
            }
        });
    }

    public TextView getHintTv()
    {
        return mHintTv;
    }
    

    public final void setHintTvValue(CharSequence hintValue)
    {
        if (mHintTv == null)
        {
            return;
        }
        if (hintValue != null)
        {
            mHintTv.setText(hintValue);
        }
    }

    /**
     * 设置确定按钮的点击事件
     * 
     * @param listener
     */
    public final void setConfrimBtnOnclick(View.OnClickListener listener)
    {
        if (mConfrimBtn == null)
        {
            return;
        }
        mConfrimBtn.setOnClickListener(listener);

    }

    public final void setCancelBtnOnclick(View.OnClickListener listener)
    {
        if (mCancelBtn == null)
        {
            return;
        }
        mCancelBtn.setOnClickListener(listener);

    }

    public void setConfrimBtnValue(CharSequence confrimBtnValue)
    {
        if (mConfrimBtn == null)
        {
            return;
        }
        if (confrimBtnValue == null)
        {
            return;
        }
        mConfrimBtn.setText(confrimBtnValue);
    }

    public void setCancelBtnValue(CharSequence cancelBtnValue)
    {
        if (mCancelBtn == null)
        {
            return;
        }
        if (cancelBtnValue == null)
        {
            return;
        }
        mCancelBtn.setText(cancelBtnValue);
    }

}
