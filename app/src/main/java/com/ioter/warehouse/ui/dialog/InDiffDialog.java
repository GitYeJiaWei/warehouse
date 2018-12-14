package com.ioter.warehouse.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ioter.warehouse.R;
import com.ioter.warehouse.bean.Sku;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Connect password dialog to enter reader password to connect with reader
 */
public class InDiffDialog extends Dialog
{

    @BindView(R.id.epc_lv)
    ListView epc_lv;
    MyAdapter adapter;
    private Context context;
    @BindView(R.id.commit_btn)
    Button commit_btn;
    @BindView(R.id.title_num_tv)
    TextView title_num_tv;
    @BindView(R.id.back_btn)
    Button back_btn;
    private ArrayList<Sku> mDiffList;
    private Icommit icommit;

    public interface Icommit
    {
        public void commit();
    }

    /**
     * Constructor of the class
     *
     * @param activity activity context
     */
    public InDiffDialog(Activity activity, int type, ArrayList<Sku> diff, Icommit icommit)
    {
        super(activity);
        this.context = activity;
        this.mDiffList = diff;
        this.icommit = icommit;
        setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_epc_in_diff);
        ButterKnife.bind(this);

        adapter = new MyAdapter(context);
        epc_lv.setAdapter(adapter);
        if (mDiffList != null && mDiffList.size() > 0)
        {
            adapter.update(mDiffList);
        }
    }

    @OnClick({R.id.commit_btn,R.id.back_btn})
    public void click(View view)
    {
        switch (view.getId())
        {
            case R.id.commit_btn:
                final BaseDialog baseDialog = new BaseDialog(context, 1);
                baseDialog.setHintTvValue("确定提交收货差异数据？");
                baseDialog.setConfrimBtnOnclick(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        dismiss();
                        baseDialog.dismiss();
                        if(icommit!=null)
                        {
                            icommit.commit();
                        }
                    }
                });
                break;
            case R.id.back_btn:
                dismiss();
                break;
        }
    }

    private class MyAdapter extends BaseAdapter
    {

        private Context mContext;
        private ArrayList<Sku> mDataList;

        public MyAdapter(Context context)
        {
            mContext = context;
            mDataList = new ArrayList<Sku>();
        }

        public void update(ArrayList<Sku> dataList)
        {
            if (dataList.size() == 0)
            {
                return;
            }
            mDataList.clear();
            mDataList.addAll(dataList);
            dataList.clear();
            notifyDataSetChanged();
        }

        public void clear()
        {
            mDataList.clear();
            notifyDataSetChanged();
        }

        public ArrayList<Sku> getDataList()
        {
            return mDataList;
        }

        @Override
        public int getCount()
        {
            return mDataList.size();
        }

        @Override
        public Sku getItem(int position)
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
            ViewHolder holder;
            if (convertView == null)
            {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_sku_single_barcode, parent, false);
                holder = new ViewHolder();
                holder.num_tv = (TextView) convertView.findViewById(R.id.title_num_tv);
                holder.skuNum_tv = (TextView) convertView.findViewById(R.id.title_skuNum_tv);
                holder.color = (TextView) convertView.findViewById(R.id.title_diff_color);
                holder.size = (TextView) convertView.findViewById(R.id.title_diff_size);
                convertView.setTag(holder);
            } else
            {
                holder = (ViewHolder) convertView.getTag();
            }

            Sku item = getItem(position);
            holder.num_tv.setText(item.boxNum);
            holder.skuNum_tv.setText(item.style);
            holder.color.setText(item.color);
            holder.size.setText(item.size);
            holder.color.setTextColor(context.getResources().getColor(R.color.md_red_A200));
            holder.size.setTextColor(context.getResources().getColor(R.color.md_red_A200));

            return convertView;
        }

        private class ViewHolder
        {
            TextView num_tv;
            TextView skuNum_tv;
            TextView color;
            TextView size;
        }


    }

}
