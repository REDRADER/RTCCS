package com.example.rtccs.AdminActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rtccs.Model.Students;
import com.example.rtccs.Prevalent.Prevalent;
import com.example.rtccs.R;
import com.example.rtccs.StudentActivity.StudentIndexActivity;
import com.example.rtccs.StudentActivity.StudentLogin;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class AdminLogin extends AppCompatActivity {
    EditText mphone,mpassword;
    Button mlogin;
    TextView mnotadmin;
    CheckBox remember;
    String parentdb="Admins";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        mphone=findViewById(R.id.phone);
        mpassword=findViewById(R.id.password);
        mnotadmin=findViewById(R.id.notadmin);
        remember=findViewById(R.id.remember);
        mlogin=findViewById(R.id.ad_login);
        progressDialog=new ProgressDialog(this);
        mnotadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getApplicationContext(), StudentLogin.class);
                startActivity(in);
            }
        });

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkdetails();
            }
        });
    }
    private void checkdetails() {
        String phone=mphone.getText().toString();

        String password=mpassword.getText().toString();
        if (TextUtils.isEmpty(phone))
        {
            mphone.setError("required");
        }
        else if (TextUtils.isEmpty(password))
        {
            mpassword.setError("required");
        }
        else
        {
            progressDialog.setTitle("Logging into Account");
            progressDialog.setMessage("Wait....");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            login(phone,password);
        }


    }

    private void login(String phone, String password) {

        if (remember.isChecked())
        {
            Paper.book().write(Prevalent.userPhonekey,phone);
            Paper.book().write(Prevalent.userPasswordkey,password);
            Paper.book().write(Prevalent.userdbnamekey,parentdb);

        }
        DatabaseReference dbref= FirebaseDatabase.getInstance().getReference();
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentdb).child(phone).exists())
                {
                    Students studentsdata=dataSnapshot.child(parentdb).child(phone).getValue(Students.class);

                    if (studentsdata.getPhone().equals(phone))
                    {
                        if (studentsdata.getPassword().equals(password))
                        {
                            progressDialog.dismiss();
                            Prevalent.currentonlineuser=studentsdata;

                            Intent ii=new Intent(getApplicationContext(),AdminIndexActivity.class);
                            ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(ii);

                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "check your password", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Check the phone number", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Account doesn't exists with this phone number ", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}