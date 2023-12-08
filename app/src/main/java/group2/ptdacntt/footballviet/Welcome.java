package group2.ptdacntt.footballviet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Welcome extends AppCompatActivity {
    Button btnLoginPage, btnSignUpPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();
        addEvents();
    }

    private void addEvents() {
        btnLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Welcome.this, Login_Page.class);
                startActivity(intent);
            }
        });

        btnSignUpPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Welcome.this, SignUp_Page.class);
                startActivity(intent);
            }
        });
    }

    private void addControls() {
        btnSignUpPage = findViewById(R.id.btnSignUpPage);
        btnLoginPage = findViewById(R.id.btnLoginPage);
    }
}