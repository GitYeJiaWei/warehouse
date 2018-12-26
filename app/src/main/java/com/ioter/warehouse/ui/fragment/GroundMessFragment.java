package com.ioter.warehouse.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.ioter.warehouse.AppApplication;
import com.ioter.warehouse.R;
import com.ioter.warehouse.bean.ListUomBean;
import com.ioter.warehouse.bean.TrackBean;
import com.ioter.warehouse.common.util.SoundManage;
import com.zebra.adc.decoder.Barcode2DWithSoft;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroundMessFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroundMessFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Unbinder unbinder;
    @BindView(R.id.sp_cangku)
    Spinner spCangku;
    @BindView(R.id.ed_genzonghao)
    EditText edGenzonghao;
    @BindView(R.id.ed_chanpin)
    EditText edChanpin;
    @BindView(R.id.ed_kuwei)
    EditText edKuwei;
    @BindView(R.id.ef_shuliang)
    EditText efShuliang;
    @BindView(R.id.edt_kuwei)
    EditText edtKuwei;
    @BindView(R.id.edt_baozhuang)
    EditText edtBaozhuang;
    @BindView(R.id.ed_zongshu)
    EditText edZongshu;
    @BindView(R.id.edt_pinming)
    EditText edtPinming;
    private int a = 1;

    // TODO: Rename and change types of parameters
    private ArrayList<TrackBean> mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public GroundMessFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroundMessFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroundMessFragment newInstance(ArrayList<TrackBean> param1, String param2) {
        GroundMessFragment fragment = new GroundMessFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (ArrayList<TrackBean>) getArguments().getSerializable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ground_mess, container, false);
        unbinder = ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        ArrayList<TrackBean> message = null;
        if (bundle != null) {
            message = (ArrayList<TrackBean>) bundle.getSerializable(ARG_PARAM1);
        }
        edtKuwei.setText(message.get(0).getRecommendLoc());
        edZongshu.setText(message.get(0).getShelfQty()+"");
        edtPinming.setText(message.get(0).getProductName());
        edtBaozhuang.setText(message.get(0).getPacking());

        initView(message.get(0).getListUom());
        return view;
    }

    private void initView(List<ListUomBean> list) {
        /*
         * 第二个参数是显示的布局
         * 第三个参数是在布局显示的位置id
         * 第四个参数是将要显示的数据
         */
        ArrayList<String> arrayList = new ArrayList<>();
        int select=0;
        for (int i = 0; i < list.size(); i++) {
            arrayList.add(list.get(i).getUom());
            if (list.get(i).isIsDefault()){
                select=i;
            }
        }

        ArrayAdapter adapter2 = new ArrayAdapter(getContext(), R.layout.item, R.id.text_item, arrayList);
        spCangku.setAdapter(adapter2);
        //设置默认值
        spCangku.setSelection(select, true);
        //spCangku.setPrompt("测试");
        spCangku.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edGenzonghao.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edGenzonghao.setFocusableInTouchMode(true);
                    edGenzonghao.requestFocus();
                    a = 1;
                }
            }
        });
        edChanpin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edChanpin.setFocusableInTouchMode(true);
                    edChanpin.requestFocus();
                    a = 2;
                }
            }
        });
        edKuwei.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edKuwei.setFocusableInTouchMode(true);
                    edKuwei.requestFocus();
                    a = 3;
                }
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 139 || keyCode == 280) {
            if (event.getRepeatCount() == 0) {
                ScanBarcode();
            }
        }
        return true;
    }

    //扫条码
    private void ScanBarcode() {
        if (AppApplication.barcode2DWithSoft != null) {
            AppApplication.barcode2DWithSoft.scan();
            AppApplication.barcode2DWithSoft.setScanCallback(ScanBack);
        }
    }

    public Barcode2DWithSoft.ScanCallback
            ScanBack = new Barcode2DWithSoft.ScanCallback() {
        @Override
        public void onScanComplete(int i, int length, byte[] bytes) {
            if (length < 1) {
            } else {
                final String barCode = new String(bytes, 0, length);
                if (barCode != null && barCode.length() > 0) {
                    SoundManage.PlaySound(getContext(), SoundManage.SoundType.SUCCESS);
                    if (a == 2) {
                        edChanpin.setText(barCode);
                        a = 3;
                    } else if (a == 1) {
                        edGenzonghao.setText(barCode);
                        a = 2;
                    } else if (a == 3) {
                        edKuwei.setText(barCode);
                        a = 1;
                    } else {
                        return;
                    }
                }
            }
        }
    };

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
