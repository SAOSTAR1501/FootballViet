package group2.ptdacntt.footballviet.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import group2.ptdacntt.footballviet.Interfaces.IClick;
import group2.ptdacntt.footballviet.Models.BookingStadium;
import group2.ptdacntt.footballviet.R;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {
    List<BookingStadium> list;
    Context context;
    IClick iClick;

    public BookingAdapter(List<BookingStadium> list, Context context, IClick iClick) {
        this.list = list;
        this.context = context;
        this.iClick = iClick;
    }

    @NonNull
    @Override
    public BookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_history_booking_stadium_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull BookingAdapter.ViewHolder holder, int position) {
        BookingStadium bookingStadium = list.get(position);
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(bookingStadium.getUserId());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                if (userSnapshot.exists()) {
                    DatabaseReference stadiumRef = FirebaseDatabase.getInstance().getReference("stadiums").child(bookingStadium.getStadiumId()).child("stadiumName");
                    stadiumRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot stadiumSnapshot) {
                            if(stadiumSnapshot.exists()) {
                                String userFullName = userSnapshot.child("fullName").getValue(String.class);
                                String stadiumName = stadiumSnapshot.getValue(String.class);
                                String headerBooking = userFullName + " đã đặt " + stadiumName;
                                SpannableString spannableString = new SpannableString(headerBooking);
                                StyleSpan boldSpan1 = new StyleSpan(Typeface.BOLD);
                                spannableString.setSpan(boldSpan1, 0, userFullName.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                                StyleSpan boldSpan2 = new StyleSpan(Typeface.BOLD);
                                spannableString.setSpan(boldSpan2, userFullName.length() + 7, headerBooking.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                                holder.txtHeader.setText(spannableString);
                            }
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
        holder.txtTime.setText(bookingStadium.getDateBooking());
        holder.txtKhungGio.setText(bookingStadium.getTimeSlot()+"h");
        FirebaseDatabase.getInstance().getReference("bookings").child(bookingStadium.getStadiumId()).child(bookingStadium.getDateBooking()).child(bookingStadium.getBookingId()).child("active").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot bookingSnapshot) {
                if (bookingSnapshot.exists()) {
                    String active = bookingSnapshot.getValue(String.class);
                    if (active.equals("false")) {
                        holder.btnCancelBook.setEnabled(false);
                        holder.btnReBook.setEnabled(true);
                    } else {
                        holder.btnReBook.setEnabled(false);
                        holder.btnCancelBook.setEnabled(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.btnCancelBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Hủy đặt sân này?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase.getInstance().getReference("bookings").child(bookingStadium.getStadiumId()).child(bookingStadium.getDateBooking()).child(bookingStadium.getBookingId()).child("active").setValue("false");
                                FirebaseDatabase.getInstance().getReference("users").child(bookingStadium.getUserId()).child("bookings").child(bookingStadium.getBookingId()).child("active").setValue("false");
                                FirebaseDatabase.getInstance().getReference("stadiums").child(bookingStadium.getStadiumId()).child("owner").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot ownerSnapshot) {
                                        if(ownerSnapshot.exists()) {
                                            String owner = ownerSnapshot.getValue(String.class);
                                            FirebaseDatabase.getInstance().getReference("users").child(owner).child("stadiums").child(bookingStadium.getStadiumId()).child("bookings").child(bookingStadium.getDateBooking()).child(bookingStadium.getStadiumId()).child("active").setValue("false");
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                dialog.dismiss(); // Đóng dialog
                            }
                        })
                        .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Xử lý khi người dùng chọn "Không"
                                dialog.dismiss(); // Đóng dialog
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        holder.btnReBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClick.onClickOrderStadium(bookingStadium.getStadiumId());
            }
        });
    }

    @Override
    public int getItemCount() {
        if(list == null) {
            return 0;
        }
        return list.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView cimProfile;
        TextView txtHeader, txtKhungGio, txtTime;
        Button btnCancelBook, btnReBook;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cimProfile = itemView.findViewById(R.id.cimProfile);
            txtHeader = itemView.findViewById(R.id.txtHeader);
            txtKhungGio = itemView.findViewById(R.id.txtKhungGio);
            txtTime = itemView.findViewById(R.id.txtTime);
            btnCancelBook = itemView.findViewById(R.id.btnCancelBook);
            btnReBook = itemView.findViewById(R.id.btnReBook);
        }
    }
}
