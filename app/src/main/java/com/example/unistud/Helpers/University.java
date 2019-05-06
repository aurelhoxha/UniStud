package com.example.unistud.Helpers;

public class University {
    private String universityId;
    private String universityName;
    private String unversityCountry;

    public University(){

    }

    public University(String universityID, String universityName, String universityCountry){
        this.universityId = universityId;
        this.universityName = universityName;
        this.unversityCountry = universityCountry;
    }

    public String getUniversityID() {
        return universityId;
    }

    public void setUniversityID(String universityID) {
        this.universityId = universityID;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public String getUnversityCountry() {
        return unversityCountry;
    }

    public void setUnversityCountry(String unversityCountry) {
        this.unversityCountry = unversityCountry;
    }
}
