package project.com2025; // Update with your package name

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    public ImageView productImage;
    public TextView productName;
    public TextView quantityText;
    public ImageButton minusButton;
    public ImageButton plusButton;
    public Button addToCartButton;

    public ProductViewHolder(View itemView) {
        super(itemView);
        productImage = itemView.findViewById(R.id.productImage);
        productName = itemView.findViewById(R.id.productName);
        quantityText = itemView.findViewById(R.id.quantityText);
        minusButton = itemView.findViewById(R.id.minusButton);
        plusButton = itemView.findViewById(R.id.plusButton);
        addToCartButton = itemView.findViewById(R.id.addToCartButton);
    }
}