package creator.android.clipboard;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import creator.android.clipboard.placeholder.ListItem;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<ListItem> items;
    private Context context;

    // Constructor
    // Constructor
    public ListAdapter(Context context, List<ListItem> items) {
        this.context = context;
        this.items = items;
    }


    public void updateData(List<ListItem> newItemList) {
        this.items = newItemList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_two_line, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListItem item = items.get(position);
        holder.text1.setText(item.getName());
        holder.text2.setText(item.getCount()==0? "No account added": item.getCount() + " accounts");

        holder.itemView.setTag(position);

        // Set a click listener on the entire item view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to start the ContactDetailsActivity
                Intent intent = new Intent(context, AccountDetailsActivity.class);
                intent.putExtra("name", item.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // Interface for handling clicks
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text1;
        TextView text2;

        public ViewHolder(View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.text1);
            text2 = itemView.findViewById(R.id.text2);
        }
    }
}
