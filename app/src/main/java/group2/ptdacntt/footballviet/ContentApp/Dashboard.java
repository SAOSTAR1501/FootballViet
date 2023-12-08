package group2.ptdacntt.footballviet.ContentApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import group2.ptdacntt.footballviet.R;

public class Dashboard extends AppCompatActivity {
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        addControls();
    }

    private void addControls() {
        toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
    }
}