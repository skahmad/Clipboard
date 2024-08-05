package creator.android.clipboard.data;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import creator.android.clipboard.repositories.AccountRepository;
import creator.android.clipboard.placeholder.Information;
import creator.android.clipboard.placeholder.ListItem;

public class InformationDataSource {
    private AccountRepository accountRepository;

    public InformationDataSource(Context context) {
        accountRepository = new AccountRepository(context);
        accountRepository.open();
    }

    public List<Information> getInformations(Integer accountId) {
        List<Information> informationList = new ArrayList<>();
        Cursor cursor = accountRepository.getAllInformations(accountId);
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
        ListItem item = accountRepository.getAccount(accountId);
        if(item != null ) {
            Information information = new Information(accountId);
            information.setName(name);
            information.setDetails(details);
            information.setCreatedAt(new Date().toString());
            information.setUpdatedAt(new Date().toString());

            accountRepository.addInformation(accountId, information);
            accountRepository.incrementInformation(accountId, item.getCount());
        }
    }

    public void update(int id, String key, String value) {
        accountRepository.updateInformation(id, key, value);
    }

    public Information getInformation(int id) {
        return accountRepository.getInformation(id);
    }

    public void delete(int id) {
        Information information = accountRepository.getInformation(id);
        ListItem account = accountRepository.getAccount(information.getAccountId());
        accountRepository.deleteInformation(id);
        accountRepository.decrementInformation(account.getIntId(), account.getCount());
    }

    public List<Information> findByName(String text, int accountId) {
        Cursor cursor = accountRepository.getInformationByNameContains(accountId, text);
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
