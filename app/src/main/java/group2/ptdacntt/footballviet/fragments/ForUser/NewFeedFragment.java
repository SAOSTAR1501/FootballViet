package group2.ptdacntt.footballviet.fragments.ForUser;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import group2.ptdacntt.footballviet.Models.NewFeed;
import group2.ptdacntt.footballviet.Models.Stadium;
import group2.ptdacntt.footballviet.R;
import group2.ptdacntt.footballviet.adapters.NewFeedAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewFeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewFeedFragment extends Fragment implements NewFeedAdapter.ClickMess, SearchView.OnQueryTextListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final int PICK_IMAGE_REQUEST = 1;
    RecyclerView rcv;
    Button btnPost;
    NavController navController;
    List<NewFeed> list;
    NewFeedAdapter adapter;
    FirebaseAuth auth;
    FirebaseUser user;

    SearchView searchView;
    ImageView imgBtnMes;

    public NewFeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewFeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewFeedFragment newInstance(String param1, String param2) {
        NewFeedFragment fragment = new NewFeedFragment();
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
        return inflater.inflate(R.layout.fragment_new_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcv=view.findViewById(R.id.rcvNewFeed);
        btnPost=view.findViewById(R.id.btnPost);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        navController= NavHostFragment.findNavController(NewFeedFragment.this);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_newFeedFragment_to_postFragment);
            }
        });
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        rcv.setLayoutManager(linearLayoutManager);

        FirebaseDatabase.getInstance().getReference("posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list=new ArrayList<>();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    NewFeed newFeed=dataSnapshot.getValue(NewFeed.class);
                    list.add(newFeed);
                }
                Collections.reverse(list);
                adapter=new NewFeedAdapter(list,getContext(),NewFeedFragment.this, user.getUid());
                rcv.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        searchView = view.findViewById(R.id.svStadiums);
        searchView.setOnQueryTextListener(this);

        imgBtnMes = view.findViewById(R.id.imgBtnMes);
        imgBtnMes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_newFeedFragment_to_userChatFragment);
            }
        });
    }

    @Override
    public void onClick(NewFeed newFeed) {
        Bundle bundle=new Bundle();
        bundle.putString("user_id", newFeed.getEmail());
        navController.navigate(R.id.action_newFeedFragment_to_chatActivity,bundle);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        List<NewFeed> filteredNewfeeds = filterNewfeeds(list, newText);
        adapter.setList(filteredNewfeeds);
        return true;
    }

    private List<NewFeed> filterNewfeeds(List<NewFeed> newFeeds, String query) {
        query = query.toLowerCase(Locale.getDefault());
        List<NewFeed> filteredList = new ArrayList<>();
        for (NewFeed newFeed : newFeeds) {
            // Thực hiện kiểm tra theo tên sân hoặc tên đội
            if (newFeed.getSan().toLowerCase(Locale.getDefault()).contains(query) ||
                    newFeed.getContent().toLowerCase(Locale.getDefault()).contains(query) ||
                    newFeed.getName().toLowerCase(Locale.getDefault()).contains(query)) {
                filteredList.add(newFeed);
            }
        }
        return filteredList;
    }
}