package project.com2025;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Product> cartItems;
    private CartItemListener listener;

    public interface CartItemListener {
        void onQuantityChanged(int position, int newQuantity);
        void onItemRemoved(int position);
    }

    public CartAdapter(List<Product> cartItems, CartItemListener listener) {
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = cartItems.get(position);

        holder.productImage.setImageResource(product.getImageResId());
        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.format("$%.2f", product.getPrice()));
        holder.quantityText.setText(String.valueOf(product.getQuantity()));

        holder.minusButton.setOnClickListener(v -> {
            int newQty = product.getQuantity() - 1;
            if (newQty >= 0) {
                product.setQuantity(newQty);
                holder.quantityText.setText(String.valueOf(newQty));
                listener.onQuantityChanged(position, newQty);
            }
        });

        holder.plusButton.setOnClickListener(v -> {
            int newQty = product.getQuantity() + 1;
            product.setQuantity(newQty);
            holder.quantityText.setText(String.valueOf(newQty));
            listener.onQuantityChanged(position, newQty);
        });

        holder.removeButton.setOnClickListener(v -> {
            listener.onItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productPrice;
        TextView quantityText;
        ImageButton minusButton;
        ImageButton plusButton;
        Button removeButton;

        public CartViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.cartProductImage);
            productName = itemView.findViewById(R.id.cartProductName);
            productPrice = itemView.findViewById(R.id.cartProductPrice);
            quantityText = itemView.findViewById(R.id.cartQuantityText);
            minusButton = itemView.findViewById(R.id.cartMinusButton);
            plusButton = itemView.findViewById(R.id.cartPlusButton);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }
}
