package com.ioter.warehouse.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ioter.warehouse.AppApplication;
import com.ioter.warehouse.R;
import com.ioter.warehouse.bean.EPC;
import com.ioter.warehouse.bean.SelectWindow;
import com.ioter.warehouse.common.CustomProgressDialog;
import com.ioter.warehouse.common.rx.RxHttpReponseCompat;
import com.ioter.warehouse.common.rx.subscriber.AdapterItemSubcriber;
import com.ioter.warehouse.ui.adapter.ReceiveDialogadapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReceiveDiaolgActivity extends NewBaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.lv_dial)
    ListView lvDial;
    @BindView(R.id.bt_sure)
    Button btSure;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    private CustomProgressDialog progressDialog;
    private ReceiveDialogadapter receiveDialogadapter;
    private ArrayList<EPC> epcArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_diaolg);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String windowsType = intent.getStringExtra("windowsType");
        String DefaultText = intent.getStringExtra("DefaultText");
        ArrayList<String> ListTitle = intent.getStringArrayListExtra("ListTitle");

        receiveDialogadapter = new ReceiveDialogadapter(this);
        lvDial.setAdapter(receiveDialogadapter);

        takeData();
    }

    private void takeData() {
        progressDialog = new CustomProgressDialog(this, "获取数据中...");
        progressDialog.show();

        String stockInId = getIntent().getStringExtra("num1");
        String orderNo = getIntent().getStringExtra("num2");

        Map<String, String> params = new HashMap<>();
        params.put("type", stockInId);
        params.put("pageIndex", orderNo);
        params.put("pageSize", stockInId);
        params.put("keyword", orderNo);

        AppApplication.getApplication().getAppComponent().getApiService().GetData(params).compose(RxHttpReponseCompat.<SelectWindow>compatResult())
                .subscribe(new AdapterItemSubcriber<SelectWindow>(AppApplication.getApplication()) {
                    @Override
                    public void onNext(SelectWindow recommendWhSites) {
                        if (recommendWhSites != null) {
                            recommendWhSites.getTotalCount();
                            for (int i = 0; i < recommendWhSites.getListData().size(); i++) {
                                ArrayList<String> list = (ArrayList<String>) recommendWhSites.getListData().get(i);
                                EPC epc =new EPC();
                                epc.setData1(list.get(0));
                                epc.setData2(list.get(1));
                                epcArrayList.add(epc);
                            }
                            receiveDialogadapter.updateDatas(epcArrayList);
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
                break;
            case R.id.btn_cancel:
                break;
        }
    }
}
