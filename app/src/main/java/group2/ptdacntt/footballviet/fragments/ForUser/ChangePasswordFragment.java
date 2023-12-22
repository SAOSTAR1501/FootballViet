package group2.ptdacntt.footballviet.fragments.ForUser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import group2.ptdacntt.footballviet.R;

public class ChangePasswordFragment extends Fragment {

    Button btnChange;
    EditText txtOldPassword;
    EditText txtNewPassword;
    EditText txtConfirmPassword;
    NavController navController;
    TextView txtProcessing;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        btnChange = view.findViewById(R.id.btnChange2);
        txtOldPassword = view.findViewById(R.id.txtFullName);
        txtNewPassword = view.findViewById(R.id.txtEmail);
        txtConfirmPassword = view.findViewById(R.id.txtPhone);
        progressBar = view.findViewById(R.id.progressBar);
        txtProcessing = view.findViewById(R.id.txtProcessing);
        navController = NavHostFragment.findNavController(ChangePasswordFragment.this);

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                txtProcessing.setVisibility(View.VISIBLE);
                changePassword();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    private void changePassword() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String oldPassword = txtOldPassword.getText().toString();
            String newPassword = txtNewPassword.getText().toString();
            String confirmPassword = txtConfirmPassword.getText().toString();

            if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(requireContext(), "New password and confirm password do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.updatePassword(newPassword)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {

                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressBar.setVisibility(View.GONE);
                                                txtProcessing.setVisibility(View.GONE);
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(requireContext(), "Đổi mật khẩu thành công.", Toast.LENGTH_LONG).show();
                                                    navController.navigate(R.id.action_changePasswordFragment_to_userFragment);
                                                } else {
                                                    Toast.makeText(requireContext(), "Đổi mật khẩu thất bại.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(requireContext(), "Lỗi xác thực.", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
        }
    }
}
