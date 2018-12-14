package com.ioter.warehouse.ui.fragment;

import android.graphics.Color;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ioter.warehouse.AppApplication;
import com.ioter.warehouse.R;
import com.ioter.warehouse.bean.BaseBean;
import com.ioter.warehouse.bean.BaseEpc;
import com.ioter.warehouse.bean.Pack;
import com.ioter.warehouse.bean.Sku;
import com.ioter.warehouse.bean.SkuInfo;
import com.ioter.warehouse.common.CustomProgressDialog;
import com.ioter.warehouse.common.rx.RxHttpReponseCompat;
import com.ioter.warehouse.common.rx.subscriber.AdapterItemSubcriber;
import com.ioter.warehouse.common.util.ACacheUtils;
import com.ioter.warehouse.common.util.DataUtil;
import com.ioter.warehouse.common.util.ToastUtil;
import com.ioter.warehouse.ui.activity.MainActivity;
import com.ioter.warehouse.ui.dialog.BaseDialog;
import com.ioter.warehouse.ui.dialog.InDiffDialog;
import com.rscja.deviceapi.RFIDWithUHF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 收货
 */
public class InSingleRfidFragment extends InBaseFragment {


    //  paramUnique 单号
    //newInstance()设置静态工厂，保存参数
    public static InSingleRfidFragment newInstance() {
        InSingleRfidFragment inFragment = new InSingleRfidFragment();
        return inFragment;
    }

    //ConcurrentHashMap是Java 中支持高并发，高吞吐量的hashMap实现.它是基于线程安全的一个类
    protected ConcurrentHashMap<String, SkuInfo> skuDataMap = new ConcurrentHashMap<String, SkuInfo>();//此内向交貨單 sku详情

    @BindView(R.id.type_tv)
    TextView typeTv;
    @BindView(R.id.unique_tv)
    TextView uniqueTv;
    @BindView(R.id.type_entity_single_tv)
    TextView typeEntitySingleTv;
    @BindView(R.id.player_tv)
    TextView playerTv;
    @BindView(R.id.type_barcode_rfid_tv)
    TextView typeBarcodeRfidTv;
    @BindView(R.id.title_num_tv)
    TextView titleNumTv;
    @BindView(R.id.title_skuNum_tv)
    TextView titleSkuNumTv;
    @BindView(R.id.title_play_tv)
    TextView titlePlayTv;
    @BindView(R.id.title_real_tv)
    TextView titleRealTv;
    @BindView(R.id.title_diff_tv)
    TextView titleDiffTv;
    @BindView(R.id.epc_lv)
    ListView epcLv;
    @BindView(R.id.detail_play_tv)
    TextView detailPlayTv;
    @BindView(R.id.detail_real_tv)
    TextView detailRealTv;
    @BindView(R.id.detail_diff_tv)
    TextView detailDiffTv;
    @BindView(R.id.start_btn)
    Button startBtn;
    @BindView(R.id.back_btn)
    Button backBtn;
    @BindView(R.id.box_save_tv)
    TextView boxSaveTv;

    Unbinder unbinder;
    private String paramUnique;//单号

    @Override
    public void handleUi(BaseEpc baseEpc) {
        super.handleUi(baseEpc);
        if (baseEpc._EPC == null || baseEpc._EPC.length() != 16) {
            //ToastUtil.toast("EPC不存在或不符合规则");
            return;
        }

        Iterator it = epcToReadDataMap.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            SkuInfo skuInfo = epcToReadDataMap.get(key);
            if (skuInfo == null) {
                return;
            }
            for (int i=0;i<skuInfo._EPC_LIST.size();i++){
                if (skuInfo._EPC_LIST.get(i).epc.equals(baseEpc._EPC) && !skuInfo._EPC_LIST_MAP.get(baseEpc._EPC)) {
                    skuInfo.realCount++;//默认加1
                    skuInfo._EPC_LIST_MAP.put(baseEpc._EPC, true);//赋值为已盘点
                }
            }

        }
        showBoxDetail();

    }

    //展示详细信息
    private void showBoxDetail() {
        int mPlayCount = 0;
        int mRealCount = 0;

        //遍历EPC
        Iterator it = epcToReadDataMap.keySet().iterator();
        ArrayList<SkuInfo> infoList = new ArrayList<SkuInfo>();
        while (it.hasNext()) {
            String key = (String) it.next();
            SkuInfo skuInfo = epcToReadDataMap.get(key);
            infoList.add(skuInfo);
            mPlayCount += skuInfo._EPC_LIST.size();
            mRealCount += skuInfo.realCount;
        }
        if (adapter != null) {
            adapter.update(infoList);
        }
        detailPlayTv.setText("计划总数:" + mPlayCount);
        detailRealTv.setText("实际总数:" + mRealCount);
        if (mPlayCount == mRealCount) {
            detailDiffTv.setTextColor(getResources().getColor(R.color.black));
        } else {
            detailDiffTv.setTextColor(getResources().getColor(R.color.md_red_A200));
        }
        detailDiffTv.setText("差异数:" + (mPlayCount - mRealCount));
    }

    //获取差异表
    private ArrayList<Sku> getDiff() {
        ArrayList<Sku> diff = new ArrayList<Sku>();
        Iterator it = epcToReadDataMap.keySet().iterator();
        while (it.hasNext())//遍历map，找出对应的sku
        {
            String key = (String) it.next();
            SkuInfo info2 = epcToReadDataMap.get(key);
            for (int i=0;i<info2._EPC_LIST.size();i++){
                if (!info2._EPC_LIST_MAP.get(info2._EPC_LIST.get(i).epc)){
                    diff.add(info2._EPC_LIST.get(i));
                }
            }
        }
        return  diff;
    }

    private ArrayList<String> getEpcList() {
        ArrayList<String> succ = new ArrayList<String>();
        Iterator it = epcToReadDataMap.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            SkuInfo skuInfo = epcToReadDataMap.get(key);
            for (int i=0;i<skuInfo._EPC_LIST.size();i++){
                if (skuInfo._EPC_LIST_MAP.get(skuInfo._EPC_LIST.get(i).epc)){
                    succ.add(skuInfo._EPC_LIST.get(i).epc);
                }
            }
        }
        return succ;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void init(View view) {
        super.init(view);

        Bundle arguments = getArguments();
        if (arguments != null) {
            paramUnique = arguments.getString("paramUnique");
        }
        typeTv.setText("单证类型: 门店收货");
        uniqueTv.setText("单号:" + paramUnique);
        typeEntitySingleTv.setText("单人");
        typeBarcodeRfidTv.setText("RFID");
        playerTv.setText("操作员:" + ACacheUtils.getUserName());

        startBtn.setText("开始扫描");
        backBtn.setText("提交");

        adapter = new MyAdapter(mActivity);
        epcLv.setAdapter(adapter);
        //takeData();


    }

    @Override
    protected void setReaderParam() {
        //配置收货参数
        RFIDWithUHF mReader = ((MainActivity) mActivity).mReader;
        if (mReader != null) {
            char[] params = mReader.GetGen2();
            if (params != null && params.length == 14) {
                params[11] = 1;//session
                params[12] = 0;//target
                mReader.SetGen2(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8], params[9], params[10], params[11], params[12], params[13]);
            }
        }
    }

    @Override
    public void setBarCode(String barCode) {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_epc_in_single_rfid;
    }

    @Override
    protected void takeData() {
        Map<String,String> param = new HashMap<>();
        Map<String,String> params = new HashMap<>();
        params.put("whStockOutId",paramUnique);

        String data = AppApplication.getGson().toJson(params);
        long time = System.currentTimeMillis()/1000;//获取系统时间的10位的时间戳
        String timestamp=String.valueOf(time);
        String m5 = "timestamp" + timestamp + "secret" + "iottest" + "data" + data;
        String sign= DataUtil.md5(m5);
        param.put("data",data);
        param.put("timestamp",timestamp+"");
        param.put("sign",sign);

        progressDialog = new CustomProgressDialog(mActivity, "查询内向交货单中...");
        progressDialog.show();
        //通过单号获取sku
        AppApplication.getApplication().getAppComponent().getApiService().getInboundDeliveryPack(param).compose(RxHttpReponseCompat.<List<Pack>>compatResult())
                .subscribe(new AdapterItemSubcriber<List<Pack>>(AppApplication.getApplication()) {
                    //保存数据EPC,箱码
                    @Override
                    public void onNext(List<Pack> packList) {
                        if (packList != null && packList.size() > 0) {
                            epcToReadDataMap.clear();
                            //箱码计算
                            String saveBoxStr = "";
                            ArrayList<String> boxList = new ArrayList<>();
                            for (Pack item : packList) {
                                String keyValue =item.getStyle();
                                Sku sku =new Sku();
                                sku.epc =item.getEpc();
                                sku.boxNum =item.getBoxId();
                                sku.color =item.getColor();
                                sku.size =item.getSize();
                                sku.style =item.getStyle();
                                if (!epcToReadDataMap.containsKey(keyValue)) {

                                    SkuInfo info = new SkuInfo();
                                    info.skuQty.setStyleNo(item.getStyle());
                                    info.epc = item.getEpc();
                                    info._EPC_LIST.add(sku);
                                    info._EPC_LIST_MAP.put(item.getEpc(),false);
                                    epcToReadDataMap.put(keyValue, info);
                                }else {
                                    epcToReadDataMap.get(keyValue)._EPC_LIST.add(sku);
                                    epcToReadDataMap.get(keyValue)._EPC_LIST_MAP.put(item.getEpc(),false);
                                }
                                if (!boxList.contains(item.getBoxId())){
                                    saveBoxStr+=item.getBoxId()+"  ";
                                    boxList.add(item.getBoxId());
                                }
                            }
                            showBoxDetail();

                            boxSaveTv.setText("包含箱码(" + saveBoxStr + ")");
                            //box_save_tv.setText("已保存箱码总数(" + saveBoxStr.substring(0,saveBoxStr.length()-1)+")");
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
                        ((MainActivity) mActivity).press();//退出页面
                    }
                });
    }

    @Override
    protected View getItemView(int position, View convertView, ViewGroup parent, SkuInfo item) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.listitem_sku, parent, false);
            holder = new ViewHolder();
            holder.num_tv = convertView.findViewById(R.id.num_tv);
            holder.skuNum_tv = convertView.findViewById(R.id.skuNum_tv);
            holder.diff_tv = convertView.findViewById(R.id.diff_tv);
            holder.play_tv = convertView.findViewById(R.id.play_tv);
            holder.real_tv = convertView.findViewById(R.id.real_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.num_tv.setText(position + 1 + "");
        holder.skuNum_tv.setText(item.skuQty.getStyleNo());
        holder.diff_tv.setText(item._EPC_LIST.size() - item.realCount + "");
        holder.play_tv.setText(item._EPC_LIST.size() + "");
        holder.real_tv.setText(item.realCount + "");

        if (item.skuQty.getQty() == item.realCount) {
            holder.diff_tv.setTextColor(getResources().getColor(R.color.black));
        } else {
            holder.diff_tv.setTextColor(getResources().getColor(R.color.md_red_A200));
        }

        if (item.isEdit) {// 选中时设置单纯颜色
            convertView.setBackgroundColor(Color.parseColor("#00ff00"));
        } else {// 未选中时设置selector
            if (item.isMatch) {
                convertView.setBackgroundColor(Color.parseColor("#ffffff"));
            } else {
                convertView.setBackgroundColor(getResources().getColor(R.color.md_yellow_200));
            }
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
        TextView skuNum_tv;
        TextView diff_tv;
        TextView play_tv;
        TextView real_tv;
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

    //扫描，提交
    @OnClick({R.id.start_btn, R.id.back_btn})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.start_btn:
                readTag();//开始扫描
                break;
            case R.id.back_btn:
                if (epcToReadDataMap.isEmpty()) {
                    ToastUtil.toast("还没有收货数据哦");
                    return;
                }
                createDiffDialog();
                break;
        }
    }

    private void createDiffDialog() {
        final BaseDialog baseDialog = new BaseDialog(mActivity, 1);
        String content;
        final ArrayList<Sku> diff = getDiff();
        final boolean qtyMatch = diff.size() == 0 ? true : false;
        if (qtyMatch) {
            content = "确定提交收货数据？";
        } else {
            content = "收货数据存在差异，是否查看差异记录？";
        }

        baseDialog.setHintTvValue(content);
        baseDialog.setConfrimBtnOnclick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseDialog.dismiss();
                if (qtyMatch) {
                    stopInventory();
                    stockIn(qtyMatch);
                } else {
                    InDiffDialog diffDialog = new InDiffDialog(mActivity, 1, diff, new InDiffDialog.Icommit() {
                        @Override
                        public void commit() {
                            stopInventory();
                            stockIn(qtyMatch);
                        }
                    });
                    diffDialog.show();
                }
            }
        });
    }

    //提交数据
    private void stockIn(boolean qtyMatch) {
        ArrayList<String> epcList = getEpcList();
        if (epcList.size() == 0) {
            ToastUtil.toast("提交数据为空!");
            return;
        }
        String json = AppApplication.getGson().toJson(epcList);
        progressDialog = new CustomProgressDialog(mActivity, "数据提交中...");
        progressDialog.show();


        Map<String,String> params = new HashMap<>();
        params.put("userId", ACacheUtils.getUserId());
        params.put("whStockOutId", paramUnique);
        params.put("listEpcJson", json);

        String data = AppApplication.getGson().toJson(params);
        long time = System.currentTimeMillis()/1000;//获取系统时间的10位的时间戳
        String timestamp=String.valueOf(time);
        String m5 = "timestamp" + timestamp + "secret" + "iottest" + "data" + data;
        String sign= DataUtil.md5(m5);
        Map<String,String> param = new HashMap<>();
        param.put("data",data);
        param.put("timestamp",timestamp+"");
        param.put("sign",sign);

        AppApplication.getApplication().getAppComponent().getApiService().stockIn(param).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new AdapterItemSubcriber<BaseBean>(mActivity) {
            @Override
            public void onNext(BaseBean baseBean) {
                if (baseBean.success()) {
                    ToastUtil.toast("提交成功");
                    ((MainActivity) mActivity).press();
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


}
