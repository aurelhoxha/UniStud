package com.example.unistud.Helpers;

public class SavedObject
{
    private String objectId;
    private String objectTitle;


    public SavedObject(){
    }


    public SavedObject(String id, String title){
        this.objectId = id;
        this.objectTitle = title;
    }

    public String getObjectTitle() {
        return objectTitle;
    }

    public void setObjectTitle(String objectTitle) {
        this.objectTitle = objectTitle;
    }

    public void setObjectId(String id){
        this.objectId = id;
    }

    public String getObjectId(){
        return objectId;
    }
}
