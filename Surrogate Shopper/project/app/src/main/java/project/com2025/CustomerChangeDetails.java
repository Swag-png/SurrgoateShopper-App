package project.com2025;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import android.widget.TextView;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomerChangeDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerChangeDetails extends Fragment {

    private String selectedAddresses = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CustomerChangeDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomerChangeDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomerChangeDetails newInstance(String param1, String param2) {
        CustomerChangeDetails fragment = new CustomerChangeDetails();
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.customer_change_details, container, false);

        Button name = rootView.findViewById(R.id.button2);
        Button surname = rootView.findViewById(R.id.button3);
        Button phone = rootView.findViewById(R.id.button4);
        Button email = rootView.findViewById(R.id.button5);
        Button address = rootView.findViewById(R.id.button6);
        Button password = rootView.findViewById(R.id.button7);

        EditText cname = rootView.findViewById(R.id.editname);
        EditText csurname = rootView.findViewById(R.id.editTextText2);
        EditText cphone = rootView.findViewById(R.id.editTextPhone);
        EditText cemail = rootView.findViewById(R.id.editTextTextEmailAddress);
        EditText code1 = rootView.findViewById(R.id.editTextTextPassword);
        EditText code2 = rootView.findViewById(R.id.editTextTextPassword2);

        cname.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        csurname.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        // Initialize Places API if not already done
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), "AIzaSyCpopm2bPwjo6sLGAUroXUXBMx5UUY44WY");
        }

        AutocompleteSupportFragment autoFragmentForUpdate = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment_container);

        if (autoFragmentForUpdate == null) {
            autoFragmentForUpdate = AutocompleteSupportFragment.newInstance();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.autocomplete_fragment_container, autoFragmentForUpdate)
                    .commit();
        }

        autoFragmentForUpdate.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS));

        autoFragmentForUpdate.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                selectedAddresses = place.getAddress();
                Log.d("PlaceSelected", "Selected address: " + selectedAddresses);
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.e("PlaceError", "An error occurred: " + status.getStatusMessage());
            }
        });


        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name1 = cname.getText().toString().trim();

                boolean check = Validation.validateName(name1);
                if(check==false){
                    cname.setError("Invalid Name");
                }
                else
                {
                    Context context = requireContext();
                    updateFirstName(context,Globals.email,name1);
                }
            }
        });

        surname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String surname1 = csurname.getText().toString().trim();

                boolean check = Validation.validateName(surname1);
                if(check==false){
                    csurname.setError("Invalid Name");
                }
                else
                {
                    Context context = requireContext();
                    updateSurname(context,Globals.email,surname1);
                }
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone1 = cphone.getText().toString().trim();

                boolean check = Validation.validateName(phone1);
                if(check==false){
                    cphone.setError("Invalid surname");
                }
                else
                {
                    Context context = requireContext();
                    updatePhone(context,Globals.email,phone1);
                }
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email1 = cemail.getText().toString().trim();

                boolean check = Validation.validateName(email1);
                if(check==false){
                    cemail.setError("Invalid phone number");
                }
                else
                {
                    Context context = requireContext();
                    updateEmail(context,Globals.email,email1);
                }
            }
        });

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address1 = selectedAddresses.trim();

                if(address1.isEmpty()){
                    Toast.makeText(requireContext(), "Invalid Address", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Context context = requireContext();
                    updateLocation(context, Globals.email, address1);
                    Globals.location = address1;
                }
            }
        });

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password11 = code1.getText().toString().trim();
                String password12 = code2.getText().toString().trim();

                boolean check = Validation.validateName(password11);
                if (!Validation.isValidPassword(password12)) {
                    code1.setError("Password must be 8–20 characters, include uppercase, lowercase, digit, and symbol.");
                } else if (!password11.equals(password12)) {
                    code1.setError("Passwords do not match.");
                }
                else{
                    Context context = requireContext();
                    updatePassword(context,Globals.email,password12);
                }
            }
        });



        // Find and setup the TabHost (make sure to use the ID android.R.id.tabhost)
        TabHost tabHost = rootView.findViewById(android.R.id.tabhost);
        tabHost.setup();

        // Tab 1 - Names
        TabHost.TabSpec spec1 = tabHost.newTabSpec("Names");
        spec1.setContent(R.id.Names);
        spec1.setIndicator("Names");
        tabHost.addTab(spec1);

        // Tab 2 - Contact
        TabHost.TabSpec spec2 = tabHost.newTabSpec("Contact");
        spec2.setContent(R.id.Contact);
        spec2.setIndicator("Contact");
        tabHost.addTab(spec2);

        // Tab 3 - Address
        TabHost.TabSpec spec3 = tabHost.newTabSpec("Address");
        spec3.setContent(R.id.Address);
        spec3.setIndicator("Address");
        tabHost.addTab(spec3);

        // Tab 4 - Passkey
        TabHost.TabSpec spec4 = tabHost.newTabSpec("Passkey");
        spec4.setContent(R.id.passkey);
        spec4.setIndicator("Passkey");
        tabHost.addTab(spec4);

        // Optional: set default tab
        tabHost.setCurrentTab(0);

        return rootView;
    }

    public static void updateFirstName(Context context, String email, String newFirstName) {
        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("new_name", newFirstName)  // must match PHP param
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
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(context, "Network error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                Log.d("UPDATE", "Response: " + responseBody);

                try {
                    JSONObject json = new JSONObject(responseBody);
                    String status = json.getString("status");
                    String message = json.optString("message", "No message");

                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void updateSurname(Context context, String email, String newSurname) {
        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("new_name", newSurname)
                .build();

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2669187/update_surname.php")
                .post(formBody)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(context, "Network error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                Log.d("UPDATE", "Response: " + responseBody);
                try {
                    JSONObject json = new JSONObject(responseBody);
                    String message = json.optString("message", "No message");
                    new Handler(Looper.getMainLooper()).post(() ->
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void updateLocation(Context context, String email, String newAddress) {
        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("new_address", newAddress)
                .build();

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2669187/UpdateLocation.php")
                .post(formBody)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(context, "Network error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                Log.d("UPDATE_LOCATION", "Response: " + responseBody);
                try {
                    JSONObject json = new JSONObject(responseBody);
                    String message = json.optString("message", "No message");
                    new Handler(Looper.getMainLooper()).post(() ->
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
                } catch (JSONException e) {
                    e.printStackTrace();
                    new Handler(Looper.getMainLooper()).post(() ->
                            Toast.makeText(context, "Response parsing error", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }



    public static void updatePhone(Context context, String email, String newPhone) {
        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("new_name", newPhone)
                .build();

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2669187/update_phone.php")
                .post(formBody)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(context, "Network error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                Log.d("UPDATE", "Response: " + responseBody);
                try {
                    JSONObject json = new JSONObject(responseBody);
                    String message = json.optString("message", "No message");
                    new Handler(Looper.getMainLooper()).post(() ->
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void updatePassword(Context context, String email, String newPassword) {
        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("new_name", newPassword)
                .build();

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2669187/update_password.php")
                .post(formBody)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(context, "Network error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                Log.d("UPDATE", "Response: " + responseBody);
                try {
                    JSONObject json = new JSONObject(responseBody);
                    String message = json.optString("message", "No message");
                    new Handler(Looper.getMainLooper()).post(() ->
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void updateEmail(Context context, String currentEmail, String newEmail) {
        RequestBody formBody = new FormBody.Builder()
                .add("current_email", currentEmail)
                .add("new_email", newEmail)
                .build();

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2669187/update_email.php")
                .post(formBody)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(context, "Network error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                Log.d("UPDATE_EMAIL", "Response: " + responseBody);
                try {
                    JSONObject json = new JSONObject(responseBody);
                    String message = json.optString("message", "No message");
                    new Handler(Looper.getMainLooper()).post(() ->
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}