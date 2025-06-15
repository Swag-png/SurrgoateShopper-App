package project.com2025;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {
    private Context context;
    private List<Product> productList;
    private OnQuantityChangeListener quantityListener;
    private OnAddToCartListener cartListener;

    public interface OnQuantityChangeListener {
        void onQuantityChanged(String productId, int newQuantity);
    }

    public interface OnAddToCartListener {
        void onAddToCart(String productId);
    }

    public ProductAdapter(Context context, List<Product> products, OnQuantityChangeListener qListener, OnAddToCartListener cListener) {
        this.context = context;
        this.productList = products;
        this.quantityListener = qListener;
        this.cartListener = cListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        // Set product data
        holder.productImage.setImageResource(product.getImageResId());
        holder.productName.setText(product.getName());
        holder.quantityText.setText(String.valueOf(product.getQuantity()));

        // Minus button click
        holder.minusButton.setOnClickListener(v -> {
            int newQty = product.getQuantity() - 1;
            if (newQty >= 0) {
                product.setQuantity(newQty);
                holder.quantityText.setText(String.valueOf(newQty));
                quantityListener.onQuantityChanged(product.getId(), newQty);
            }
        });

        // Plus button click
        holder.plusButton.setOnClickListener(v -> {
            int newQty = product.getQuantity() + 1;
            if (newQty <= product.getStockQuantity()) {
                product.setQuantity(newQty);
                holder.quantityText.setText(String.valueOf(newQty));
                quantityListener.onQuantityChanged(product.getId(), newQty);
            } else {
                Toast.makeText(context, "Cannot exceed available stock", Toast.LENGTH_SHORT).show();
            }
        });

        // Add to cart button
        holder.addToCartButton.setOnClickListener(v -> {
            cartListener.onAddToCart(product.getId());
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
