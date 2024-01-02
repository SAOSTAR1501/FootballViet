package group2.ptdacntt.footballviet.fragments.ForUser;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import group2.ptdacntt.footballviet.Models.NewFeed;
import group2.ptdacntt.footballviet.Models.User;
import group2.ptdacntt.footballviet.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangeInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangeInfoFragment extends Fragment {
    Button btnSave;
    Button btnChoose;
    EditText edtFullName, edtDiaChi, edtPhone;
    private static final int PICK_IMAGE_REQUEST = 1;
    ImageView userImage;
    NavController navController;
    Uri uri;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    StorageReference storageReference;

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
        edtFullName = view.findViewById(R.id.txtFullName);
        edtDiaChi = view.findViewById(R.id.txtEmail);
        edtPhone = view.findViewById(R.id.txtPhone);
        btnSave = view.findViewById(R.id.btnUpdate);
        btnChoose = view.findViewById(R.id.chooseAvatar);
        storageReference = FirebaseStorage.getInstance().getReference();
        userImage = view.findViewById(R.id.profile_image2);
        navController = NavHostFragment.findNavController(ChangeInfoFragment.this);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String date=day+"-"+month+"-"+year;
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date currentTime = calendar.getTime();
        String time = timeFormat.format(currentTime);

        FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user1 = snapshot.getValue(User.class);
                    edtFullName.setText(user1.getFullName());
                    edtPhone.setText(user1.getPhoneNumber());
                    edtDiaChi.setText(user1.getAddress());
                    Glide.with(getContext()).load(user1.getProfileImage()).into(userImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnSave.setOnClickListener(v -> {
            if (uri != null) {
                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle("Uploading...");
                progressDialog.show();
                StorageReference ref = storageReference.child(user.getEmail().toString()+"/"+user.getUid()+date+time);
                ref.putFile(uri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> download=taskSnapshot.getStorage().getDownloadUrl();
                                progressDialog.dismiss();
                                download.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String down=uri.toString();
                                        Log.d("TAG", "onSuccess: "+down);

                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

                                        String fullName = edtFullName.getText().toString().trim();
                                        String email = edtDiaChi.getText().toString().trim();
                                        String phone = edtPhone.getText().toString().trim();

                                        if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                                            Toast.makeText(requireContext(), "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        databaseReference.child("fullName").setValue(fullName);
                                        databaseReference.child("email").setValue(email);
                                        databaseReference.child("phoneNumber").setValue(phone);
                                        databaseReference.child("profileImage").setValue(down);
                                        Toast.makeText(requireContext(), "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                                        navController.navigate(R.id.action_changeInfoFragment_to_userFragment);
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("TAG", "onFailure: "+e.getMessage());
                            }
                        });

            } else {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

                String fullName = edtFullName.getText().toString().trim();
                String email = edtDiaChi.getText().toString().trim();
                String phone = edtPhone.getText().toString().trim();

                if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(requireContext(), "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }
                databaseReference.child("fullName").setValue(fullName);
                databaseReference.child("email").setValue(email);
                databaseReference.child("phoneNumber").setValue(phone);
                Toast.makeText(requireContext(), "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                navController.navigate(R.id.action_changeInfoFragment_to_userFragment);
            }
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


        if (user != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

            String fullName = edtFullName.getText().toString().trim();
            String email = edtDiaChi.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();

            if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }
            databaseReference.child("fullName").setValue(fullName);
            databaseReference.child("email").setValue(email);
            databaseReference.child("phoneNumber").setValue(phone);
            databaseReference.child("profileImage").setValue(uri);
            Toast.makeText(requireContext(), "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
        }


        navController.navigate(R.id.action_changePasswordFragment_to_userFragment);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            userImage.setImageURI(uri);
        }
    }
}