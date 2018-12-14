package com.ioter.warehouse.ui.fragment;

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ioter.warehouse.bean.BaseEpc;
import com.ioter.warehouse.bean.SkuInfo;
import com.ioter.warehouse.di.component.AppComponent;
import com.ioter.warehouse.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基本
 */
public abstract class InBaseFragment extends BaseFragment
{

    protected MyAdapter adapter;

    protected ArrayList<SkuInfo> mEpcList = new ArrayList<SkuInfo>();

    protected ConcurrentHashMap<String, SkuInfo> epcToReadDataMap = new ConcurrentHashMap<String, SkuInfo>();

    protected Boolean IsFlushList = true; // 是否刷列表
    protected Object beep_Lock = new Object();
    protected ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_SYSTEM, ToneGenerator.MAX_VOLUME);

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            BaseEpc baseEpc = (BaseEpc) msg.obj;
            if(baseEpc!=null)
            {
                handleUi(baseEpc);
            }
        }
    };

    //处理ui
    public void handleUi(BaseEpc baseEpc)
    {
        synchronized (beep_Lock)
        {
            beep_Lock.notify();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int setLayout()
    {
        return getLayoutId();
    }


    @Override
    public void setupAcitivtyComponent(AppComponent appComponent)
    {

    }

    @Override
    public void init(View view)
    {
        setReaderParam();
    }

    @Override
    public void onResume()
    {
        IsFlushList = true;
        initSound();
        super.onResume();
    }

    @Override
    public void onPause()
    {
        IsFlushList = false;
        synchronized (beep_Lock)
        {
            beep_Lock.notifyAll();
        }
        super.onPause();
    }

    //各自的布局
    protected abstract int getLayoutId();

    //取数据
    protected abstract void takeData();

    //清除扫描数据
    public void clearData()
    {
        epcToReadDataMap.clear();
        mEpcList.clear();
        adapter.clear();
        takeData();
    }

    public void myOnKeyDwon() {

    }

    //配置读写器参数
    protected  void setReaderParam()
    {

    }

    //配置读写器参数
    protected  void initSound()
    {

    }


    //各自的布局
    protected abstract View getItemView(int position, View convertView, ViewGroup parent, SkuInfo skuInfo);

    public class MyAdapter extends BaseAdapter
    {

        private Context mContext;
        private ArrayList<SkuInfo> mDataList;

        public MyAdapter(Context context)
        {
            mContext = context;
            mDataList = new ArrayList<SkuInfo>();
        }

        public void update(ArrayList<SkuInfo> dataList)
        {
            if (dataList.size() == 0)
            {
                return;
            }
            mDataList.clear();
            mDataList.addAll(dataList);
            sortDatas();
            notifyDataSetChanged();
        }

        public void clear()
        {
            mDataList.clear();
            notifyDataSetChanged();
        }

        private void sortDatas()
        {
            if (mDataList.size() > 1)
            {
                Collections.sort(mDataList, new Comparator<SkuInfo>()
                {
                    @Override
                    public int compare(SkuInfo info0, SkuInfo info1)
                    {
                        if (info0.isEdit)
                        {
                            return -1;
                        }
                        if (info1.isEdit)
                        {
                            return 1;
                        }
                        return 0;
                    }
                });
            }
        }

        public ArrayList<SkuInfo> getDataList()
        {
            return mDataList;
        }

        @Override
        public int getCount()
        {
            return mDataList.size();
        }

        @Override
        public SkuInfo getItem(int position)
        {
            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            return getItemView(position, convertView, parent, getItem(position));
        }
    }

    protected boolean loopFlag = false;

    class TagThread extends Thread
    {

        private int mBetween = 80;
        HashMap<String, String> map;

        public TagThread(int iBetween) {
            mBetween = iBetween;
        }

        public void run() {
            String strTid;
            String strResult;

            String[] res = null;
            while (loopFlag) {

                res = ((MainActivity)mActivity).mReader.readTagFromBuffer();//.readTagFormBuffer();

                if (res != null) {

                    strTid = res[0];
                    if (!strTid.equals("0000000000000000")&&!strTid.equals("000000000000000000000000")) {
                        strResult = "TID:" + strTid + "\n";
                    } else {
                        strResult = "";
                    }
                    Message msg = handler.obtainMessage();
                    BaseEpc baseEpc = new BaseEpc();
                    baseEpc._EPC = ((MainActivity)mActivity).mReader.convertUiiToEPC(res[1]);
                    try
                    {
                        baseEpc.rssi = (new Double(Double.valueOf(res[2]))).intValue();
                    }catch (Exception e)
                    {

                    }
                    msg.obj = baseEpc;
                    handler.sendMessage(msg);
                }
                try {
                    sleep(mBetween);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }


}
