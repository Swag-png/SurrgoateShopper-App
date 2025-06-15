package project.com2025;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements CartAdapter.CartItemListener {

    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private TextView totalPriceText;
    private List<Product> cartItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);

        // Initialize views
        recyclerView = findViewById(R.id.cartRecyclerView);
        totalPriceText = findViewById(R.id.totalPriceText);
        Button checkoutButton = findViewById(R.id.checkoutButton);

        // Get cart items from intent or shared preferences
        cartItems = getCartItems(); // Implement this method based on your storage

        // Setup RecyclerView
        adapter = new CartAdapter(cartItems, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Update total price
        updateTotalPrice();

        // Checkout button
        checkoutButton.setOnClickListener(v -> checkout());
    }

    private List<Product> getCartItems() {
        // Implement your logic to get cart items
        // This could be from SharedPreferences, Database, or Intent
        List<Product> items = new ArrayList<>();
        // Add sample items for testing
        items.add(new Product("ACC200", "ACC200", R.drawable.acc200, 120.00, 20));
        items.add(new Product("PANADO", "Panado", R.drawable.panado, 40.00, 20));
        return items;
    }

    private void updateTotalPrice() {
        double total = 0;
        for (Product item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
        totalPriceText.setText(format.format(total));
    }

    private void checkout() {
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Implement your checkout logic
        Toast.makeText(this, "Checkout successful!", Toast.LENGTH_SHORT).show();

        // Clear cart after checkout
        cartItems.clear();
        adapter.notifyDataSetChanged();
        updateTotalPrice();
    }

    @Override
    public void onQuantityChanged(int position, int newQuantity) {
        Product product = cartItems.get(position);
        product.setQuantity(newQuantity);
        updateTotalPrice();
    }

    @Override
    public void onItemRemoved(int position) {
        cartItems.remove(position);
        adapter.notifyItemRemoved(position);
        updateTotalPrice();
    }
}