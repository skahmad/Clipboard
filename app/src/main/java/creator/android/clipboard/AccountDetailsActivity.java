package creator.android.clipboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AccountDetailsActivity extends AppCompatActivity {
    private RecyclerView accountsRecyclerView;
    private InformationAdapter myAdapter;  // Assuming MyAdapter is already defined for the RecyclerView
    private InformationDataSource informationDataSource; // Your SQLiteOpenHelper

    private Integer accountId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);
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
        }

        Button addButton = findViewById(R.id.addButton);
        accountsRecyclerView = findViewById(R.id.accountsRecyclerView);

        // Initialize RecyclerView
        accountsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new InformationAdapter(this, informationDataSource.getInformations(accountId));
        accountsRecyclerView.setAdapter(myAdapter);

        // Handle Add Button Click
        addButton.setOnClickListener(v -> {
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
