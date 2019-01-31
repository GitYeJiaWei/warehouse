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
import com.ioter.warehouse.bean.PickModel;
import com.ioter.warehouse.common.CustomProgressDialog;
import com.ioter.warehouse.common.rx.RxHttpReponseCompat;
import com.ioter.warehouse.common.rx.subscriber.AdapterItemSubcriber;
import com.ioter.warehouse.common.util.ACache;
import com.ioter.warehouse.common.util.ACacheUtils;
import com.ioter.warehouse.common.util.SoundManage;
import com.ioter.warehouse.common.util.ToastUtil;
import com.ioter.warehouse.ui.dialog.BaseDialog;
import com.zebra.adc.decoder.Barcode2DWithSoft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PickMessActivity extends NewBaseActivity {

    @BindView(R.id.bt_sure)
    Button btSure;
    @BindView(R.id.bt_left)
    Button btLeft;
    @BindView(R.id.tv_size)
    TextView tvSize;
    @BindView(R.id.bt_right)
    Button btRight;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.tv_tick)
    TextView tvTick;
    @BindView(R.id.ed_yuanshi)
    EditText edYuanshi;
    @BindView(R.id.ed_kuwei)
    EditText edKuwei;
    @BindView(R.id.ed_pinming)
    EditText edPinming;
    @BindView(R.id.et_chanpin)
    EditText etChanpin;
    @BindView(R.id.ed_jianhuo)
    EditText edJianhuo;
    @BindView(R.id.edt_baozhuang)
    EditText edtBaozhuang;
    @BindView(R.id.ed_guige)
    EditText edGuige;
    @BindView(R.id.ed_shuliang)
    EditText edShuliang;
    @BindView(R.id.sp_cangku)
    Spinner spCangku;
    private CustomProgressDialog progressDialog;
    private ConcurrentHashMap<Integer, PickModel> map = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, PickModel> map1 = new ConcurrentHashMap<>();
    private HashMap<String,Double> doubMap = new HashMap<>();
    private int current = 1;
    private String selected =null;
    private int a =1;
    private String name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_mess);
        ButterKnife.bind(this);
        setTitle("拣货");

        initView();
        edKuwei.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edKuwei.setFocusableInTouchMode(true);
                    edKuwei.requestFocus();
                    a = 1;
                    tvTick.setText("请扫描/输入原始库位");
                }
            }
        });
        etChanpin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etChanpin.setFocusableInTouchMode(true);
                    etChanpin.requestFocus();
                    a = 2;
                    tvTick.setText("请扫描/输入产品编码");
                }
            }
        });
        name = ACache.get(AppApplication.getApplication()).getAsString("UserName");
        if (name == null) {
            ToastUtil.toast("请到系统设置中设置仓库");
        }
    }

    protected void initView() {
        progressDialog = new CustomProgressDialog(this, "获取数据中...");
        progressDialog.show();

        String stockOutId = getIntent().getStringExtra("stockOutId");

        map1.clear();
        map.clear();
        Map<String, String> params = new HashMap<>();
        params.put("stockOutId", stockOutId);

        AppApplication.getApplication().getAppComponent().getApiService().GetPickTask(params).compose(RxHttpReponseCompat.<List<PickModel>>compatResult())
                .subscribe(new AdapterItemSubcriber<List<PickModel>>(AppApplication.getApplication()) {
                    @Override
                    public void onNext(List<PickModel> recommendWhSites) {
                        if (recommendWhSites != null && recommendWhSites.size() > 0) {
                            int key = 1;
                            try {
                                for (PickModel info : recommendWhSites) {
                                    map.put(key, info);
                                    map1.put(key,info);
                                    key++;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                        showUI("nor");
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        super.onError(e);
                    }
                });
    }

    private void remakeData(){
        map.clear();
        Object[] key_arr = map1.keySet().toArray();
        Arrays.sort(key_arr);
        int current1 = 1;
        for (Object key2 : key_arr) {
            int kk = (int) key2;
            map.put(current1, map1.get(kk));
            current1++;
        }
        map1.clear();
        Iterator it = map.keySet().iterator();
        while (it.hasNext()){
            int key = (int)it.next();
            map1.put(key,map.get(key));
        }
        showUI("nor");
    }

    private void showUI(String abc) {
        if (map == null || map.size() == 0) {
            ToastUtil.toast("出库单号不存在");
            return;
        }

        if (abc.equals("nor")) {
            current = 1;
            takeclear();
            takeUI(current);
        } else if (abc.equals("left")) {
            if (current == 1) {
                return;
            }
            current--;
            takeclear();
            takeUI(current);
        } else if (abc.equals("right")) {
            if (current == hashMap.size()) {
                return;
            }
            current++;
            takeclear();
            takeUI(current);
        } else {
            return;
        }

        tvSize.setText(current + "/" + map.size());
        if (current == map.size()) {
            btRight.setVisibility(View.GONE);
        } else {
            btRight.setVisibility(View.VISIBLE);
        }
        if (current == 1) {
            btLeft.setVisibility(View.GONE);
        } else {
            btLeft.setVisibility(View.VISIBLE);
        }
    }

    private void takeclear(){
        edYuanshi.setText("");
        edPinming.setText("");
        edJianhuo.setText("");
        edGuige.setText("");
        edtBaozhuang.setText("");
        edKuwei.setText("");
        edShuliang.setText("");
        etChanpin.setText("");
        selected = null;
        tvTick.setText("请扫描/输入原始库位");
    }

    private void takeUI(int barcode) {
       edYuanshi.setText(map.get(barcode).getSourceLoc());
       edPinming.setText(map.get(barcode).getProductName());
       edJianhuo.setText(map.get(barcode).getPickQty()+"");
       edGuige.setText(map.get(barcode).getPacking());
       init(map.get(barcode).getListUom());
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
            doubMap.put(list.get(i).getUom(),list.get(i).getQty());
            arrayList.add(list.get(i).getUom());
            if (list.get(i).isIsDefault()) {
                select = i;
                selected = list.get(i).getUom();
                edtBaozhuang.setText(selected);
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
                edShuliang.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    //判断收货数据大小是否合理
    private int checkData(){
        Iterator it = doubMap.keySet().iterator();
        while (it.hasNext()){
            String key = (String) it.next();
            if (key.equals(selected)){
                double t = doubMap.get(key);
                double l = doubMap.get("EA");
                double yuqi =Double.valueOf(edJianhuo.getText().toString());
                int shouhuo = Integer.valueOf(edShuliang.getText().toString());
                if (yuqi-shouhuo*t/l<0){
                    //ToastUtil.toast("盘点数量超出标准，请重新输入");
                    return 0;
                }else if (yuqi-shouhuo*t/l>0){
                    return 1;
                }
            }
        }
        return 2;
    }

    private void takeData(){
        String locId = edKuwei.getText().toString();
        String qty = edShuliang.getText().toString();
        String productId = etChanpin.getText().toString();
        if (!locId.equals(map.get(current).getSourceLoc())){
            ToastUtil.toast("原始库位不一致，请重新扫描");
            a = 1;
            tvTick.setText("请扫描/输入原始库位");
            return;
        }
        if (!productId.equals(map.get(current).getProductId())){
            ToastUtil.toast("产品编码不一致，请重新扫描");
            a = 2;
            tvTick.setText("请扫描/输入产品");
            return;
        }
        if (TextUtils.isEmpty(qty)){
            ToastUtil.toast("拣货数量不能为空");
            return;
        }
        if (checkData()==0){
            ToastUtil.toast("拣货数量超出标准，请重新输入");
            return;
        }
        if (name == null) {
            ToastUtil.toast("请到系统设置中设置仓库");
            return;
        }

        progressDialog = new CustomProgressDialog(this, "提交数据中...");
        progressDialog.show();

        Map<String, String> params = new HashMap<>();
        params.put("taskId", map.get(current).getTaskId());
        params.put("productId",productId);
        params.put("loc", locId);
        params.put("qty", qty);
        params.put("uom", selected);
        params.put("userId", ACacheUtils.getUserId());
        params.put("whId",ACacheUtils.getWareIdByWhCode(name));

        AppApplication.getApplication().getAppComponent().getApiService().Pick(params).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new AdapterItemSubcriber<BaseBean>(this) {
            @Override
            public void onNext(BaseBean baseBean) {
                if (baseBean.success()) {
                    ToastUtil.toast("提交成功");
                    if (map1.size()==1){
                        finish();
                    }else {
                        map1.remove(current);
                        remakeData();
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
                if (map == null || map.size() == 0) {
                    ToastUtil.toast("出库单号不存在");
                    return;
                }
                if (barCode != null && barCode.length() > 0) {
                    SoundManage.PlaySound(PickMessActivity.this, SoundManage.SoundType.SUCCESS);
                    if (a == 2) {
                        etChanpin.setText(barCode);
                        if (!barCode.equals(map.get(current).getProductId())){
                            ToastUtil.toast("产品编码不一致，请重新扫描");
                            a = 2;
                        }else {
                            tvTick.setText("请输入拣货数量，并提交");
                        }
                    } else if (a == 1) {
                        edKuwei.setText(barCode);
                        if (barCode.equals(map.get(current).getSourceLoc())){
                            a = 2;
                            tvTick.setText("请扫描/输入产品");
                        }else{
                            ToastUtil.toast("原始库位不一致，请重新扫描");
                            a = 1;
                            tvTick.setText("请扫描/输入原始库位");
                        }
                    }  else {
                        return;
                    }
                }
            }
        }
    };


    private void getPressDialog(String content)
    {
        final BaseDialog baseDialog = new BaseDialog(this, 1);
        baseDialog.setHintTvValue(content);
        baseDialog.setConfrimBtnOnclick(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                baseDialog.dismiss();
                finish();
            }
        });
        baseDialog.setCancelBtnOnclick(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                baseDialog.dismiss();
            }
        });
    }

    @OnClick({R.id.bt_sure, R.id.bt_left, R.id.bt_right, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_sure:
                if (map == null || map.size() == 0) {
                    finish();
                }else {
                    takeData();
                }
                break;
            case R.id.bt_left:
                showUI("left");
                a = 1;
                break;
            case R.id.bt_right:
                showUI("right");
                a = 1;
                break;
            case R.id.btn_cancel:
                if (map == null || map.size() == 0) {
                    finish();
                }
                if (map.size()>map1.size()){
                    getPressDialog("还有产品没有拣货，是否退出？");
                }
                break;
        }
    }


}
