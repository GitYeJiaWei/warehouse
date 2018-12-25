package com.ioter.warehouse.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ioter.warehouse.AppApplication;
import com.ioter.warehouse.R;
import com.ioter.warehouse.bean.TrackBean;
import com.ioter.warehouse.common.CustomProgressDialog;
import com.ioter.warehouse.common.rx.RxHttpReponseCompat;
import com.ioter.warehouse.common.rx.subscriber.AdapterItemSubcriber;
import com.ioter.warehouse.ui.fragment.GroundMessFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroundActivity extends NewBaseActivity implements GroundMessFragment.OnFragmentInteractionListener{
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
    private GroundMessFragment groundMessFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private CustomProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ground);
        ButterKnife.bind(this);

        setTitle("上架");
        groundMessFragment = GroundMessFragment.newInstance("传递来的数据1", "传递来的数据2");
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_layout, groundMessFragment);
        fragmentTransaction.commitAllowingStateLoss();

        takeData();
    }

    protected void takeData() {
        progressDialog = new CustomProgressDialog(this, "获取数据中...");
        progressDialog.show();

        Map<String, String> params = new HashMap<>();
        params.put("trackCode", "*");

       /* String data = AppApplication.getGson().toJson(params);
        long time = System.currentTimeMillis()/1000;//获取系统时间的10位的时间戳
        String timestamp=String.valueOf(time);
        String m5 = "timestamp" + timestamp + "secret" + "iottest" + "data" + data;
        String sign= DataUtil.md5(m5);
        Map<String,String> param = new HashMap<>();
        param.put("data",data);
        param.put("timestamp",timestamp+"");
        param.put("sign",sign);*/

        AppApplication.getApplication().getAppComponent().getApiService().GetProductByTrackCode(params).compose(RxHttpReponseCompat.<List<TrackBean>>compatResult())
                .subscribe(new AdapterItemSubcriber<List<TrackBean>>(AppApplication.getApplication()) {
                    @Override
                    public void onNext(List<TrackBean> recommendWhSites) {
                        if (recommendWhSites != null && recommendWhSites.size() > 0) {
                            hashMap.clear();
                            try {
                                for (TrackBean info : recommendWhSites) {
                                    String key = info.getTaskId();
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
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        super.onError(e);
                    }
                });
    }

    @OnClick({R.id.bt_sure, R.id.bt_left, R.id.bt_right, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_sure:
                finish();
                break;
            case R.id.bt_left:
                fragmentTransaction.remove(groundMessFragment);
                //如果transaction  commit（）过  那么我们要重新得到transaction
                groundMessFragment = GroundMessFragment.newInstance("传递来的数据2", "传递来的数据2");
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_layout, groundMessFragment).commitAllowingStateLoss();
                break;
            case R.id.bt_right:
                fragmentTransaction.remove(groundMessFragment);
                //如果transaction  commit（）过  那么我们要重新得到transaction
                groundMessFragment = GroundMessFragment.newInstance("传递来的数据3", "传递来的数据2");
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_layout, groundMessFragment).commitAllowingStateLoss();
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        groundMessFragment.onKeyDown(keyCode,event);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
