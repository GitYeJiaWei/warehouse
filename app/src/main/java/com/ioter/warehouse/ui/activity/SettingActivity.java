package com.ioter.warehouse.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.ioter.warehouse.AppApplication;
import com.ioter.warehouse.R;
import com.ioter.warehouse.common.util.ACache;
import com.ioter.warehouse.common.util.ACacheUtils;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends NewBaseActivity {

    @BindView(R.id.bt_sure)
    Button btSure;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.sp_cangku)
    Spinner spCangku;
    @BindView(R.id.sp_kuqu)
    Spinner spKuqu;
    private String selected =null;

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
        spKuqu.setAdapter(adapter);
        //设置默认值
        spKuqu.setSelection(0, true);
        //spCangku.setPrompt("测试");
        spKuqu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*
         * 动态添显示下来菜单的选项，可以动态添加元素
         */
        List<String> list = ACacheUtils.getWhCodeWithUser();
        /*
         * 第二个参数是显示的布局
         * 第三个参数是在布局显示的位置id
         * 第四个参数是将要显示的数据
         */
        ArrayAdapter adapter2 = new ArrayAdapter(this, R.layout.item, R.id.text_item,list);
        spCangku.setAdapter(adapter2);
        if (list!=null){
            String a = ACache.get(AppApplication.getApplication()).getAsString("UserName");
            if (a!=null){
                for (int i = 0; i < list.size(); i++) {
                    if (a.equals(list.get(i))){
                        //设置默认值
                        spCangku.setSelection(i, true);
                    }
                }
            }else {
                //设置默认值
                spCangku.setSelection(0, true);
            }

        }
        spCangku.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //将选择的元素显示出来
                selected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @OnClick({R.id.bt_sure, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_sure:
                ACache.get(AppApplication.getApplication()).put("UserName", selected);
                finish();
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }

}
