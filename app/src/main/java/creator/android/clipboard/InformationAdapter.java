package creator.android.clipboard;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import creator.android.clipboard.placeholder.Information;

public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.ViewHolder> {
    private List<Information> items;
    private Context context;
    AccountDetailsActivity accountDetailsActivity;

    public InformationAdapter(Context context, List<Information> items, AccountDetailsActivity activity) {
        this.context = context;
        this.items = items;
        accountDetailsActivity = activity;
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

        // Set long click listener to show context menu
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                v.setTag(item);
                v.showContextMenu();  // Show the context menu on long press
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        TextView name;
        TextView details;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text1);
            details = itemView.findViewById(R.id.text2);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuInflater inflater = ((Activity) context).getMenuInflater();
            inflater.inflate(R.menu.information_context_menu, menu);
            Information information = (Information) v.getTag();
            menu
                    .findItem(R.id.information_view_option)
                    .setOnMenuItemClickListener(item -> {
                        accountDetailsActivity.openViewDialog(information.getId());
                        return true;
                    });

            menu
                    .findItem(R.id.information_edit_option)
                    .setOnMenuItemClickListener(item -> {
                        accountDetailsActivity.openEditDialog(information.getId());
                        return true;
                    });

            menu
                    .findItem(R.id.information_delete_option)
                    .setOnMenuItemClickListener(item->{
                        accountDetailsActivity.deleteInformation(information.getId());
                       return true;
                    });
        }
    }
}