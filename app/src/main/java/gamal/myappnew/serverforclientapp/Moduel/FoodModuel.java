package gamal.myappnew.serverforclientapp.Moduel;

public class FoodModuel {
    private String Food, Image, Description, MenuId, FoodId,Discount;

String Price;
    public FoodModuel() {
    }

    public FoodModuel(String food, String image, String description, String menuId, String foodId, String discount, String price) {
        Food = food;
        Image = image;
        Description = description;
        MenuId = menuId;
        FoodId = foodId;
        Discount = discount;
        Price = price;
    }

    public String getFood() {
        return Food;
    }

    public void setFood(String food) {
        Food = food;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }

    public String getFoodId() {
        return FoodId;
    }

    public void setFoodId(String foodId) {
        FoodId = foodId;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}