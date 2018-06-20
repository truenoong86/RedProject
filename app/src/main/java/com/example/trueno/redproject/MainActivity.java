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
import android.view.MenuItem;

import com.example.trueno.redproject.homefragments.Card;
import com.example.trueno.redproject.homefragments.Faq;
import com.example.trueno.redproject.homefragments.History;
import com.example.trueno.redproject.homefragments.Home;
import com.example.trueno.redproject.homefragments.Support;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity{
    FragmentTransaction ft;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureNavigationDrawer();
        configureToolbar();
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
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        startActivity(new Intent(MainActivity.this, LandingPage.class));
                }
                return false;
            }
        });
//        navView.setNavigationItemSelectedListener(menuItem -> {
//            int itemId = menuItem.getItemId();
//            return false;
//        });

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
