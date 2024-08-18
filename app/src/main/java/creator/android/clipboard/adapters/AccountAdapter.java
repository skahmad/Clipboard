package creator.android.clipboard.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import creator.android.clipboard.activities.InformationActivity;
import creator.android.clipboard.R;
import creator.android.clipboard.activities.MainActivity;
import creator.android.clipboard.models.Account;
import creator.android.clipboard.models.Information;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {
    private List<Account> items;
    private Context context;
    private MainActivity activity;

    public AccountAdapter(Context context, List<Account> items) {
        this.context = context;
        this.items = items;
        activity = (MainActivity) context;
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
            Intent intent = new Intent(context, InformationActivity.class);
            intent.putExtra("name", item.getName());
            intent.putExtra("count", item.getCount());
            intent.putExtra("accountId", item.getId());
            context.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(v -> {
            v.setTag(item);
            v.showContextMenu();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView text1;
        TextView text2;

        public ViewHolder(View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.text1);
            text2 = itemView.findViewById(R.id.text2);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo contextMenuInfo) {
            MenuInflater inflater = ((Activity) context).getMenuInflater();
            inflater.inflate(R.menu.account_context_menu, menu);
            Account account = (Account) v.getTag();

            menu
                    .findItem(R.id.account_edit_option)
                    .setOnMenuItemClickListener(item -> {
                        activity.openEditDialog(Integer.parseInt(account.getId()));
                        return true;
                    });

            menu
                    .findItem(R.id.account_delete_option)
                    .setOnMenuItemClickListener(item->{
                        activity.deleteAccount(Integer.parseInt(account.getId()));
                        return true;
                    });
        }
    }
}
