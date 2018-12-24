package com.ioter.warehouse.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ioter.warehouse.AppApplication;
import com.ioter.warehouse.R;
import com.ioter.warehouse.bean.ListLotBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReceiveDateActivity extends NewBaseActivity {

    @BindView(R.id.bt_sure)
    Button btSure;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.layout_content)
    LinearLayout layoutContent;
    private int mYear,mMonth,mDay,mhourOfDay,mminute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_date);
        ButterKnife.bind(this);
        setTitle("收货");

        ArrayList<ListLotBean> listLotBeans = (ArrayList<ListLotBean>) getIntent().getSerializableExtra("listlost");
        initView(listLotBeans);
    }

    private void initView(ArrayList<ListLotBean> listLotBeans) {
        for (int i = 0; i < listLotBeans.size(); i++) {
            String title = listLotBeans.get(i).getTitle();
            int type = listLotBeans.get(i).getType();
            String value = listLotBeans.get(i).getValue();
            if (type==1){
                //下拉框
                Map<String, String> map = AppApplication.getGson().fromJson(listLotBeans.get(i).getListOption().toString(), Map.class);
                Iterator it = map.keySet().iterator();
                ArrayList<String> arrayList = new ArrayList<>();
                while (it.hasNext()) {
                    String key1 = (String) it.next();
                    arrayList.add(map.get(key1));
                }

                final Spinner spinner = new Spinner(this);
                /*
                 * 第二个参数是显示的布局
                 * 第三个参数是在布局显示的位置id
                 * 第四个参数是将要显示的数据
                 */
                ArrayAdapter adapter2 = new ArrayAdapter(this, R.layout.item, R.id.text_item, arrayList);
                spinner.setAdapter(adapter2);
                //设置默认值
                spinner.setSelection(0, true);
                //spCangku.setPrompt("测试");
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selected = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                // 动态把控件添加到
                layoutContent.addView(spinner);
            }else if (type == 2){
                //日期框
                final TextView textView = new TextView(this);
                //初始化日期
                final Calendar calendar=Calendar.getInstance();
                mYear =calendar.get(Calendar.YEAR);
                mMonth =calendar.get(Calendar.MONTH);
                mDay =calendar.get(Calendar.DAY_OF_MONTH);
                String newDateStr = mYear+"-" + (mMonth + 1)+"-"+mDay;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟
                    Date date = sdf.parse(newDateStr);
                    textView.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }else if (type == 3){
                //日期时间

            }else if (type == 4){
                //数字框

            }else if (type == 5){
                //弹出框

            }else if (type == 6){
                //文本框
                final EditText editText = new EditText(this);
                layoutContent.addView(editText);

            }else {
                return;
            }
        }



    }

    @OnClick({R.id.bt_sure, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_sure:
                if (getIntent().getBooleanExtra("size", false)) {
                    Intent intent = new Intent(ReceiveDateActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }
}
