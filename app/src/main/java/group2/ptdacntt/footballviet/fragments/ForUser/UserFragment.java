package group2.ptdacntt.footballviet.fragments.ForUser;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import group2.ptdacntt.footballviet.ContentApp.Dashboard;
import group2.ptdacntt.footballviet.Login;
import group2.ptdacntt.footballviet.Models.User;
import group2.ptdacntt.footballviet.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ImageView imageProfile;
    TextView tvUsernameProfile, tvFullnameProfile, tvAddressProfile, tvPhoneProfile;
    Button btnUpdate, btnLogout;
    FirebaseAuth mAuth;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
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
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            getUserData(user);
        }

        initEvents();
    }

    private void initEvents() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });
    }

    private void logOut() {
        mAuth.signOut();
        startActivity(new Intent(getActivity(), Login.class));
    }

    private void initView(View view) {
        imageProfile = view.findViewById(R.id.image_userProfile);
        tvUsernameProfile = view.findViewById(R.id.tvUsernameProfile);
        tvFullnameProfile = view.findViewById(R.id.tvFullnameProfile);
        tvAddressProfile = view.findViewById(R.id.tvAddressProfile);
        tvPhoneProfile = view.findViewById(R.id.tvPhoneProfile);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnLogout = view.findViewById(R.id.btnLogout);
        mAuth = FirebaseAuth.getInstance();
    }


    private void getUserData(FirebaseUser user) {
        String userId = user.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    tvUsernameProfile.setText(snapshot.child("email").getValue(String.class));
                    tvFullnameProfile.setText(snapshot.child("fullName").getValue(String.class));
                    tvAddressProfile.setText(snapshot.child("address").getValue(String.class));
                    tvPhoneProfile.setText(snapshot.child("phoneNumber").getValue(String.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}