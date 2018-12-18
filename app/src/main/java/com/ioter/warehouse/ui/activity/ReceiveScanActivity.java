package com.ioter.warehouse.ui.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.ioter.warehouse.AppApplication;
import com.ioter.warehouse.R;
import com.ioter.warehouse.bean.BaseEpc;
import com.ioter.warehouse.common.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReceiveScanActivity extends NewBaseActivity {

    @BindView(R.id.et_danhao)
    EditText etDanhao;
    @BindView(R.id.lv_)
    ListView lv;
    @BindView(R.id.bt_sure)
    Button btSure;
    @BindView(R.id.btn_cancel)
    Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_scan);
        ButterKnife.bind(this);

        setTitle("收货");
    }

    //获取EPC群读数据
    @Override
    public void handleUi(BaseEpc baseEpc) {
        super.handleUi(baseEpc);
        etDanhao.setText(baseEpc._EPC);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 139 || keyCode == 280)
        {
            if (event.getRepeatCount() == 0)
            {
                readTag(1);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 139 || keyCode == 280)
        {
            if (event.getRepeatCount() == 0)
            {
                readTag(2);
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    private void readTag(int a) {
        if (a==1) {
            if (AppApplication.mReader.startInventoryTag((byte) 0, (byte) 0)) {
                loopFlag = true;
                new TagThread(10).start();
            } else {
                AppApplication.mReader.stopInventory();
                loopFlag = false;
                ToastUtil.toast("开始扫描失败");
            }
        } else {
            AppApplication.mReader.stopInventory();
            loopFlag = false;
        }
    }

    @OnClick({R.id.bt_sure, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_sure:
                finish();
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }
}
