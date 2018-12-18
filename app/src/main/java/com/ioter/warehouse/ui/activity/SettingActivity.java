package com.ioter.warehouse.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.ioter.warehouse.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends NewBaseActivity implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.bt_sure)
    Button btSure;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.sp_cangku)
    Spinner spCangku;
    @BindView(R.id.sp_kuqu)
    Spinner spKuqu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        setTitle("系统设置");

        initView();
    }

    private void initView(){
        /*静态的显示下来出来的菜单选项，显示的数组元素提前已经设置好了
         * 第二个参数：已经编写好的数组
         * 第三个数据：默认的样式
         */
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.number_array, android.R.layout.simple_spinner_item);
        //设置spinner中每个条目的样式，同样是引用android提供的布局文件
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCangku.setAdapter(adapter);
        //spCangku.setPrompt("测试");
        spCangku.setOnItemSelectedListener(this);

        /*
         * 动态添显示下来菜单的选项，可以动态添加元素
         */
        ArrayList<String> list = new ArrayList<String>();
        list.add("1.苹果");
        list.add("2.橘子");
        /*
         * 第二个参数是显示的布局
         * 第三个参数是在布局显示的位置id
         * 第四个参数是将要显示的数据
         */
        ArrayAdapter adapter2 = new ArrayAdapter(this, R.layout.item, R.id.text_item,list);
        spKuqu.setAdapter(adapter2);
        spKuqu.setOnItemSelectedListener(this);
    }


    @OnClick({R.id.bt_sure, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_sure:
                finish();
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()){
            case R.id.sp_cangku:
                //将选择的元素显示出来
                String selected = parent.getItemAtPosition(position).toString();
                break;
            case R.id.sp_kuqu:
                //将选择的元素显示出来
                String selected1 = parent.getItemAtPosition(position).toString();
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
