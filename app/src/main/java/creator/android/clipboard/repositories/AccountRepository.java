package creator.android.clipboard.repositories;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import creator.android.clipboard.data.SQLLiteDataSource;

public class AccountRepository {
    creator.android.clipboard.data.SQLLiteDataSource SQLLiteDataSource;

    public AccountRepository(Context context) {
        SQLLiteDataSource = new SQLLiteDataSource(context);
        SQLLiteDataSource.open();
    }

    public void addAccount(String name) {
        SQLLiteDataSource.addAccount(name);
    }

    public void addAccount(Account account) {

    }


    public List<creator.android.clipboard.models.Account> getItems() {
        List<creator.android.clipboard.models.Account> items = new ArrayList<>();
        Cursor cursor = SQLLiteDataSource.getAllAccount();
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int count = cursor.getInt(cursor.getColumnIndex("count"));
            String createdAt = cursor.getString(cursor.getColumnIndex("createdAt"));
            String updatedAt = cursor.getString(cursor.getColumnIndex("updatedAt"));

            creator.android.clipboard.models.Account item = new creator.android.clipboard.models.Account(id, name);
            item.setCount(count)
                .setUpdatedAt(updatedAt)
                .setCreatedAt(createdAt);

            items.add(item);
        }
        cursor.close();
        return items;
    }

    public void deleteAccount(long id) {
        SQLLiteDataSource.deleteAccount(id);
    }

    public void updateAccount(long id, Account account) {
        SQLLiteDataSource.updateAccount(id, "", 0);
    }

    public List<creator.android.clipboard.models.Account> findByName(String text) {
        Cursor cursor = SQLLiteDataSource.getAccountByNameContains(text);
        List<creator.android.clipboard.models.Account> items = new ArrayList<>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int count = cursor.getInt(cursor.getColumnIndex("count"));
            String createdAt = cursor.getString(cursor.getColumnIndex("createdAt"));
            String updatedAt = cursor.getString(cursor.getColumnIndex("updatedAt"));

            creator.android.clipboard.models.Account item = new creator.android.clipboard.models.Account(id, name);
            item.setCount(count)
                    .setUpdatedAt(updatedAt)
                    .setCreatedAt(createdAt);

            items.add(item);
        }
        cursor.close();
        return items;
    }


    public static class Account {

    }

    public void close() {
        SQLLiteDataSource.close();
    }
}
