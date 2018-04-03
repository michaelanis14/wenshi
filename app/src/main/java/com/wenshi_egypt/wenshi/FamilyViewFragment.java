package com.wenshi_egypt.wenshi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static com.wenshi_egypt.wenshi.CustomerMapActivity.REQUEST_INVITE;

public class FamilyViewFragment extends Fragment implements View.OnClickListener {

    DatabaseReference rootRef, fm;
    EditText name1, name2, name3, mobile1, mobile2, mobile3;
    ImageButton add1, add2, add3, edit1, edit2, edit3, requestService;
    @SuppressWarnings("unused")
    private FamilyViewFragment.OnFragmentInteractionListener mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_family_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {





        rootRef = FirebaseDatabase.getInstance().getReference();

        //noinspection ConstantConditions
        name1 = getView().findViewById(R.id.editText_family_name1);
        name2 = getView().findViewById(R.id.editText_family_name2);
        name3 = getView().findViewById(R.id.editText_family_name3);
        mobile1 = getView().findViewById(R.id.editText_family_mobile1);
        mobile2 = getView().findViewById(R.id.editText_family_mobile2);
        mobile3 = getView().findViewById(R.id.editText_family_mobile3);

        add1 = getView().findViewById(R.id.imageButton_family_add1);
        add2 = getView().findViewById(R.id.imageButton_family_add2);
        add3 = getView().findViewById(R.id.imageButton_family_add3);
        edit1 = getView().findViewById(R.id.imageButton_family_edit1);
        edit2 = getView().findViewById(R.id.imageButton_family_edit2);
        edit3 = getView().findViewById(R.id.imageButton_family_edit3);

        requestService = getView().findViewById(R.id.imageButton_family_request);

        assert getActivity() != null;

        fm = rootRef.child("Users").child("Customers").child(((CustomerMapActivity) getActivity()).getCustomer().getID()).child("Family");
        fm.addValueEventListener(new ValueEventListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, String> value;
                if (dataSnapshot.hasChild("newMem1")) {
                    DataSnapshot mem1 = dataSnapshot.child("newMem1");
                    value = (HashMap<String, String>) mem1.getValue();
                    assert value != null;
                    name1.setText(value.get("name"));
                    mobile1.setText(value.get("mobile"));
                    if (!name1.getText().toString().isEmpty()) {
                        name1.setEnabled(false);
                        mobile1.setEnabled(false);
                    }
                }
                if (dataSnapshot.hasChild("newMem2")) {
                    DataSnapshot mem2 = dataSnapshot.child("newMem2");
                    value = (HashMap<String, String>) mem2.getValue();
                    assert value != null;
                    name2.setText(value.get("name"));
                    mobile2.setText(value.get("mobile"));
                    if (!name2.getText().toString().isEmpty()) {
                        name2.setEnabled(false);
                        mobile2.setEnabled(false);
                    }
                }
                if (dataSnapshot.hasChild("newMem3")) {
                    DataSnapshot mem3 = dataSnapshot.child("newMem3");
                    value = (HashMap<String, String>) mem3.getValue();
                    assert value != null;
                    name3.setText(value.get("name"));
                    mobile3.setText(value.get("mobile"));
                    if (!name3.getText().toString().isEmpty()) {
                        name3.setEnabled(false);
                        mobile3.setEnabled(false);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFamily(name1, mobile1, 1);
            }
        });

        edit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editFamily(name1, mobile1);
            }
        });

        add2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFamily(name2, mobile2, 2);
            }
        });

        edit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editFamily(name2, mobile2);
            }
        });

        add3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFamily(name3, mobile3, 3);
            }
        });

        edit3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editFamily(name3, mobile3);
            }
        });

        requestService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //noinspection ConstantConditions
                if(name1.getText().toString().isEmpty() && name2.getText().toString().isEmpty() && name3.getText().toString().isEmpty())    {
                    Toast.makeText(getActivity(), "Please enter a family member", Toast.LENGTH_LONG).show();
                }
                else {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.mainFrame, new FamilyRequestFragment(name1.getText().toString(), name2.getText().toString(), name3.getText().toString()));
                    ft.commit();
                }
            }
        });
    }

    void addFamily(EditText name, EditText mobile, int memNum) {
        if ((!name.getText().toString().isEmpty() && !mobile.getText().toString().isEmpty())
                || (name.getText().toString().isEmpty() && mobile.getText().toString().isEmpty())) {
            assert getActivity() != null;
            DatabaseReference addFamMem = rootRef.child("Users").child("Customers").child(((CustomerMapActivity) getActivity()).getCustomer().getID()).child("Family").child("newMem" + memNum);
            addFamMem.child("name").setValue(name.getText().toString());
            addFamMem.child("mobile").setValue(mobile.getText().toString());
        } else
            Toast.makeText(getActivity(), "Cannot save one field only", Toast.LENGTH_LONG).show();
        if (!name.getText().toString().isEmpty() && !mobile.getText().toString().isEmpty() && name.isEnabled()) {
            name.setEnabled(false);
            mobile.setEnabled(false);
            Toast.makeText(getActivity(), "Family member added successfully", Toast.LENGTH_LONG).show();
        }
    }

    void editFamily(EditText name, EditText mobile) {
        name.setEnabled(true);
        mobile.setEnabled(true);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FamilyViewFragment.OnFragmentInteractionListener) {
            mListener = (FamilyViewFragment.OnFragmentInteractionListener) context;
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

