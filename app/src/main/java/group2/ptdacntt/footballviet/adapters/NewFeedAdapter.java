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

import group2.ptdacntt.footballviet.Models.NewFeed;
import group2.ptdacntt.footballviet.R;

public class NewFeedAdapter extends RecyclerView.Adapter<NewFeedAdapter.ViewHolder> {
    List<NewFeed> list;
    Context context;

    public NewFeedAdapter(List<NewFeed> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_newfeed,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        NewFeed newFeed=list.get(position);
        holder.tvUsername.setText(user.getEmail());
        holder.tvTenDoi.setText(newFeed.getName());
        holder.tvSan.setText(newFeed.getSan());
        holder.tvNgay.setText(newFeed.getNgay());
        holder.tvGio.setText(newFeed.getGio());
        holder.tvNoiDung.setText(newFeed.getContent());
        Glide.with(context).load(newFeed.getImage()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        if(list==null){
            return 0;
        }
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvUsername,tvTenDoi,tvSan,tvNgay,tvGio,tvNoiDung;
        ImageView img;
        Button btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername=itemView.findViewById(R.id.tvUserName);
            tvTenDoi=itemView.findViewById(R.id.tvTenDoi);
            tvSan=itemView.findViewById(R.id.tvSan);
            tvNgay=itemView.findViewById(R.id.tvNgay);
            tvGio=itemView.findViewById(R.id.tvGio);
            tvNoiDung=itemView.findViewById(R.id.tvNoiDung);
            img=itemView.findViewById(R.id.imgAnhDoi);
            btn=itemView.findViewById(R.id.btnNhanTin);
        }
    }
}
