package creator.android.clipboard.repositories;

import android.content.Context;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;
import creator.android.clipboard.data.AccountDataSource;
import creator.android.clipboard.models.Account;

public class AccountRepository {
    AccountDataSource accountDataSource;

    public AccountRepository(Context context) {
        accountDataSource = new AccountDataSource(context);
        accountDataSource.open();
    }

    public Account findById(int id) {
        throw new RuntimeException("not implemented");
    }
    public List<Account> findAllByName(String name) {
        throw new RuntimeException("not implemented");
    }
    public void add(Account account) {
        throw new RuntimeException("not implemented");
    }
    public void deleteById(int id) {
        throw new RuntimeException("not implemented");
    }
    public void updateAccount(int id, Account account) {
        throw new RuntimeException("not implemented");
    }

    public void addAccount(String name) {
        accountDataSource.addAccount(name);
    }


    public List<Account> getItems() {
        List<Account> items = new ArrayList<>();
        Cursor cursor = accountDataSource.getAllAccount();
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int count = cursor.getInt(cursor.getColumnIndex("count"));
            String createdAt = cursor.getString(cursor.getColumnIndex("createdAt"));
            String updatedAt = cursor.getString(cursor.getColumnIndex("updatedAt"));

            Account item = new Account(id, name);
            item.setCount(count)
                .setUpdatedAt(updatedAt)
                .setCreatedAt(createdAt);

            items.add(item);
        }
        cursor.close();
        return items;
    }

    public void deleteAccount(long id) {
        accountDataSource.deleteAccount(id);
    }

    public void updateAccount(long id, Account account) {
        accountDataSource.updateAccount(id, "", 0);
    }

    public List<Account> findByName(String text) {
        Cursor cursor = accountDataSource.getAccountByNameContains(text);
        List<Account> items = new ArrayList<>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int count = cursor.getInt(cursor.getColumnIndex("count"));
            String createdAt = cursor.getString(cursor.getColumnIndex("createdAt"));
            String updatedAt = cursor.getString(cursor.getColumnIndex("updatedAt"));

            Account item = new Account(id, name);
            item.setCount(count)
                    .setUpdatedAt(updatedAt)
                    .setCreatedAt(createdAt);

            items.add(item);
        }
        cursor.close();
        return items;
    }
}
