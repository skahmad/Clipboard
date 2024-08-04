package creator.android.clipboard;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import creator.android.clipboard.databinding.InformationActivityBinding;
import creator.android.clipboard.placeholder.Information;

public class InformationActivity extends AppCompatActivity {
    private InformationActivityBinding binding;

    private RecyclerView accountsRecyclerView;
    private InformationAdapter myAdapter;  // Assuming MyAdapter is already defined for the RecyclerView
    private InformationDataSource informationDataSource; // Your SQLiteOpenHelper

    private int accountId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        informationDataSource = new InformationDataSource(this);

        binding = InformationActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MaterialToolbar toolbar = findViewById(R.id.information_toolbar);

        setSupportActionBar(toolbar);

        binding.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.fab)
                .setAction("Action", null).show());

        // Get the intent that started this activity and extract the contact details
        Intent intent = getIntent();
        if(intent != null) {
            String name = intent.getStringExtra("name");
            this.accountId= Integer.valueOf(intent.getStringExtra("accountId"));

            toolbar.setTitle(name);
        }

        accountsRecyclerView = findViewById(R.id.information_list_view);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(accountsRecyclerView.getContext(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        accountsRecyclerView.addItemDecoration(dividerItemDecoration);

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
        Toast.makeText(this, "Information updated", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(InformationActivity.this, "account not found: " + name, Toast.LENGTH_SHORT).show();

            if (!name.isEmpty() && !details.isEmpty()) {
                informationDataSource.addInformation(accountId, name, details);
                myAdapter.updateData(informationDataSource.getInformations(accountId));
                myAdapter.notifyDataSetChanged();

                Toast.makeText(InformationActivity.this, "Contact added: " + name, Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(InformationActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            }
        });

        dialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Show the dialog
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }
}