package creator.android.clipboard.repositories;

import android.content.Context;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import creator.android.clipboard.data.AccountDataSource;
import creator.android.clipboard.data.InformationDataSource;
import creator.android.clipboard.models.Information;
import creator.android.clipboard.models.Account;

public class InformationRepository {
    private final InformationDataSource informationDataSource;
    private final AccountDataSource accountDataSource;


    public InformationRepository(Context context) {
        informationDataSource = new InformationDataSource(context);
        accountDataSource = new AccountDataSource(context);
        accountDataSource.open();
        informationDataSource.open();
    }

    public List<Information> getInformations(Integer accountId) {
        List<Information> informationList = new ArrayList<>();
        Cursor cursor = informationDataSource.getAllInformations(accountId);
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
        Account account = accountDataSource.getAccount(accountId);
        if(account != null ) {
            Information information = new Information(accountId);
            information.setName(name);
            information.setDetails(details);
            information.setCreatedAt(new Date().toString());
            information.setUpdatedAt(new Date().toString());

            informationDataSource.addInformation(accountId, information);
            informationDataSource.incrementInformation(accountId, account.getCount());
        }
    }

    public void update(int id, String key, String value) {
        informationDataSource.updateInformation(id, key, value);
    }

    public Information getInformation(int id) {
        return informationDataSource.getInformation(id);
    }

    public void delete(int id) {
        Information information = informationDataSource.getInformation(id);
        Account account = accountDataSource.getAccount(information.getAccountId());
        informationDataSource.deleteInformation(id);
        informationDataSource.decrementInformation(account.getIntId(), account.getCount());
    }

    public List<Information> findByName(String text, int accountId) {
        Cursor cursor = informationDataSource.getInformationByNameContains(accountId, text);
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

    public void deleteByAccountId(Integer id) {
        informationDataSource.deleteAllByAccount(id);
    }
}
