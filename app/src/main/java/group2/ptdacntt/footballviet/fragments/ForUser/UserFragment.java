package group2.ptdacntt.footballviet.fragments.ForUser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import group2.ptdacntt.footballviet.Login;
import group2.ptdacntt.footballviet.Models.User;
import group2.ptdacntt.footballviet.R;

public class UserFragment extends Fragment {

    Button btnChange, btnEdit, btnLogout, btnHistoryPosts, btnHistoryBookings,btnMess;
    NavController navController;
    TextView txtFullName;
    TextView txtEmail;
    ImageView profile_image2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        btnChange = view.findViewById(R.id.btnChange);
        btnEdit = view.findViewById(R.id.btnEdit);
        btnHistoryPosts = view.findViewById(R.id.btnHistoryPosts);
        btnHistoryBookings = view.findViewById(R.id.btnHistoryBookings);
        btnLogout = view.findViewById(R.id.btnLogout);
        txtFullName = view.findViewById(R.id.textInputLayout);
        txtEmail = view.findViewById(R.id.userEmail);
        btnMess=view.findViewById(R.id.btnMess);
        profile_image2=view.findViewById(R.id.profile_image2);
        navController = NavHostFragment.findNavController(UserFragment.this);

        if (user != null) {
            DatabaseReference userRef = databaseReference.child("users").child(user.getUid());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        User userProfile = snapshot.getValue(User.class);

                        if (userProfile != null) {
                            txtFullName.setText(userProfile.getFullName());
                            txtEmail.setText(userProfile.getEmail());
                            if(userProfile.getProfileImage()!=null){
                                Glide.with(getContext()).load(userProfile.getProfileImage()).into(profile_image2);
                            }else{
                                profile_image2.setImageResource(R.drawable.user);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_userFragment_to_changePasswordFragment);
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_userFragment_to_changeInfoFragment);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(requireContext(), Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                requireActivity().finish();
            }
        });

        btnHistoryPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_userFragment_to_historyPostsFragment);
            }
        });
        btnHistoryBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_userFragment_to_historyBookingsFragment);
            }
        });
        btnMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_userFragment_to_userChatFragment);
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
}
