package project.com2025;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.app.Activity;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class send_php {
    public static void validateRegistration(Context context, View v, String firstName, String surname, String idNum, String emailAddress, String cellNumber, String streetAddress, String passWord, String role) {
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder()
                .add("First Name", firstName)
                .add("Last Name", surname)
                .add("ID Number", idNum)
                .add("Email Address", emailAddress)
                .add("Cell Number", cellNumber)
                .add("Street Address", streetAddress)
                .add("Password", passWord)
                .add("Role", role)
                .build();

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2669187/Registration.php")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {

                    throw new IOException("Unexpected code" + response);

                } else {

                    final String responseData = response.body().string();


                    // Run view-related code back on the main thread
                    try {
                        JSONObject jsonResponse = new JSONObject(responseData);
                        String status = jsonResponse.getString("status");

                        if (context instanceof Activity) {
                            ((Activity) context).runOnUiThread(() -> {
                                if (status.equals("Success")) {
                                    Toast.makeText(context, "Sign up successful", Toast.LENGTH_SHORT).show();
                                } else if (status.equals("Exists")) {
                                    Toast.makeText(context, "User already exists", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Sign up failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}