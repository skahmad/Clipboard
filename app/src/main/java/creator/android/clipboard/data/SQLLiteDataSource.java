package creator.android.clipboard.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SQLLiteDataSource {
    protected SQLiteDatabase database;
    private final MyDatabaseHelper dbHelper;
    public final Context context;

    public SQLLiteDataSource(Context context) {
        dbHelper = new MyDatabaseHelper(context);
        this.context = context;
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }
}
