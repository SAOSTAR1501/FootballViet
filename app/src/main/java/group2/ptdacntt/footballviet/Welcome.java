package group2.ptdacntt.footballviet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import group2.ptdacntt.footballviet.ContentApp.Dashboard;
import group2.ptdacntt.footballviet.HostStadiumActivity.Dashboard_Host_Stadium;
import group2.ptdacntt.footballviet.Models.User;

public class Welcome extends AppCompatActivity {
    Button btnLoginPage, btnSignUpPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void addEvents() {
        btnLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Welcome.this, Login.class);
                startActivity(intent);
            }
        });

        btnSignUpPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Welcome.this, SignUp.class);
                startActivity(intent);
            }
        });
    }

    private void addControls() {
        btnSignUpPage = findViewById(R.id.btnSignUpPage);
        btnLoginPage = findViewById(R.id.btnLoginPage);
    }
}