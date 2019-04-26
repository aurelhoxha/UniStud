package com.example.unistud.Helpers;

public class Tutorial {
    private String tutorialId;
    private String tutorialTitle;
    private String tutorialTopic;
    private String tutorialDate;
    private String tutorialURL;
    private String tutorialCreatorId;

    public Tutorial() {
    }

    public Tutorial(String tutorialId, String tutorialTitle, String tutorialTopic,  String tutorialDate, String tutorialURL, String tutorialCreatorId) {
        this.tutorialId = tutorialId;
        this.tutorialTitle = tutorialTitle;
        this.tutorialTopic = tutorialTopic;
        this.tutorialDate = tutorialDate;
        this.tutorialURL = tutorialURL;
        this.tutorialCreatorId = tutorialCreatorId;
    }

    public String getTutorialId() {
        return tutorialId;
    }

    public void setTutorialId(String tutorialId) {
        this.tutorialId = tutorialId;
    }

    public String getTutorialTitle() {
        return tutorialTitle;
    }

    public void setTutorialTitle(String tutorialTitle) {
        this.tutorialTitle = tutorialTitle;
    }

    public String getTutorialTopic() {
        return tutorialTopic;
    }

    public void setTutorialTopic(String tutorialTopic) {
        this.tutorialTopic= tutorialTopic;
    }

    public String getTutorialDate() {
        return tutorialDate;
    }

    public void setTutorialDate(String tutorialDate) {
        this.tutorialDate = tutorialDate;
    }

    public String getTutorialURL() {
        return tutorialURL;
    }

    public void setTutorialURL(String tutorialURL) {
        this.tutorialURL = tutorialURL;
    }

    public String getTutorialCreatorId() {
        return tutorialCreatorId;
    }

    public void setTutorialCreatorId(String tutorialCreatorId) {
        this.tutorialCreatorId = tutorialCreatorId;
    }
}
