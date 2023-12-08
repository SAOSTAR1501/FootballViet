package group2.ptdacntt.footballviet;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login_Page extends AppCompatActivity {
    EditText edtUsername, edtPassword;
    Button btnLogin;
    TextView tvForgotPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        addControls();
        addEvents();
    }

    private void addEvents() {
    }

    private void addControls() {
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvForgotPassword.setPaintFlags(tvForgotPassword.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
    }
}