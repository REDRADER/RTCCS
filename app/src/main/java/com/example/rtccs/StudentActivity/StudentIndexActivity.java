package com.example.rtccs.StudentActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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

import com.example.rtccs.AdminActivity.ui.AboutusFragment;
import com.example.rtccs.AdminActivity.ui.FacultyFragment;
import com.example.rtccs.AdminActivity.ui.GalleryFragment;
import com.example.rtccs.AdminActivity.ui.HomeFragment;
import com.example.rtccs.AdminActivity.ui.NoticeFragment;
import com.example.rtccs.AdminActivity.ui.ProfileFragment;
import com.example.rtccs.AdminActivity.ui.StudentFragment;
import com.example.rtccs.Model.Students;
import com.example.rtccs.Prevalent.Prevalent;
import com.example.rtccs.R;
import com.example.rtccs.StudentActivity.StudentUi.StudentAboutusFragment;
import com.example.rtccs.StudentActivity.StudentUi.StudentFacultyFragment;
import com.example.rtccs.StudentActivity.StudentUi.StudentGalleryFragment;
import com.example.rtccs.StudentActivity.StudentUi.StudentHomeFragment;
import com.example.rtccs.StudentActivity.StudentUi.StudentNoticeFragment;
import com.example.rtccs.StudentActivity.StudentUi.StudentProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class StudentIndexActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView bottomNavigationView;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    Button logout;
    DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_index);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.navigation_view);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.drawee);
        Paper.init(getApplicationContext());
        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        Fragment fragment=new StudentHomeFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.Student_fragment, fragment).commit();

        View navigationdrawer=navigationView.getHeaderView(0);
        ImageView navimage=navigationdrawer.findViewById(R.id.NavimageView);
        TextView navname=navigationdrawer.findViewById(R.id.Navfullname);
        TextView navyear=navigationdrawer.findViewById(R.id.Navyear);
        navimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new StudentProfileFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.Student_fragment, fragment).commit();
                drawerLayout.closeDrawer(GravityCompat.START);

            }
        });
        dbref= FirebaseDatabase.getInstance().getReference().child("Students").child(Prevalent.currentonlineuser.getYear()).child(Prevalent.currentonlineuser.getPhone());
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    Students studentsdata=snapshot.getValue(Students.class);
                    navname.setText(studentsdata.getFullname());
                    navyear.setText(studentsdata.getYear());
                    Picasso.get().load(studentsdata.getImage()).into(navimage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
     }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new StudentHomeFragment();


                    break;
                case R.id.navigation_notice:
                    fragment = new StudentNoticeFragment();
                    break;
                case R.id.navigation_gallery:
                    fragment = new StudentGalleryFragment();
                    break;
                case R.id.navigation_Profile:
                    fragment = new StudentProfileFragment();
            }
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.Student_fragment, fragment).commit();


            } else {
                // error in creating fragment
                Log.e("MainActivity", "Error in creating fragment");
            }
            return true;
        }
    };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment sfragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (item.getItemId()) {
            case R.id.student_navigation_faculty:
                sfragment = new StudentFacultyFragment();
                break;
            case R.id.student_navigation_aboutus:
                sfragment = new StudentAboutusFragment();
                break;
            case R.id.student_navigation_chat:
              chat();
                break;
            case R.id.student_navigation_website:
                Intent viewIntent =new Intent("android.intent.action.VIEW",
                                Uri.parse("https://www.stwilfreds.org/"));
                startActivity(viewIntent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        if (sfragment != null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.Student_fragment, sfragment).commit();


        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }

        return true;
    }

   private void chat()
   {
            dbref=FirebaseDatabase.getInstance().getReference().child("Chat").child(Prevalent.currentonlineuser.getYear());
            dbref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String uri=snapshot.child("uri").getValue(String.class);
                   // Toast.makeText(StudentIndexActivity.this, ""+uri, Toast.LENGTH_SHORT).show();
                    if (uri!=null)
                    {
                        Intent yyt =new Intent("android.intent.action.VIEW",
                                Uri.parse(uri));
                        startActivity(yyt);
                    }
                    else
                    {
                        Toast.makeText(StudentIndexActivity.this, "chat not loading chat ", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(StudentIndexActivity.this, "error loading chat ", Toast.LENGTH_SHORT).show();
                }
            });

   }


}