package creator.android.clipboard.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import java.util.List;
import creator.android.clipboard.R;
import creator.android.clipboard.adapters.AccountAdapter;
import creator.android.clipboard.repositories.AccountRepository;
import creator.android.clipboard.databinding.ActivityMainBinding;
import creator.android.clipboard.models.Account;

public class MainActivity extends AppCompatActivity {
    AccountRepository accountRepository;
    AccountAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountRepository = new AccountRepository(this);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        binding.fab.setOnClickListener(view -> {
            openAddAccount();
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);

        List<Account> items = accountRepository.getItems();
        adapter = new AccountAdapter(this, items);
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
        List<Account> filterItems = accountRepository.findByName(text);
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
            //openAddAccount();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshAccount() {
        adapter.updateData(accountRepository.getItems());
        adapter.notifyDataSetChanged();
    }

    public void openAddAccount() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_account, null);
        TextInputEditText editTextName = dialogView.findViewById(R.id.editTextName);
        new MaterialAlertDialogBuilder(this)
                .setTitle("Add account")
                .setView(dialogView)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = editTextName.getText().toString();
                        if (name != null && !name.isEmpty()) {
                            accountRepository.addAccount(name);
                            adapter.updateData(accountRepository.getItems());
                            adapter.notifyDataSetChanged();

                            Toast.makeText(MainActivity.this, "Contact added: " + name, Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(MainActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}