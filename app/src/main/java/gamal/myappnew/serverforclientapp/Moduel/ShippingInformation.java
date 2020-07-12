package gamal.myappnew.serverforclientapp.Moduel;

public class ShippingInformation {
    private String orderId,shipperPhone,name;
    private Double lat,lng;

    public ShippingInformation(String orderId, String shipperPhone, Double lat, Double lng, String name) {
        this.orderId = orderId;
        this.shipperPhone = shipperPhone;
        this.lat = lat;
        this.lng = lng;
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ShippingInformation() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getShipperPhone() {
        return shipperPhone;
    }

    public void setShipperPhone(String shipperPhone) {
        this.shipperPhone = shipperPhone;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
