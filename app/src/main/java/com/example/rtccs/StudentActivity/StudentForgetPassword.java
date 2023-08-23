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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rtccs.Model.Students;
import com.example.rtccs.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentForgetPassword extends AppCompatActivity {
    EditText mpassword,memail,mphone;
    Spinner myear;
    TextView mlogin,mpass,mprepass;
    Button getpass;
    ProgressDialog progressDialog;
    DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_forget_password);
        mpassword=findViewById(R.id.password);
        mphone=findViewById(R.id.phone);
        memail=findViewById(R.id.email);
        myear=findViewById(R.id.year);
        getpass=findViewById(R.id.get);
        mpass=findViewById(R.id.pass);
        mprepass=findViewById(R.id.pre_pass);

        mlogin=findViewById(R.id.fg_login);
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
        getpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email=memail.getText().toString();
                String phone=mphone.getText().toString();
                String year=myear.getSelectedItem().toString();

             if (TextUtils.isEmpty(email))
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
                    getpassword(email,phone,year);
                }
            }
        });


    }

    private void getpassword(String email, String phone, String year) {
    dbref= FirebaseDatabase.getInstance().getReference();
    dbref.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.child("Students").child(year).child(phone).exists())
            {
                Students stddata=snapshot.child("Students").child(year).child(phone).getValue(Students.class);
                if (stddata.getPhone().equals(phone))
                {
                    if (stddata.getEmail().equals(email))
                    {
                        progressDialog.dismiss();
                        mpass.setVisibility(View.VISIBLE);
                        mprepass.setVisibility(View.VISIBLE);
                        mpass.setText(stddata.getPassword());

                    }else
                    {
                        Toast.makeText(StudentForgetPassword.this, "check email", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(StudentForgetPassword.this, "check phone", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });

    }
}