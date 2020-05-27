package com.example.app.recycleView_cardView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends  RecyclerView.Adapter<CommentAdapter.ViewHolder> {




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image;
        TextView text;
        RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.profileImage);
            text = itemView.findViewById(R.id.Comment);
            relativeLayout = itemView.findViewById(R.id.parent_layout);

        }
    }

}
