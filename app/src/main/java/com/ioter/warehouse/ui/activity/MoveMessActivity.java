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
import com.ioter.warehouse.bean.BaseBean;
import com.ioter.warehouse.bean.ListUomBean;
import com.ioter.warehouse.bean.StockMoveModel;
import com.ioter.warehouse.common.CustomProgressDialog;
import com.ioter.warehouse.common.rx.subscriber.AdapterItemSubcriber;
import com.ioter.warehouse.common.util.ACacheUtils;
import com.ioter.warehouse.common.util.SoundManage;
import com.ioter.warehouse.common.util.ToastUtil;
import com.zebra.adc.decoder.Barcode2DWithSoft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MoveMessActivity extends NewBaseActivity {

    @BindView(R.id.sp_cangku)
    Spinner spCangku;
    @BindView(R.id.bt_sure)
    Button btSure;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.ed_yiku)
    EditText edYiku;
    @BindView(R.id.ed_kucun)
    EditText edKucun;
    @BindView(R.id.ed_yuankuwie)
    EditText edYuankuwie;
    @BindView(R.id.ed_genzonghao)
    EditText edGenzonghao;
    @BindView(R.id.ed_keyong)
    EditText edKeyong;
    @BindView(R.id.ed_mubiao)
    EditText edMubiao;
    @BindView(R.id.ed_yuanyin)
    EditText edYuanyin;
    private String selected =null;
    private int a = 1;
    private CustomProgressDialog progressDialog;
    private StockMoveModel list=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_mess);
        ButterKnife.bind(this);

        setTitle("库存移动");

        initView();
        edMubiao.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edMubiao.setFocusableInTouchMode(true);
                    edMubiao.requestFocus();
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
                        edMubiao.setText(barCode);
                        a = 2;
                    } else {
                        return;
                    }
                }
            }
        }
    };

    private void initView() {
        list = (StockMoveModel) getIntent().getSerializableExtra("list");
        if (list==null){
            return;
        }
        edKucun.setText(list.getStockQty()+"");
        edKeyong.setText(list.getAvailQty()+"");
        edYuankuwie.setText(list.getLocId()+"");
        init(list.getListUom());
    }

    private void init(List<ListUomBean> list) {
        /*
         * 第二个参数是显示的布局
         * 第三个参数是在布局显示的位置id
         * 第四个参数是将要显示的数据
         */
        ArrayList<String> arrayList = new ArrayList<>();
        int select = 0;
        for (int i = 0; i < list.size(); i++) {
            arrayList.add(list.get(i).getUom());
            if (list.get(i).isIsDefault()) {
                select = i;
                selected = list.get(i).getUom();
            }
        }

        ArrayAdapter adapter2 = new ArrayAdapter(this, R.layout.item, R.id.text_item, arrayList);
        spCangku.setAdapter(adapter2);
        //设置默认值
        spCangku.setSelection(select, true);
        //spCangku.setPrompt("测试");
        spCangku.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void takeData(){
        if (list==null){
            return;
        }
        progressDialog = new CustomProgressDialog(this, "提交数据中...");
        progressDialog.show();
        String qty = edYiku.getText().toString();
        String sourceLoc = edYuankuwie.getText().toString();
        String targetLoc = edMubiao.getText().toString();
        String targetTrackCode = edGenzonghao.getText().toString();
        String reason = edYuanyin.getText().toString();

        Map<String, String> params = new HashMap<>();
        params.put("productId", list.getProductId());
        params.put("qty", qty);
        params.put("uom", selected);
        params.put("sourceLoc", sourceLoc);
        params.put("targetLoc", targetLoc);
        params.put("sourceTrackCode", list.getTrackCode());
        params.put("targetTrackCode", targetTrackCode);
        params.put("userId", ACacheUtils.getUserId());
        params.put("reason", reason);

        AppApplication.getApplication().getAppComponent().getApiService().StockMovePostParam(params).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new AdapterItemSubcriber<BaseBean>(this)
        {
            @Override
            public void onNext(BaseBean baseBean)
            {
                if (baseBean.success())
                {
                    ToastUtil.toast("提交成功");
                } else
                {
                    ToastUtil.toast("提交失败：" + baseBean.getMessage());
                }
            }

            @Override
            public void onComplete()
            {
                progressDialog.dismiss();
            }

            @Override
            public void onError(Throwable e)
            {
                progressDialog.dismiss();
                super.onError(e);
            }
        });
    }

    @OnClick({R.id.bt_sure, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_sure:
                takeData();
                finish();
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }
}
