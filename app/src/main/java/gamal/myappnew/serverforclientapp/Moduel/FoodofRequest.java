package gamal.myappnew.serverforclientapp.Moduel;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class FoodofRequest {

    private int uid;
    private String FoodId;
    private String ImageFood;
    private String FoodName;
    private String DisCount;
    private String Price;
    private   String Quantity;

    public FoodofRequest() {
    }

    public FoodofRequest(int uid, String foodId, String imageFood, String foodName, String disCount, String price, String quantity) {
        this.uid = uid;
        FoodId = foodId;
        ImageFood = imageFood;
        FoodName = foodName;
        DisCount = disCount;
        Price = price;
        Quantity = quantity;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getFoodId() {
        return FoodId;
    }

    public void setFoodId(String foodId) {
        FoodId = foodId;
    }

    public String getImageFood() {
        return ImageFood;
    }

    public void setImageFood(String imageFood) {
        ImageFood = imageFood;
    }

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        FoodName = foodName;
    }

    public String getDisCount() {
        return DisCount;
    }

    public void setDisCount(String disCount) {
        DisCount = disCount;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }
}
