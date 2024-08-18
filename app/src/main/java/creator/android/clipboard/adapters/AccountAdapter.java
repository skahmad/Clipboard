package creator.android.clipboard.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import creator.android.clipboard.activities.InformationActivity;
import creator.android.clipboard.R;
import creator.android.clipboard.models.Account;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {
    private List<Account> items;
    private Context context;

    public AccountAdapter(Context context, List<Account> items) {
        this.context = context;
        this.items = items;
    }


    public void updateData(List<Account> newItemList) {
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
        Account item = items.get(position);
        holder.text1.setText(item.getName());
        holder.text2.setText(item.getCount()==0? "No information found": item.getCount() + " information's");

        holder.itemView.setTag(position);

        // Set a click listener on the entire item view
        holder.itemView.setOnClickListener(v -> {
            // Create an intent to start the ContactDetailsActivity
            Intent intent = new Intent(context, InformationActivity.class);
            intent.putExtra("name", item.getName());
            intent.putExtra("count", item.getCount());
            intent.putExtra("accountId", item.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text1;
        TextView text2;

        public ViewHolder(View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.text1);
            text2 = itemView.findViewById(R.id.text2);
        }
    }
}
