package creator.android.clipboard.adapters;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import creator.android.clipboard.activities.InformationActivity;
import creator.android.clipboard.repositories.InformationRepository;
import creator.android.clipboard.R;
import creator.android.clipboard.models.Information;

public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.ViewHolder> {
    private List<Information> items;
    private final Context context;
    InformationActivity informationActivity;
    private InformationRepository informationRepository;

    public InformationAdapter(Context context, List<Information> items, InformationActivity activity) {
        this.context = context;
        this.items = items;
        informationActivity = activity;
    }

    @NonNull
    @Override
    public InformationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.list_item_two_line, parent, false);
        return new InformationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InformationAdapter.ViewHolder holder, int position) {
        Information item = items.get(position);
        holder.name.setText(item.getName());
        holder.details.setText(item.getDetails());

        holder.itemView.setTag(position);

        holder.itemView.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Account Details", item.getDetails());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();
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

    public void updateData(List<Information> newItemList) {
        this.items = newItemList;
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
                        informationActivity.openViewDialog(information.getId());
                        return true;
                    });

            menu
                    .findItem(R.id.information_edit_option)
                    .setOnMenuItemClickListener(item -> {
                        informationActivity.openEditDialog(information.getId());
                        return true;
                    });

            menu
                    .findItem(R.id.information_delete_option)
                    .setOnMenuItemClickListener(item->{
                        informationActivity.deleteInformation(information.getId());
                       return true;
                    });
        }
    }
}