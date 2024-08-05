package creator.android.clipboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import creator.android.clipboard.data.AccountDataSource;
import creator.android.clipboard.databinding.ActivityMainBinding;
import creator.android.clipboard.placeholder.ListItem;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private AccountDataSource accountDataSource;
    ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountDataSource = new AccountDataSource(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);

        List<ListItem> items = accountDataSource.getItems();
        adapter = new ListAdapter(this, items);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // create search view
        MenuItem searchItem = menu.findItem(R.id.action_search_account);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search account");


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchAccountByName(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchAccountByName(newText);
                return false;
            }
        });

        return true;
    }

    private void searchAccountByName(String text) {
        List<ListItem> filterItems = accountDataSource.findByName(text);
        adapter.updateData(filterItems);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        /*if (id == R.id.action_settings) {
            return true;
        }*/

        if( id == R.id.action_information_refresh) {
            refreshAccount();
            return true;
        }

        if (id == R.id.action_add_account) {
            openAddAccount();
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshAccount() {
        adapter.updateData(accountDataSource.getItems());
        adapter.notifyDataSetChanged();
    }

    public void openAddAccount() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View dialogView = layoutInflater.inflate(R.layout.dialog_add_account, null);

        // Add Account Dialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = dialogView.findViewById(R.id.editTextName);

        dialogBuilder.setTitle("Add Account");
        dialogBuilder.setPositiveButton("Add", (dialog, which) -> {
            String name = editTextName.getText().toString().trim();
            if (!name.isEmpty()) {
                accountDataSource.addAccount(name);
                adapter.updateData(accountDataSource.getItems());
                adapter.notifyDataSetChanged();

                Toast.makeText(MainActivity.this, "Contact added: " + name, Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(MainActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            }
        });

        dialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Show the dialog
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }
}