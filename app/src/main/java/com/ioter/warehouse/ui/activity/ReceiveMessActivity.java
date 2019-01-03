package com.ioter.warehouse.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.ioter.warehouse.bean.EPC;
import com.ioter.warehouse.bean.ListLotBean;
import com.ioter.warehouse.bean.PackingBean;
import com.ioter.warehouse.bean.StockBean;
import com.ioter.warehouse.common.CustomProgressDialog;
import com.ioter.warehouse.common.rx.RxHttpReponseCompat;
import com.ioter.warehouse.common.rx.subscriber.AdapterItemSubcriber;
import com.ioter.warehouse.common.util.ACache;
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

public class ReceiveMessActivity extends NewBaseActivity {

    @BindView(R.id.bt_while)
    Button btWhile;
    @BindView(R.id.bt_sure)
    Button btSure;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.et_danhao)
    EditText etDanhao;
    @BindView(R.id.et_dingdan)
    EditText etDingdan;
    @BindView(R.id.sp_cangku)
    Spinner spCangku;
    @BindView(R.id.edt_pinming)
    EditText edtPinming;
    @BindView(R.id.edt_yuqi)
    EditText edtYuqi;
    @BindView(R.id.edt_baozhuang)
    EditText edtBaozhuang;
    @BindView(R.id.edt_yishou)
    EditText edtYishou;
    @BindView(R.id.ed_plan)
    EditText edPlan;
    @BindView(R.id.ed_shouhuo)
    EditText edShouhuo;
    private int a = 1;
    private boolean size = false;
    protected static int RAG = 1;
    protected static int RAB = 2;
    protected static String TAG = "logware";
    protected CustomProgressDialog progressDialog;
    protected Map<String, String> map = new HashMap<>();
    private ArrayList<StockBean> sb = null;
    private ArrayList<ListLotBean> listLotBeans = null;
    private ArrayList<String> listEpc = null;
    private ArrayList<EPC> epclis = null;
    private String selected=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_mess);
        ButterKnife.bind(this);

        setTitle("收货");

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

        takeData();
    }

    private void initView(List<PackingBean> list) {
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

    protected void takeData() {
        progressDialog = new CustomProgressDialog(this, "获取数据中...");
        progressDialog.show();

        String stockInId = getIntent().getStringExtra("num1");
        String orderNo = getIntent().getStringExtra("num2");

        Map<String, String> params = new HashMap<>();
        params.put("stockInId", stockInId);
        params.put("orderNo", orderNo);

       /* String data = AppApplication.getGson().toJson(params);
        long time = System.currentTimeMillis()/1000;//获取系统时间的10位的时间戳
        String timestamp=String.valueOf(time);
        String m5 = "timestamp" + timestamp + "secret" + "iottest" + "data" + data;
        String sign= DataUtil.md5(m5);
        Map<String,String> param = new HashMap<>();
        param.put("data",data);
        param.put("timestamp",timestamp+"");
        param.put("sign",sign);*/

        AppApplication.getApplication().getAppComponent().getApiService().QueryAsn(params).compose(RxHttpReponseCompat.<List<StockBean>>compatResult())
                .subscribe(new AdapterItemSubcriber<List<StockBean>>(AppApplication.getApplication()) {
                    @Override
                    public void onNext(List<StockBean> recommendWhSites) {
                        if (recommendWhSites != null && recommendWhSites.size() > 0) {
                            hashMap.clear();
                            map.clear();
                            try {
                                for (StockBean info : recommendWhSites) {
                                    String key = info.getProductId();
                                    ArrayList<StockBean> arrayList = new ArrayList<>();
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
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        super.onError(e);
                    }
                });
    }

    private void showUI(String barCode) {
        if (hashMap == null) {
            return;
        }
        if (!hashMap.containsKey(barCode)) {
            ToastUtil.toast("不包含此产品，请重新扫描");
            a = 1;
            return;
        }
        if (map.containsKey(barCode)) {
            ToastUtil.toast("此产品已提交，请重新扫描");
            a = 1;
            return;
        } else {
            if (sb != null) {
                sb.clear();
            }
            sb = hashMap.get(barCode);
            edtPinming.setText(sb.get(0).getProductName());
            edtYuqi.setText(sb.get(0).getPreQty() + "");
            edtYishou.setText(sb.get(0).getStockQty() + "");
            edPlan.setText(sb.get(0).getPlanLoc() + "");
            edtBaozhuang.setText(sb.get(0).getPacking() + "");
            map.put(barCode, barCode);
            /*
             * 动态添显示下拉菜单的选项，可以动态添加元素
             */
            initView(sb.get(0).getListUom());

            listLotBeans = (ArrayList<ListLotBean>) sb.get(0).getListLot();

            listEpc = (ArrayList<String>) sb.get(0).getListEpc();
        }

        if (map.size() == hashMap.size()) {
            Log.d(TAG, "showUI: map" + map.size() + " hashmap" + hashMap.size());
            size = true;
        } else {
            Log.d(TAG, "showUI: map" + map.size() + " hashmap" + hashMap.size());
            size = false;
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
                    SoundManage.PlaySound(ReceiveMessActivity.this, SoundManage.SoundType.SUCCESS);
                    if (a == 2) {
                        etDingdan.setText(barCode);
                        a = 1;
                    } else if (a == 1) {
                        etDanhao.setText(barCode);
                        a = 2;
                        showUI(barCode);
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
        if (requestCode == RAG) {
            if (resultCode == RESULT_OK) {
                //清空数据，等待下次扫描
                takeClear();
            }
        }
        if (requestCode == RAB) {
            if (resultCode == RESULT_OK) {
                epclis = (ArrayList<EPC>) data.getSerializableExtra("list");
                if (epclis != null) {
                    edShouhuo.setText(epclis.size() + "");
                }
            }
        }
    }

    private void takeClear(){
        if (epclis!=null){
            epclis.clear();
        }
        sb.clear();
        if (listLotBeans!=null){
            listLotBeans.clear();
        }
        listEpc.clear();
        selected = null;
        edPlan.setText("");
        etDingdan.setText("");
        etDanhao.setText("");
        edtPinming.setText("");
        edtYuqi.setText("");
        edtYishou.setText("");
        edPlan.setText("");
        edtBaozhuang.setText("");
        edShouhuo.setText("");
    }

    private void commitData(){
        String stockLoc = edPlan.getText().toString();
        String trackCode =etDingdan.getText().toString();
        ArrayList<String> listEpcJson = new ArrayList<>();
        String stockQty ="0";
        if (epclis!=null){
            stockQty = epclis.size()+"";
            for (int i = 0; i < epclis.size(); i++) {
                listEpcJson.add(epclis.get(i).getEpc());
            }
        }

        String name = ACache.get(AppApplication.getApplication()).getAsString("UserName");
        if (name==null){
            ToastUtil.toast("请到系统设置中设置仓库");
        }
        progressDialog = new CustomProgressDialog(this, "提交数据中...");
        progressDialog.show();

        Map<String, String> params = new HashMap<>();
        params.put("asnDetailId", sb.get(0).getAsnDetailId());
        params.put("productId", sb.get(0).getProductId());
        params.put("stockQty", stockQty);
        params.put("uom", selected);
        params.put("stockLoc", stockLoc);
        params.put("trackCode", trackCode);
        params.put("listEpcJson", AppApplication.getGson().toJson(listEpcJson));
        params.put("listLotTitleJson", "");
        params.put("listLotValueJson", "");
        params.put("userId", ACacheUtils.getUserId());
        params.put("whId", ACacheUtils.getWareIdByWhCode(name));

        AppApplication.getApplication().getAppComponent().getApiService().StockIn(params).subscribeOn(Schedulers.io())
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
                takeClear();
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

    @OnClick({R.id.bt_while, R.id.bt_sure, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_while:
                Intent intent = new Intent(ReceiveMessActivity.this, ReceiveScanActivity.class);
                intent.putExtra("listepc", listEpc);
                intent.putExtra("pinming", edtPinming.getText().toString());
                intent.putExtra("yiqishu", edtYuqi.getText().toString());
                startActivityForResult(intent, RAB);
                break;
            case R.id.bt_sure:
                if (listLotBeans == null || listLotBeans.size() == 0) {
                    if (size){
                        //提交数据，并退出
                        commitData();
                        finish();
                    }else {
                        commitData();
                        //提交数据,清空数据，等待下次扫描
                    }
                } else {
                    Intent intent1 = new Intent(ReceiveMessActivity.this, ReceiveDateActivity.class);
                    intent1.putExtra("size", size);//返回界面的依据
                    intent1.putExtra("listlost", listLotBeans);//动态数组
                    intent1.putExtra("epclis", epclis);//扫描的EPC
                    intent1.putExtra("sb", sb);//获取到的查询数据
                    intent1.putExtra("uom",selected);
                    String stockLoc = edPlan.getText().toString();
                    String trackCode =etDingdan.getText().toString();
                    intent1.putExtra("stockLoc",stockLoc);
                    intent1.putExtra("trackCode",trackCode);
                    startActivityForResult(intent1, RAG);
                }
                break;
            case R.id.btn_cancel:
                this.finish();
                break;
        }
    }

}
