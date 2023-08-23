package com.example.rtccs.CommonActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rtccs.AdminActivity.AdminIndexActivity;
import com.example.rtccs.AdminActivity.AdminLogin;
import com.example.rtccs.Model.Students;
import com.example.rtccs.Prevalent.Prevalent;
import com.example.rtccs.R;
import com.example.rtccs.StudentActivity.StudentIndexActivity;
import com.example.rtccs.StudentActivity.StudentLogin;
import com.example.rtccs.StudentActivity.StudentSignup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    Button mlog;
    TextView msignup;
   // ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mlog = findViewById(R.id.login);
        msignup = findViewById(R.id.signup);
       // progressDialog = new ProgressDialog(this);

     //   Paper.init(this);


        mlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StudentLogin.class);
                startActivity(intent);

            }
        });

        msignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(getApplicationContext(), StudentSignup.class);
                startActivity(ii);
            }
        });

//        String userphone = Paper.book().read(Prevalent.userPhonekey);
//        String userpassword = Paper.book().read(Prevalent.userPasswordkey);
//        String useryear = Paper.book().read(Prevalent.userYearkey);
//        String userdbname = Paper.book().read(Prevalent.userdbnamekey);
//        if (userphone != null && userdbname != null && userpassword != null) {
//            if (!TextUtils.isEmpty(userphone) && !TextUtils.isEmpty(userpassword) && !TextUtils.isEmpty(userdbname))
//                ;
//            {
//                progressDialog.setTitle("Allready Logged in ");
//                progressDialog.setMessage("Wait....");
//                progressDialog.setCanceledOnTouchOutside(false);
//                progressDialog.show();
//                AllowAccess(userphone, userdbname, userpassword, useryear);
//            }
//        }
//


    }

//    private void AllowAccess(String phone, String parentdb, String password, String year) {
//        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
//        String student = "Students";
//        String admin = "Admins";
//
//        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                if (parentdb.equals(student)) {
//                    if (dataSnapshot.child(parentdb).child(year).child(phone).exists()) {
//                        Students studentsdata = dataSnapshot.child(parentdb).child(year).child(phone).getValue(Students.class);
//
//                        if (studentsdata.getPhone().equals(phone)) {
//                            if (studentsdata.getPassword().equals(password)) {
//                                progressDialog.dismiss();
//                                Prevalent.currentonlineuser = studentsdata;
//                                Toast.makeText(getApplicationContext(), "Logged in succesfully", Toast.LENGTH_SHORT).show();
//                                Intent ii = new Intent(getApplicationContext(), StudentIndexActivity.class);
//                                ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(ii);
//
//                            } else {
//                                Toast.makeText(getApplicationContext(), "check your password", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            Toast.makeText(getApplicationContext(), "Check the phone number", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Account doesn't exists with this phone number ", Toast.LENGTH_SHORT).show();
//                        progressDialog.dismiss();
//                    }
//                }
//
//                if (parentdb.equals(admin)) {
//                    if (dataSnapshot.child(parentdb).child(phone).exists()) {
//                        Students studentsdata = dataSnapshot.child(parentdb).child(phone).getValue(Students.class);
//
//                        if (studentsdata.getPhone().equals(phone)) {
//                            if (studentsdata.getPassword().equals(password)) {
//                                progressDialog.dismiss();
//                                Prevalent.currentonlineuser = studentsdata;
//                                Toast.makeText(getApplicationContext(), "Logged in succesfully", Toast.LENGTH_SHORT).show();
//                                Intent ii = new Intent(getApplicationContext(), AdminIndexActivity.class);
//                                ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(ii);
//
//                            } else {
//                                Toast.makeText(getApplicationContext(), "check your password", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            Toast.makeText(getApplicationContext(), "Check the phone number", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Account doesn't exists with this phone number ", Toast.LENGTH_SHORT).show();
//                        progressDialog.dismiss();
//                    }
//                }
//            }
//
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//
//    }
    }
