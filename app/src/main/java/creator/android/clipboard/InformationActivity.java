package creator.android.clipboard;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import creator.android.clipboard.databinding.ActivityAccountDetailsBinding;
import creator.android.clipboard.databinding.InformationActivityBinding;

public class InformationActivity extends AppCompatActivity {

    //private AppBarConfiguration appBarConfiguration;
    private InformationActivityBinding binding;

    private RecyclerView accountsRecyclerView;
    private InformationAdapter myAdapter;  // Assuming MyAdapter is already defined for the RecyclerView
    private InformationDataSource informationDataSource; // Your SQLiteOpenHelper

    private int accountId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = InformationActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MaterialToolbar toolbar = findViewById(R.id.information_toolbar);

        setSupportActionBar(toolbar);

        binding.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.fab)
                .setAction("Action", null).show());

        informationDataSource = new InformationDataSource(this);

        // Get the intent that started this activity and extract the contact details
        Intent intent = getIntent();
        if(intent != null) {
            String name = intent.getStringExtra("name");
            this.accountId= Integer.valueOf(intent.getStringExtra("accountId"));

            toolbar.setTitle(name);
        }

        accountsRecyclerView = findViewById(R.id.information_list_view);

        // Initialize RecyclerView
        accountsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new InformationAdapter(this, informationDataSource.getInformations(accountId), null);
        accountsRecyclerView.setAdapter(myAdapter);

        // Handle Add Button Click
        binding.fab.setOnClickListener(v -> {
            openAddInformationDialog();
        });
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