package project.com2025;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HomePage extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public static HomePage newInstance(String param1, String param2) {
        HomePage fragment = new HomePage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public HomePage() {
        // Required empty public constructor
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

        View view = inflater.inflate(R.layout.home_page, container, false);
        View vii = inflater.inflate(R.layout.customer_profile, container, false);

        Spinner spinner = view.findViewById(R.id.list);

        TextView head = view.findViewById(R.id.heading);

        EditText mails = view.findViewById(R.id.edit_email);
        EditText code = view.findViewById(R.id.edit_password);

        Button signup = view.findViewById(R.id.signup);
        Button enter = view.findViewById(R.id.enter);

        String[] items = getResources().getStringArray(R.array.my_spinner_items);

        // Step 1: Build spinner list with hint at index 0
        List<String> spinnerList = new ArrayList<>();
        spinnerList.add("Change User Type"); // hint
        spinnerList.addAll(Arrays.asList(items)); // actual items

        // Step 2: Adapter setup
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                spinnerList
        ) {
            @Override
            public boolean isEnabled(int position) {
                // Disable the hint item
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Grey out the hint in the dropdown
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Step 3: Selection listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View selectedView, int position, long id) {
                if (position == 0) return; // Skip hint
                String selected = parent.getItemAtPosition(position).toString();
                Toast.makeText(requireContext(), "Selected: " + selected, Toast.LENGTH_SHORT).show();

                //Gara's variable type for php stuff
                if (selected.equalsIgnoreCase("Volunteer")) {
                    Globals.role = "Volunteer";
                    head.setText("Volunteer Login");
                } else if (selected.equalsIgnoreCase("Customer")) {
                    Globals.role = "Customer";
                    head.setText("Customer Login");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // What to do when the signup button is clicked
                Toast.makeText(requireContext(), "Signup button clicked", Toast.LENGTH_SHORT).show();
                if (Globals.role.equals("Volunteer")){
                    NavController navController = Navigation.findNavController(v);
                    navController.navigate(R.id.action_home_page_to_volunteer_reg);
                }
                else{
                    NavController navController = Navigation.findNavController(v);
                    navController.navigate(R.id.action_home_page_to_customerRegistration);
                }

                // You can start a new Fragment or Activity here
                // Or do some form validation
            }
        });

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // What to do when the enter button is clicked
                String s_mail = mails.getText().toString().trim();
                String s_code = code.getText().toString().trim();

                if (s_mail.isEmpty() || s_code.isEmpty()) {
                    Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                enter.setEnabled(false);

                LogIn(view,s_mail,s_code, Globals.role);

                assign.getDetails(s_mail,Globals.role);

                TextView one = vii.findViewById(R.id.user_name);
                TextView two = vii.findViewById(R.id.user_lastname);
                TextView three = vii.findViewById(R.id.user_address);
                TextView four = vii.findViewById(R.id.user_orders);

                one.setText(Customer.cus_fname);
                two.setText(Customer.cus_lname);
                three.setText(Customer.cus_location);
                // For example: navigate to the DashboardFragment or validate login
            }
        });
        return view;
    }

    public void LogIn(View v, String emailBox, String passes, String roles){
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder()
                .add("Email", emailBox)
                .add("Password", passes)
                .add("Role", roles)
                .build();

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2669187/LogInPage.php")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "Network error. Please try again.", Toast.LENGTH_SHORT).show()
                );
            }


            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected Code" + response);
                } else {
                    final String responseBody = response.body().string();

                    Log.d("SERVER_RESPONSE", responseBody);

                    try {
                        JSONObject jsonObject = new JSONObject(responseBody);
                        String Status = jsonObject.getString("Status");

                        requireActivity().runOnUiThread(() -> {
                            if (Status.equals("Success")) {
                                Toast.makeText(requireActivity(), "Log in successful!", Toast.LENGTH_SHORT).show();

                                if (roles.equals("Customer")) {
                                    NavController navController = Navigation.findNavController(v);
                                    navController.navigate(R.id.action_home_page_to_customerHomePage);
                                }
                                //Qhama Twala
                                else if (roles.equals("Volunteer")) {
                                    NavController navController = Navigation.findNavController(v);
                                    navController.navigate(R.id.action_home_page_to_volunteer_home);
                                }

                            } else if (Status.equals("Failed")) {
                                Toast.makeText(requireActivity(), "Incorrect student number or password", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(requireActivity(), "User not found", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}