package project.com2025;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.widget.TextView;
import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import java.io.IOException;

public class assign {
    public static void getDetails( String email, String role){

        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("role", role)
                .build();

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2669187/get.php")
                .post(formBody)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();

                    try {
                        JSONObject jsonObject = new JSONObject(responseBody);
                        String status = jsonObject.getString("status");

                        if (status.equals("Success")) {
                            JSONObject data = jsonObject.getJSONObject("data");

                            // For Customer:
                            if (role.equals("Customer")) {
                                Customer.cus_fname = data.getString("cust_fname");
                                Customer.cus_lname = data.getString("cust_lname");
                                Customer.cus_id = data.getString("cust_id");
                                Customer.cus_phone = data.getString("cust_phone");
                                Customer.cus_location = data.getString("Address_Street");
                                Customer.cus_email = email;
                            }

                            // For Volunteer:
                            else if (role.equals("Volunteer")) {
                                Volunteer.volunteer_fname = data.getString("volunteer_fname");
                                Volunteer.volunteer_lname = data.getString("volunteer_lname");
                                Volunteer.volunteer_id = data.getString("volunteer_id");
                                Volunteer.volunteer_phone = data.getString("volunteer_cell");
                                Volunteer.num_of_deliveries = data.getString("number_of_deliveries");
                                Volunteer.volunteer_email = email;
                            }

                        } else {
                            Log.e("SERVER_RESPONSE", "Server said: " + status);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
}
