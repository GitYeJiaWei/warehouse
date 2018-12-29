package com.ioter.warehouse.ui.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.ioter.warehouse.bean.StockTake;
import com.ioter.warehouse.common.CustomProgressDialog;
import com.ioter.warehouse.common.rx.subscriber.AdapterItemSubcriber;
import com.ioter.warehouse.common.util.ACacheUtils;
import com.ioter.warehouse.common.util.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CheckMessActivity extends NewBaseActivity {

    @BindView(R.id.bt_while)
    Button btWhile;
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
    @BindView(R.id.ed_mubiao)
    EditText edMubiao;
    @BindView(R.id.ed_kuwei)
    EditText edKuwei;
    @BindView(R.id.ed_pinming)
    EditText edPinming;
    @BindView(R.id.et_shangpin)
    EditText etShangpin;
    @BindView(R.id.ed_kucun)
    EditText edKucun;
    @BindView(R.id.edt_baozhuang)
    EditText edtBaozhuang;
    @BindView(R.id.ed_pandian)
    EditText edPandian;
    @BindView(R.id.sp_cangku)
    Spinner spCangku;
    @BindView(R.id.ed_danwei)
    EditText edDanwei;
    private int current = 1;
    private HashMap<Integer, StockTake> map1 = new HashMap<>();
    private String selected = null;
    private CustomProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_mess);
        ButterKnife.bind(this);

        setTitle("库存盘点");

        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        String start = intent.getStringExtra("start");
        String end = intent.getStringExtra("end");
        HashMap<Integer, StockTake> map =
                (HashMap<Integer, StockTake>) intent.getSerializableExtra("list");
        if (start == null || end == null || map == null) {
            return;
        }

        int key = 1;
        if (start.equals("start")) {
            key = 1;
        } else {
            Iterator it = map.keySet().iterator();
            while (it.hasNext()) {
                int key1 = (int) it.next();
                if (start.equals(map.get(key1).getTaskId())) {
                    key = key1;
                }
            }
        }

        int keydown = map.size();
        if (end.equals("end")) {
            keydown = map.size();
        } else {
            Iterator it = map.keySet().iterator();
            while (it.hasNext()) {
                int key1 = (int) it.next();
                if (end.equals(map.get(key1).getTaskId())) {
                    keydown = key1;
                }
            }
        }
        Object[] key_arr = map.keySet().toArray();
        Arrays.sort(key_arr);
        int current1 = 1;
        for (Object key2 : key_arr) {
            int kk = (int) key2;
            if (kk >= key && kk <= keydown) {
                map1.put(current1, map.get(kk));
                current1++;
            }
        }
        showUI("nor");
    }

    private void takeUI(int a) {
        edMubiao.setText(map1.get(a).getLoc());
        edPinming.setText(map1.get(a).getProductName());
        etShangpin.setText(map1.get(a).getProductId());
        edKucun.setText(map1.get(a).getQty() + "");
        edtBaozhuang.setText(map1.get(a).getPacking());
        initView(map1.get(a).getListUom());
    }

    private void initView(List<ListUomBean> list) {
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
                edDanwei.setText(selected);
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

    private void takeData() {

       /* ArrayList<String> listEpcJson = new ArrayList<>();
        for (int i = 0; i < epclis.size(); i++) {
            listEpcJson.add(epclis.get(i).getEpc());
        }*/

        progressDialog = new CustomProgressDialog(this, "提交数据中...");
        progressDialog.show();
        String loc = edMubiao.getText().toString();
        String qty = edPandian.getText().toString();


        Map<String, String> params = new HashMap<>();
        params.put("taskId", map1.get(current).getTaskId());
        params.put("loc", loc);
        params.put("qty", qty);
        params.put("uom", selected);
        params.put("listEpcJson", "");
        params.put("userId", ACacheUtils.getUserId());

        AppApplication.getApplication().getAppComponent().getApiService().StockTask(params).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new AdapterItemSubcriber<BaseBean>(this) {
            @Override
            public void onNext(BaseBean baseBean) {
                if (baseBean.success()) {
                    ToastUtil.toast("提交成功");
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

    private void showUI(String abc) {
        if (map1 == null) {
            return;
        }

        if (abc.equals("nor")) {
            current = 1;
            takeUI(current);
        } else if (abc.equals("left")) {
            if (current == 1) {
                return;
            }
            current--;
            takeUI(current);
        } else if (abc.equals("right")) {
            if (current == hashMap.size()) {
                return;
            }
            current++;
            takeUI(current);
        } else {
            return;
        }

        tvSize.setText(current + "/" + map1.size());
        if (current == map1.size()) {
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

    @OnClick({R.id.bt_while, R.id.bt_sure, R.id.bt_left, R.id.bt_right, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_while:
                startActivity(new Intent(CheckMessActivity.this, CheckScanActivity.class));
                break;
            case R.id.bt_sure:
                takeData();
                finish();
                break;
            case R.id.bt_left:
                showUI("left");
                break;
            case R.id.bt_right:
                showUI("right");
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }
}
