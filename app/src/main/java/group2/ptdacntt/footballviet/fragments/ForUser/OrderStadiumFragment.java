package group2.ptdacntt.footballviet.fragments.ForUser;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import group2.ptdacntt.footballviet.Models.BookingStadium;
import group2.ptdacntt.footballviet.Models.Stadium;
import group2.ptdacntt.footballviet.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderStadiumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderStadiumFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageView imgAnhSan;
    TextView txtTenSan, txtDiaChi, txtGia;
    EditText edtNgayDat;
    Button btnXacNhanDatSan;
    ImageButton btnOpenDatePicker;
    String stadiumId;
    static String date;
    List<String> listTime;
    Spinner spGio;
    NavController nav;
    public OrderStadiumFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static OrderStadiumFragment newInstance(String param1, String param2) {
        OrderStadiumFragment fragment = new OrderStadiumFragment();
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
        return inflater.inflate(R.layout.fragment_order_stadium, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nav = NavHostFragment.findNavController(OrderStadiumFragment.this);
        imgAnhSan = view.findViewById(R.id.imgAnhSanOrder);
        txtTenSan = view.findViewById(R.id.txtTenSan);
        txtDiaChi = view.findViewById(R.id.txtDiaChi);
        txtGia = view.findViewById(R.id.txtGia);
        edtNgayDat = view.findViewById(R.id.edtNgayDat);
        btnOpenDatePicker = view.findViewById(R.id.btnOpenDatePicker);
        btnXacNhanDatSan = view.findViewById(R.id.btnXacNhanDatSan);
        spGio=view.findViewById(R.id.spGio);

        stadiumId = getArguments().getString("stadiumId");

        FirebaseAuth auth =FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        Log.d("TAG", "On booking: " +user.getEmail());
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        date = dayOfMonth + "-" + month + "-" + year;
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date currentTime = calendar.getTime();
        String time = timeFormat.format(currentTime);
        FirebaseDatabase.getInstance().getReference("stadiums").child(stadiumId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Stadium stadium = snapshot.getValue(Stadium.class);
                    Log.d("TAG", "onDataChange: "+stadium.getStadiumId());
                    txtTenSan.setText(stadium.getStadiumName());
                    txtDiaChi.setText(stadium.getAddress());
                    txtGia.setText(stadium.getPrice());
                    Glide.with(getContext()).load(stadium.getImage()).into(imgAnhSan);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnOpenDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a DatePickerDialog and show it
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                String selectedDate = formatDate(selectedYear, selectedMonth, selectedDay);
                                edtNgayDat.setText(selectedDate);
                                FirebaseDatabase.getInstance().getReference("stadiums").child(stadiumId).child("time").child(selectedDate).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                        listTime= Arrays.asList("7-9","9-11","13-15","15-17","17-19","19-21","21-23");
                                        listTime=new ArrayList<>();
                                        listTime.add("7-9");
                                        listTime.add("9-11");
                                        listTime.add("13-15");
                                        listTime.add("15-17");
                                        listTime.add("17-19");
                                        listTime.add("19-21");
                                        listTime.add("21-23");
                                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                            String chon=dataSnapshot.getValue(String.class);
                                            listTime.remove(chon);
                                            Log.d("TAG", "onDataChange: "+chon+"  "+listTime.get(2));
                                        }
                                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,listTime);
                                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spGio.setAdapter(adapter);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        },
                        year,
                        month,
                        dayOfMonth
                );
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
            private String formatDate(int year, int month, int day) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                return sdf.format(calendar.getTime());
            }
//            private boolean isValidDate(int year, int month, int day) {
//                Calendar selectedDate = Calendar.getInstance();
//                selectedDate.set(year, month, day);
//                // Get current date
//                Calendar currentDate = Calendar.getInstance();
//                // Compare the selected date with the current date
//                return !selectedDate.before(currentDate);
//            }
        });

        btnXacNhanDatSan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final ProgressDialog progressDialog = new ProgressDialog(getContext());
//                progressDialog.setTitle("Đang đặt sân...");
//                progressDialog.show();
                String selectedDate=edtNgayDat.getText().toString().trim();
                String gioChon=spGio.getSelectedItem().toString();
                BookingStadium bookingStadium = new BookingStadium(user.getUid() + date + time, stadiumId,user.getUid(), gioChon, selectedDate, "true");
                //Lưu lần đặt sân theo user đặt theo ngày
                FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("bookings").child(user.getUid() + date + time).setValue(bookingStadium);
                //Lưu lần đặt sân theo bookings 'out' theo sân sau đó theo ngày
                FirebaseDatabase.getInstance().getReference("bookings").child(stadiumId).child(selectedDate).child(user.getUid() + date + time).setValue(bookingStadium);
                //Lưu khung giờ đã chọn vào stadium đó theo ngày
                FirebaseDatabase.getInstance().getReference("stadiums").child(stadiumId).child("time").child(selectedDate).child(gioChon).setValue(gioChon);
                //Lưu lần đặt sân vào stadiums 'out' theo ngày
                FirebaseDatabase.getInstance().getReference("stadiums").child(stadiumId).child("bookings").child(selectedDate).setValue(bookingStadium);

                FirebaseDatabase.getInstance().getReference("stadiums").child(stadiumId).child("owner").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String owner = snapshot.getValue(String.class);
                            //Lưu thời gian đặt sân theo sân của chủ sân
                            FirebaseDatabase.getInstance().getReference("users").child(owner).child("stadiums").child(stadiumId).child("time").child(selectedDate).child(gioChon).setValue(gioChon);
                            //Lưu bookings vào sân của chủ sân
                            FirebaseDatabase.getInstance().getReference("users").child(owner).child("stadiums").child(stadiumId).child("bookings").child(selectedDate).child(user.getUid() + date + time).setValue(bookingStadium);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Toast.makeText(getContext(), "Đặt sân thành công! Xem chi tiết trong lịch sử đặt sân.", Toast.LENGTH_SHORT).show();
                nav.navigate(R.id.action_orderStadiumFragment_to_manageStadiums2);


            }
        });
    }
}