package com.example.surrogatenew;

import android.app.DownloadManager;
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

public class VolunteerRequests extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_volunteer_requests);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setTitle("Requests");

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2814267/Requests.php")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseData = response.body().string();
                try {
                    JSONArray jsonArray = new JSONArray(responseData);
                    runOnUiThread(() -> {
                        LinearLayout l = findViewById(R.id.requestContainer);
                        ArrayList<String>requests = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject requestInfo = jsonArray.getJSONObject(i);
                                String fname = requestInfo.getString("FNAME");
                                String sname = requestInfo.getString("SNAME");
                                String items = requestInfo.getString("ITEMS");
                                String location = requestInfo.getString("LOCATION");
                                String uri = "http://maps.google.com/maps?q=" + Uri.encode(location);


                                LinearLayout row = new LinearLayout(VolunteerRequests.this);
                                row.setOrientation(LinearLayout.HORIZONTAL);
                                row.setLayoutParams(new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                ));
                                row.setPadding(16, 16, 16, 16);

                                TextView requestView = new TextView(VolunteerRequests.this);
                                requestView.setText("Name: " + fname + " " + sname + "\n" + "Items: "
                                        + items + "\n" + "Location: " + location+"(Click to see on google maps)");
                                String info = requestView.getText().toString();
                                requests.add(info);
                                String[] lines = info.split("\n");

                                if (lines.length >= 3) {
                                    String nameLine = lines[0];
                                    String itemsLine = lines[1];
                                    String locationLine = lines[2]; // "Location: 123 Main St, Springfield"
                                    String locationText = locationLine.replace("Location: ", "").trim();

                                    SpannableString spannableString = new SpannableString(info);

                                    // Find start and end of location line
                                    int startIndex = info.indexOf(locationLine);
                                    int endIndex = startIndex + locationLine.length();

                                    // Make the location line clickable
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
                                            ds.setColor(Color.BLUE); // Make it look like a link
                                            ds.setUnderlineText(true);
                                        }
                                    };

                                    spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                    requestView.setText(spannableString);
                                    requestView.setMovementMethod(LinkMovementMethod.getInstance()); // Important!
                                }

                                requestView.setLayoutParams(new LinearLayout.LayoutParams(
                                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1
                                ));

                                ImageButton tickButton = new ImageButton(VolunteerRequests.this);
                                tickButton.setImageResource(android.R.drawable.checkbox_on_background);
                                tickButton.setBackgroundColor(Color.TRANSPARENT);




                                tickButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        l.removeAllViews();
                                        l.addView(row);
                                        row.removeView(tickButton);
                                        toolbar.setTitle("Request Information");

                                    }
                                });


                                row.addView(requestView);
                                row.addView(tickButton);


                                l.addView(row);




                            } catch (JSONException e) {
                                e.printStackTrace();

                            }


                        }
                        Button reset = findViewById(R.id.btnAllRequests);
                        reset.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                l.removeAllViews();
                                LinearLayout row = new LinearLayout(VolunteerRequests.this);
                                row.setOrientation(LinearLayout.HORIZONTAL);
                                row.setLayoutParams(new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                ));
                                row.setPadding(16, 16, 16, 16);

                                for (int j = 0; j < requests.size(); j++) {
                                    TextView requestView = new TextView(VolunteerRequests.this);
                                    requestView.setText(requests.get(j));

                                    requestView.setLayoutParams(new LinearLayout.LayoutParams(
                                            0, LinearLayout.LayoutParams.WRAP_CONTENT, 1
                                    ));

                                    ImageButton tickButton = new ImageButton(VolunteerRequests.this);
                                    tickButton.setImageResource(android.R.drawable.checkbox_on_background);
                                    tickButton.setBackgroundColor(Color.TRANSPARENT);

                                    row.addView(requestView);
                                    row.addView(tickButton);


                                    l.addView(row);
                                }



                            }
                        });
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }
    public void acceptRequest(View v){

    }
}