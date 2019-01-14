package com.ioter.warehouse.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ioter.warehouse.R;
import com.ioter.warehouse.bean.EPC;

import java.util.ArrayList;
import java.util.List;

public class ReceiveDialogadapter extends BaseAdapter {
    //定义需要包装的JSONArray对象
    public List<EPC> mymodelList=new ArrayList<>();
    private Context context=null;
    //视图容器
    private LayoutInflater layoutInflater;
    //类型
    private int type;

    public ReceiveDialogadapter(Context _context,int _type){
        this.context=_context;
        //创建视图容器并设置上下文
        this.layoutInflater= LayoutInflater.from(_context);
        this.type = _type;
    }

    public void updateDatas(List<EPC> datalist){
        if(datalist == null)
        {
            return;
        }else{
            mymodelList.clear();
            mymodelList.addAll(datalist);
            notifyDataSetChanged();
        }

    }

    /**
     * 清空列表的所有数据
     */
    public void clearData()
    {
        mymodelList.clear();
        notifyDataSetChanged();
    }



    @Override
    public int getCount() {
        return this.mymodelList.size();
    }

    @Override
    public Object getItem(int position) {
        if (getCount()>0){
            return this.mymodelList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemView listItemView =null;
        if (convertView ==null){
            //获取list_item布局文件的视图

            convertView = layoutInflater.inflate(R.layout.dialog_list_item,null);
            //获取控件对象
            listItemView =new ListItemView();
            if (type==4){
                listItemView.total  = (TextView) convertView.findViewById(R.id.tv_num);
                listItemView.left = (TextView) convertView.findViewById(R.id.tv_left);
                listItemView.right = (TextView) convertView.findViewById(R.id.tv_right);
                listItemView.right.setVisibility(View.GONE);
            }else {
                listItemView.total  = (TextView) convertView.findViewById(R.id.tv_num);
                listItemView.left = (TextView) convertView.findViewById(R.id.tv_left);
                listItemView.right = (TextView) convertView.findViewById(R.id.tv_right);
            }
            //设置控件集到convertView
            convertView.setTag(listItemView);
        }
        else{
            listItemView = (ReceiveDialogadapter.ListItemView)convertView.getTag();
        }

        final EPC m1=(EPC) this.getItem(position);
        if (type==4){
            listItemView.total.setText(position+1+"");
            listItemView.left.setText(m1.getData1());
        }else {
            listItemView.total.setText(position+1+"");
            listItemView.left.setText(m1.getData1());
            listItemView.right.setText(m1.getData2());
        }
        return convertView;
    }

    /**
     * 使用一个类来保存Item中的元素
     * 自定义控件集合
     */
    public final class ListItemView{
        TextView total,left,right;
    }
}
