package creator.android.clipboard.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "account.db";
    private static final int DATABASE_VERSION = 1;

    // Table name
    public static final String TABLE_ACCOUNT = "accounts";
    public static final String TABLE_INFORMATION = "information";
    public static final String[] TABLE_INFORMATION_COLUMNS = {
            "id", "account_id",
            "name", "details",
            "createdAt", "updatedAt", "deletedAt",
            "isDeleted"
    };

    public static final String[] TABLE_ACCOUNT_COLUMNS = {
            "id", "count",
            "name",
            "createdAt", "updatedAt"
    };

    // Table columns
    @Deprecated
    public static final String COLUMN_ID = "id";
    @Deprecated
    public static final String COLUMN_NAME = "name";
    @Deprecated
    public static final String COLUMN_PHONE = "phone";
    @Deprecated
    public static final String COLUMN_EMAIL = "email";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createAccountsTable = "CREATE TABLE accounts (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "count INTEGER NOT NULL, " +
                "createdAt DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "updatedAt DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "name TEXT NOT NULL)";

        String createInformationTable = "CREATE TABLE information (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "account_id INTEGER NOT NULL, " +
                "name TEXT NOT NULL, " +
                "details TEXT, " +
                "createdAt DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "updatedAt DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "isDeleted INTEGER DEFAULT 0, " +
                "deletedAt DATETIME, " +
                "FOREIGN KEY(account_id) REFERENCES accounts(id))";


        db.execSQL(createAccountsTable);
        db.execSQL(createInformationTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the old table and create a new one
        db.execSQL("DROP TABLE IF EXISTS information");
        db.execSQL("DROP TABLE IF EXISTS accounts");
        onCreate(db);
    }
}
