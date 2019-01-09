package com.ioter.warehouse.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ioter.warehouse.AppApplication;
import com.ioter.warehouse.R;
import com.ioter.warehouse.bean.EPC;
import com.ioter.warehouse.bean.SelectWindow;
import com.ioter.warehouse.bean.WindowsModelBean;
import com.ioter.warehouse.common.CustomProgressDialog;
import com.ioter.warehouse.common.rx.RxHttpReponseCompat;
import com.ioter.warehouse.common.rx.subscriber.AdapterItemSubcriber;
import com.ioter.warehouse.common.util.ToastUtil;
import com.ioter.warehouse.ui.adapter.ReceiveDialogadapter;
import com.ioter.warehouse.ui.widget.AutoListView;

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
    @BindView(R.id.bt_sure)
    Button btSure;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.paiking_record)
    AutoListView paikingRecord;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;
    @BindView(R.id.top)
    ImageView top;
    @BindView(R.id.et_chaxun)
    EditText etChaxun;
    @BindView(R.id.tv_Default)
    TextView tvDefault;
    @BindView(R.id.tv_DefaultText)
    TextView tvDefaultText;
    @BindView(R.id.btn_chaxun)
    Button btnChaxun;
    @BindView(R.id.tv_tick)
    TextView tvTick;
    private CustomProgressDialog progressDialog;
    private ReceiveDialogadapter receiveDialogadapter;
    private ArrayList<EPC> epcArrayList = new ArrayList<>();
    private int page = 1;
    private int windowsType = 0;
    private String DefaultText = null;
    private ArrayList<String> ListTitle = null;
    private String value = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_diaolg);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        WindowsModelBean windowsModelBean = (WindowsModelBean) intent.getSerializableExtra("windowsModelBean");
        windowsType = windowsModelBean.getWindowsType();
        DefaultText = windowsModelBean.getDefaultText();
        ListTitle = (ArrayList<String>) windowsModelBean.getListTitle();
        tvLeft.setText(ListTitle.get(0));
        tvRight.setText(ListTitle.get(1));
        tvDefault.setText(ListTitle.get(1));
        tvTick.setText("请通过关键字查询,点击选取"+ListTitle.get(1));

        receiveDialogadapter = new ReceiveDialogadapter(this);
        paikingRecord.setAdapter(receiveDialogadapter);

        //上拉加载
        paikingRecord.setOnLoadListener(new AutoListView.OnLoadListener() {
            @Override
            public void onLoad() {
                page++;
                onRefreshfruit();
            }

            @Override
            public void onAfterScroll(int firstVisibleItem) {
                if (firstVisibleItem >= 2) {
                    top.setVisibility(View.VISIBLE);
                } else {
                    top.setVisibility(View.GONE);
                }
            }
        });

        //下拉刷新监听
        swipe.setColorSchemeResources(R.color.colorPrimary);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                if (epcArrayList != null) {
                    epcArrayList.clear();
                }
                onRefreshfruit();
            }
        });

        //返回顶部
        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 6) {
                    paikingRecord.smoothScrollToPosition(0);//返回顶部由滑动效果
                } else {
                    paikingRecord.setSelection(0);//直接返回顶部
                }
            }
        });

        paikingRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == receiveDialogadapter.getCount())
                    return;
                EPC epc = epcArrayList.get(position);
                String state = epc.getData2();
                tvDefaultText.setText(state);
                value = epc.getData1();
            }
        });
        takeData();
    }

    /**
     * SwipeRefreshLayout 下拉刷新
     */
    private void onRefreshfruit() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                /*try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        takeData();//初始化数据
                        //receiveDialogadapter.updateDatas(epcArrayList);//更新adapter
                        swipe.setRefreshing(false);//刷新结束，隐藏刷新进度条

                        paikingRecord.onLoadComplete();
                        //paikingRecord.setResultSize(9);
                    }
                });
            }
        }).start();
    }

    private void takeData() {
        String keyword = etChaxun.getText().toString();

        progressDialog = new CustomProgressDialog(this, "获取数据中...");
        progressDialog.show();

        Map<String, String> params = new HashMap<>();
        params.put("type", windowsType + "");
        params.put("pageIndex", page + "");
        params.put("pageSize", 10 + "");
        params.put("keyword", keyword);

        AppApplication.getApplication().getAppComponent().getApiService().GetData(params).compose(RxHttpReponseCompat.<SelectWindow>compatResult())
                .subscribe(new AdapterItemSubcriber<SelectWindow>(AppApplication.getApplication()) {
                    @Override
                    public void onNext(SelectWindow recommendWhSites) {
                        if (recommendWhSites != null) {
                            for (int i = 0; i < recommendWhSites.getListData().size(); i++) {
                                ArrayList<String> list = (ArrayList<String>) recommendWhSites.getListData().get(i);
                                EPC epc = new EPC();
                                epc.setData1(list.get(0));
                                epc.setData2(list.get(1));
                                epcArrayList.add(epc);
                            }
                            if (epcArrayList != null) {
                                int count = recommendWhSites.getTotalCount();
                                if (count == epcArrayList.size()) {
                                    paikingRecord.setResultSize(9);
                                } else if (epcArrayList.size() == 0) {
                                    paikingRecord.setResultSize(0);
                                } else {
                                    paikingRecord.setResultSize(10);
                                }
                            }
                            receiveDialogadapter.updateDatas(epcArrayList);//更新adapter
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

    @OnClick({R.id.btn_chaxun, R.id.bt_sure, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_sure:
                if (TextUtils.isEmpty(value)) {
                    ToastUtil.toast("请选择" + ListTitle.get(1));
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("value", value);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_chaxun:
                page = 1;
                if (epcArrayList != null) {
                    epcArrayList.clear();
                }
                takeData();
                break;
        }
    }
}
