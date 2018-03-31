package com.wenshi_egypt.wenshi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CustomerSettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CustomerSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerSettingsFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CustomerSettingsFragment() {
        // Required empty public constructor
    }

    public static CustomerSettingsFragment newInstance(String param1, String param2) {
        CustomerSettingsFragment fragment = new CustomerSettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mListener != null) {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http").authority("www.merply.com").appendPath("Winshe").appendPath("types").appendQueryParameter("type", "1").appendQueryParameter("sort", "relevance").fragment("settings");
            mListener.onFragmentInteraction(builder.build());
        }
        return inflater.inflate(R.layout.fragment_settings_customer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getView().findViewById(R.id.profile_btn).setOnClickListener(this);
        getView().findViewById(R.id.payment_btn).setOnClickListener(this);
        getView().findViewById(R.id.family_btn).setOnClickListener(this);
        getView().findViewById(R.id.history_btn).setOnClickListener(this);
        getView().findViewById(R.id.vehicles_btn).setOnClickListener(this);
        getView().findViewById(R.id.inviteFriends_btn).setOnClickListener(this);
        getView().findViewById(R.id.about_btn).setOnClickListener(this);


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
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
            ((CustomerMapActivity)getActivity()).showSettingsTabs(view.getId());
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
