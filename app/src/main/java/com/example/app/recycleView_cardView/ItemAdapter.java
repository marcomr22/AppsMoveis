package com.example.app.recycleView_cardView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app.R;

import java.util.ArrayList;

import com.example.app.models.Advert;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<Advert> mList;
    private Class nextActivityClass;

    public ItemAdapter(Context context, ArrayList<Advert> list, Class nextActivityClass){
        mContext=context;
        mList=list;
        this.nextActivityClass=nextActivityClass;
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_services_items,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder holder, int position) {

        final Advert item = mList.get(position);
        final ArrayList<String> URLs = (ArrayList<String>) item.getImagesURL();

        if(!item.getImagesURL().isEmpty()){
            Glide.with(mContext).asBitmap().load(item.getImagesURL().get(0)).into(holder.item_image);
        }

        holder.item_description.setText(item.getDescription());

        holder.item_price.setText(String.valueOf(item.getPrice()));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, nextActivityClass);
                intent.putExtra("Advert", item);
                intent.putStringArrayListExtra("URLs", URLs);
                mContext.startActivity(intent);

            }
        });

    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView item_image;
        TextView item_description,item_price;
        LinearLayout parentLayout;
        public ViewHolder (View itemView) {
            super(itemView);

            item_image = itemView.findViewById(R.id.item_image);
            item_description = itemView.findViewById(R.id.item_description);
            item_price = itemView.findViewById(R.id.item_price);
            parentLayout = itemView.findViewById(R.id.parentLayout);
        }
    }
}
