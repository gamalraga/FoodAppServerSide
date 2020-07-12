package gamal.myappnew.serverforclientapp.Moduel;

public class Rating {
    String userid;
    String foodid;
    String ratevalue;
    String commont;

    public Rating() {
    }

    public Rating(String userid, String foodid, String ratevalue, String commont) {
        this.userid = userid;
        this.foodid = foodid;
        this.ratevalue = ratevalue;
        this.commont = commont;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getFoodid() {
        return foodid;
    }

    public void setFoodid(String foodid) {
        this.foodid = foodid;
    }

    public String getRatevalue() {
        return ratevalue;
    }

    public void setRatevalue(String ratevalue) {
        this.ratevalue = ratevalue;
    }

    public String getCommont() {
        return commont;
    }

    public void setCommont(String commont) {
        this.commont = commont;
    }
}
