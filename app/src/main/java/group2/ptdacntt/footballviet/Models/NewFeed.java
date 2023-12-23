package group2.ptdacntt.footballviet.Models;

public class NewFeed {
    private String id;
    private String name;
    private String san;
    private String ngay;
    private String gio;
    private String content;
    private String image;
    private String email;

    public NewFeed(String id, String name, String san, String ngay, String gio, String content, String image, String email) {
        this.id = id;
        this.name = name;
        this.san = san;
        this.ngay = ngay;
        this.gio = gio;
        this.content = content;
        this.image = image;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public NewFeed(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSan() {
        return san;
    }

    public void setSan(String san) {
        this.san = san;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public String getGio() {
        return gio;
    }

    public void setGio(String gio) {
        this.gio = gio;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
