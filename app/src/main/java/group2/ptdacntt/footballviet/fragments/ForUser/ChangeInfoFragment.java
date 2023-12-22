package group2.ptdacntt.footballviet.fragments.ForUser;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import group2.ptdacntt.footballviet.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangeInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangeInfoFragment extends Fragment {
    Button btnSave;
    Button btnChoose;
    EditText etFullName;
    EditText etEmail;
    EditText etPhone;
    ImageView userImage;
    NavController navController;
    Uri uri;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChangeInfoFragment() {
        // Required empty public constructor
    }
    private ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            uri = result.getData().getData();
                            userImage.setImageURI(uri);
                        }
                    });
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangeInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangeInfoFragment newInstance(String param1, String param2) {
        ChangeInfoFragment fragment = new ChangeInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_info, container, false);

        return inflater.inflate(R.layout.fragment_change_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        etFullName = view.findViewById(R.id.txtFullName);
        etEmail = view.findViewById(R.id.txtEmail);
        etPhone = view.findViewById(R.id.txtPhone);
        btnSave = view.findViewById(R.id.btnUpdate);
        btnChoose = view.findViewById(R.id.chooseAvatar);
        userImage = view.findViewById(R.id.profile_image2);
        navController = NavHostFragment.findNavController(ChangeInfoFragment.this);

        btnSave.setOnClickListener(v -> {
            updateUserInfo();
        });
        btnChoose.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            imagePickerLauncher.launch(intent);
        });

        super.onViewCreated(view, savedInstanceState);
    }

    private void updateUserInfo() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

            String fullName = etFullName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();

            if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }
            databaseReference.child("fullName").setValue(fullName);
            databaseReference.child("email").setValue(email);
            databaseReference.child("phoneNumber").setValue(phone);
            databaseReference.child("image").setValue(uri);
            Toast.makeText(requireContext(), "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
        }


        navController.navigate(R.id.action_changePasswordFragment_to_userFragment);
    }
}