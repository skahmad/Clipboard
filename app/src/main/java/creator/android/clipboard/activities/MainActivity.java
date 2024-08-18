package creator.android.clipboard.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import java.util.List;
import java.util.concurrent.Executor;
import creator.android.clipboard.R;
import creator.android.clipboard.adapters.AccountAdapter;
import creator.android.clipboard.repositories.AccountRepository;
import creator.android.clipboard.databinding.ActivityMainBinding;
import creator.android.clipboard.models.Account;
import creator.android.clipboard.repositories.InformationRepository;

public class MainActivity extends AppCompatActivity {
    AccountRepository accountRepository;
    InformationRepository informationRepository;
    AccountAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountRepository = new AccountRepository(this);
        informationRepository = new InformationRepository(this);

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
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = editTextName.getText().toString();
                    if (name != null && !name.isEmpty()) {
                        accountRepository.addAccount(name);
                        adapter.updateData(accountRepository.getItems());
                        adapter.notifyDataSetChanged();

                        Toast.makeText(MainActivity.this, "Contact added: " + name, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(MainActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    public void openEditDialog(int position) {
        Account account = accountRepository.findById(position);

        String name = account.getName();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit " + account.getName());

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_edit_account, (ViewGroup) findViewById(android.R.id.content), false);
        final EditText nameInput = viewInflated.findViewById(R.id.account_name);

        nameInput.setText(name);

        builder.setView(viewInflated);

        builder.setPositiveButton("Update", (dialog, id) -> {
            String newName = nameInput.getText().toString();
            account.setName(newName);
            accountRepository.updateAccount(Integer.parseInt(account.getId()), account);
            dialog.dismiss();
            adapter.updateData(accountRepository.getItems());
            adapter.notifyDataSetChanged();
        });

        builder.setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void deleteAccount(Integer id) {
        accountRepository.deleteById(id);

        informationRepository.deleteByAccountId(id);
        adapter.updateData(accountRepository.getItems());
        adapter.notifyDataSetChanged();
    }
}