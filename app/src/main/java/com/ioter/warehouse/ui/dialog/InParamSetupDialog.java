package com.ioter.warehouse.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ioter.warehouse.R;
import com.ioter.warehouse.common.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Connect password dialog to enter reader password to connect with reader
 */
public class InParamSetupDialog extends Dialog {
    private final Activity activity;
    @BindView(R.id.type_tv)
    TextView typeTv;
    @BindView(R.id.unique_et)
    EditText uniqueEt;
    @BindView(R.id.paramUnique_lly)
    LinearLayout paramUniqueLly;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_connect)
    Button btnConnect;


    private int paramType;//0收货 1上架 2出库 3盘点

    public void setBarCode(String barCode) {
        if (paramType == 0) {
            uniqueEt.setText(barCode);
        }

    }

    //定义一个抽象类
    public interface ISelection {
        void onStartBarCode();

        /**
         * @param paramUnique   单号
         */
        void onConfirm(String paramUnique);


        /**
         * @param paramPullType
         * @param paramWareNum
         */
        void onShelfConfirm(int paramPullType, String paramWareNum);
    }

    private ISelection mSelection;


    /**
     * Constructor of the class
     *
     * @param activity activity context
     */
    public InParamSetupDialog(Activity activity, int paramType, ISelection selection) {
        super(activity);
        this.activity = activity;
        this.paramType = paramType;
        this.mSelection = selection;
        setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_inparamsetup);
        ButterKnife.bind(this);

        if (paramType == 0) {
            typeTv.setText("出库单号");
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 280 && event.getAction() == KeyEvent.ACTION_DOWN)//左右按键
        {
            if (mSelection != null) {
                mSelection.onStartBarCode();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @OnClick({R.id.btn_connect, R.id.btn_cancel})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_connect:
                if (paramType == 0) {
                    String trim = uniqueEt.getText().toString().trim();
                    if (TextUtils.isEmpty(trim)) {
                        ToastUtil.toast("请输入单号");
                        return;
                    }
                    dismiss();
                    if (mSelection != null) {
                        mSelection.onConfirm( uniqueEt.getText().toString().trim());
                    }
                }
                break;
            default:
                break;
        }
    }
}
