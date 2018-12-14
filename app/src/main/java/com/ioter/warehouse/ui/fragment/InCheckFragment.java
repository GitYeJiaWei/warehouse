package com.ioter.warehouse.ui.fragment;

import android.media.ToneGenerator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ioter.warehouse.AppApplication;
import com.ioter.warehouse.R;
import com.ioter.warehouse.bean.BaseEpc;
import com.ioter.warehouse.bean.Check;
import com.ioter.warehouse.bean.SkuInfo;
import com.ioter.warehouse.common.CustomProgressDialog;
import com.ioter.warehouse.common.rx.RxHttpReponseCompat;
import com.ioter.warehouse.common.rx.subscriber.AdapterItemSubcriber;
import com.ioter.warehouse.common.util.ACacheUtils;
import com.ioter.warehouse.common.util.DataUtil;
import com.ioter.warehouse.common.util.ToastUtil;
import com.ioter.warehouse.ui.activity.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * im_pandian
 */
public class InCheckFragment extends InBaseFragment {

    @BindView(R.id.player_tv)
    TextView playerTv;
    @BindView(R.id.title_num_tv)
    TextView titleNumTv;
    @BindView(R.id.epc_lv)
    ListView epcLv;
    @BindView(R.id.totial)
    TextView totial;
    @BindView(R.id.start_btn)
    Button startBtn;
    Unbinder unbinder;

    public static InCheckFragment newInstance() {
        InCheckFragment inFragment = new InCheckFragment();
        return inFragment;
    }


    @Override
    public void handleUi(BaseEpc baseEpc) {
        super.handleUi(baseEpc);
        if (baseEpc._EPC == null || baseEpc._EPC.length() != 16) {
            return;
        }
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Iterator it = epcToReadDataMap.keySet().iterator();
        mEpcList.clear();
        while (it.hasNext()) {
            String key = (String) it.next();
            SkuInfo info = epcToReadDataMap.get(key);
            for (int i = 0; i < info.get_EPC_LIST.size(); i++) {
                if (info.get_EPC_LIST.get(i).getEpc().equals(baseEpc._EPC) && info.get_EPC_LIST.get(i).getProductName() == null) {
                    info.get_EPC_LIST.get(i).setProductName(format.format(date));
                    info.realCount += 1;
                }
            }
            mEpcList.add(info);
        }
        if (adapter != null) {
            adapter.update(mEpcList);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void init(View view) {
        super.init(view);
        startBtn.setText("开始扫描");

        playerTv.setText("操作员:" + ACacheUtils.getUserName());
        adapter = new MyAdapter(mActivity);
        epcLv.setAdapter(adapter);
        takeData();
    }


    @Override
    protected void setReaderParam() {
    }

    @Override
    public void setBarCode(String barCode) {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_epc_check;
    }

    @Override
    protected void takeData() {
        progressDialog = new CustomProgressDialog(mActivity, "获取盘点数据中...");
        progressDialog.show();

        Map<String,String> param = new HashMap<>();
        Map<String,String> params = new HashMap<>();

        String data = AppApplication.getGson().toJson(params);
        long time = System.currentTimeMillis()/1000;//获取系统时间的10位的时间戳
        String timestamp=String.valueOf(time);
        String m5 = "timestamp" + timestamp + "secret" + "iottest" + "data" + data;
        String sign= DataUtil.md5(m5);
        param.put("data",data);
        param.put("timestamp",timestamp+"");
        param.put("sign",sign);

        AppApplication.getApplication().getAppComponent().getApiService().getStockTakeList(param).compose(RxHttpReponseCompat.<List<Check>>compatResult())
                .subscribe(new AdapterItemSubcriber<List<Check>>(AppApplication.getApplication()) {
                    @Override
                    public void onNext(List<Check> recommendWhSites) {
                        if (recommendWhSites != null && recommendWhSites.size() > 0) {
                            totial.setText("totial : " + recommendWhSites.size());
                            epcToReadDataMap.clear();
                            for (Check info : recommendWhSites) {
                                try {
                                    String key = info.getStyle() + "@" + info.getColor() + "@" + info.getSize();

                                    Check check = new Check();
                                    check.setEpc(info.getEpc());
                                    check.setStyle(info.getStyle());
                                    check.setColor(info.getColor());
                                    check.setSize(info.getSize());
                                    if (!epcToReadDataMap.containsKey(key)) {
                                        SkuInfo skuInfo = new SkuInfo();
                                        skuInfo.style = info.getStyle();
                                        skuInfo.color = info.getColor();
                                        skuInfo.size = info.getSize();
                                        skuInfo.get_EPC_LIST.add(check);
                                        epcToReadDataMap.put(key, skuInfo);
                                    } else {
                                        epcToReadDataMap.get(key).get_EPC_LIST.add(check);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        listshow();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        super.onError(e);
                    }
                });
    }

    private void listshow() {
        Iterator it = epcToReadDataMap.keySet().iterator();
        mEpcList.clear();
        while (it.hasNext()) {
            String key1 = (String) it.next();
            SkuInfo temp = epcToReadDataMap.get(key1);
            mEpcList.add(temp);
        }
        if (adapter != null) {
            adapter.update(mEpcList);
        }
    }

    @Override
    protected View getItemView(int position, View convertView, ViewGroup parent, SkuInfo item) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.listitem_check_sku, parent, false);
            holder = new ViewHolder();
            holder.num_tv = convertView.findViewById(R.id.num_tv);
            holder.skustyle_tv = convertView.findViewById(R.id.skustyle_tv);
            holder.skucolor_tv = convertView.findViewById(R.id.skucolor_tv);
            holder.skusize_tv = convertView.findViewById(R.id.skusize_tv);
            holder.count_tv = convertView.findViewById(R.id.count_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.num_tv.setText(item.get_EPC_LIST.size() + "");
        holder.skustyle_tv.setText(item.style);
        holder.skucolor_tv.setText(item.color);
        holder.skusize_tv.setText(item.size);
        if (item.realCount == 0) {
            holder.count_tv.setText("0");
            holder.count_tv.setTextColor(getResources().getColor(R.color.md_red_A700));
        } else {
            holder.count_tv.setText(item.realCount + "");
            holder.count_tv.setTextColor(getResources().getColor(R.color.md_green_500));
        }
        return convertView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private class ViewHolder {
        TextView num_tv;
        TextView skucolor_tv;
        TextView count_tv;
        TextView skustyle_tv;
        TextView skusize_tv;

    }

    @Override
    protected void initSound() {
        AppApplication.getExecutorService().submit(new Runnable() { // 蜂鸣器发声
            @Override
            public void run() {
                while (IsFlushList) {
                    synchronized (beep_Lock) {
                        try {
                            beep_Lock.wait();
                        } catch (InterruptedException e) {
                        }
                    }
                    if (IsFlushList) {
                        toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP);
                    }
                }
            }
        });

    }

    @Override
    public void myOnKeyDwon() {
        readTag();
    }


    private void readTag() {
        if (startBtn.getText().toString().equals("开始扫描")) {
            if (((MainActivity) mActivity).mReader.startInventoryTag((byte) 0, (byte) 0)) {
                loopFlag = true;
                startBtn.setText("停止扫描");
                new TagThread(10).start();
            } else {
                ((MainActivity) mActivity).mReader.stopInventory();
                loopFlag = false;
                startBtn.setText("开始扫描");
                ToastUtil.toast("开始扫描失败");
            }
        } else {
            ((MainActivity) mActivity).mReader.stopInventory();
            loopFlag = false;
            startBtn.setText("开始扫描");
        }
    }

    @Override
    public void onDestroy() {
        stopInventory();
        super.onDestroy();
    }

    /**
     * 停止识别
     */
    public void stopInventory() {

        if (loopFlag) {
            loopFlag = false;
            if (((MainActivity) mActivity).mReader.stopInventory()) {
                startBtn.setText("开始扫描");
            } else {
                ToastUtil.toast("停止扫描失败");

            }

        }
    }

    @OnClick({R.id.start_btn})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.start_btn:
                readTag();
                break;
        }
    }


}
