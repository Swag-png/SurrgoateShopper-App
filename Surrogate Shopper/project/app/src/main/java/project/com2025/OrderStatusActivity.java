package project.com2025;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class OrderStatusActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        TextView statusText = findViewById(R.id.statusText);
        ProgressBar progressBar = findViewById(R.id.progressBar);

        // Update based on actual status from your database/backend
        statusText.setText("Your order is on its way!");
        progressBar.setProgress(50); // Set appropriate progress
    }
}