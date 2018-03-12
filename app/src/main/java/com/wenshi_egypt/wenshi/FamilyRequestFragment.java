package com.wenshi_egypt.wenshi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FamilyRequestFragment extends Fragment implements View.OnClickListener {

    DatabaseReference rootRef;
    Spinner memberName;
    EditText memberLocation;
    RadioButton rb1;
    RadioGroup radioGroup;
    String name1, name2, name3;

    private FamilyRequestFragment.OnFragmentInteractionListener mListener;

    public FamilyRequestFragment()  {}

    @SuppressLint("ValidFragment")
    public FamilyRequestFragment(String name1, String name2, String name3) {
        this.name1 = name1;
        this.name2 = name2;
        this.name3 = name3;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_family_request, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        //noinspection ConstantConditions
        memberName = getView().findViewById(R.id.spinner_family_member);
        memberLocation = getView().findViewById(R.id.editText_family_location);
        rb1 = getView().findViewById(R.id.radioButton_family_cash);
        radioGroup = getView().findViewById(R.id.radiogroup);

        ArrayAdapter<String> adapter;
        List<String> list;

        list = new ArrayList<String>();
        if(!name1.isEmpty())
            list.add(name1);
        if(!name2.isEmpty())
            list.add(name2);
        if(!name3.isEmpty())
            list.add(name3);

        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        memberName.setAdapter(adapter);

        rootRef = FirebaseDatabase.getInstance().getReference();

/*        rootRef.child("About").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, String> value = (HashMap<String, String>) dataSnapshot.getValue();
                //step1 = value.get("demoAbout");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FamilyRequestFragment.OnFragmentInteractionListener) {
            mListener = (FamilyRequestFragment.OnFragmentInteractionListener) context;
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

}

