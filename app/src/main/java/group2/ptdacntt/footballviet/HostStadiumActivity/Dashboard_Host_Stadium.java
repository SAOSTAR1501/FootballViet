package group2.ptdacntt.footballviet.HostStadiumActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import group2.ptdacntt.footballviet.R;

public class Dashboard_Host_Stadium extends AppCompatActivity {
    BottomNavigationView bnvHostStadium;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_host_stadium);

        addcontrols();
        addEvents();
    }

    private void addEvents() {
    }

    private void addcontrols() {
        bnvHostStadium = findViewById(R.id.bnvHostStadium);
        NavHostFragment navHostFragment= (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_stadium_host_fragment);
        NavController navController=navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bnvHostStadium, navController);
    }
}