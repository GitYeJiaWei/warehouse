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
import com.zebra.adc.decoder.Barcode2DWithSoft;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckActivity extends NewBaseActivity {

    @BindView(R.id.bt_sure)
    Button btSure;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.et_pandian)
    EditText etPandian;
    @BindView(R.id.ed_kaishi)
    EditText edKaishi;
    @BindView(R.id.ed_jieshu)
    EditText edJieshu;
    private int a=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        ButterKnife.bind(this);

        setTitle("库存盘点");
        etPandian.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etPandian.setFocusableInTouchMode(true);
                    etPandian.requestFocus();
                    a = 1;
                }
            }
        });
        edKaishi.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edKaishi.setFocusableInTouchMode(true);
                    edKaishi.requestFocus();
                    a = 2;
                }
            }
        });
        edJieshu.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edJieshu.setFocusableInTouchMode(true);
                    edJieshu.requestFocus();
                    a = 3;
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
                    SoundManage.PlaySound(CheckActivity.this, SoundManage.SoundType.SUCCESS);
                    if (a == 2) {
                        edKaishi.setText(barCode);
                        a = 3;
                    } else if (a == 1) {
                        etPandian.setText(barCode);
                        a = 2;
                    } else if (a== 3){
                        edJieshu.setText(barCode);
                        a = 1;
                    }  else{
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
                startActivity(new Intent(CheckActivity.this, CheckMessActivity.class));
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }
}
