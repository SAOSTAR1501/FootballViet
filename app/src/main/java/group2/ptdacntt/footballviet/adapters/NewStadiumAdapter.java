package group2.ptdacntt.footballviet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import group2.ptdacntt.footballviet.Interfaces.IClick;
import group2.ptdacntt.footballviet.Interfaces.ImageClickListener;
import group2.ptdacntt.footballviet.Models.Stadium;
import group2.ptdacntt.footballviet.R;

public class NewStadiumAdapter extends RecyclerView.Adapter<NewStadiumAdapter.ViewHolder> {
    List<Stadium> list;
    Context context;
    IClick iClick;

    public void setList(List<Stadium> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public NewStadiumAdapter(List<Stadium> list, Context context, IClick iClick) {
        this.list = list;
        this.context = context;
        this.iClick = iClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_stadium_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Stadium stadium = list.get(position);
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(stadium.getOwner());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    holder.txtUsername.setText(snapshot.child("fullName").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.txtUsername.setText(stadium.getOwner());
        holder.txtTenSan.setText(stadium.getStadiumName());
        holder.txtDiaChi.setText(stadium.getAddress());
        holder.txtGia.setText(stadium.getPrice() + "đ");
        Glide.with(context).load(stadium.getImage()).into(holder.imgAnhSan);
        holder.btnBookStadium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClick.onClickOrderStadium(stadium.getStadiumId());
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtUsername, txtTenSan, txtDiaChi, txtGia;
        ImageView imgAnhSan;
        Button btnBookStadium;
        ImageClickListener imageClickListener;

        public ViewHolder (@NonNull View itemView) {
            super(itemView);
            txtUsername = itemView.findViewById(R.id.txtUserName);
            txtTenSan = itemView.findViewById(R.id.txtTenSan);
            txtDiaChi = itemView.findViewById(R.id.txtDiaChi);
            txtGia = itemView.findViewById(R.id.txtGia);
            imgAnhSan = itemView.findViewById(R.id.imgAnhSan);
            btnBookStadium = itemView.findViewById(R.id.btnDatSan);

        }


    }
}
