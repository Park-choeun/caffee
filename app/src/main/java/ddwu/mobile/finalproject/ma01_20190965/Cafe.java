package ddwu.mobile.finalproject.ma01_20190965;

import java.io.Serializable;

public class Cafe implements Serializable {
    private long _id;
    private String name;
    private String address;
    private String phone;
    private String review;
    private String rating;
    private String type;
    private String img;
    private double lat;
    private double lng;
    private String placeId;

    public Cafe(long _id, String name, String address, String phone, String review, String rating, String type, double lat, double lng, String placeId) {
        this._id = _id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.review = review;
        this.rating = rating;
        this.type = type;
        this.lat = lat;
        this.lng = lng;
        this.placeId = placeId;
    }

    public Cafe(long _id, String name, String address, String phone, String review, String rating, String type, String img, double lat, double lng, String placeId) {
        this._id = _id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.review = review;
        this.rating = rating;
        this.type = type;
        this.img = img;
        this.lat = lat;
        this.lng = lng;
        this.placeId = placeId;
    }

    public Cafe(String name, String address, String phone, String review, String rating, String type, String img, double lat, double lng, String placeId) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.review = review;
        this.rating = rating;
        this.type = type;
        this.img = img;
        this.lat = lat;
        this.lng = lng;
        this.placeId = placeId;
    }

    public long get_id() { return _id; }

    public void set_id(long _id) { this._id = _id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public String getReview() { return review; }

    public void setReview(String review) { this.review = review; }

    public String getRating() { return rating; }

    public void setRating(String rating) { this.rating = rating; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getImg() { return img; }

    public void setImg(String img) { this.img = img; }

    public double getLat() { return lat; }

    public void setLat(double lat) { this.lat = lat; }

    public double getLng() { return lng; }

    public void setLng(double lng) { this.lng = lng; }

    public String getPlaceId() { return placeId; }

    public void setPlaceId(String placeId) { this.placeId = placeId; }
}
