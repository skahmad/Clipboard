package creator.android.clipboard;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import creator.android.clipboard.R;
import creator.android.clipboard.databinding.ActivityAccountDetailsBinding;
import creator.android.clipboard.databinding.ActivityMainBinding;
import creator.android.clipboard.placeholder.Information;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AccountDetailsActivity extends AppCompatActivity {
    private RecyclerView accountsRecyclerView;
    private InformationAdapter myAdapter;  // Assuming MyAdapter is already defined for the RecyclerView
    private InformationDataSource informationDataSource; // Your SQLiteOpenHelper
    private ActivityAccountDetailsBinding binding;

    private Integer accountId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        binding = ActivityAccountDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        informationDataSource = new InformationDataSource(this);

        TextView textViewName = findViewById(R.id.textViewName);
        TextView textViewDescription = findViewById(R.id.descriptionTextView);

        // Get the intent that started this activity and extract the contact details
        Intent intent = getIntent();
        if(intent != null) {
            String name = intent.getStringExtra("name");
            String count = intent.getStringExtra("count");
            this.accountId= Integer.valueOf(intent.getStringExtra("accountId"));

            // Set the contact details to the views
            textViewName.setText(name);
            textViewDescription.setText(count);

            binding.toolbar.setTitle(name);
        }

        //Button addButton = findViewById(R.id.addButton);
        accountsRecyclerView = findViewById(R.id.accountsRecyclerView);

        // register for context menu
        //registerForContextMenu(accountsRecyclerView);

        // Initialize RecyclerView
        accountsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new InformationAdapter(this, informationDataSource.getInformations(accountId), this);
        accountsRecyclerView.setAdapter(myAdapter);

        // Handle Add Button Click
        binding.fab.setOnClickListener(v -> {
            openAddInformationDialog();
        });
    }


    public void deleteInformation(int id) {
        informationDataSource.delete(id);
    }

    private void saveEditedData(int id, String key, String value) {
        informationDataSource.update(id, key, value);
        Toast.makeText(AccountDetailsActivity.this, "Information updated", Toast.LENGTH_SHORT).show();
    }
    public void openEditDialog(int position) {
        // Get the item data based on position
        Information information = informationDataSource.getInformation(position);

        String title = information.getName();
        String key = information.getName();
        String value = information.getDetails();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit " + title);

        // Inflate the custom layout with key and value fields
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.information_edit_dialog, (ViewGroup) findViewById(android.R.id.content), false);
        final EditText inputKey = viewInflated.findViewById(R.id.input_key);
        final EditText inputValue = viewInflated.findViewById(R.id.input_value);

        inputKey.setText(key);
        inputValue.setText(value);

        builder.setView(viewInflated);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Save the data
                String newKey = inputKey.getText().toString();
                String newValue = inputValue.getText().toString();
                saveEditedData(position, newKey, newValue);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void openViewDialog(int position) {
        Information information = informationDataSource.getInformation(position);

        // Get the item data based on position
        String title = information.getName();
        String description = information.getDetails();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(description);

        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Copy", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Copy to clipboard
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Description", description);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void openAddInformationDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View dialogView = layoutInflater.inflate(R.layout.dialog_add_information, null);

        // Add Account Dialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = dialogView.findViewById(R.id.editTextName);
        final EditText editTextDetails = dialogView.findViewById(R.id.editTextDetails);

        dialogBuilder.setTitle("Add Information");
        dialogBuilder.setPositiveButton("Add", (dialog, which) -> {
            String name = editTextName.getText().toString().trim();
            String details = editTextDetails.getText().toString().trim();

            if(accountId == -1)
                Toast.makeText(AccountDetailsActivity.this, "account not found: " + name, Toast.LENGTH_SHORT).show();

            if (!name.isEmpty() && !details.isEmpty()) {
                informationDataSource.addInformation(accountId, name, details);
                myAdapter.updateData(informationDataSource.getInformations(accountId));
                myAdapter.notifyDataSetChanged();

                Toast.makeText(AccountDetailsActivity.this, "Contact added: " + name, Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(AccountDetailsActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            }
        });

        dialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Show the dialog
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }
}
