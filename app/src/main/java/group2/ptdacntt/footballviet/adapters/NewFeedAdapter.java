package group2.ptdacntt.footballviet.adapters;

import android.annotation.SuppressLint;
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
import group2.ptdacntt.footballviet.Models.NewFeed;
import group2.ptdacntt.footballviet.R;

public class NewFeedAdapter extends RecyclerView.Adapter<NewFeedAdapter.ViewHolder> {
    List<NewFeed> list;
    Context context;
    ClickMess clickMess;
    String email;

    public NewFeedAdapter(List<NewFeed> list, Context context, ClickMess clickMess, String email) {
        this.list = list;
        this.context = context;
        this.clickMess = clickMess;
        this.email = email;
    }

    public void setList(List<NewFeed> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_newfeed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        NewFeed newFeed = list.get(position);
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(newFeed.getEmail());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    holder.tvUsername.setText(snapshot.child("fullName").getValue(String.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.tvTenDoi.setText(newFeed.getName());
        holder.tvSan.setText(newFeed.getSan());
        holder.tvNgay.setText(newFeed.getNgay());
        holder.tvGio.setText(newFeed.getGio());
        holder.tvNoiDung.setText(newFeed.getContent());
        Glide.with(context).load(newFeed.getImage()).into(holder.img);

        if(this.email.equals(newFeed.getEmail())) {
            holder.btn.setVisibility(View.GONE);
        } else {
            holder.btn.setVisibility(View.VISIBLE);
            holder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickMess.onClick(list.get(position));
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvTenDoi, tvSan, tvNgay, tvGio, tvNoiDung;
        ImageView img;
        Button btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUserName);
            tvTenDoi = itemView.findViewById(R.id.tvTenDoi);
            tvSan = itemView.findViewById(R.id.tvSan);
            tvNgay = itemView.findViewById(R.id.tvNgay);
            tvGio = itemView.findViewById(R.id.tvGio);
            tvNoiDung = itemView.findViewById(R.id.tvNoiDung);
            img = itemView.findViewById(R.id.imgAnhDoi);
            btn = itemView.findViewById(R.id.btnNhanTin);
        }
    }
    public interface ClickMess {
        void onClick(NewFeed newFeed);
    }
}
