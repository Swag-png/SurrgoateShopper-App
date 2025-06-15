package project.com2025;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link volunteer_password#newInstance} factory method to
 * create an instance of this fragment.
 */
public class volunteer_password extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public volunteer_password() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment volunteer_password.
     */
    // TODO: Rename and change types and number of parameters
    public static volunteer_password newInstance(String param1, String param2) {
        volunteer_password fragment = new volunteer_password();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout
        View view = inflater.inflate(R.layout.volunteer_password, container, false);

        // Initialize views
        Button next = view.findViewById(R.id.btn_volunteer_enter);  // Replace with your actual button ID
        EditText pass1 = view.findViewById(R.id.edit_volunteer_password1);
        EditText pass2 = view.findViewById(R.id.edit_volunteer_password2);

        // Set onClick listener
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = pass1.getText().toString().trim();
                String confirmPassword = pass2.getText().toString().trim();

                if (!Validation.isValidPassword(password)) {
                    pass1.setError("Password must be 8–20 characters, include uppercase, lowercase, digit, and symbol.");
                } else if (!password.equals(confirmPassword)) {
                    pass2.setError("Passwords do not match.");
                } else {
                    Globals.password = password;
                    Globals.location = "";
                    // This is where we go to the next page and send the php details to the database
                    try {
                        // Attempt to validate registration and navigate
                        validateRegistration(view,
                                Globals.firstName, Globals.lastName, Globals.id,
                                Globals.email, Globals.location, Globals.phone,
                                Globals.password, Globals.role
                        );
                        assign.getDetails(Globals.email,Globals.role);
                    } catch (Exception e) {
                        // Log the error for debugging
                        Log.e("PasswordCreation", "Error during registration or navigation", e);
                    }
                }
            }
        });

        return view;
    }

    public void validateRegistration(View v, String firstName, String surname, String idNum, String emailAddress, String streetAddress, String cellNumber, String passWord, String role) {
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder()
                .add("first_name", firstName)
                .add("last_name", surname)
                .add("id_number", idNum)
                .add("street_address", streetAddress)
                .add("email", emailAddress)
                .add("cell_number", cellNumber)
                .add("password", passWord)
                .add("role", role)
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
                    Log.d("RawServerResponse", responseData); // 👈 Add this to inspect server response

                    try {
                        JSONObject jsonResponse = new JSONObject(responseData);

                        if (jsonResponse.has("status")) {
                            String status = jsonResponse.getString("status");

                            if (status.equals("Success")) {
                                requireActivity().runOnUiThread(() -> {
                                    Toast.makeText(requireContext(), "Sign up successful", Toast.LENGTH_SHORT).show();
                                    NavController navController = Navigation.findNavController(v);
                                    navController.navigate(R.id.action_volunteer_password_to_volunteer_home);
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
                        } else {
                            Log.e("ServerResponse", "Missing 'status' key in response: " + responseData);
                            requireActivity().runOnUiThread(() -> {
                                Toast.makeText(requireContext(), "Unexpected server response", Toast.LENGTH_SHORT).show();
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(requireContext(), "Response parsing failed", Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            }

        });
    }
}