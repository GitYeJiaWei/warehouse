package com.ioter.warehouse.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ioter.warehouse.AppApplication;
import com.ioter.warehouse.R;
import com.ioter.warehouse.bean.BaseEpc;
import com.ioter.warehouse.common.util.SoundManage;
import com.ioter.warehouse.common.util.ToastUtil;
import com.zebra.adc.decoder.Barcode2DWithSoft;

public class SettingActivity extends NewBaseActivity {
    private Button tv_read,tv_scan;
    private TextView tv_read1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        tv_scan = findViewById(R.id.tv_scan);
        tv_read = findViewById(R.id.tv_read);
        tv_read1 = findViewById(R.id.tv_read1);
        new SettingActivity.InitBarCodeTask().execute();
        tv_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanBarcode();
            }
        });
    }

    //扫条码
    private void ScanBarcode()
    {
        if (barcode2DWithSoft != null)
        {
            barcode2DWithSoft.scan();
            barcode2DWithSoft.setScanCallback(ScanBack);
        }
    }

    public Barcode2DWithSoft.ScanCallback
            ScanBack = new Barcode2DWithSoft.ScanCallback()
    {
        @Override
        public void onScanComplete(int i, int length, byte[] bytes)
        {
            if (length < 1)
            {
            } else
            {
                String barCode = new String(bytes, 0, length);
                if (barCode != null && barCode.length() > 0)
                {
                    SoundManage.PlaySound(SettingActivity.this, SoundManage.SoundType.SUCCESS);
                    //收货弹出框
                    tv_read1.setText(barCode);
                }
            }
        }
    };


    //获取EPC群读数据
    @Override
    public void handleUi(BaseEpc baseEpc) {
        super.handleUi(baseEpc);
        tv_read1.setText(baseEpc._EPC);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 139 || keyCode == 280)
        {
            if (event.getRepeatCount() == 0)
            {
                readTag();
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
                readTag();
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    private void readTag() {
        if (tv_read.getText().toString().equals("开始扫描")) {
            if (AppApplication.mReader.startInventoryTag((byte) 0, (byte) 0)) {
                loopFlag = true;
                tv_read.setText("停止扫描");
                new TagThread(10).start();
            } else {
                AppApplication.mReader.stopInventory();
                loopFlag = false;
                tv_read.setText("开始扫描");
                ToastUtil.toast("开始扫描失败");
            }
        } else {
            AppApplication.mReader.stopInventory();
            loopFlag = false;
            tv_read.setText("开始扫描");
        }
    }

    @Override
    protected void initSound() {
        // 蜂鸣器发声
        AppApplication.getExecutorService().submit(new Runnable() {
            @Override
            public void run() {
                while (IsFlushList) {
                    synchronized (beep_Lock) {
                        try {
                            beep_Lock.wait();
                        } catch (InterruptedException e) {
                        }
                    }
                    if (IsFlushList) {
                        toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP);
                    }
                }
            }
        });

    }



    /**
     * 停止识别
     */
    public void stopInventory() {
        if (loopFlag) {
            loopFlag = false;
            if (AppApplication.mReader.stopInventory()) {
                tv_read.setText("开始扫描");
            } else {
                ToastUtil.toast("停止扫描失败");
            }
        }
    }



}
