package com.ioter.warehouse.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ioter.warehouse.R;
import com.ioter.warehouse.bean.GetStock;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FindMessFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindMessFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Unbinder unbinder;
    @BindView(R.id.et_huozhu)
    EditText etHuozhu;
    @BindView(R.id.ed_chanpin)
    EditText edChanpin;
    @BindView(R.id.ed_pinming)
    EditText edPinming;
    @BindView(R.id.ed_kuwei)
    EditText edKuwei;
    @BindView(R.id.ed_kucun)
    EditText edKucun;
    @BindView(R.id.ed_fenpei)
    EditText edFenpei;
    @BindView(R.id.ed_genzonghao)
    EditText edGenzonghao;
    @BindView(R.id.edt_baozhuang)
    EditText edtBaozhuang;
    @BindView(R.id.ed_keyong)
    EditText edKeyong;


    // TODO: Rename and change types of parameters
    private ArrayList<GetStock> mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FindMessFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FindMessFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FindMessFragment newInstance(ArrayList<GetStock> param1, String param2) {
        FindMessFragment fragment = new FindMessFragment();
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
            mParam1 = (ArrayList<GetStock>) getArguments().getSerializable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find_mess, container, false);
        unbinder = ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        ArrayList<GetStock> message = null;
        if (bundle != null) {
            message = (ArrayList<GetStock>) bundle.getSerializable(ARG_PARAM1);
        }

        etHuozhu.setText(message.get(0).getOwnerName()+"");
        edChanpin.setText(message.get(0).getProductId()+"");
        edPinming.setText(message.get(0).getProductName()+"");
        edKuwei.setText(message.get(0).getLocId()+"");
        edKucun.setText(message.get(0).getStockQty()+"");
        edFenpei.setText(message.get(0).getDistributeQty()+"");
        edGenzonghao.setText(message.get(0).getTrackCode()+"");
        edtBaozhuang.setText(message.get(0).getUom()+"");
        edKeyong.setText(message.get(0).getAvailableQty()+"");
        return view;
    }

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
