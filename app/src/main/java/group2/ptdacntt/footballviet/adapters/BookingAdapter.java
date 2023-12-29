package group2.ptdacntt.footballviet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import group2.ptdacntt.footballviet.Models.BookingStadium;
import group2.ptdacntt.footballviet.R;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {
    List<BookingStadium> list;
    Context context;

    public BookingAdapter(List<BookingStadium> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public BookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_history_booking_stadium_item, parent, false);
        return null;
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
                                String userFullName = userSnapshot.getValue(String.class);
                                String stadiumName = stadiumSnapshot.getValue(String.class);
                                holder.txtHeader.setText(userFullName + " đã đặt " + stadiumName);
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
        holder.txtKhungGio.setText(bookingStadium.getTimeSlot());
        holder.btnCancelBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.btnReBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
