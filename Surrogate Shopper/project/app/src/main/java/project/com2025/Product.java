package project.com2025;

public class Product {
    private String id;          // Product ID like "ACC200"
    private String name;        // Product name
    private int imageResId;     // Drawable resource ID
    private double price;       // Product price
    private int quantity;       // Current selected quantity (starts at 0)
    private int stockQuantity;  // Maximum available stock

    public Product(String id, String name, int imageResId, double price, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.imageResId = imageResId;
        this.price = price;
        this.quantity = 0;          // Always start at 0
        this.stockQuantity = stockQuantity;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public int getImageResId() { return imageResId; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public int getStockQuantity() { return stockQuantity; }

    // Setters
    public void setQuantity(int quantity) {
        // Ensure quantity stays within bounds
        this.quantity = Math.max(0, Math.min(quantity, stockQuantity));
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
        // Ensure current quantity doesn't exceed new stock
        if (this.quantity > stockQuantity) {
            this.quantity = stockQuantity;
        }
    }

    // Helper method to increase quantity
    public boolean increaseQuantity() {
        if (quantity < stockQuantity) {
            quantity++;
            return true;
        }
        return false;
    }

    // Helper method to decrease quantity
    public boolean decreaseQuantity() {
        if (quantity > 0) {
            quantity--;
            return true;
        }
        return false;
    }

    public double getTotalPrice() {
        return quantity * price;
    }
    public Product(Product other) {
        this.id = other.id;
        this.name = other.name;
        this.imageResId = other.imageResId;
        this.price = other.price;
        this.quantity = other.quantity;
        this.stockQuantity = other.stockQuantity;
    }
}