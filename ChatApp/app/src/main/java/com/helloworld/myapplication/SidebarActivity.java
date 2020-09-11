package com.helloworld.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;

public class SidebarActivity extends AppCompatActivity {

    private AppBarConfiguration configuration;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sidebar);

        Toolbar t = findViewById(R.id.toolbar_for_sidebar);
        setSupportActionBar(t);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_for_sidebar);
        NavigationView navigationView = findViewById(R.id.nav_host);
        configuration = new AppBarConfiguration.Builder(
                R.id.nav_chatrooms,R.id.nav_users,R.id.nav_myprofile,R.id.nav_logout)
                .setDrawerLayout(drawerLayout)
                .build();

        navController = Navigation.findNavController(this,R.id.frag_container);
        NavigationUI.setupActionBarWithNavController(this,navController,configuration);
        NavigationUI.setupWithNavController(navigationView,navController);

    }

    @Override
    public void onBackPressed() {
        navController.popBackStack();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, configuration)
                || super.onSupportNavigateUp();
    }
}