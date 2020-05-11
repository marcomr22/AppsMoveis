package CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.app.R;

public class CustomAdapter extends ArrayAdapter<String> {

    Context context;
    String[] CategoryName;
    int[] images;


    public CustomAdapter(Context context, String[] names, int[] images) {
        super(context,R.layout.spinner_item,names);
        this.context = context;
        this.CategoryName = names;
        this.images = images;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.spinner_item, null);
        TextView text1 = (TextView) row.findViewById(R.id.textView11);
        ImageView imageView1 = (ImageView) row.findViewById(R.id.imageView);

        text1.setText(CategoryName[position]);
        imageView1.setImageResource(images[position]);

        return row;
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.spinner_item, null);
        TextView text1 = (TextView) row.findViewById(R.id.textView11);
        ImageView imageView1 = (ImageView) row.findViewById(R.id.imageView);

        text1.setText(CategoryName[position]);
        imageView1.setImageResource(images[position]);

        return row;

    }
}
