package creator.android.clipboard.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.Date;

import creator.android.clipboard.models.Account;

public class AccountDataSource extends SQLLiteDataSource {
    public AccountDataSource(Context context) {
        super(context);
        open();
    }

    public long addAccount(String name) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("createdAt", new Date().toString());
        values.put("updatedAt", new Date().toString());
        values.put("count", 0);
        return database.insert(MyDatabaseHelper.TABLE_ACCOUNT, null, values);
    }

    public Cursor getAllAccount() {
        return database.query(
                MyDatabaseHelper.TABLE_ACCOUNT,
                new String[]{"id", "name", "count", "createdAt", "updatedAt"},
                null,
                null,
                null,
                null,
                null
        );
    }

    public int updateAccount(long id, String name, int count) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("count", count);

        return database.update(
                MyDatabaseHelper.TABLE_ACCOUNT,
                values,
                "id" + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    public void deleteAccount(long id) {
        database.delete(
                MyDatabaseHelper.TABLE_ACCOUNT,
                "id" + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    public Account getAccount(Integer id) {
        Cursor cursor = database.query(
                MyDatabaseHelper.TABLE_ACCOUNT,
                new String[]{"id", "name", "count", "createdAt", "updatedAt"},
                "id = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,
                "1" // Limit the result to 1
        );

        if(cursor != null && cursor.moveToFirst()) {
            String iid = cursor.getString(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            Integer count = cursor.getInt(cursor.getColumnIndex("count"));
            String createdAt = cursor.getString(cursor.getColumnIndex("createdAt"));
            String updatedAt = cursor.getString(cursor.getColumnIndex("updatedAt"));

            Account item = new Account(iid, name);
            item.setCount(count)
                    .setUpdatedAt(updatedAt)
                    .setCreatedAt(createdAt);
            return item;
        }
        return null;
    }

    public Cursor getAccountByNameContains(String text) {
        return database.query(
                MyDatabaseHelper.TABLE_ACCOUNT,
                MyDatabaseHelper.TABLE_ACCOUNT_COLUMNS,
                "name LIKE ?",                                 // The WHERE clause (selection)
                new String[]{"%" + text + "%"},
                null,
                null,
                null
        );
    }
}
