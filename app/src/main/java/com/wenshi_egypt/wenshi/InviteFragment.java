package com.wenshi_egypt.wenshi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import static com.wenshi_egypt.wenshi.CustomerMapActivity.REQUEST_INVITE;

public class InviteFragment extends Fragment implements View.OnClickListener {

    DatabaseReference rootRef;
    String step1;
    TextView demo;

    private InviteFragment.OnFragmentInteractionListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_invite, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {





        //ArrayList<String> request = howToRequest();
        demo = getView().findViewById(R.id.inviteDemo);
        final ArrayList<String> results = new ArrayList<>();
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.child("Invite").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
          //      HashMap<String, String> value = (HashMap<String, String>) dataSnapshot.getValue();
          //      step1 = value.get("demoInvite");
         //       demo.setText(step1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InviteFragment.OnFragmentInteractionListener) {
            mListener = (InviteFragment.OnFragmentInteractionListener) context;
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

//    private ArrayList<String> howToRequest() {
//        final ArrayList<String> results = new ArrayList<>();
//        rootRef = FirebaseDatabase.getInstance().getReference();
//        rootRef.child("Help").orderByKey().addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot child : dataSnapshot.getChildren()) {
//                    //noinspection unchecked
//                    HashMap<String, String> value = (HashMap<String, String>) child.getValue();
//                    assert value != null;
//                    step1 = value.get("step1");
//                    step2 = value.get("step2");
//                    step3 = value.get("step3");
//
//                    demo.setText(step1 + step2 + step3);
//                  //  results.add(new HistoryModel(date, from, to, Double.parseDouble(cost)));
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//        return results;
//    }

}

