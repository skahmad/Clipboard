package creator.android.clipboard.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.Date;

import creator.android.clipboard.models.Information;

public class InformationDataSource extends SQLLiteDataSource {
    public InformationDataSource(Context context) {
        super(context);
        open();
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

    public long updateInformation(int accountId, String key, String value) {
        ContentValues values = new ContentValues();
        Date d = new Date();
        values.put("name", key);
        values.put("updatedAt", d.toString());
        values.put("details", value);

        return database.update(
                MyDatabaseHelper.TABLE_INFORMATION,
                values,
                "id" + " = ?",
                new String[]{String.valueOf(accountId)}
        );
    }

    public void deleteInformation(long id) {
        database.delete(
                MyDatabaseHelper.TABLE_INFORMATION,
                "id" + " = ?",
                new String[]{String.valueOf(id)}
        );
    }


    public Information getInformation(Integer id) {
        Cursor cursor = database.query(
                MyDatabaseHelper.TABLE_INFORMATION,
                MyDatabaseHelper.TABLE_INFORMATION_COLUMNS,
                "id = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,
                "1" // Limit the result to 1
        );

        if(cursor != null && cursor.moveToFirst()) {
            String iid = cursor.getString(cursor.getColumnIndex("id"));
            int account = cursor.getInt(cursor.getColumnIndex("account_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String details = cursor.getString(cursor.getColumnIndex("details"));
            String createdAt = cursor.getString(cursor.getColumnIndex("createdAt"));
            String updatedAt = cursor.getString(cursor.getColumnIndex("updatedAt"));

            Information information = new Information(account);
            information.setName(name)
                    .setDetails(details)
                    .setId(Integer.valueOf(iid))
                    .setUpdatedAt(updatedAt)
                    .setCreatedAt(createdAt);
            return information;
        }
        return null;
    }

    public void decrementInformation(Integer accountId, int count) {
        ContentValues values = new ContentValues();
        values.put("count", (count-1));

        database.update(
                MyDatabaseHelper.TABLE_ACCOUNT,
                values,
                "id" + " = ?",
                new String[]{String.valueOf(accountId)}
        );
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

    public Cursor getInformationByNameContains(int accountId, String text) {
        return database.query(
                MyDatabaseHelper.TABLE_INFORMATION,
                MyDatabaseHelper.TABLE_INFORMATION_COLUMNS,
                "account_id = ? AND name LIKE ?",
                new String[]{String.valueOf(accountId), "%" + text + "%"},
                null,
                null,
                null
        );
    }

    public void deleteAllByAccount(long id) {
        database.delete(
                MyDatabaseHelper.TABLE_INFORMATION,
                "account_id" + " = ?",
                new String[]{String.valueOf(id)}
        );
    }
}
