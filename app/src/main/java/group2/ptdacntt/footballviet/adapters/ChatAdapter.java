package group2.ptdacntt.footballviet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import group2.ptdacntt.footballviet.Models.Chat;
import group2.ptdacntt.footballviet.R;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;
    List<Chat> list;
    Context context;
    FirebaseUser fuser;
    String imgUrl;

    public ChatAdapter(List<Chat> list, Context context,String imgUrl) {
        this.list = list;
        this.context = context;
        this.imgUrl=imgUrl;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==MSG_TYPE_RIGHT) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_right,parent,false);
            return new ChatViewHolder(view);
        }else{
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_left,parent,false);
            return new ChatViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat=list.get(position);
        holder.tvMessage.setText(chat.getMessage());
        if(imgUrl.equals("default")){
            holder.imgProfile.setImageResource(R.drawable.user);
        }else{
            Glide.with(context).load(imgUrl).into(holder.imgProfile);
        }
    }

    @Override
    public int getItemCount() {
        if(list==null){
            return 0;
        }
        return list.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder{
        TextView tvMessage;
        ImageView imgProfile;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage=itemView.findViewById(R.id.tvChatLeft);
            imgProfile=itemView.findViewById(R.id.imgProfile);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        if(list.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
}
