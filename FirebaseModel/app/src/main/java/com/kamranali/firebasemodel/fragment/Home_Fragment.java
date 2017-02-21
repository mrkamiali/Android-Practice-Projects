package com.kamranali.firebasemodel.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.kamranali.firebasemodel.AppController;
import com.kamranali.firebasemodel.R;
import com.kamranali.firebasemodel.signupfragment.Signup_fragment;
import com.kamranali.firebasemodel.utils.Util;

public class Home_Fragment extends Fragment {


    private TextView signup_button;
    private ProgressDialog progressDialog;
    private View view;
    private FirebaseAuth auth;


    public Home_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth= FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view = inflater.inflate(R.layout.fragment_home_, container, false);
        Button signin = (Button) view.findViewById(R.id.signin);
        final EditText emailId = (EditText) view.findViewById(R.id.editTextEmail);
        final EditText password = (EditText) view.findViewById(R.id.editTextPassword);

        signup_button = (TextView) view.findViewById(R.id.signup_view);
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "ButtonCLicked in fragment", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new Signup_fragment()).commit();
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog.show(getActivity(), "Signing in", "Connecting...", true, false);
                if (AppController.checkConectivity(getContext())) {
                    if (emailId.getText().toString().trim().equals("") && password.getText().toString().equals("")) {
                        progressDialog.dismiss();
                        Util.failureToast(getContext(),"Please enter fields");
                        return;
                    } else {
//                        progressDialog = ProgressDialog.show(getActivity(), "Signing In", "Connecting", true, false);
                        auth.signInWithEmailAndPassword(emailId.getText().toString().trim(),password.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        progressDialog.dismiss();
                                        if (task.isSuccessful()){
                                            Util.successToast(getContext(),"SigninSuccess");
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Util.successToast(getContext(),e.getMessage());
                                e.printStackTrace();
                            }
                        });
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
                }
            }


        });

        return view;
    }

}
