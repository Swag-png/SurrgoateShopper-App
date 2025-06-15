package project.com2025;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class Send_php{
    public static void validateRegistration(View v, String firstName, String surname, String idNum, String emailAddress, String cellNumber, String streetAddress, String passWord, String role){
        boolean validateInfo = true;

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder()
                .add("First Name", firstName)
                .add("Last Name", surname)
                .add("ID Number", idNum)
                .add("Email Address", emailAddress)
                .add("Cell Number", cellNumber)
                .add("Street Address", streetAddress)
                .add("Password",passWord)
                .add("Role",role)
                .build();

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2814267/Registration.php")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();

            }

            @Override
            public void esponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {

                    throw new IOException("Unexpected code" + response);

                } else {

                    final String responseData = response.body().string();


                    // Run view-related code back on the main thread
                    try {
                        JSONObject jsonResponse = new JSONObject(responseData);
                        String status = jsonResponse.getString("status");

                        if (status.equals("Success")) {
                            requireActivity().runOnUiThread(() -> {

                                Toast.makeText(requireContext(), "Sign up successful", Toast.LENGTH_SHORT).show();
                            });
                        } else if (status.equals("Exists")) {

                            requireActivity().runOnUiThread(() -> {
                                Toast.makeText(requireContext(), "User already exists", Toast.LENGTH_SHORT).show();
                            });

                        } else {
                            requireActivity().runOnUiThread(() -> {
                                Toast.makeText(requireContext(), "Sign up failed", Toast.LENGTH_SHORT).show();
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