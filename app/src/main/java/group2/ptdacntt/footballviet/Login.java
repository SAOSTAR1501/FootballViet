package group2.ptdacntt.footballviet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import group2.ptdacntt.footballviet.Administrator.Dashboard_Administrator;
import group2.ptdacntt.footballviet.ContentApp.Dashboard;
import group2.ptdacntt.footballviet.Models.User;

public class Login extends AppCompatActivity {
    EditText edtUsername, edtPassword;
    Button btnLogin, btnSignUp;
    TextView tvForgotPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        addControls();
        addEvents();
    }

    private void addEvents() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, SignUp.class);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        String email = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Lấy UID của người dùng đăng nhập
                                String uid = user.getUid();

                                // Kiểm tra vai trò của người dùng
                                checkUserRole(uid);
                            }
                        } else {
                            // Đăng nhập không thành công, xử lý lỗi
                            Toast.makeText(Login.this, "Đăng nhập không thành công.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkUserRole(String uid) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Lấy thông tin người dùng
                    User user = dataSnapshot.getValue(User.class);

                    // Kiểm tra vai trò và chuyển hướng đến màn hình tương ứng
                    if (user != null && user.getRole().equals("administrator")) {
                        // Người dùng là admin, chuyển đến màn hình admin
                        startActivity(new Intent(Login.this, Dashboard_Administrator.class));
                    } else {
                        // Người dùng là user, chuyển đến màn hình user
                        startActivity(new Intent(Login.this, Dashboard.class));
                    }

                    finishAffinity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Login.this, "Không thể đăng nhập.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addControls() {
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvForgotPassword.setPaintFlags(tvForgotPassword.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        btnSignUp = findViewById(R.id.btnSignUp);

        mAuth = FirebaseAuth.getInstance();
    }
}