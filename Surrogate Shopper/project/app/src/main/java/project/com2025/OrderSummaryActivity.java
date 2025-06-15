package project.com2025;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class OrderSummaryActivity extends AppCompatActivity {
    private EditText thankYouMessage;
    private Button submitButton;
    private ListView itemsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        // Get the list of items from intent
        ArrayList<String> itemsList = getIntent().getStringArrayListExtra("ITEMS_LIST");

        thankYouMessage = findViewById(R.id.thankYouMessage);
        submitButton = findViewById(R.id.submitThankYou);
        itemsListView = findViewById(R.id.itemsListView);

        // Set up list view adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, itemsList);
        itemsListView.setAdapter(adapter);

        submitButton.setOnClickListener(v -> {
            String message = thankYouMessage.getText().toString();
            if (!message.isEmpty()) {
                // Save message to database/backend
                saveThankYouMessage(message);
                Toast.makeText(this, "Thank you message sent!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveThankYouMessage(String message) {
        // Implement your database/backend logic here
        // This should save the message to the volunteer's profile
    }
}