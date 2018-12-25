package com.ioter.warehouse.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.ioter.warehouse.AppApplication;
import com.ioter.warehouse.R;
import com.ioter.warehouse.bean.BaseEpc;
import com.ioter.warehouse.bean.EPC;
import com.ioter.warehouse.common.util.ToastUtil;
import com.ioter.warehouse.ui.adapter.RecevieScamadapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReceiveScanActivity extends NewBaseActivity {

    @BindView(R.id.lv_scan)
    ListView lvScan;
    @BindView(R.id.bt_sure)
    Button btSure;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    RecevieScamadapter recevieScamadapter;
    @BindView(R.id.ed_xuliehao)
    EditText edXuliehao;
    @BindView(R.id.et_pinming)
    EditText etPinming;
    @BindView(R.id.edt_yuqi)
    EditText edtYuqi;
    @BindView(R.id.edt_saomiao)
    EditText edtSaomiao;
    @BindView(R.id.edt_chayi)
    EditText edtChayi;
    private ArrayList<EPC> epcList = new ArrayList<>();
    private ArrayList<String> list = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_scan);
        ButterKnife.bind(this);

        setTitle("收货");

        recevieScamadapter = new RecevieScamadapter(this);
        lvScan.setAdapter(recevieScamadapter);
        Intent intent = getIntent();
        list = intent.getStringArrayListExtra("listepc");

        String pinming = intent.getStringExtra("pinming");
        String yuqi = intent.getStringExtra("yiqishu");
        if (pinming != null) {
            etPinming.setText(pinming);
        }
        if (yuqi != null) {
            edtYuqi.setText(yuqi);
        }
    }

    //获取EPC群读数据
    @Override
    public void handleUi(BaseEpc baseEpc) {
        super.handleUi(baseEpc);
        edXuliehao.setText(baseEpc._EPC);
        for (int i = 0; i < epcList.size(); i++) {
            if (epcList.get(i).getEpc().equals(baseEpc._EPC)) {
                return;
            }
        }
        if (list != null) {
            if (list.contains(baseEpc._EPC)) {
                return;
            }
        }

        EPC epc = new EPC();
        epc.setEpc(baseEpc._EPC);
        epcList.add(epc);
        recevieScamadapter.updateDatas(epcList);
        edtSaomiao.setText(epcList.size()+"");
        edtChayi.setText(Double.valueOf(edtYuqi.getText().toString())-(double) epcList.size()+"");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 139 || keyCode == 280) {
            if (event.getRepeatCount() == 0) {
                readTag(1);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 139 || keyCode == 280) {
            if (event.getRepeatCount() == 0) {
                readTag(2);
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    private void readTag(int a) {
        if (a == 1) {
            if (AppApplication.mReader.startInventoryTag((byte) 0, (byte) 0)) {
                loopFlag = true;
                new TagThread(10).start();
            } else {
                AppApplication.mReader.stopInventory();
                loopFlag = false;
                ToastUtil.toast("开始扫描失败");
            }
        } else {
            AppApplication.mReader.stopInventory();
            loopFlag = false;
        }
    }

    @OnClick({R.id.bt_sure, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_sure:
                Intent intent = new Intent();
                intent.putExtra("list", epcList);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }
}
