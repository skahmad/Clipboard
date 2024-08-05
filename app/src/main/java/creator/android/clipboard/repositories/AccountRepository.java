package creator.android.clipboard.repositories;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import creator.android.clipboard.data.SQLLiteDataSource;
import creator.android.clipboard.models.ListItem;

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


    public List<ListItem> getItems() {
        List<ListItem> items = new ArrayList<>();
        Cursor cursor = SQLLiteDataSource.getAllAccount();
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
        SQLLiteDataSource.deleteAccount(id);
    }

    public void updateAccount(long id, Account account) {
        SQLLiteDataSource.updateAccount(id, "", 0);
    }

    public List<ListItem> findByName(String text) {
        Cursor cursor = SQLLiteDataSource.getAccountByNameContains(text);
        List<ListItem> items = new ArrayList<>();
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


    public static class Account {

    }

    public void close() {
        SQLLiteDataSource.close();
    }
}
