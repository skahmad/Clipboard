package creator.android.clipboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AccountDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        TextView textViewName = findViewById(R.id.textViewName);
        TextView textViewPhone = findViewById(R.id.textViewPhone);
        TextView textViewEmail = findViewById(R.id.textViewEmail);

        // Get the intent that started this activity and extract the contact details
        Intent intent = getIntent();
        if(intent != null) {
            String name = intent.getStringExtra("name");
            String phone = intent.getStringExtra("phone");
            String email = intent.getStringExtra("email");

            // Set the contact details to the views
            textViewName.setText(name);
            textViewPhone.setText(phone);
            textViewEmail.setText(email);
        }
    }
}
