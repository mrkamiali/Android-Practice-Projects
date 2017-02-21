package com.kamranali.firebasemodel.signupfragment;


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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.kamranali.firebasemodel.R;
import com.kamranali.firebasemodel.fragment.Home_Fragment;
import com.kamranali.firebasemodel.model.User;
import com.kamranali.firebasemodel.utils.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class Signup_fragment extends Fragment  implements View.OnClickListener {

    private View view;
    private EditText email_view, userID_view, firstName_view, lastName_view, pasword_view, conformPassword_view;
    String email, userID, password, confirmPassword, firstname, lastname;
    private Button signup_button;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;


    public Signup_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_signup_fragment, container, false);

        initView();

        return view;
    }

    private void initView() {
        email_view = (EditText) view.findViewById(R.id.editText_email);
        userID_view = (EditText) view.findViewById(R.id.editText_userID);
        firstName_view = (EditText) view.findViewById(R.id.editText_fname);
        lastName_view = (EditText) view.findViewById(R.id.editText_lastName);
        pasword_view = (EditText) view.findViewById(R.id.editText_password);
        conformPassword_view = (EditText) view.findViewById(R.id.editText_confirmPassword);
        signup_button = (Button) view.findViewById(R.id.buttonSignUp);
        signup_button.setOnClickListener(this);
        addFocusListeners();
    }

    private void addFocusListeners() {
        email_view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String  text= ((EditText)v).getText().toString().trim();
                    if (text.length()==0){
                        email_view.setError("Enter Email Address");
                    }else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()){
                        email_view.setError("Enter Valid Email Address");
                    }
                }
            }
        });
        firstName_view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String  text= ((EditText)v).getText().toString().trim();
                    if (text.length()==0){
                        firstName_view.setError("Enter First Name");
                    } else if (!text.matches("[a-zA-Z ]+")) {
                        firstName_view.setError("Invalid Name");
                    }
                }
            }
        });
        lastName_view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String text = ((EditText) v).getText().toString().trim();
                    if (text.length() == 0) {
                        lastName_view.setError("Enter Last Name");
                    } else if (!text.matches("[a-zA-Z ]+")) {
                        lastName_view.setError("Invalid Name");
                    }
                }
            }
        });
        pasword_view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    String s = ((EditText) v).getText().toString().trim();
                    if (s.length()!=6){
                        pasword_view.setError("Password should be at least 6 character.");
                    }
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        email = email_view.getText().toString().trim();
        userID = userID_view.getText().toString().trim();
        firstname = firstName_view.getText().toString().trim();
        lastname = lastName_view.getText().toString().trim();
        password = pasword_view.getText().toString();
        confirmPassword = conformPassword_view.getText().toString();

        boolean invalid = false;

        if ( (firstname.equals("")
                || lastname.equals("")
                || userID.equals("")
                || confirmPassword.equals("")
                || email.equals("")
                || password.equals("")) ) {
            Util.failureToast(getActivity(),"Fields Should not be left Empty");
            invalid = true;
        }
        Pattern pattern = Pattern.compile("^[0-9]");
        Matcher matcher = pattern.matcher(userID);
        if(matcher.find(0)){
            userID_view.setError("UserID must start with Alphabet");
            invalid = true;
        }
        if(email.length()==0 || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ){
            email_view.setError("Enter Valid Email Address");
            invalid = true;
        }
        if(firstname.length()== 0 || !firstname.matches("[a-zA-Z ]+")){
            firstName_view.setError("Invalid Name");
            invalid = true;
        }
        if(lastname.length() == 0 || !lastname.matches("[a-zA-Z ]+")){
            lastName_view.setError("Invalid Name");
            invalid = true;
        }
        if (!password.equals(confirmPassword)) {
            conformPassword_view.setError("Passwords does not match");
            invalid = true;
        }
        if (invalid) {
            return;
        }

        progressDialog = ProgressDialog.show(getActivity(), "Signing Up", "Connecting...", true, false);

        User user = new User(email, userID, firstname, lastname, password);
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Util.successToast(getActivity(),"CreatedUser Successfully!");
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new Home_Fragment()).commit();
                        progressDialog.dismiss();
                    }else {
                        Util.failureToast(getActivity(),"SignupFailed");
                    }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });

    }
}
