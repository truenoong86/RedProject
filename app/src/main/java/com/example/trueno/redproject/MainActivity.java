package com.example.trueno.redproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.trueno.redproject.homefragments.Card;
import com.example.trueno.redproject.homefragments.Faq;
import com.example.trueno.redproject.homefragments.History;
import com.example.trueno.redproject.homefragments.Home;
import com.example.trueno.redproject.homefragments.Support;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;

public class MainActivity extends AppCompatActivity {

    TextView tvViewProfile;
    FragmentTransaction ft;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureNavigationDrawer();
        configureToolbar();

        NavigationView navigationView = (NavigationView) findViewById(R.id.left_drawer);
        View headerView = navigationView.getHeaderView(0);
        tvViewProfile = (TextView) headerView.findViewById(R.id.tvViewProfile);

        tvViewProfile.setText(Html.fromHtml("<u>View Profile</u>"));

        tvViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                CloseDrawer();
            }
        });

        if(savedInstanceState==null){
            replacefragment(new Home());
        }
    }

    private void configureNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.left_drawer);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId){
                    case R.id.action_home:
                        replacefragment(new Home());
                        CloseDrawer();
                        return true;

                    case R.id.action_history:
                        replacefragment(new History());
                        CloseDrawer();
                        return true;

                    case R.id.action_card:
                        replacefragment(new Card());
                        CloseDrawer();
                        return true;

                    case R.id.action_faq:
                        replacefragment(new Faq());
                        CloseDrawer();
                        return true;

                    case R.id.action_support:
                        replacefragment(new Support());
                        CloseDrawer();
                        return true;

                    case R.id.action_logout:
                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                            for (UserInfo userInfo : FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
                                if (userInfo.getProviderId().equals("facebook.com")) {
                                    LoginManager.getInstance().logOut();
                                    FirebaseAuth.getInstance().signOut();
                                    finish();
                                    startActivity(new Intent(MainActivity.this, LandingPage.class));
                                } else {
                                    FirebaseAuth.getInstance().signOut();
                                    finish();
                                    startActivity(new Intent(MainActivity.this, LandingPage.class));
                                }
                            }
                        } else {
                            startActivity(new Intent(MainActivity.this, LandingPage.class));
                        }

                        break;
                }
                return false;
            }
        });
    }

    private void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionbar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            // Android home
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    CloseDrawer();
                    return true;
                } else {
                    OpenDrawer();
                    return true;
                }
                // manage other entries if you have it ...
        }
        return false;
    }

    public void replacefragment(Fragment fragment) {
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, fragment);
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
    }

    public void CloseDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void OpenDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }
}
