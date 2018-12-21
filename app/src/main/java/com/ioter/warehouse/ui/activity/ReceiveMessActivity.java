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
import com.ioter.warehouse.bean.StockBean;
import com.ioter.warehouse.common.CustomProgressDialog;
import com.ioter.warehouse.common.rx.RxHttpReponseCompat;
import com.ioter.warehouse.common.rx.subscriber.AdapterItemSubcriber;
import com.ioter.warehouse.common.util.SoundManage;
import com.zebra.adc.decoder.Barcode2DWithSoft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    private int a = 1;
    protected static int RAG = 1;
    protected CustomProgressDialog progressDialog;
    protected Map<String ,String> map = new HashMap<>();


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

        initView();
        takeData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        showUI();
    }

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

        AppApplication.getApplication().getAppComponent().getApiService().stock(params).compose(RxHttpReponseCompat.<List<StockBean>>compatResult())
                .subscribe(new AdapterItemSubcriber<List<StockBean>>(AppApplication.getApplication()) {
                    @Override
                    public void onNext(List<StockBean> recommendWhSites) {
                        if (recommendWhSites != null && recommendWhSites.size() > 0) {
                            hashMap.clear();
                            try {
                                for (StockBean info : recommendWhSites) {
                                    String key = info.getAsnDetailId();
                                    ArrayList<StockBean> arrayList = new ArrayList<>();
                                    arrayList.add(info);
                                    hashMap.put(key, arrayList);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                                /*try {
                                    for (int i = 0; i < info.getListLot().size(); i++) {
                                        if (info.getListLot().get(i).getType()==1){
                                            Map<String,String> map = AppApplication.getGson().fromJson(info.getListLot().get(i).getListOption().toString(), Map.class);
                                            Iterator it =map.keySet().iterator();
                                            while (it.hasNext()){
                                                String key1 =(String) it.next();
                                                String value = map.get(key1);
                                                etDanhao.setText(value);
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }*/
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
        Iterator iterator = hashMap.keySet().iterator();

        while (iterator.hasNext()) {
            String key1 = (String) iterator.next();
            if (!map.containsKey(key1)){
                ArrayList<StockBean> sb = hashMap.get(key1);
                edtPinming.setText(sb.get(0).getProductName());
                edtYuqi.setText(sb.get(0).getPreQty()+"");
                edtYishou.setText(sb.get(0).getStockQty()+"");
                map.put(key1,key1);
                break;
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
                    SoundManage.PlaySound(ReceiveMessActivity.this, SoundManage.SoundType.SUCCESS);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RAG) {
            if (resultCode == RESULT_OK) {
                showUI();
            }else {

            }
        }
    }

    @OnClick({R.id.bt_while, R.id.bt_sure, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_while:
                Intent intent = new Intent(ReceiveMessActivity.this, ReceiveScanActivity.class);
                startActivityForResult(intent,RAG);
                break;
            case R.id.bt_sure:
                startActivity(new Intent(ReceiveMessActivity.this, ReceiveDateActivity.class));
                break;
            case R.id.btn_cancel:
                this.finish();
                break;
        }
    }

}
