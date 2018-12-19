package com.ioter.warehouse.ui.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.ioter.warehouse.AppApplication;
import com.ioter.warehouse.R;
import com.ioter.warehouse.common.util.SoundManage;
import com.zebra.adc.decoder.Barcode2DWithSoft;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoveMessActivity extends NewBaseActivity {

    @BindView(R.id.sp_cangku)
    Spinner spCangku;
    @BindView(R.id.bt_sure)
    Button btSure;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.ed_genzonghao)
    EditText edGenzonghao;
    @BindView(R.id.ed_kuwei)
    EditText edKuwei;
    private int a =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_mess);
        ButterKnife.bind(this);

        setTitle("库存移动");

        initView();
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
                    SoundManage.PlaySound(MoveMessActivity.this, SoundManage.SoundType.SUCCESS);
                    if (a == 2) {
                        edGenzonghao.setText(barCode);
                        a = 1;
                    } else if (a == 1) {
                        edKuwei.setText(barCode);
                        a = 2;
                    } else {
                        return;
                    }
                }
            }
        }
    };

    private void initView() {
        /*静态的显示下来出来的菜单选项，显示的数组元素提前已经设置好了
         * 第二个参数：已经编写好的数组
         * 第三个数据：默认的样式
         */
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.number_array, android.R.layout.simple_spinner_item);
        //设置spinner中每个条目的样式，同样是引用android提供的布局文件
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCangku.setAdapter(adapter);
        //设置默认值
        spCangku.setSelection(2, true);
        //spCangku.setPrompt("测试");
        spCangku.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
