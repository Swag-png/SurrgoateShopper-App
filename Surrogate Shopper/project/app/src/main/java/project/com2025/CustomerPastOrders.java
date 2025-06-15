package project.com2025;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class CustomerPastOrders extends Fragment {

    private Spinner spinner;
    private boolean isFirstSelection = true;

    public CustomerPastOrders() {
        // Required empty public constructor
    }

    public static CustomerPastOrders newInstance() {
        return new CustomerPastOrders();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate your layout (make sure the filename matches exactly)
        View view = inflater.inflate(R.layout.customer_past_orders, container, false);

        spinner = view.findViewById(R.id.droppers);

        // Sample data - replace this with your actual data later
        final List<String> orders = new ArrayList<>();
        orders.add("Order 1");
        orders.add("Order 2");
        orders.add("Order 3");

        // Add a dummy first item to act as the hint
        orders.add(0, "Select Order");

        // Create adapter with the orders list
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                orders
        ) {
            @Override
            public boolean isEnabled(int position) {
                // Disable the first item ("Select Order") to prevent selection
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Gray out the "Select Order" hint in dropdown
                    tv.setTextColor(0xFF888888); // gray color
                } else {
                    tv.setTextColor(0xFF000000); // black color
                }
                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setSelection(0); // Show the hint initially

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View selectedView, int position, long id) {
                if (position == 0) {
                    // Hint selected, do nothing
                    return;
                }
                String selectedOrder = orders.get(position);
                Toast.makeText(requireContext(), "Selected: " + selectedOrder, Toast.LENGTH_SHORT).show();

                // TODO: Handle showing details about the selected order here
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });

        return view;
    }
}
