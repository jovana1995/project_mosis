package com.example.aleksandra.backpack.models;

import java.io.Serializable;

public class PlaceModel implements Serializable {
    public String comment;
    public String placeName;
    public String placeLocation;
    public String placeState;
    public String placePhone;
    public String placeImage;
    public String timePassed;

    public PlaceModel(String comment, String placeName, String placeLocation, String placeState, String placePhone, String placeImage, String timePassed) {
        this.comment = comment;
        this.placeName = placeName;
        this.placeLocation = placeLocation;
        this.placeState = placeState;
        this.placePhone = placePhone;
        this.placeImage = placeImage;
        this.timePassed = timePassed;
    }

    public String getPlaceState() {
        return placeState;
    }

    public void setPlaceState(String placeState) {
        this.placeState = placeState;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceLocation() {
        return placeLocation;
    }

    public void setPlaceLocation(String placeLocation) {
        this.placeLocation = placeLocation;
    }

    public String getPlacePhone() {
        return placePhone;
    }

    public void setPlacePhone(String placePhone) {
        this.placePhone = placePhone;
    }

    public String getPlaceImage() {
        return placeImage;
    }

    public void setPlaceImage(String placeImage) {
        this.placeImage = placeImage;
    }

    public String getTimePassed() {
        return timePassed;
    }

    public void setTimePassed(String timePassed) {
        this.timePassed = timePassed;
    }
}
