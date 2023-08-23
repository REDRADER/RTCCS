package com.example.rtccs.StudentActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rtccs.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class StudentSignup extends AppCompatActivity {

    EditText mfullname,mpassword,memail,mphone;
    Spinner myear;
    TextView mlogin;
    Button msignup;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_signup);
        mfullname=findViewById(R.id.fullname);
        mpassword=findViewById(R.id.password);
        memail=findViewById(R.id.email);
        mphone=findViewById(R.id.phone);
        myear=findViewById(R.id.year);
        msignup=findViewById(R.id.signup);
        mlogin=findViewById(R.id.signup_login);
        progressDialog=new ProgressDialog(this);

        ArrayAdapter<String> adapter= new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_custom,getResources().getStringArray(R.array.years));
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_custom);
        myear.setAdapter(adapter);



        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),StudentLogin.class));
            }
        });

        msignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createAccount();

            }
        });

    }

    private void createAccount() {
        String fullname=mfullname.getText().toString();
        String password=mpassword.getText().toString();
        String email=memail.getText().toString();
        String phone=mphone.getText().toString();
        String year=myear.getSelectedItem().toString();
        String status="notverified";

        if (TextUtils.isEmpty(fullname))
        {
            mfullname.setError("Required");

        }
        else if (TextUtils.isEmpty(password))
        {
            mpassword.setError("Required");
        }
        else if (TextUtils.isEmpty(email))
        {
            memail.setError("Required");
        }
        else if (TextUtils.isEmpty(phone))
        {
            mphone.setError("Required");
        }

       else
        {
            progressDialog.setTitle("Creating Account");
            progressDialog.setMessage("Wait....");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            SubmitData(fullname,password,email,phone,year,status);
        }





    }

    private void SubmitData(String fullname, String password, String email, String phone, String year,String status) {

        final DatabaseReference dbref;
        dbref= FirebaseDatabase.getInstance().getReference();

        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Students").child(year).child(phone).exists()))
                {
                    HashMap<String,Object> studdata=new HashMap<>();
                    studdata.put("fullname",fullname);
                    studdata.put("phone",phone);
                    studdata.put("password",password);
                    studdata.put("email",email);
                    studdata.put("year",year);
                    studdata.put("status",status);

                    dbref.child("Students").child(year).child(phone).updateChildren(studdata)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                   if (task.isSuccessful())
                                   {
                                       progressDialog.dismiss();
                                       Toast.makeText(StudentSignup.this, "Account has been created succesfully proceed to login", Toast.LENGTH_SHORT).show();
                                       startActivity(new Intent(getApplicationContext(),StudentLogin.class));


                                   }
                                   else
                                   {
                                       progressDialog.dismiss();
                                       Toast.makeText(StudentSignup.this, "Something went Wrong try again!", Toast.LENGTH_SHORT).show();
                                   }
                                }
                            });

                 }
                else
                {
                    Toast.makeText(StudentSignup.this, "Account with this "+phone+"already exists", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Toast.makeText(StudentSignup.this, "click on login to go to login", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}