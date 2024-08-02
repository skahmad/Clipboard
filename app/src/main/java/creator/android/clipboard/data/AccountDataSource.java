package creator.android.clipboard.data;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import creator.android.clipboard.placeholder.ListItem;

public class AccountDataSource {
    AccountRepository accountRepository;

    public AccountDataSource(Context context) {
        accountRepository = new AccountRepository(context);
        accountRepository.open();
    }

    public void addAccount(String name) {
        accountRepository.addAccount(name);
    }

    public void addAccount(Account account) {

    }

    public void addDetails(String key, String value) {}

    public List<ListItem> getItems() {
        List<ListItem> items = new ArrayList<>();
        Cursor cursor = accountRepository.getAllAccount();
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int count = cursor.getInt(cursor.getColumnIndex("count"));
            String createdAt = cursor.getString(cursor.getColumnIndex("createdAt"));
            String updatedAt = cursor.getString(cursor.getColumnIndex("updatedAt"));

            ListItem item = new ListItem(id, name);
            item.setCount(count)
                .setUpdatedAt(updatedAt)
                .setCreatedAt(createdAt);

            items.add(item);
        }
        cursor.close();
        return items;
    }

    public void deleteAccount(long id) {
        accountRepository.deleteAccount(id);
    }

    public void updateAccount(long id, Account account) {
        accountRepository.updateAccount(id, "", 0);
    }


    public static class Account {

    }

    public void close() {
        accountRepository.close();
    }
}
