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

import java.util.List;

import group2.ptdacntt.footballviet.Models.Stadium;
import group2.ptdacntt.footballviet.R;

public class NewStadiumAdapter extends RecyclerView.Adapter<NewStadiumAdapter.ViewHolder> {
    List<Stadium> list;
    Context context;

    public NewStadiumAdapter(List<Stadium> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_stadium_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Stadium stadium = list.get(position);
        holder.txtUsername.setText(user.getEmail());
        holder.txtTenSan.setText(stadium.getStadiumName());
        holder.txtDiaChi.setText(stadium.getAddress());
        holder.txtGia.setText(stadium.getPrice());
        Glide.with(context).load(stadium.getImage()).into(holder.imgAnhSan);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtUsername, txtTenSan, txtDiaChi, txtGia;
        ImageView imgAnhSan;

        public ViewHolder (@NonNull View itemView) {
            super(itemView);
            txtUsername = itemView.findViewById(R.id.txtUserName);
            txtTenSan = itemView.findViewById(R.id.txtTenSan);
            txtDiaChi = itemView.findViewById(R.id.txtDiaChi);
            txtGia = itemView.findViewById(R.id.txtGia);
            imgAnhSan = itemView.findViewById(R.id.imgAnhSan);
        }

    }
}
