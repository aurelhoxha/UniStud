package com.example.unistud;

public class Organization {

    //Variables
    private String account_type;
    private String description;
    private String domain;
    private String email;
    private String fullname;
    private String location;
    private String profile_completed;
    private String profile_photo;

    public Organization() {

    }

    public Organization(String account_type, String description, String domain, String email, String fullname, String location, String profile_completed, String profile_photo) {
        this.account_type = account_type;
        this.description = description;
        this.domain = domain;
        this.email = email;
        this.fullname = fullname;
        this.location = location;
        this.profile_completed = profile_completed;
        this.profile_photo = profile_photo;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfile_completed() {
        return profile_completed;
    }

    public void setProfile_completed(String profile_completed) {
        this.profile_completed = profile_completed;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }
}
