package com.ioter.warehouse.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ioter.warehouse.AppApplication;
import com.ioter.warehouse.R;
import com.ioter.warehouse.common.util.SoundManage;
import com.ioter.warehouse.common.util.ToastUtil;
import com.zebra.adc.decoder.Barcode2DWithSoft;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReceiveActivity extends NewBaseActivity {
    @BindView(R.id.et_danhao)
    EditText etDanhao;
    @BindView(R.id.et_dingdan)
    EditText etDingdan;
    @BindView(R.id.bt_sure)
    Button btSure;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    private int a = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
        ButterKnife.bind(this);

        setTitle("收货");

        etDanhao.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etDanhao.setFocusableInTouchMode(true);
                    etDanhao.requestFocus();
                    a = 1;
                }
            }
        });
        etDingdan.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etDingdan.setFocusableInTouchMode(true);
                    etDingdan.requestFocus();
                    a = 2;
                }
            }
        });
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 139 || keyCode == 280) {
            if (event.getRepeatCount() == 0) {
                ScanBarcode();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    //扫条码
    private void ScanBarcode() {
        if (AppApplication.barcode2DWithSoft!=null){
            AppApplication.barcode2DWithSoft.scan();
            AppApplication.barcode2DWithSoft.setScanCallback(ScanBack);
        }
    }

    public Barcode2DWithSoft.ScanCallback
            ScanBack = new Barcode2DWithSoft.ScanCallback() {
        @Override
        public void onScanComplete(int i, int length, byte[] bytes) {
            if (length < 1) {
            } else {
                final String barCode = new String(bytes, 0, length);
                if (barCode != null && barCode.length() > 0) {
                    SoundManage.PlaySound(ReceiveActivity.this, SoundManage.SoundType.SUCCESS);
                    if (a == 2) {
                        etDingdan.setText(barCode);
                        a=1;
                    } else if (a == 1) {
                        etDanhao.setText(barCode);
                        a=2;
                    } else {
                        return;
                    }
                }
            }
        }
    };

    @OnClick({R.id.bt_sure, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_sure:
                String etdan = etDanhao.getText().toString().trim();
                String etding = etDingdan.getText().toString().trim();
                if ((etdan==null || etdan.equals("")) && (etding ==null || etding.equals(""))){
                    ToastUtil.toast("入库单号和客户订单号至少填一个");
                    return;
                }
                Intent intent = new Intent(ReceiveActivity.this, ReceiveMessActivity.class);
                intent.putExtra("num1",etdan);
                intent.putExtra("num2",etding);
                startActivity(intent);
                break;
            case R.id.btn_cancel:
                this.finish();
                break;
        }
    }

}
