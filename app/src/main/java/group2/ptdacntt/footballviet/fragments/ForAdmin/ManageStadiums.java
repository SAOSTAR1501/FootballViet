package group2.ptdacntt.footballviet.fragments.ForAdmin;

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
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import group2.ptdacntt.footballviet.Models.Stadium;
import group2.ptdacntt.footballviet.R;
import group2.ptdacntt.footballviet.adapters.NewStadiumAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageStadiums#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageStadiums extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    NavController navController;

    RecyclerView rcv;
    Button btnPost;
    List<Stadium> list;
    NewStadiumAdapter adapter;
    FirebaseAuth auth;
    FirebaseUser user;

    public ManageStadiums() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageStadiums.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageStadiums newInstance(String param1, String param2) {
        ManageStadiums fragment = new ManageStadiums();
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
        return inflater.inflate(R.layout.fragment_manage_stadiums, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController= NavHostFragment.findNavController(ManageStadiums.this);
        rcv = view.findViewById(R.id.rcvNewStadium);
        btnPost = view.findViewById(R.id.btnAddStadium);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_manageStadiums3_to_newStadiumFragment2);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcv.setLayoutManager(linearLayoutManager);
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot usersSnapshot) {
                list = new ArrayList<>();
                for(DataSnapshot userSnapshot: usersSnapshot.getChildren()) {
                    DatabaseReference stadiumsRef = FirebaseDatabase.getInstance().getReference("stadiums");
                    stadiumsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot stadiumsSnapshot) {
                            for (DataSnapshot stadiumSnapshot : stadiumsSnapshot.getChildren()) {
                                Stadium stadium = stadiumSnapshot.getValue(Stadium.class);
                                list.add(stadium);
                            }
                            adapter = new NewStadiumAdapter(list, getContext());
                            rcv.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}