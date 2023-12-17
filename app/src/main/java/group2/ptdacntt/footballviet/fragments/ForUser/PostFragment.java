package group2.ptdacntt.footballviet.fragments.ForUser;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

import group2.ptdacntt.footballviet.Models.NewFeed;
import group2.ptdacntt.footballviet.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final int PICK_IMAGE_REQUEST = 1;
    Button btnUp,btnChoose;
    ImageView imgAnh;
    private Uri imageUri;
    private StorageReference storageReference;
    DatabaseReference databaseReference;
    EditText edtName,edtNgay,edtGio,edtSan,edtContent;
    static String date;


    public PostFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostFragment newInstance(String param1, String param2) {
        PostFragment fragment = new PostFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnUp=view.findViewById(R.id.btnUp);
        btnChoose=view.findViewById(R.id.btnChoose);
        imgAnh=view.findViewById(R.id.img);
        edtName=view.findViewById(R.id.edtName);
        edtSan=view.findViewById(R.id.edtSan);
        edtNgay=view.findViewById(R.id.edtNgay);
        edtGio=view.findViewById(R.id.edtGio);
        edtContent=view.findViewById(R.id.edtContent);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        FirebaseAuth auth =FirebaseAuth.getInstance();
        FirebaseUser user= auth.getCurrentUser();
        Log.d("TAG", "onViewCreated: "+ user.getEmail());
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        date=day+"-"+month+"-"+year;
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUri != null) {
                    final ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();
                    StorageReference ref = storageReference.child(user.getEmail().toString()+"/"+user.getUid()+date);
                    ref.putFile(imageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> download=taskSnapshot.getStorage().getDownloadUrl();
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Upload successful", Toast.LENGTH_SHORT).show();
                                    download.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String down=uri.toString();
                                            Log.d("TAG", "onSuccess: "+down);
                                            String name=edtName.getText().toString().trim();
                                            String san=edtSan.getText().toString().trim();
                                            String ngay=edtNgay.getText().toString().trim();
                                            String gio=edtGio.getText().toString().trim();
                                            String content=edtContent.getText().toString().trim();
                                            NewFeed newFeed=new NewFeed(user.getEmail() + "/"+user.getUid()+date,name,san,ngay,gio,content,down);
                                            databaseReference.child("users").child(user.getUid()).child("posts").child(user.getUid()+date).setValue(newFeed);
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
                    Toast.makeText(getContext(), "No file selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imgAnh.setImageURI(imageUri);
        }
    }
}