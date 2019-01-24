package com.ioter.warehouse.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
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
    @BindView(R.id.edt_yishangjia)
    EditText edtYishangjia;
    @BindView(R.id.edt_genzonghao)
    EditText edtGenzonghao;
    private CustomProgressDialog progressDialog;
    private int a = 1;
    private HashMap<String, String> map = new HashMap<>();
    private String selected = null;
    private HashMap<String,Double> doubMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ground);
        ButterKnife.bind(this);

        setTitle("上架");
        edChanpin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edChanpin.setFocusableInTouchMode(true);
                    edChanpin.requestFocus();
                    a = 2;
                }
            }
        });
        edGenzonghao.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edGenzonghao.setFocusableInTouchMode(true);
                    edGenzonghao.requestFocus();
                    a = 1;
                }
            }
        });
        edKuwei.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edKuwei.setFocusableInTouchMode(true);
                    edKuwei.requestFocus();
                    a = 3;
                }
            }
        });
    }

    protected void takeData(String barCode) {
        progressDialog = new CustomProgressDialog(this, "获取数据中...");
        progressDialog.show();

        Map<String, String> params = new HashMap<>();
        params.put("trackCode", barCode);
        hashMap.clear();
        map.clear();

        AppApplication.getApplication().getAppComponent().getApiService().GetProductByTrackCode(params).compose(RxHttpReponseCompat.<List<TrackBean>>compatResult())
                .subscribe(new AdapterItemSubcriber<List<TrackBean>>(AppApplication.getApplication()) {
                    @Override
                    public void onNext(List<TrackBean> recommendWhSites) {
                        if (recommendWhSites != null && recommendWhSites.size() > 0) {
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
        if (hashMap == null || hashMap.size() == 0) {
            ToastUtil.toast("不存在该跟踪号，请重新扫描/输入");
            return;
        }
        if (hashMap.size() > 1) {
            tvTick.setText("该跟踪号有多个产品，请扫描/输入确认");
            a = 2;
            return;
        } else {
            a = 3;
            tvTick.setText("请扫描/输入目标库位，并选择/填写相关信息");
            Iterator it = hashMap.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                ArrayList<TrackBean> trackBean = hashMap.get(key);
                edZongshu.setText(trackBean.get(0).getShelfQty() + "");
                edtBaozhuang.setText(trackBean.get(0).getPacking());
                edtYishangjia.setText(trackBean.get(0).getAlreadyQty() + "");
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
        if (!hashMap.containsKey(barCode)) {
            ToastUtil.toast("该跟踪号不包含此产品");
            return;
        }
        if (map.containsKey(barCode)) {
            tvTick.setText("该产品已经提交请重新扫描");
            a = 2;
            return;
        }
        ArrayList<TrackBean> trackBean = hashMap.get(barCode);
        edZongshu.setText(trackBean.get(0).getShelfQty() + "");
        edtBaozhuang.setText(trackBean.get(0).getPacking());
        edtYishangjia.setText(trackBean.get(0).getAlreadyQty() + "");
        edtKuwei.setText(trackBean.get(0).getRecommendLoc());
        edtPinming.setText(trackBean.get(0).getProductName());
        init(trackBean.get(0).getListUom());
        a = 3;
        tvSize.setText(map.size() + 1 + "/" + hashMap.size());
        tvTick.setText("请扫描/输入目标库位,并选择/填写相关信息");
    }

    private void init(List<ListUomBean> list) {
        doubMap.clear();
        /*
         * 第二个参数是显示的布局
         * 第三个参数是在布局显示的位置id
         * 第四个参数是将要显示的数据
         */
        ArrayList<String> arrayList = new ArrayList<>();
        int select = 0;
        for (int i = 0; i < list.size(); i++) {
            arrayList.add(list.get(i).getUom());
            doubMap.put(list.get(i).getUom(),list.get(i).getQty());
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

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case 66:
                    if (a == 2) {
                        String bar = edChanpin.getText().toString();
                        showUIT(bar);
                    } else if (a == 1) {
                        String bar = edGenzonghao.getText().toString();
                        edtGenzonghao.setText(bar);
                        takeData(bar);
                    }else if (a==3){
                        takeData();
                    }
                    break;
            }
        }
        return super.dispatchKeyEvent(event);
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
                        edtGenzonghao.setText(barCode);
                        takeData(barCode);
                    } else if (a == 3) {
                        edKuwei.setText(barCode);
                    } else {
                        return;
                    }
                }
            }
        }
    };

    //判断收货数据大小是否合理
    private int checkData(){
        Iterator it = doubMap.keySet().iterator();
        while (it.hasNext()){
            String key = (String) it.next();
            if (key.equals(selected)){
                double t = doubMap.get(key);
                double l = doubMap.get("EA");
                double yuqi =Double.valueOf(edZongshu.getText().toString());
                double yihou =Double.valueOf(edtYishangjia.getText().toString());
                int shouhuo = Integer.valueOf(efShuliang.getText().toString());
                double result = yihou+shouhuo*t/l;
                if (shouhuo < 0) {
                    ToastUtil.toast("上架数不能为负数");
                    return 3;
                }else if (yuqi-result<0){
                    ToastUtil.toast("上架数大于预期数-已收数");
                    return 0;
                }else if (yuqi-result>0){
                    return 1;
                }
            }
        }
        return 2;
    }

    //清除数据
    private void takeclear(int b) {
        if (b == 1){
            tvTick.setText("请扫描/输入跟踪号");
            edGenzonghao.setText("");
            edtGenzonghao.setText("");
            edChanpin.setText("");
            efShuliang.setText("");
            edtBaozhuang.setText("");
            edZongshu.setText("");
            edKuwei.setText("");
            edtKuwei.setText("");
            edtPinming.setText("");
            edtYishangjia.setText("");
            selected = null;
            a = 1;
            tvSize.setText("");
        }else {
            tvTick.setText("请扫描/输入产品");
            edChanpin.setText("");
            efShuliang.setText("");
            edtBaozhuang.setText("");
            edZongshu.setText("");
            edKuwei.setText("");
            edtKuwei.setText("");
            edtPinming.setText("");
            edtYishangjia.setText("");
            selected = null;
            a = 2;
        }

    }

    private void takeData() {
        String trackCode = edtGenzonghao.getText().toString();
        String qty = efShuliang.getText().toString();
        String loc = edKuwei.getText().toString();
        String chan = edChanpin.getText().toString();

        if (TextUtils.isEmpty(chan)){
            ToastUtil.toast("产品不能为空");
            return;
        }
        if (TextUtils.isEmpty(qty)){
            ToastUtil.toast("上架数量不能为空");
            return;
        }
        if (TextUtils.isEmpty(loc)){
            ToastUtil.toast("目标库位不能为空");
            return;
        }
        if (TextUtils.isEmpty(trackCode)){
            ToastUtil.toast("目标跟踪号不能为空");
            return;
        }

        TrackBean trackBean = null;
        if (hashMap != null && !edChanpin.getText().equals("")) {
            trackBean = (TrackBean) hashMap.get(chan).get(0);
        }

        if (trackBean==null){
            ToastUtil.toast("产品与相关数据不符");
            return;
        }
        if (checkData()==0 || checkData() ==3){
            return;
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
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new AdapterItemSubcriber<BaseBean>(this) {
            @Override
            public void onNext(BaseBean baseBean) {
                if (baseBean.success()) {
                    ToastUtil.toast("提交成功");
                    String bar = edChanpin.getText().toString();
                    if (!TextUtils.isEmpty(bar)) {
                        map.put(bar, bar);
                        if (map.size() == hashMap.size()) {
                            takeclear(1);
                        } else {
                            takeclear(2);
                        }
                    }
                } else {
                    ToastUtil.toast("提交失败：" + baseBean.getMessage());
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
                takeData();
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }
}
