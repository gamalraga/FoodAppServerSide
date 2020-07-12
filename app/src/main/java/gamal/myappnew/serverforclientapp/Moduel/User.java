package gamal.myappnew.serverforclientapp.Moduel;

public class User {
    String username,bio,imageurl,phone,id;
    boolean IsStaff;

    public User(String username, String bio, String imageurl, String phone, String id,boolean IsStaff) {
        this.username = username;
        this.bio = bio;
        this.imageurl = imageurl;
        this.phone = phone;
        this.id = id;
        this.IsStaff=IsStaff;
    }

    public User() {
    }

    public boolean getisStaff() {
        return IsStaff;
    }

    public void setStaff(boolean staff) {
        IsStaff = staff;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
