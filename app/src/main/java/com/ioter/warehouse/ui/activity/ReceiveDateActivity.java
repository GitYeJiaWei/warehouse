package com.ioter.warehouse.ui.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ioter.warehouse.AppApplication;
import com.ioter.warehouse.R;
import com.ioter.warehouse.bean.BaseBean;
import com.ioter.warehouse.bean.EPC;
import com.ioter.warehouse.bean.ListLotBean;
import com.ioter.warehouse.bean.StockBean;
import com.ioter.warehouse.bean.WindowsModelBean;
import com.ioter.warehouse.common.CustomProgressDialog;
import com.ioter.warehouse.common.rx.subscriber.AdapterItemSubcriber;
import com.ioter.warehouse.common.util.ACache;
import com.ioter.warehouse.common.util.ACacheUtils;
import com.ioter.warehouse.common.util.DateUtil;
import com.ioter.warehouse.common.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ReceiveDateActivity extends NewBaseActivity {

    @BindView(R.id.bt_sure)
    Button btSure;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.layout_content)
    LinearLayout layoutContent;
    private int mYear, mMonth, mDay, mhourOfDay, mminute;
    protected CustomProgressDialog progressDialog;
    private Intent intent = null;
    private ArrayList<String> listLotTitleJson = new ArrayList<>();
    private ArrayList<String> listLotValueJson = new ArrayList<>();
    private int select = 0;
    private int itemId = 0;
    private int RAG = 0;
    private HashMap<Integer, String> map1 = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_date);
        ButterKnife.bind(this);
        setTitle("收货");
        intent = getIntent();

        ArrayList<ListLotBean> listLotBeans = (ArrayList<ListLotBean>) intent.getSerializableExtra("listlost");
        initView(listLotBeans);
    }

    private void initView(ArrayList<ListLotBean> listLotBeans) {
        for (int i = 0; i < listLotBeans.size(); i++) {
            itemId++;
            String title = listLotBeans.get(i).getTitle();
            final TextView textView1 = new TextView(this);
            textView1.setTextSize(18);
            textView1.setText(title);
            listLotTitleJson.add(title);
            layoutContent.addView(textView1);

            int type = listLotBeans.get(i).getType();
            String value = listLotBeans.get(i).getValue();

            if (type == 1) {
                //下拉框
                Map<String, String> map = AppApplication.getGson().fromJson(listLotBeans.get(i).getListOption().toString(), Map.class);
                Iterator it = map.keySet().iterator();
                ArrayList<String> arrayList = new ArrayList<>();
                select = 0;
                int a = 0;
                while (it.hasNext()) {
                    String key1 = (String) it.next();
                    if (value.equals(key1)) {
                        select = a;
                        map1.put(itemId, value);
                    }
                    arrayList.add(map.get(key1));
                    a++;
                }
                if (!map1.containsKey(itemId)) {
                    map1.put(itemId, value);
                }

                final Spinner spinner = new Spinner(this);
                spinner.setId(itemId);
                /*
                 * 第二个参数是显示的布局
                 * 第三个参数是在布局显示的位置id
                 * 第四个参数是将要显示的数据
                 */
                ArrayAdapter adapter2 = new ArrayAdapter(this, R.layout.item, R.id.text_item, arrayList);
                spinner.setAdapter(adapter2);
                //设置默认值
                spinner.setSelection(select, true);
                //spCangku.setPrompt("测试");
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selected = parent.getItemAtPosition(position).toString();
                        if (map1.containsKey(view.getId())) {
                            map1.remove(view.getId());
                            map1.put(view.getId(), selected);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                // 动态把控件添加到
                layoutContent.addView(spinner);
            } else if (type == 2) {
                //日期框
                final TextView textView = new TextView(this);
                textView.setId(itemId);
                //初始化日期
                final Calendar calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);
                String newDateStr = mYear + "-" + (mMonth + 1) + "-" + mDay;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟
                    Date date = sdf.parse(newDateStr);
                    if (value != null && !value.equals("")) {
                        textView.setText(value);
                    } else {
                        textView.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (!map1.containsKey(itemId)) {
                    map1.put(itemId, textView.getText().toString());
                }
                textView.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onClick(final View v) {
                        if (map1.containsKey(v.getId())) {
                            map1.remove(v.getId());

                        }
                        new DatePickerDialog(ReceiveDateActivity.this, 2, new DatePickerDialog.OnDateSetListener() {
                            // 绑定监听器(How the parent is notified that the date is set.)
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // 此处得到选择的时间，可以进行你想要的操作
                                textView.setText("您选择了：" + year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
                                map1.put(v.getId(), year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }
                                // 设置初始日期
                                , calendar.get(Calendar.YEAR)
                                , calendar.get(Calendar.MONTH)
                                , calendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });
                layoutContent.addView(textView);

            } else if (type == 3) {
                //日期时间
                final TextView textView = new TextView(this);
                textView.setId(itemId);
                //初始化日期
                final Calendar calendar = Calendar.getInstance();
                mhourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                mminute = calendar.get(Calendar.MINUTE);
                String newDateStr = mhourOfDay + ":" + mminute;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");//小写的mm表示的是分钟
                    Date date = sdf.parse(newDateStr);
                    if (value != null && !value.equals("")) {
                        textView.setText(value);
                    } else {
                        textView.setText(new SimpleDateFormat("hh:mm").format(date));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (!map1.containsKey(itemId)) {
                    map1.put(itemId, value);
                }
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                       /* DateUtil.showTimePickerDialog(ReceiveDateActivity.this,2,
                                textView,calendar);*/
                        if (map1.containsKey(v.getId())) {
                            map1.remove(v.getId());
                        }
                        new TimePickerDialog(ReceiveDateActivity.this, 2,
                                // 绑定监听器
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        textView.setText("您选择了：" + hourOfDay + "时" + minute + "分");
                                        map1.put(v.getId(), hourOfDay + "-" + minute);
                                    }
                                }
                                // 设置初始时间
                                , calendar.get(Calendar.HOUR_OF_DAY)
                                , calendar.get(Calendar.MINUTE)
                                // true表示采用24小时制
                                , true).show();
                    }
                });
                layoutContent.addView(textView);

            } else if (type == 4) {
                //数字框
                final EditText editText = new EditText(this);
                editText.setText(value);
                editText.setId(itemId);
                if (!map1.containsKey(itemId)) {
                    map1.put(itemId, value);
                }
                editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (map1.containsKey(v.getId())) {
                            map1.remove(v.getId());
                            map1.put(v.getId(), v.getText().toString());
                        }
                        return false;
                    }
                });

                editText.setBackgroundResource(R.drawable.back_text);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                layoutContent.addView(editText);

            } else if (type == 5) {
                //弹出框
                final Button button = new Button(this);

                button.setText(value);
                button.setId(itemId);

                final WindowsModelBean windowsModelBean = listLotBeans.get(i).getWindowsModel();
                if (!map1.containsKey(itemId)) {
                    map1.put(itemId, windowsModelBean.getTextField());
                }
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (map1.containsKey(v.getId())) {
                            map1.remove(v.getId());
                            RAG = v.getId();
                        }
                        Intent intent1 = new Intent(ReceiveDateActivity.this, ReceiveDiaolgActivity.class);
                        intent1.putExtra("windowsModelBean", windowsModelBean);
                        startActivityForResult(intent1, RAG);
                    }
                });
                layoutContent.addView(button);
            } else if (type == 6) {
                //文本框
                final EditText editText = new EditText(this);
                editText.setText(value);
                editText.setId(itemId);
                if (!map1.containsKey(itemId)) {
                    map1.put(itemId, value);
                }
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (map1.containsKey(editText.getId())) {
                            map1.remove(editText.getId());
                            map1.put(editText.getId(), s.toString());
                        }
                    }
                });

                editText.setBackgroundResource(R.drawable.back_text);
                layoutContent.addView(editText);
            } else {
                return;
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RAG) {
            if (resultCode == RESULT_OK) {
                //获取弹出框的数据
                String value = data.getStringExtra("value");
                if (!TextUtils.isEmpty(value)) {
                    map1.put(RAG, value);
                    Button v = findViewById(RAG);
                    v.setText(value);
                }
            }
        }

    }


    @OnClick({R.id.bt_sure, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_sure:
                takeData();
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }

    private void takeData() {
        if (listLotValueJson != null) {
            listLotValueJson.clear();
        }

        Object[] key_arr = map1.keySet().toArray();
        Arrays.sort(key_arr);
        for (Object key2 : key_arr) {
            int kk = (int) key2;
            String va = map1.get(kk);
            listLotValueJson.add(va);
        }

        ArrayList<StockBean> sb = (ArrayList<StockBean>) intent.getSerializableExtra("sb");
        ArrayList<EPC> epclis = (ArrayList<EPC>) intent.getSerializableExtra("epclis");
        String uom = intent.getStringExtra("uom");
        String stockLoc = intent.getStringExtra("stockLoc");
        String trackCode = intent.getStringExtra("trackCode");
        String stockQty = intent.getStringExtra("stockQty");

        ArrayList<String> listEpcJson = new ArrayList<>();
        if (epclis != null) {
            for (int i = 0; i < epclis.size(); i++) {
                listEpcJson.add(epclis.get(i).getEpc());
            }
        }

        String name = ACache.get(AppApplication.getApplication()).getAsString("UserName");
        if (name == null) {
            ToastUtil.toast("请到系统设置中设置仓库");
        }
        progressDialog = new CustomProgressDialog(this, "提交数据中...");
        progressDialog.show();

        Map<String, String> params = new HashMap<>();
        params.put("asnDetailId", sb.get(0).getAsnDetailId());
        params.put("productId", sb.get(0).getProductId());
        params.put("stockQty", stockQty);
        params.put("uom", uom);
        params.put("stockLoc", stockLoc);
        params.put("trackCode", trackCode);
        params.put("listEpcJson", AppApplication.getGson().toJson(listEpcJson));
        params.put("listLotTitleJson", AppApplication.getGson().toJson(listLotTitleJson));
        params.put("listLotValueJson", AppApplication.getGson().toJson(listLotValueJson));
        params.put("userId", ACacheUtils.getUserId());
        params.put("whId", ACacheUtils.getWareIdByWhCode(name));

        AppApplication.getApplication().getAppComponent().getApiService().StockIn(params).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new AdapterItemSubcriber<BaseBean>(this) {
            @Override
            public void onNext(BaseBean baseBean) {
                if (baseBean.success()) {
                    ToastUtil.toast("提交成功");
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
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
