package com.wenshi_egypt.wenshi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wenshi_egypt.wenshi.model.UserModel;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReviewRequestFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReviewRequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewRequestFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private UserModel user;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ReviewRequestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReviewRequestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReviewRequestFragment newInstance(String param1, String param2) {
        ReviewRequestFragment fragment = new ReviewRequestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        user = ((CustomerMapActivity)getActivity()).getCustomer();

        ((CustomerMapActivity)getActivity()).getDirectionsData.getDuration();
      // Log.i("USER",user.toString()) ;

        ((TextView)getView().findViewById(R.id.pickupValue)).setText(user.getPickupAddress());
        ((TextView)getView().findViewById(R.id.dropoffValue)).setText(user.getDestinationAddress());
        ((TextView)getView().findViewById(R.id.serviceValue)).setText(user.getServiceType());
        ((TextView)getView().findViewById(R.id.cartypeValue)).setText(user.getDefaultVehicle().getType());
        ((TextView)getView().findViewById(R.id.carmodelValue)).setText(user.getDefaultVehicle().getModel());
        ((TextView)getView().findViewById(R.id.etaValue)).setText( ((CustomerMapActivity)getActivity()).getDirectionsData.getDuration());
        ((TextView)getView().findViewById(R.id.distanceValue)).setText( ((CustomerMapActivity)getActivity()).getDirectionsData.getDistance());
        ((TextView)getView().findViewById(R.id.paymentMethodValue)).setText(getString(R.string.radioButton_payment_cash));

        try {
            double distance =Double.parseDouble(((CustomerMapActivity)getActivity()).getDirectionsData.getDistance().split(" ")[0]) ;
            ((TextView)getView().findViewById(R.id.fareValue)).setText("" +distance*15+" LE");

        }catch (Exception e){
            ((TextView)getView().findViewById(R.id.fareValue)).setText("Contact Driver");

        }

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mListener != null) {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http").authority("www.merply.com").appendPath("Winshe").appendPath("types").appendQueryParameter("type", "1").appendQueryParameter("sort", "relevance").fragment("profile");
            mListener.onFragmentInteraction(builder.build());
        }

        return inflater.inflate(R.layout.fragment_review_request, container, false);
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
