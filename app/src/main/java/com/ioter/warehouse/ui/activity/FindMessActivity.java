package com.ioter.warehouse.ui.activity;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ioter.warehouse.R;
import com.ioter.warehouse.ui.fragment.FindMessFragment;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_mess);
        ButterKnife.bind(this);

        setTitle("库存查询");
        findMessFragment = FindMessFragment.newInstance("传递来的数据1", "传递来的数据2");
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_layout, findMessFragment);
        fragmentTransaction.commitAllowingStateLoss();

    }

    @OnClick({R.id.bt_sure, R.id.bt_left, R.id.bt_right, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_sure:
                startActivity(new Intent(FindMessActivity.this,FindDataActivity.class));
                break;
            case R.id.bt_left:
                fragmentTransaction.remove(findMessFragment);
                //如果transaction  commit（）过  那么我们要重新得到transaction
                findMessFragment = FindMessFragment.newInstance("传递来的数据2", "传递来的数据2");
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_layout, findMessFragment).commitAllowingStateLoss();
                break;
            case R.id.bt_right:
                fragmentTransaction.remove(findMessFragment);
                //如果transaction  commit（）过  那么我们要重新得到transaction
                findMessFragment = FindMessFragment.newInstance("传递来的数据3", "传递来的数据2");
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_layout, findMessFragment).commitAllowingStateLoss();
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
