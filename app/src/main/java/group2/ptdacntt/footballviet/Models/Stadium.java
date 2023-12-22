package group2.ptdacntt.footballviet.Models;

public class Stadium {
    private String stadiumId;
    private String stadiumName;
    private String address;
    private String price;
    private String image;

    public Stadium(String stadiumId, String stadiumName, String address, String price, String image) {
        this.stadiumId = stadiumId;
        this.stadiumName = stadiumName;
        this.address = address;
        this.price = price;
        this.image = image;
    }

    public Stadium() {
    }

    public String getStadiumId() {
        return stadiumId;
    }

    public void setStadiumId(String stadiumId) {
        this.stadiumId = stadiumId;
    }

    public String getStadiumName() {
        return stadiumName;
    }

    public void setStadiumName(String stadiumName) {
        this.stadiumName = stadiumName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
