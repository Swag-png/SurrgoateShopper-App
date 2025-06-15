package project.com2025;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.activity.OnBackPressedCallback;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link volunteer_reg#newInstance} factory method to
 * create an instance of this fragment.
 */
public class volunteer_reg extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public volunteer_reg() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment volunteer_reg.
     */
    // TODO: Rename and change types and number of parameters
    public static volunteer_reg newInstance(String param1, String param2) {
        volunteer_reg fragment = new volunteer_reg();
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
                EditText editText1 = view.findViewById(R.id.edit_name_volunteer);
                EditText editText2 = view.findViewById(R.id.edit_surname_volunteer);
                EditText editText3 = view.findViewById(R.id.edit_phone_volunteer);
                EditText editText4 = view.findViewById(R.id.edit_email_volunteer);
                EditText editText5 = view.findViewById(R.id.edit_id_volunteer);

                editText1.setText("");
                editText2.setText("");
                editText3.setText("");
                editText4.setText("");
                editText5.setText("");

                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_volunteer_reg_to_home_page);
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout and store the view
        View view = inflater.inflate(R.layout.volunteer_reg, container, false);

        // Find your button by ID
        Button registerButton = view.findViewById(R.id.next_volunteer); // Replace with your actual ID

        EditText f_name = view.findViewById(R.id.edit_name_volunteer);
        EditText l_name = view.findViewById(R.id.edit_surname_volunteer);
        EditText edit_phone = view.findViewById(R.id.edit_phone_volunteer);
        EditText edit_email = view.findViewById(R.id.edit_email_volunteer);
        EditText edit_id = view.findViewById(R.id.edit_id_volunteer);

        f_name.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        l_name.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        // Set up the onClick listener
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // What happens when the button is clicked
                Globals.firstName = f_name.getText().toString().trim();
                Globals.lastName = l_name.getText().toString().trim();
                Globals.phone = edit_phone.getText().toString().trim();
                Globals.email = edit_email.getText().toString().trim();
                Globals.id = edit_id.getText().toString().trim();

                int icount=0;
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

                if(icount==0){
                    NavController navController = Navigation.findNavController(v);
                    navController.navigate(R.id.action_volunteer_reg_to_volunteer_password);
                }
            }
        });

        return view;
    }
}