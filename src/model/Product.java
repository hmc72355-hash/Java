package model;

public class Product {

    private int id;
    private String name;
    private double price;
    private int quantity;

    public Product() {
    }

    // สำหรับ INSERT (ไม่ต้องส่ง id เพราะ AUTO_INCREMENT)
    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // สำหรับ UPDATE / SELECT
    public Product(int id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // ===== Getter / Setter =====

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "ID : " + id +
                ", ชื่อสินค้า : " + name +
                ", ราคา : " + price +
                ", จำนวน : " + quantity;
    }
}
