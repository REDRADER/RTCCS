package com.example.rtccs.CommonActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.rtccs.AdminActivity.AdminIndexActivity;
import com.example.rtccs.Model.Students;
import com.example.rtccs.Prevalent.Prevalent;
import com.example.rtccs.R;
import com.example.rtccs.StudentActivity.StudentIndexActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        Paper.init(this);

        String userphone = Paper.book().read(Prevalent.userPhonekey);
        String userpassword = Paper.book().read(Prevalent.userPasswordkey);
        String useryear = Paper.book().read(Prevalent.userYearkey);
        String userdbname = Paper.book().read(Prevalent.userdbnamekey);
        if (userphone != null && userdbname != null && userpassword != null) {
            if (!TextUtils.isEmpty(userphone) && !TextUtils.isEmpty(userpassword) && !TextUtils.isEmpty(userdbname))
                ;
            {

                AllowAccess(userphone, userdbname, userpassword, useryear);
            }
        } else
        {
            Runnable r = new Runnable() {

                @Override
                public void run() {
                    // if you are redirecting from a fragment then use getActivity() as the context.
                    Intent ii = new Intent(getApplicationContext(),MainActivity.class);
                    ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(ii);

                }
            };


            Handler h = new Handler();

            h.postDelayed(r, 3000);


        }

    }
    private void AllowAccess(String phone, String parentdb, String password, String year) {
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
        String student = "Students";
        String admin = "Admins";

        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (parentdb.equals(student)) {
                    if (dataSnapshot.child(parentdb).child(year).child(phone).exists()) {
                        Students studentsdata = dataSnapshot.child(parentdb).child(year).child(phone).getValue(Students.class);

                        if (studentsdata.getPhone().equals(phone)) {
                            if (studentsdata.getPassword().equals(password)) {

                                Prevalent.currentonlineuser = studentsdata;

                                Intent ii = new Intent(getApplicationContext(), StudentIndexActivity.class);
                                ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(ii);

                            } else {
                                Toast.makeText(getApplicationContext(), "check your password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Check the phone number", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Account doesn't exists with this phone number ", Toast.LENGTH_SHORT).show();

                    }
                }

                if (parentdb.equals(admin)) {
                    if (dataSnapshot.child(parentdb).child(phone).exists()) {
                        Students studentsdata = dataSnapshot.child(parentdb).child(phone).getValue(Students.class);

                        if (studentsdata.getPhone().equals(phone)) {
                            if (studentsdata.getPassword().equals(password)) {

                                Prevalent.currentonlineuser = studentsdata;

                                Intent ii = new Intent(getApplicationContext(), AdminIndexActivity.class);
                                ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(ii);

                            } else {
                                Toast.makeText(getApplicationContext(), "check your password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Check the phone number", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Account doesn't exists with this phone number ", Toast.LENGTH_SHORT).show();

                    }
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}