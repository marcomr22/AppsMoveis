package recycleView_cardView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;

import java.util.ArrayList;

import static android.view.LayoutInflater.from;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<ItemModel> mList;

    public ItemAdapter(Context context, ArrayList<ItemModel> list){
        mContext=context;
        mList=list;
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = from(mContext);

        View view;
        view = layoutInflater.inflate(R.layout.rv_services_items,parent,false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder holder, int position) {

        ItemModel item = mList.get(position);
        ImageView image = holder.item_image;
        TextView description, price;

        description = holder.item_description;
        price = holder.item_price;

        image.setImageResource(mList.get(position).getImage());

        description.setText(item.getDescription());
        price.setText(item.getPrice());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView item_image;
        TextView item_name,item_description,item_price;
        public ViewHolder (View itemView) {
            super(itemView);

            item_image = itemView.findViewById(R.id.item_image);
            item_name = itemView.findViewById(R.id.item_description);
            item_price = itemView.findViewById(R.id.item_price);
        }
    }
}
