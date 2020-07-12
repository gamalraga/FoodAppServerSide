package gamal.myappnew.serverforclientapp.Moduel;

public class Order {

    public String FoodId;
    public String ImageFood;
    public String FoodName;
    public String DisCount;
    public String Price;
    public  String Quantity;

    public Order() {
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

    public Order(String foodId, String imageFood, String foodName, String disCount, String price, String quantity) {
        FoodId = foodId;
        ImageFood = imageFood;
        FoodName = foodName;
        DisCount = disCount;
        Price = price;
        Quantity = quantity;
    }

    @Override
    public String toString() {
        return "Order{" +
                "FoodId='" + FoodId + '\'' +
                ", ImageFood='" + ImageFood + '\'' +
                ", FoodName='" + FoodName + '\'' +
                ", DisCount='" + DisCount + '\'' +
                ", Price='" + Price + '\'' +
                ", Quantity='" + Quantity + '\'' +
                '}';
    }
}
