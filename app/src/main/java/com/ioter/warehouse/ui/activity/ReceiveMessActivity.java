package com.ioter.warehouse.ui.activity;

import android.content.Intent;
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
    @BindView(R.id.tv_tick)
    TextView tvTick;
    private int a = 1;
    protected static int RAG = 1;
    protected static int RAB = 2;
    protected static String TAG = "logware";
    protected CustomProgressDialog progressDialog;
    protected Map<String, String> map = new HashMap<>();
    private ArrayList<StockBean> sb = null;
    private ArrayList<ListLotBean> listLotBeans = null;
    private ArrayList<String> listEpc = null;
    private ArrayList<EPC> epclis = null;
    private String selected = null;
    private HashMap<String, Double> doubMap = new HashMap<>();
    private double result = 0;
    private String stockInId;
    private String orderNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_mess);
        ButterKnife.bind(this);

        setTitle("收货");

        edShouhuo.setText("");
        etDingdan.setText("*");

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
        edPlan.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edPlan.setFocusableInTouchMode(true);
                    edPlan.requestFocus();
                    a = 3;
                }
            }
        });
        takeData();
    }

    private void initView(final List<PackingBean> list) {
        doubMap.clear();
        /*
         * 第二个参数是显示的布局
         * 第三个参数是在布局显示的位置id
         * 第四个参数是将要显示的数据
         */
        ArrayList<String> arrayList = new ArrayList<>();
        int select = 0;
        for (int i = 0; i < list.size(); i++) {
            doubMap.put(list.get(i).getUom(), list.get(i).getQty());
            arrayList.add(list.get(i).getUom());
            if (list.get(i).isIsDefault()) {
                //默认item
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
                edShouhuo.setText("");
                if (epclis != null) {
                    epclis.clear();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    protected void takeData() {
        progressDialog = new CustomProgressDialog(this, "获取数据中...");
        progressDialog.show();

        stockInId = getIntent().getStringExtra("num1");
        orderNo = getIntent().getStringExtra("num2");

        Map<String, String> params = new HashMap<>();
        params.put("stockInId", stockInId);
        params.put("orderNo", orderNo);

        hashMap.clear();
        map.clear();

        AppApplication.getApplication().getAppComponent().getApiService().QueryAsn(params).compose(RxHttpReponseCompat.<List<StockBean>>compatResult())
                .subscribe(new AdapterItemSubcriber<List<StockBean>>(AppApplication.getApplication()) {
                    @Override
                    public void onNext(List<StockBean> recommendWhSites) {
                        if (recommendWhSites != null && recommendWhSites.size() > 0) {
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

    //根据产品展示数据
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
                sb = null;
            }
            sb = hashMap.get(barCode);
            edtPinming.setText(sb.get(0).getProductName());
            edtYuqi.setText(sb.get(0).getPreQty() + "");
            edtYishou.setText(sb.get(0).getStockQty() + "");
            edPlan.setText(sb.get(0).getPlanLoc() + "");
            edtBaozhuang.setText(sb.get(0).getPacking() + "");
            /*
             * 动态添显示下拉菜单的选项，可以动态添加元素
             */
            initView(sb.get(0).getListUom());
            listLotBeans = (ArrayList<ListLotBean>) sb.get(0).getListLot();
            listEpc = (ArrayList<String>) sb.get(0).getListEpc();
            tvTick.setText("请扫描/输入收货跟踪号，并选择/输入相关信息");
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

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case 66:
                    if (a == 2) {
                        commit();
                    } else if (a == 1) {
                        a = 3;
                        String bar = etDanhao.getText().toString();
                        showUI(bar);
                    }else if (a==3){
                        a = 2;
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
                    SoundManage.PlaySound(ReceiveMessActivity.this, SoundManage.SoundType.SUCCESS);
                    if (a == 2) {
                        etDingdan.setText(barCode);
                    } else if (a == 1) {
                        etDanhao.setText(barCode);
                        a = 3;
                        showUI(barCode);
                    } else if(a==3){
                        edPlan.setText(barCode);
                        a = 2;
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
                //清空数据，保存已提交数据，等待下次扫描
                String bar = etDanhao.getText().toString();
                if (!TextUtils.isEmpty(bar)) {
                    if (checkData() == 2) {
                        map.put(bar, bar);
                        if (map.size() == hashMap.size()) {
                            finish();
                        } else {
                            takeClear();
                        }
                    } else if (checkData() == 1) {
                        ArrayList<StockBean> s = hashMap.get(bar);
                        s.get(0).setStockQty(result);
                        takeClear();
                    }
                }
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

    private void takeClear() {
        if (epclis != null) {
            epclis = null;
        }
        if (sb != null) {
            sb = null;
        }
        if (listLotBeans != null) {
            listLotBeans = null;
        }
        if (listEpc != null) {
            listEpc = null;
        }
        a = 1;
        selected = null;
        result = 0;
        tvTick.setText("请扫描/输入产品");
        edPlan.setText("");
        etDingdan.setText("*");
        etDanhao.setText("");
        edtPinming.setText("");
        edtYuqi.setText("");
        edtYishou.setText("");
        edPlan.setText("");
        edtBaozhuang.setText("");
        edShouhuo.setText("");
    }

    private void commitData() {
        String name = ACache.get(AppApplication.getApplication()).getAsString("UserName");
        if (TextUtils.isEmpty(name)) {
            ToastUtil.toast("请到系统设置中设置仓库");
            return;
        }

        String stockLoc = edPlan.getText().toString();
        String trackCode = etDingdan.getText().toString();
        ArrayList<String> listEpcJson = new ArrayList<>();
        String stockQty = edShouhuo.getText().toString();
        if (TextUtils.isEmpty(stockLoc)) {
            ToastUtil.toast("请输入收货库位");
            return;
        }
        if (TextUtils.isEmpty(trackCode)) {
            ToastUtil.toast("请输入收货跟踪号");
            return;
        }
        if (TextUtils.isEmpty(stockQty)) {
            ToastUtil.toast("请输入收货数量");
            return;
        }
        if (checkData() == 3 || checkData() == 0) {
            return;
        }

        if (epclis != null) {
            for (int i = 0; i < epclis.size(); i++) {
                listEpcJson.add(epclis.get(i).getEpc());
            }
        }

        progressDialog = new CustomProgressDialog(this, "提交数据中...");
        progressDialog.show();

        Map<String, String> params = new HashMap<>();
        params.put("stockInId", stockInId);
        params.put("orderNo", orderNo);
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
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new AdapterItemSubcriber<BaseBean>(this) {
            @Override
            public void onNext(BaseBean baseBean) {
                if (baseBean.success()) {
                    ToastUtil.toast("提交成功");
                    String bar = etDanhao.getText().toString();
                    if (!TextUtils.isEmpty(bar)) {
                        if (checkData() == 2) {
                            map.put(bar, bar);
                            if (map.size() == hashMap.size()) {
                                finish();
                            } else {
                                takeClear();
                            }
                        } else if (checkData() == 1) {
                            ArrayList<StockBean> s = hashMap.get(bar);
                            s.get(0).setStockQty(result);
                            takeClear();
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

    //判断收货数据大小是否合理
    private int checkData() {
        Iterator it = doubMap.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            if (key.equals(selected)) {
                double t = doubMap.get(key);
                double l = doubMap.get("EA");
                double yuqi = Double.valueOf(edtYuqi.getText().toString());
                double yihou = Double.valueOf(edtYishou.getText().toString());
                int shouhuo = Integer.valueOf(edShouhuo.getText().toString());
                result = yihou + shouhuo * t / l;
                if (shouhuo < 0) {
                    ToastUtil.toast("收货数不能为负数");
                    return 3;
                } else if (yuqi - result < 0) {
                    ToastUtil.toast("收货数大于预期数-已收数");
                    return 0;
                } else if (yuqi - result > 0) {
                    return 1;
                }
            }
        }
        return 2;
    }

    private void commit() {
        if (listLotBeans == null || listLotBeans.size() == 0) {
            commitData();
        } else {
            String stockQty = edShouhuo.getText().toString();
            String stockLoc = edPlan.getText().toString();
            String trackCode = etDingdan.getText().toString();
            if (TextUtils.isEmpty(trackCode)) {
                ToastUtil.toast("收货跟踪号不能为空");
                return;
            }
            if (TextUtils.isEmpty(stockQty)) {
                ToastUtil.toast("收货数量不能为空");
                return;
            }
            if (TextUtils.isEmpty(stockLoc)) {
                ToastUtil.toast("收货库位不能为空");
                return;
            }
            if (checkData() == 3 || checkData() == 0) {
                return;
            }
            Intent intent1 = new Intent(ReceiveMessActivity.this, ReceiveDateActivity.class);
            intent1.putExtra("listlost", listLotBeans);//动态数组
            intent1.putExtra("epclis", epclis);//扫描的EPC
            intent1.putExtra("stockQty", stockQty);//收货数量
            intent1.putExtra("sb", sb);//获取到的查询数据
            intent1.putExtra("uom", selected);
            intent1.putExtra("stockLoc", stockLoc);
            intent1.putExtra("trackCode", trackCode);
            intent1.putExtra("stockInId", stockInId);
            intent1.putExtra("orderNo", orderNo);
            startActivityForResult(intent1, RAG);
        }
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
                commit();
                break;
            case R.id.btn_cancel:
                this.finish();
                break;
        }
    }
}
