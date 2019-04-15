package com.example.unistud.Helpers;

public class Internship {

    private String internshipTitle;
    private String internshipDesc;
    private String internshipDate;
    private String internshipImage;
    private String internshipCreatorId;

    public Internship() {

    }

    public Internship(String internshipTitle, String internshipDesc, String internshipDate, String internshipImage, String internshipCreatorId) {
        this.internshipTitle = internshipTitle;
        this.internshipDesc = internshipDesc;
        this.internshipDate = internshipDate;
        this.internshipImage = internshipImage;
        this.internshipCreatorId = internshipCreatorId;
    }

    public String getInternshipTitle() {
        return internshipTitle;
    }

    public void setInternshipTitle(String internshipTitle) {
        this.internshipTitle = internshipTitle;
    }

    public String getInternshipDesc() {
        return internshipDesc;
    }

    public void setInternshipDesc(String internshipDesc) {
        this.internshipDesc = internshipDesc;
    }

    public String getInternshipDate() {
        return internshipDate;
    }

    public void setInternshipDate(String internshipDate) {
        this.internshipDate = internshipDate;
    }

    public String getInternshipImage() {
        return internshipImage;
    }

    public void setInternshipImage(String internshipImage) {
        this.internshipImage = internshipImage;
    }

    public String getInternshipCreatorId() {
        return internshipCreatorId;
    }

    public void setInternshipCreatorId(String internshipCreatorId) {
        this.internshipCreatorId = internshipCreatorId;
    }
}
