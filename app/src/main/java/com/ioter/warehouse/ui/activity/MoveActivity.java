package com.ioter.warehouse.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bumptech.glide.load.engine.Resource;
import com.ioter.warehouse.AppApplication;
import com.ioter.warehouse.R;
import com.ioter.warehouse.common.util.SoundManage;
import com.zebra.adc.decoder.Barcode2DWithSoft;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoveActivity extends NewBaseActivity {

    @BindView(R.id.bt_sure)
    Button btSure;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.ed_kuwei)
    EditText edKuwei;
    @BindView(R.id.ed_genzonghao)
    EditText edGenzonghao;
    @BindView(R.id.ed_chanpin)
    EditText edChanpin;
    @BindView(R.id.ed_pinming)
    EditText edPinming;
    private int a = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move);
        ButterKnife.bind(this);

        edKuwei.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edKuwei.setFocusableInTouchMode(true);
                    edKuwei.requestFocus();
                    a = 1;
                }
            }
        });
        edGenzonghao.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edGenzonghao.setFocusableInTouchMode(true);
                    edGenzonghao.requestFocus();
                    a = 2;
                }
            }
        });
        edChanpin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edChanpin.setFocusableInTouchMode(true);
                    edChanpin.requestFocus();
                    a = 3;
                }
            }
        });

        setTitle("库存移动");
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
        if (AppApplication.barcode2DWithSoft != null) {
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
                    SoundManage.PlaySound(MoveActivity.this, SoundManage.SoundType.SUCCESS);
                    if (a == 2) {
                        edGenzonghao.setText(barCode);
                        a = 1;
                        edChanpin.setEnabled(false);
                        edChanpin.setBackgroundColor(getResources().getColor(R.color.dark_grey));
                    } else if (a == 1) {
                        edKuwei.setText(barCode);
                        a = 2;
                    } else if (a == 3) {
                        edChanpin.setText(barCode);
                        a = 1;
                        edGenzonghao.setEnabled(false);
                        edGenzonghao.setBackgroundColor(getResources().getColor(R.color.dark_grey));
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
                startActivity(new Intent(MoveActivity.this, MoveMessActivity.class));
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }
}
