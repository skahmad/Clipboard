package creator.android.clipboard;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import creator.android.clipboard.placeholder.Information;

public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.ViewHolder> {
    private List<Information> items;
    private Context context;

    public InformationAdapter(Context context, List<Information> items) {
        this.context = context;
        this.items = items;
    }


    public void updateData(List<Information> newItemList) {
        this.items = newItemList;
    }


    @NonNull
    @Override
    public InformationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_two_line, parent, false);
        return new InformationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InformationAdapter.ViewHolder holder, int position) {
        Information item = items.get(position);
        holder.name.setText(item.getName());
        holder.details.setText(item.getDetails());

        holder.itemView.setTag(position);

        // Set a click listener on the entire item view
        holder.itemView.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Account Details", item.getDetails());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();
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
        TextView name;
        TextView details;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text1);
            details = itemView.findViewById(R.id.text2);
        }
    }
}