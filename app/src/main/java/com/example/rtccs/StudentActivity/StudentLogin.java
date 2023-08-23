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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rtccs.AdminActivity.AdminLogin;
import com.example.rtccs.Model.Students;
import com.example.rtccs.Prevalent.Prevalent;
import com.example.rtccs.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class StudentLogin extends AppCompatActivity {

    EditText mphone,mpassword;
    Button mlogin;
    TextView madmin,msignup,mforgtepassword;
    CheckBox remember;
    Spinner myear;
    String parentdb="Students";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        mphone=findViewById(R.id.phone);
        mpassword=findViewById(R.id.password);
        madmin=findViewById(R.id.admin);
        remember=findViewById(R.id.remember);
        myear=findViewById(R.id.year);
        mlogin=findViewById(R.id.login);
        msignup=findViewById(R.id.signup_login);
        mforgtepassword=findViewById(R.id.forgetpassword);
        progressDialog=new ProgressDialog(this);

        Paper.init(this);

        ArrayAdapter<String> adapter= new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_custom,getResources().getStringArray(R.array.years));
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_custom);
        myear.setAdapter(adapter);


        msignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),StudentSignup.class));
            }
        });

        mforgtepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),StudentForgetPassword.class));
            }
        });


        madmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getApplicationContext(), AdminLogin.class);
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
        String year=myear.getSelectedItem().toString();
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
            login(phone,password,year);
        }


    }

    private void login(String phone, String password,String year) {

        if (remember.isChecked())
        {
            Paper.book().write(Prevalent.userPhonekey,phone);
            Paper.book().write(Prevalent.userPasswordkey,password);
            Paper.book().write(Prevalent.userYearkey,year);
            Paper.book().write(Prevalent.userdbnamekey,parentdb);


        }

            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
            dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(parentdb).child(year).child(phone).exists()) {
                        Students studentsdata = dataSnapshot.child(parentdb).child(year).child(phone).getValue(Students.class);
                        if (studentsdata.getStatus().equals("verified"))
                        {
                            if (studentsdata.getPhone().equals(phone)) {
                                if (studentsdata.getPassword().equals(password)) {
                                    progressDialog.dismiss();
                                    Prevalent.currentonlineuser = studentsdata;

                                    Intent ii = new Intent(getApplicationContext(), StudentIndexActivity.class);
                                    ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(ii);

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(StudentLogin.this, "check your password", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(StudentLogin.this, "Check the phone number", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(StudentLogin.this, "you are not verified by the Admin wait for the verification", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }




                    } else {
                        Toast.makeText(StudentLogin.this, "Account doesn't exists with this phone number ", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



    }
}