package com.ioter.warehouse.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ioter.warehouse.AppApplication;
import com.ioter.warehouse.R;
import com.ioter.warehouse.bean.BaseBean;
import com.ioter.warehouse.bean.ListUomBean;
import com.ioter.warehouse.bean.TrackBean;
import com.ioter.warehouse.common.CustomProgressDialog;
import com.ioter.warehouse.common.rx.RxHttpReponseCompat;
import com.ioter.warehouse.common.rx.subscriber.AdapterItemSubcriber;
import com.ioter.warehouse.common.util.ACacheUtils;
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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GroundActivity extends NewBaseActivity {
    @BindView(R.id.ed_genzonghao)
    EditText edGenzonghao;
    @BindView(R.id.ef_shuliang)
    EditText efShuliang;
    @BindView(R.id.sp_cangku)
    Spinner spCangku;
    @BindView(R.id.edt_kuwei)
    EditText edtKuwei;
    @BindView(R.id.ed_chanpin)
    EditText edChanpin;
    @BindView(R.id.edt_baozhuang)
    EditText edtBaozhuang;
    @BindView(R.id.ed_zongshu)
    EditText edZongshu;
    @BindView(R.id.ed_kuwei)
    EditText edKuwei;
    @BindView(R.id.edt_pinming)
    EditText edtPinming;
    @BindView(R.id.bt_sure)
    Button btSure;
    @BindView(R.id.tv_size)
    TextView tvSize;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.tv_tick)
    TextView tvTick;
    private CustomProgressDialog progressDialog;
    private int a = 1;
    private HashMap<String, String> map = new HashMap<>();
    private String selected =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ground);
        ButterKnife.bind(this);

        setTitle("上架");
    }

    protected void takeData(String barCode) {
        progressDialog = new CustomProgressDialog(this, "获取数据中...");
        progressDialog.show();

        Map<String, String> params = new HashMap<>();
        params.put("trackCode", barCode);

        AppApplication.getApplication().getAppComponent().getApiService().GetProductByTrackCode(params).compose(RxHttpReponseCompat.<List<TrackBean>>compatResult())
                .subscribe(new AdapterItemSubcriber<List<TrackBean>>(AppApplication.getApplication()) {
                    @Override
                    public void onNext(List<TrackBean> recommendWhSites) {
                        if (recommendWhSites != null && recommendWhSites.size() > 0) {
                            hashMap.clear();
                            try {
                                for (TrackBean info : recommendWhSites) {
                                    String key = info.getProductId();
                                    ArrayList<TrackBean> arrayList = new ArrayList<>();
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

    private void showUI() {
        if (hashMap == null) {
            return;
        }
        if (hashMap.size() > 1) {
            tvTick.setText("多个产品请扫描确认");
            a = 2;
            return;
        } else {
            a = 3;
            Iterator it = hashMap.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                ArrayList<TrackBean> trackBean = hashMap.get(key);
                efShuliang.setText(trackBean.get(0).getShelfQty() + "");
                edtBaozhuang.setText(trackBean.get(0).getPacking());
                edZongshu.setText(trackBean.get(0).getAlreadyQty() + "");
                edtKuwei.setText(trackBean.get(0).getRecommendLoc());
                edtPinming.setText(trackBean.get(0).getProductName());
                edChanpin.setText(trackBean.get(0).getProductId());
                init(trackBean.get(0).getListUom());
                tvSize.setText("1/1");
            }
        }

    }

    private void showUIT(String barCode) {
        if (hashMap == null) {
            return;
        }
        if(map.containsKey(barCode)){
            tvTick.setText("该产品已经提交请重新扫描");
            a = 2;
            return;
        }
        Iterator it = hashMap.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            if (barCode.equals(key)){
                ArrayList<TrackBean> trackBean = hashMap.get(key);
                efShuliang.setText(trackBean.get(0).getShelfQty() + "");
                edtBaozhuang.setText(trackBean.get(0).getPacking());
                edZongshu.setText(trackBean.get(0).getAlreadyQty() + "");
                edtKuwei.setText(trackBean.get(0).getRecommendLoc());
                edtPinming.setText(trackBean.get(0).getProductName());
                init(trackBean.get(0).getListUom());
                a = 3;
                tvSize.setText(map.size()+1+"/"+hashMap.size());
            }
        }
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
                    SoundManage.PlaySound(GroundActivity.this, SoundManage.SoundType.SUCCESS);
                    if (a == 2) {
                        edChanpin.setText(barCode);
                        showUIT(barCode);
                    } else if (a == 1) {
                        edGenzonghao.setText(barCode);
                        takeData(barCode);
                    } else if (a == 3) {
                        edKuwei.setText(barCode);
                        a = 1;
                    } else {
                        return;
                    }
                }
            }
        }
    };

    private void takeData(){
        String trackCode = edGenzonghao.getText().toString();
        String qty = efShuliang.getText().toString();
        String loc = edKuwei.getText().toString();
        String chan=edChanpin.getText().toString();

        TrackBean trackBean=null;
        if (hashMap!=null && !edChanpin.getText().equals("") ){
            trackBean =(TrackBean)hashMap.get(chan).get(0);
        }

        progressDialog = new CustomProgressDialog(this, "提交数据中...");
        progressDialog.show();

        Map<String, String> params = new HashMap<>();
        params.put("taskId", trackBean.getTaskId());
        params.put("trackCode", trackCode);
        params.put("productId", trackBean.getProductId());
        params.put("qty", qty);
        params.put("uom", selected);
        params.put("loc", loc);
        params.put("userId", ACacheUtils.getUserId());


        AppApplication.getApplication().getAppComponent().getApiService().Shelf(params).subscribeOn(Schedulers.io())
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
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }
}
