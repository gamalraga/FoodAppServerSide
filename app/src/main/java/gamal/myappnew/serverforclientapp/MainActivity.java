package gamal.myappnew.serverforclientapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import gamal.myappnew.serverforclientapp.Common.Common;
import gamal.myappnew.serverforclientapp.Moduel.User;
import gamal.myappnew.serverforclientapp.Server.Lisiner;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
  public   static  NavController navController;
    DrawerLayout drawer;
  public static   Context activity;
    public static FloatingActionButton fab;
    boolean issent=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    navController.navigate(R.id.nav_addcategory);
                    getSupportActionBar().setTitle("Add Category");

            }
        });
        activity=this;

            Intent intent = new Intent(MainActivity.this, Lisiner.class);
            startService(intent);

         drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_addcategory, R.id.nav_category,
                R.id.nav_foodlist, R.id.nav_requests, R.id.nav_myAccount)
                .setDrawerLayout(drawer)
                .build();
         navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
        View headerLayout = navigationView.getHeaderView(0);
        final ImageView imageView=headerLayout.findViewById(R.id.imageview_header);
        final TextView username=headerLayout.findViewById(R.id.username_header);
        if (isNetworkConnected()==false)
        {
            getSupportActionBar().setTitle("Waiti, to connect with network");

        }
        final TextView bio=headerLayout.findViewById(R.id.bio_header);
        if (Common.isconnectiontointernet(getApplicationContext())) {
            FirebaseDatabase.getInstance().getReference(Common.USERS_REF)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            if (user.getisStaff()) {
                                Common.CURRENT_USER = user;
                                Glide.with(getApplicationContext()).load(Common.CURRENT_USER.getImageurl())
                                        .into(imageView);
                                username.setText(Common.CURRENT_USER.getUsername());
                                bio.setText(Common.CURRENT_USER.getBio());
                            }else {
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(MainActivity.this,StartActivity.class));
                                Toast.makeText(MainActivity.this, "you not found in staff", Toast.LENGTH_SHORT).show();
                            finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }else {
            getSupportActionBar().setTitle("Check The internet");


            Toast.makeText(this, "Check internet", Toast.LENGTH_SHORT).show();
        }

        Bundle bundle=getIntent().getExtras();

        if (bundle!=null)
        {
            String menuid= bundle.getString("menuid");
            String menuname=bundle.getString("menuname");
            SharedPreferences.Editor editor=getSharedPreferences("PREFS",MODE_PRIVATE).edit();
            editor.putString("menuid", menuid);
            editor.apply();
            navController.navigate(R.id.nav_showfood);
            getSupportActionBar().setTitle(menuname);

        }
        ////////////////////


    }




    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        drawer.closeDrawers();
        switch (item.getItemId())
        {
            case R.id.nav_home:
                navController.navigate(R.id.nav_home);
                getSupportActionBar().setTitle("Home");
                break;

            case R.id.nav_category:
                navController.navigate(R.id.nav_category);
                getSupportActionBar().setTitle("Category");
                break;

            case R.id.nav_foodlist:
                navController.navigate(R.id.nav_foodlist);
                getSupportActionBar().setTitle("FoodList");
                break;
            case R.id.nav_creatShipper:
               startActivity(new Intent(getApplicationContext(),CreateShipper.class));
                break;

            case R.id.nav_myAccount:
                navController.navigate(R.id.nav_myAccount);
                getSupportActionBar().setTitle("Account");
                break;

            case R.id.nav_addcategory:
                navController.navigate(R.id.nav_addcategory);
                getSupportActionBar().setTitle("Add Category");
                break;

            case R.id.nav_requests:
                navController.navigate(R.id.nav_requests);
                getSupportActionBar().setTitle("Requests");
                break;

            case R.id.Logout:
                AlertDialog alertDialog=new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setMessage("are you sure to log out ?");
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancle",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "Log Out", Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(MainActivity.this,StartActivity.class));
                                finish();
                            }
                        });
                alertDialog.show();
                getSupportActionBar().setTitle("Log Out");
                break;

        }
        return false;
    }
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);

        }
        else {
            super.onBackPressed();
        }
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                android.app.AlertDialog alertDialog=new android.app.AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setMessage("are you sure to log out ?");
                alertDialog.setButton(android.app.AlertDialog.BUTTON_NEGATIVE, "Cancle",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "Log Out", Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();
                                MainActivity.this.startActivity(new Intent(MainActivity.this, StartActivity.class));
                                MainActivity.this.finish();
                            }
                        });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
