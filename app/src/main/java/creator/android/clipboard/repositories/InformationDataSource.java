package creator.android.clipboard.repositories;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import creator.android.clipboard.data.SQLLiteDataSource;
import creator.android.clipboard.models.Information;
import creator.android.clipboard.models.ListItem;

public class InformationDataSource {
    private creator.android.clipboard.data.SQLLiteDataSource SQLLiteDataSource;

    public InformationDataSource(Context context) {
        SQLLiteDataSource = new SQLLiteDataSource(context);
        SQLLiteDataSource.open();
    }

    public List<Information> getInformations(Integer accountId) {
        List<Information> informationList = new ArrayList<>();
        Cursor cursor = SQLLiteDataSource.getAllInformations(accountId);
        while (cursor.moveToNext()) {
            Integer id = cursor.getInt(cursor.getColumnIndex("id"));
            Integer accId = cursor.getInt(cursor.getColumnIndex("account_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String details = cursor.getString(cursor.getColumnIndex("details"));
            String createdAt = cursor.getString(cursor.getColumnIndex("createdAt"));
            String updatedAt = cursor.getString(cursor.getColumnIndex("updatedAt"));

            Information information = new Information(accId);
            information.setId(id)
                    .setName(name)
                    .setDetails(details)
                    .setUpdatedAt(updatedAt)
                    .setCreatedAt(createdAt);

            informationList.add(information);
        }
        cursor.close();
        return informationList;
    }

    public void addInformation(Integer accountId, String name, String details) {
        ListItem item = SQLLiteDataSource.getAccount(accountId);
        if(item != null ) {
            Information information = new Information(accountId);
            information.setName(name);
            information.setDetails(details);
            information.setCreatedAt(new Date().toString());
            information.setUpdatedAt(new Date().toString());

            SQLLiteDataSource.addInformation(accountId, information);
            SQLLiteDataSource.incrementInformation(accountId, item.getCount());
        }
    }

    public void update(int id, String key, String value) {
        SQLLiteDataSource.updateInformation(id, key, value);
    }

    public Information getInformation(int id) {
        return SQLLiteDataSource.getInformation(id);
    }

    public void delete(int id) {
        Information information = SQLLiteDataSource.getInformation(id);
        ListItem account = SQLLiteDataSource.getAccount(information.getAccountId());
        SQLLiteDataSource.deleteInformation(id);
        SQLLiteDataSource.decrementInformation(account.getIntId(), account.getCount());
    }

    public List<Information> findByName(String text, int accountId) {
        Cursor cursor = SQLLiteDataSource.getInformationByNameContains(accountId, text);
        List<Information> items = new ArrayList<>();

        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            //String accountId = cursor.getString(cursor.getColumnIndex("account_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String details = cursor.getString(cursor.getColumnIndex("details"));
            String createdAt = cursor.getString(cursor.getColumnIndex("createdAt"));
            String updatedAt = cursor.getString(cursor.getColumnIndex("updatedAt"));

            Information information = new Information(accountId);
            information.setDetails(details)
                    .setId(Integer.parseInt(id))
                    .setName(name)
                    .setUpdatedAt(updatedAt)
                    .setCreatedAt(createdAt);

            items.add(information);
        }
        cursor.close();
        return items;
    }
}
