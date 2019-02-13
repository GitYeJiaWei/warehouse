package com.ioter.warehouse.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ioter.warehouse.AppApplication;
import com.ioter.warehouse.R;
import com.ioter.warehouse.bean.GetStock;
import com.ioter.warehouse.common.CustomProgressDialog;
import com.ioter.warehouse.common.rx.RxHttpReponseCompat;
import com.ioter.warehouse.common.rx.subscriber.AdapterItemSubcriber;
import com.ioter.warehouse.common.util.ACache;
import com.ioter.warehouse.common.util.ACacheUtils;
import com.ioter.warehouse.common.util.ToastUtil;
import com.ioter.warehouse.ui.fragment.FindMessFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindMessActivity extends NewBaseActivity implements FindMessFragment.OnFragmentInteractionListener{
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
    private FindMessFragment findMessFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private int current =1;
    private ConcurrentHashMap<Integer,ArrayList> map = new ConcurrentHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_mess);
        ButterKnife.bind(this);

        setTitle("库存查询");

        Intent intent = getIntent();
        ArrayList<GetStock> array = (ArrayList<GetStock>)intent.getSerializableExtra("map");
        map.clear();
        int key =1;
        try {
            for (GetStock info : array) {
                ArrayList<GetStock> arrayList = new ArrayList<>();
                arrayList.add(info);
                map.put(key, arrayList);
                key++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        showUI("nor");
    }



    private void showUI(String abc) {
        if (map == null) {
            ToastUtil.toast("库存查询失败，请返回重新查询");
            return;
        }

        if (abc.equals("nor")) {
            current = 1;
        } else if (abc.equals("left")) {
            if (current == 1) {
                return;
            }
            current--;
            fragmentTransaction.remove(findMessFragment);
        } else if (abc.equals("right")) {
            if (current == hashMap.size()) {
                return;
            }
            current++;
            fragmentTransaction.remove(findMessFragment);
        } else {
            return;
        }
        findMessFragment = FindMessFragment.newInstance(map.get(current), "传递来的数据1");
        fragmentManager = getSupportFragmentManager();
        //如果transaction  commit（）过  那么我们要重新得到transaction
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_layout, findMessFragment).commitAllowingStateLoss();
        tvSize.setText(current + "/" + map.size());
        if (current==map.size()){
            btRight.setVisibility(View.GONE);
        }else {
            btRight.setVisibility(View.VISIBLE);
        }
        if (current==1){
            btLeft.setVisibility(View.GONE);
        }else {
            btLeft.setVisibility(View.VISIBLE);
        }

    }

    @OnClick({R.id.bt_sure, R.id.bt_left, R.id.bt_right, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_sure:
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
