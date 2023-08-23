package com.example.rtccs.AdminActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.rtccs.AdminActivity.ui.AboutusFragment;
import com.example.rtccs.AdminActivity.ui.ChatFragment;
import com.example.rtccs.AdminActivity.ui.FacultyFragment;
import com.example.rtccs.AdminActivity.ui.GalleryFragment;
import com.example.rtccs.AdminActivity.ui.HomeFragment;
import com.example.rtccs.AdminActivity.ui.NoticeFragment;
import com.example.rtccs.AdminActivity.ui.ProfileFragment;
import com.example.rtccs.AdminActivity.ui.StudentFragment;
import com.example.rtccs.Model.Students;
import com.example.rtccs.Prevalent.Prevalent;
import com.example.rtccs.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import io.paperdb.Paper;

public class AdminIndexActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
Button logout;

private BottomNavigationView bottomNavigationView;

private Toolbar toolbar;
private DrawerLayout drawerLayout;
private ActionBarDrawerToggle toggle;
private NavigationView navigationView;
DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_index);
        bottomNavigationView=findViewById(R.id.bottomNavigationView);

        drawerLayout=findViewById(R.id.drawer_layout);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView=findViewById(R.id.navigation_view);
        Paper.init(getApplicationContext());
        toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        View navigationdrawer=navigationView.getHeaderView(0);
        ImageView navimage=navigationdrawer.findViewById(R.id.NavimageView);
        TextView navname=navigationdrawer.findViewById(R.id.Navfullname);
        TextView navyear=navigationdrawer.findViewById(R.id.Navyear);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.drawee);

        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        navimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new ProfileFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, fragment).commit();
                drawerLayout.closeDrawer(GravityCompat.START);

            }
        });
try {
    dbref= FirebaseDatabase.getInstance().getReference().child("Admins").child(Prevalent.currentonlineuser.getPhone());
    dbref.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists())
            {
                Students studentsdata=snapshot.getValue(Students.class);
                navname.setText(studentsdata.getFullname());
                navyear.setText(studentsdata.getPhone());
                Picasso.get().load(studentsdata.getImage()).into(navimage);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
}catch (Exception e)
{
    Toast.makeText(this, "unable to find year ", Toast.LENGTH_SHORT).show();
}


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment=null;
            switch (item.getItemId())
            {
                case R.id.navigation_home:
                    fragment = new HomeFragment();


                    break;
                case R.id.navigation_notice:
                    fragment = new NoticeFragment();
                    break;

                case R.id.navigation_gallery:
                    fragment = new GalleryFragment();
                    break;
                case R.id.navigation_Profile:
                    fragment = new ProfileFragment();
            }
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, fragment).commit();


            } else {
                // error in creating fragment
                Log.e("MainActivity", "Error in creating fragment");
            }
            return true;
        }
    };
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment sfragment=null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (item.getItemId())
        {
            case R.id.navigation_student:
                sfragment = new StudentFragment();


                break;
            case R.id.navigation_chat:
                sfragment = new ChatFragment();
                break;
            case R.id.navigation_faculty:
                sfragment = new FacultyFragment();
                break;
            case R.id.navigation_aboutus:
                sfragment = new AboutusFragment();
                break;
            case R.id.navigation_website:
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://www.stwilfreds.org/"));
                startActivity(viewIntent);
        }
       drawerLayout.closeDrawer(GravityCompat.START);
        if (sfragment != null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment, sfragment).commit();


        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }

        return true;
    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        Fragment sfragment=new StudentFragment();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        switch (item.getItemId())
//        {
//            case R.id.navigation_student:
//                sfragment = new StudentFragment();
//                break;
//            case R.id.navigation_faculty:
//                sfragment = new StudentFragment();
//
//                break;
//            case R.id.navigation_aboutus:
//                sfragment = new StudentFragment();
//                break;
//            case R.id.navigation_website:
//                sfragment = new StudentFragment();
//                break;
//
//        }
//
//
//
//
//
//        return true;
//    }
}