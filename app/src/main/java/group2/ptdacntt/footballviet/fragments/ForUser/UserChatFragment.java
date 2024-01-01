package group2.ptdacntt.footballviet.fragments.ForUser;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import group2.ptdacntt.footballviet.Models.User;
import group2.ptdacntt.footballviet.R;
import group2.ptdacntt.footballviet.adapters.UserChatAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserChatFragment extends Fragment implements UserChatAdapter.IClickChatUser {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    List<User> list;
    UserChatAdapter userChatAdapter;
    RecyclerView rcv;

    public UserChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserChatFragment newInstance(String param1, String param2) {
        UserChatFragment fragment = new UserChatFragment();
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
        return inflater.inflate(R.layout.fragment_user_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcv=view.findViewById(R.id.rcvChatUer);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        rcv.setLayoutManager(linearLayoutManager);
        list=new ArrayList<>();
        userChatAdapter=new UserChatAdapter(list,getContext(),UserChatFragment.this);
        rcv.setAdapter(userChatAdapter);
        loadData();
    }
    private void loadData() {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    User user=dataSnapshot.getValue(User.class);
                    if(user.getEmail().equals("admin@gmail.com")){
                        continue;
                    }
                    list.add(user);
                    userChatAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(String userId) {
        Bundle bundle=new Bundle();
        bundle.putString("user_id",userId);
        NavController navController= NavHostFragment.findNavController(UserChatFragment.this);
        navController.navigate(R.id.action_userChatFragment_to_chatActivity,bundle);
    }
}