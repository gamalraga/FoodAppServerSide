package gamal.myappnew.serverforclientapp.Moduel;

public class CategoryModuel {
    String menuid,name,image;

    public CategoryModuel(String menuid, String name, String image) {
        this.menuid = menuid;
        this.name = name;
        this.image = image;
    }

    public CategoryModuel() {
    }

    public String getMenuid() {
        return menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
