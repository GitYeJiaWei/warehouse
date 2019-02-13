package com.ioter.warehouse.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.ioter.warehouse.AppApplication;
import com.ioter.warehouse.R;
import com.ioter.warehouse.bean.GetStock;
import com.ioter.warehouse.common.CustomProgressDialog;
import com.ioter.warehouse.common.rx.RxHttpReponseCompat;
import com.ioter.warehouse.common.rx.subscriber.AdapterItemSubcriber;
import com.ioter.warehouse.common.util.ACache;
import com.ioter.warehouse.common.util.ACacheUtils;
import com.ioter.warehouse.common.util.SoundManage;
import com.ioter.warehouse.common.util.ToastUtil;
import com.zebra.adc.decoder.Barcode2DWithSoft;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindActivity extends NewBaseActivity {

    @BindView(R.id.bt_sure)
    Button btSure;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.et_danhao)
    EditText etDanhao;
    @BindView(R.id.et_dingdan)
    EditText etDingdan;
    @BindView(R.id.cb_islgnore)
    CheckBox cbIslgnore;
    @BindView(R.id.tv_tick)
    TextView tvTick;
    private int a = 1;
    private boolean check = false;
    private String name = null;
    private CustomProgressDialog progressDialog=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        ButterKnife.bind(this);

        setTitle("库存查询");

        name = ACache.get(AppApplication.getApplication()).getAsString("UserName");
        if (name == null) {
            ToastUtil.toast("请到系统设置中设置仓库");
        }

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

        cbIslgnore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    check = true;
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
                    SoundManage.PlaySound(FindActivity.this, SoundManage.SoundType.SUCCESS);
                    if (a == 2) {
                        etDingdan.setText(barCode);
                        a = 1;
                    } else if (a == 1) {
                        etDanhao.setText(barCode);
                        a = 2;
                    } else {
                        return;
                    }
                }
            }
        }
    };

    protected void takeData(String locId,String productId) {
        boolean isIgnoreLot = check;
        if (name == null) {
            ToastUtil.toast("请到系统设置中设置仓库");
            return;
        }

        progressDialog = new CustomProgressDialog(this, "获取数据中...");
        progressDialog.show();

        Map<String, String> params = new HashMap<>();
        params.put("loc", locId);
        params.put("productId", productId);
        params.put("isIgnoreLot", isIgnoreLot+"");
        params.put("whId", ACacheUtils.getWareIdByWhCode(name));

        AppApplication.getApplication().getAppComponent().getApiService().GetStock(params).compose(RxHttpReponseCompat.<List<GetStock>>compatResult())
                .subscribe(new AdapterItemSubcriber<List<GetStock>>(AppApplication.getApplication()) {
                    @Override
                    public void onNext(List<GetStock> recommendWhSites) {
                        if (recommendWhSites != null && recommendWhSites.size() > 0) {
                            Intent intent = new Intent(FindActivity.this, FindMessActivity.class);
                            intent.putExtra("map", (Serializable) recommendWhSites);
                            startActivity(intent);
                        }else {
                            ToastUtil.toast("库位或产品不存在");
                        }
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        super.onError(e);
                    }
                });
    }

    @OnClick({R.id.bt_sure, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_sure:
                String locId = etDanhao.getText().toString();
                String productId = etDingdan.getText().toString();
                if (TextUtils.isEmpty(locId) || TextUtils.isEmpty(productId)) {
                    tvTick.setText("库位和产品不能为空");
                    return;
                }
                takeData(locId,productId);
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }
}
