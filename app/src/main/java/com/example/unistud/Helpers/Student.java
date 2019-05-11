package com.example.unistud.Helpers;

public class Student {

    //Variables
    private String accountId;
    private String account_type;
    private String birthday;
    private String email;
    private String fullname;
    private String gender;
    private String mobile_phone;
    private String profile_completed;
    private String profile_photo;
    private String university_country;
    private String university_name;

    public Student(){

    }

    public Student(String accountId, String account_type, String birthday, String email, String fullname, String gender, String mobile_phone, String profile_completed, String profile_photo,String university_country, String university_name) {
        this.accountId = accountId;
        this.account_type = account_type;
        this.birthday = birthday;
        this.email = email;
        this.fullname = fullname;
        this.gender = gender;
        this.mobile_phone = mobile_phone;
        this.profile_completed = profile_completed;
        this.profile_photo = profile_photo;
        this.university_country = university_country;
        this.university_name = university_name;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
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

    public String getUniversity_country() {
        return university_country;
    }

    public void setUniversity_country(String university_country) {
        this.university_country = university_country;
    }

    public String getUniversity_name() {
        return university_name;
    }

    public void setUniversity_name(String university_name) {
        this.university_name = university_name;
    }
}
