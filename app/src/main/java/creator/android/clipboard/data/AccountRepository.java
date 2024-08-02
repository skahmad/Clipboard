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
}
