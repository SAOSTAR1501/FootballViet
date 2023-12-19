package group2.ptdacntt.footballviet.Models;

public class User {
    private String userId;
    private String email;
    private String password;
    private String username;
    private String fullName;
    private String address;
    private String role;
    private String phoneNumber;
    private String profileimage;


    public User(String userId, String email, String password, String username, String fullName, String address, String role, String phoneNumber) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.username = username;
        this.fullName = fullName;
        this.address = address;
        this.role = role;
        this.phoneNumber = phoneNumber;
    }



    public User() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }
}
