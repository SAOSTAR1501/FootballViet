package group2.ptdacntt.footballviet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import group2.ptdacntt.footballviet.ContentApp.Dashboard;
import group2.ptdacntt.footballviet.Models.User;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    EditText edtEmail, edtFullName, edtUsername, edtAddress, edtPhoneNumber, edtPassword, edtConfirmPassword;
    Button btnSignUp;
    RadioButton rbPerson, rbHostStadium;
    RadioGroup rbRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAccount();
            }
        });
    }

    private void addAccount() {
        String email = edtEmail.getText().toString().trim();
        String fullName = edtFullName.getText().toString().trim();
        String username = edtUsername.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String phoneNumber = edtPhoneNumber.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        String Role = getSelectedRole();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(username) || TextUtils.isEmpty(address)
                || TextUtils.isEmpty(phoneNumber) || (TextUtils.isEmpty(Role)) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) ||TextUtils.isEmpty(fullName)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra xác nhận mật khẩu
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu không khớp.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Đăng ký thành công, lấy ID người dùng

                            String userId = mAuth.getCurrentUser().getUid();

                            // Lưu thông tin người dùng vào Firebase Realtime Database
                            saveUserInfoToFirebase(userId, email,password, username, fullName, address, Role, phoneNumber);
                        } else {
                            // Đăng ký không thành công, xử lý lỗi
                            Toast.makeText(SignUp.this, "Đăng ký không thành công.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveUserInfoToFirebase(String userId, String email, String password, String username, String fullName, String address, String role, String phoneNumber) {
        // Tạo đối tượng User
        User user = new User(userId, email, password, username,fullName, address, role, phoneNumber);
        usersRef.child(userId).setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUp.this, Dashboard.class));
                            finishAffinity();
                        } else {
                            Toast.makeText(SignUp.this, "Lưu thông tin người dùng không thành công.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private String getSelectedRole() {
        int selectedId = rbRole.getCheckedRadioButtonId();

        if (selectedId == R.id.rbPerson) {
            return "Cá nhân";
        } else if (selectedId == R.id.rbHostStadium) {
            return "Chủ sân";
        } else {
            // Nếu không có RadioButton nào được chọn
            return "";
        }
    }
    private void addControls() {
        edtEmail = findViewById(R.id.edtEmail);
        edtUsername = findViewById(R.id.edtUsername);
        edtFullName = findViewById(R.id.edtFullName);
        edtAddress = findViewById(R.id.edtAddress);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        rbPerson = findViewById(R.id.rbPerson);
        rbHostStadium = findViewById(R.id.rbHostStadium);
        rbRole = findViewById(R.id.rbRole);
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");
    }
}