package deliveryapp.saalventure.madrasmarket.Model;


public class My_order_detail_model {

    String sale_item_id;
    String sale_id;
    String product_id;
    String product_name;
    String qty;
    String unit;
    String unit_value;
    String price;
    String qty_in_kg;
    String product_image;

    public String getSale_item_id() {
        return sale_item_id;
    }

    public String getSale_id() {
        return sale_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getQty() {
        return qty;
    }

    public String getUnit() {
        return unit;
    }

    public String getUnit_value() {
        return unit_value;
    }

    public String getPrice() {
        return price;
    }

    public String getQty_in_kg() {
        return qty_in_kg;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setSale_item_id(String sale_item_id) {
        this.sale_item_id = sale_item_id;
    }

    public void setSale_id(String sale_id) {
        this.sale_id = sale_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setUnit_value(String unit_value) {
        this.unit_value = unit_value;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setQty_in_kg(String qty_in_kg) {
        this.qty_in_kg = qty_in_kg;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }
}
