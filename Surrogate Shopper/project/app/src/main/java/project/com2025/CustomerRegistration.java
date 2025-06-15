package project.com2025;

import android.widget.TextView;
import java.util.Arrays;
import android.os.Bundle;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.text.InputFilter;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.ArrayList;

public class CustomerRegistration extends Fragment {

    private String selectedAddress = "";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public CustomerRegistration() {
        // Required empty public constructor
    }

    public static CustomerRegistration newInstance(String param1, String param2) {
        CustomerRegistration fragment = new CustomerRegistration();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                EditText editText1 = view.findViewById(R.id.edit_name);
                EditText editText2 = view.findViewById(R.id.edit_surname);
                EditText editText3 = view.findViewById(R.id.edit_phone);
                EditText editText4 = view.findViewById(R.id.edit_email);
                EditText editText5 = view.findViewById(R.id.edit_id);

                editText1.setText("");
                editText2.setText("");
                editText3.setText("");
                editText4.setText("");
                editText5.setText("");

                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_customerRegistration_to_home_page2);
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout and store the view
        View view = inflater.inflate(R.layout.customer_registration, container, false);

        // Find your button by ID
        Button registerButton = view.findViewById(R.id.next); // Replace with your actual ID

        EditText f_name = view.findViewById(R.id.edit_name);
        EditText l_name = view.findViewById(R.id.edit_surname);
        EditText edit_phone = view.findViewById(R.id.edit_phone);
        EditText edit_email = view.findViewById(R.id.edit_email);
        EditText edit_id = view.findViewById(R.id.edit_id);

        f_name.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        l_name.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        // Initialize Places API if not already done
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), "AIzaSyCpopm2bPwjo6sLGAUroXUXBMx5UUY44WY");
        }

        AutocompleteSupportFragment autoFragmentForRegister =
                (AutocompleteSupportFragment) getChildFragmentManager()
                        .findFragmentById(R.id.auto);

        if (autoFragmentForRegister != null) {
            autoFragmentForRegister.setPlaceFields(Arrays.asList(
                    Place.Field.ID,
                    Place.Field.NAME,
                    Place.Field.ADDRESS
            ));

            autoFragmentForRegister.setHint("Enter street address");

            autoFragmentForRegister.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    selectedAddress = place.getAddress();
                    Log.d("Autocomplete", "Place selected (register): " + place.getAddress());
                }

                @Override
                public void onError(@NonNull Status status) {
                    Toast.makeText(requireContext(), "Error: " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Set up the onClick listener
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globals.firstName = f_name.getText().toString().trim();
                Globals.lastName = l_name.getText().toString().trim();
                Globals.phone = edit_phone.getText().toString().trim();
                Globals.email = edit_email.getText().toString().trim();
                Globals.id = edit_id.getText().toString().trim();

                String address1 = selectedAddress.trim();

                int icount = 0;
                Boolean fname = Validation.validateName(Globals.firstName);
                Boolean lname = Validation.validateName(Globals.lastName);
                Boolean phone_no = Validation.validatePhoneNumber(Globals.phone);
                Boolean email_address = Validation.emailValidate(Globals.email);
                Boolean _id = Validation.validId(Globals.id);

                if (!fname) {
                    icount++;
                    f_name.setError("Invalid first name");
                }
                if (!lname) {
                    icount++;
                    l_name.setError("Invalid last name");
                }
                if (!phone_no) {
                    icount++;
                    edit_phone.setError("Invalid phone number");
                }
                if (!email_address) {
                    icount++;
                    edit_email.setError("Invalid email address");
                }
                if (!_id) {
                    icount++;
                    edit_id.setError("Invalid ID number");
                }
                if (address1.isEmpty()) {
                    icount++;
                    Toast.makeText(requireContext(), "Please select a valid address", Toast.LENGTH_SHORT).show();
                } else {
                    Globals.location = address1;
                }

                if (icount == 0) {
                    NavController navController = Navigation.findNavController(v);
                    navController.navigate(R.id.action_customerRegistration_to_passwordCreation);
                }
            }
        });

        return view;
    }
}