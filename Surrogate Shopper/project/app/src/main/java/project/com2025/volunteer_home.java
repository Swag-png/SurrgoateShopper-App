package project.com2025;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class volunteer_home extends AppCompatActivity {

    private ArrayList<String> requests = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.fragment_volunteer_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setTitle("Requests");

        LinearLayout container = findViewById(R.id.requestContainer);
        Button reset = findViewById(R.id.btnAllRequests);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2814267/Requests.php")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseData = response.body().string();
                try {
                    JSONArray jsonArray = new JSONArray(responseData);
                    runOnUiThread(() -> {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject requestInfo = jsonArray.getJSONObject(i);
                                String fname = requestInfo.getString("FNAME");
                                String sname = requestInfo.getString("SNAME");
                                String items = requestInfo.getString("ITEMS");
                                String location = requestInfo.getString("LOCATION");
                                String uri = "http://maps.google.com/maps?q=" + Uri.encode(location);

                                String fullText = "Name: " + fname + " " + sname + "\n" +
                                        "Items: " + items + "\n" +
                                        "Location: " + location + " (Click to see on google maps)";
                                requests.add(fullText);

                                LinearLayout row = createRequestRow(fullText, location, toolbar, container);
                                container.addView(row);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        reset.setOnClickListener(v -> {
                            container.removeAllViews();
                            for (String info : requests) {
                                String[] lines = info.split("\n");
                                String locationText = lines.length >= 3
                                        ? lines[2].replace("Location: ", "").replace("(Click to see on google maps)", "").trim()
                                        : "";
                                LinearLayout row = createRequestRow(info, locationText, toolbar, container);
                                container.addView(row);
                            }
                        });
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private LinearLayout createRequestRow(String fullText, String locationText, MaterialToolbar toolbar, LinearLayout container) {
        LinearLayout row = new LinearLayout(volunteer_home.this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        row.setPadding(16, 16, 16, 16);

        TextView requestView = new TextView(volunteer_home.this);
        SpannableString spannableString = new SpannableString(fullText);
        int startIndex = fullText.indexOf("Location:");
        int endIndex = fullText.length();

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                String uri = "http://maps.google.com/maps?q=" + Uri.encode(locationText);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                if (intent.resolveActivity(widget.getContext().getPackageManager()) != null) {
                    widget.getContext().startActivity(intent);
                } else {
                    widget.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
                }
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
                ds.setUnderlineText(true);
            }
        };

        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        requestView.setText(spannableString);
        requestView.setMovementMethod(LinkMovementMethod.getInstance());
        requestView.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1
        ));

        ImageButton tickButton = new ImageButton(volunteer_home.this);
        tickButton.setImageResource(android.R.drawable.checkbox_on_background);
        tickButton.setBackgroundColor(Color.TRANSPARENT);
        tickButton.setOnClickListener(v -> {
            container.removeAllViews();
            container.addView(row);
            row.removeView(tickButton);
            toolbar.setTitle("Request Information");
        });

        row.addView(requestView);
        row.addView(tickButton);

        return row;
    }
}
