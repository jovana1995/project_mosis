package com.example.aleksandra.backpack.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aleksandra.backpack.CommentModel;
import com.example.aleksandra.backpack.R;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolderPot> {
    private List<CommentModel> commentsList;

    public CommentsAdapter(List<CommentModel> potsList) {
        this.commentsList = potsList;
    }

    @NonNull
    @Override
    public ViewHolderPot onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return new ViewHolderPot(LayoutInflater.from(context).inflate(R.layout.row_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPot holder, int position) {

        holder.name.setText(commentsList.get(position).getName());
        holder.state.setText(commentsList.get(position).getState());
        holder.comment.setText(commentsList.get(position).getComment());
    }

    @Override
    public int getItemCount() {
        return commentsList != null ? commentsList.size() : 0;
    }

    class ViewHolderPot extends RecyclerView.ViewHolder {
        private TextView name, state, comment;
        private ImageView image; //todo ovde treba odgovarajuca slika da se ubaci

        private ViewHolderPot(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tw_comment_name);
            state = itemView.findViewById(R.id.tw_comment_state);
            comment = itemView.findViewById(R.id.tw_comment_comment);
            image = itemView.findViewById(R.id.iv_comment_image);
        }
    }
}
