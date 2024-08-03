package creator.android.clipboard.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import creator.android.clipboard.placeholder.Information;
import creator.android.clipboard.placeholder.ListItem;

public class AccountRepository {

    private SQLiteDatabase database;
    private MyDatabaseHelper dbHelper;

    public AccountRepository(Context context) {
        dbHelper = new MyDatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
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

    public Cursor getAllInformations(Integer accountId) {
        return database.query(
                MyDatabaseHelper.TABLE_INFORMATION,
                new String[]{"id", "account_id", "name", "details", "createdAt", "updatedAt"},
                "account_id = ?",        // Selection clause (WHERE clause)
                new String[]{String.valueOf(accountId)},
                null,
                null,
                null
        );
    }

    public long addInformation(Integer accountId, Information information) {
        ContentValues values = new ContentValues();
        Date d = new Date();
        values.put("account_id", accountId);
        values.put("name", information.getName());
        values.put("createdAt", d.toString());
        values.put("updatedAt", d.toString());
        values.put("details", information.getDetails());
        values.put("deletedAt", d.toString());
        values.put("isDeleted", 0);
        return database.insert(MyDatabaseHelper.TABLE_INFORMATION, null, values);
    }

    public ListItem getAccount(Integer id) {
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

            ListItem item = new ListItem(iid, name);
            item.setCount(count)
                    .setUpdatedAt(updatedAt)
                    .setCreatedAt(createdAt);
            return item;
        }
        return null;
    }

    public void incrementInformation(Integer accountId, int count) {
        ContentValues values = new ContentValues();
        values.put("count", (count+1));

        database.update(
                MyDatabaseHelper.TABLE_ACCOUNT,
                values,
                "id" + " = ?",
                new String[]{String.valueOf(accountId)}
        );
    }
}
