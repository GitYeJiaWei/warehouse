package com.ioter.warehouse.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ioter.warehouse.AppApplication;
import com.ioter.warehouse.R;
import com.ioter.warehouse.bean.StockMoveModel;
import com.ioter.warehouse.common.CustomProgressDialog;
import com.ioter.warehouse.common.rx.RxHttpReponseCompat;
import com.ioter.warehouse.common.rx.subscriber.AdapterItemSubcriber;
import com.ioter.warehouse.common.util.ACache;
import com.ioter.warehouse.common.util.SoundManage;
import com.ioter.warehouse.common.util.ToastUtil;
import com.zebra.adc.decoder.Barcode2DWithSoft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    @BindView(R.id.tv_tick)
    TextView tvTick;
    private int a = 1;
    private CustomProgressDialog progressDialog= null;
    private StockMoveModel stockMoveModel =null;
    private int RAD =1;
    private String name = null;

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
        name = ACache.get(AppApplication.getApplication()).getAsString("UserName");
        if (name == null) {
            ToastUtil.toast("请到系统设置中设置仓库");
        }
    }

    protected void takeData(String barCode) {
        if (name == null) {
            ToastUtil.toast("请到系统设置中设置仓库");
            return;
        }
        progressDialog = new CustomProgressDialog(this, "获取数据中...");
        progressDialog.show();

        Map<String, String> params = new HashMap<>();
        params.put("whId",name);
        params.put("loc", barCode);
        hashMap.clear();
        AppApplication.getApplication().getAppComponent().getApiService().GetStockMoveInfo(params).compose(RxHttpReponseCompat.<List<StockMoveModel>>compatResult())
                .subscribe(new AdapterItemSubcriber<List<StockMoveModel>>(AppApplication.getApplication()) {
                    @Override
                    public void onNext(List<StockMoveModel> recommendWhSites) {
                        if (recommendWhSites != null && recommendWhSites.size() > 0) {
                            try {
                                for (StockMoveModel info : recommendWhSites) {
                                    String key = info.getProductId()+info.getTrackCode();
                                    ArrayList<StockMoveModel> arrayList = new ArrayList<>();
                                    arrayList.add(info);
                                    hashMap.put(key, arrayList);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        showUI();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        super.onError(e);
                    }
                });
    }

    private void showUI(){
        if (hashMap ==null){
            return;
        }
        if (hashMap.size()==1){
            Iterator it = hashMap.keySet().iterator();
            while (it.hasNext()){
                String key =(String) it.next();
                ArrayList<StockMoveModel> list = hashMap.get(key);
                stockMoveModel = list.get(0);
                edGenzonghao.setText(stockMoveModel.getTrackCode());
                edChanpin.setText(stockMoveModel.getProductId());
                edPinming.setText(stockMoveModel.getProductName());
            }
        }
        if (hashMap.size()>1){
            tvTick.setText("请扫描/输入原始跟踪号");
            a=2;
        }
    }

    private void showUIT(String barCode){
        int index=0;
        if (hashMap == null){
            return;
        }
        if (hashMap.size()>1){
            Iterator it = hashMap.keySet().iterator();
            while (it.hasNext()){
                String key =(String) it.next();
                ArrayList<StockMoveModel> list = hashMap.get(key);
                if (list.get(0).getTrackCode().equals(barCode)){
                    index++;
                    stockMoveModel=list.get(0);
                }
            }
            if (index==1){
                edChanpin.setText(stockMoveModel.getProductId());
                edPinming.setText(stockMoveModel.getProductName());
            }else {
                tvTick.setText("请扫描/输入产品");
                a=3;
            }
        }
    }

    private void showUIY(String barCode){
        if (hashMap ==null){
            return;
        }
        if (hashMap.size()>1){
            Iterator it = hashMap.keySet().iterator();
            while (it.hasNext()){
                String key =(String) it.next();
                ArrayList<StockMoveModel> list = hashMap.get(key);
                if (key.equals(list.get(0).getProductId()+list.get(0).getTrackCode())){
                    stockMoveModel = list.get(0);
                    edPinming.setText(stockMoveModel.getProductName());
                }
            }
        }
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
                        showUIT(barCode);
                    } else if (a == 1) {
                        edKuwei.setText(barCode);
                        takeData(barCode);
                    } else if (a == 3) {
                        edChanpin.setText(barCode);
                        showUIY(barCode);
                    } else {
                        return;
                    }
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RAD){
            if (resultCode==RESULT_OK){
                a=1;
                edKuwei.setText("");
                edGenzonghao.setText("");
                edChanpin.setText("");
                edPinming.setText("");
                if (stockMoveModel!=null){
                    stockMoveModel=null;
                }
            }
        }
    }

    @OnClick({R.id.bt_sure, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_sure:
                if (stockMoveModel==null){
                    ToastUtil.toast("请输入正确信息");
                    return;
                }
                Intent intent = new Intent(MoveActivity.this, MoveMessActivity.class);
                intent.putExtra("list",stockMoveModel);
                startActivityForResult(intent,RAD);
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }
}
