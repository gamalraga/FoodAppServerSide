package gamal.myappnew.serverforclientapp.Moduel;


import java.util.List;

public class Request {
    String request_id,phone,name,adress,total,status,imageurl,comment,paymentStatus,lat,lng, date;
   List<FoodofRequest> foods;

    public Request(String phone, String name, String adress, String total, List<FoodofRequest> foods,String imageurl,String comment,String paymentStatus,String lat,String lng ,String date){
        this.phone = phone;
        this.name = name;
        this.adress = adress;
        this.total = total;
       this.foods = foods;
       this.lat=lat;
       this.paymentStatus=paymentStatus;
       this.lng=lng;
       this.comment=comment;
       this.imageurl=imageurl;
        this.status="0";//default is 0:0:placed,1:shipping,2:shipped
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public Request() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<FoodofRequest> getFoods() {
        return foods;
    }

    public void setFoods(List<FoodofRequest> foods) {
        this.foods = foods;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

