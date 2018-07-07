package com.example.aleksandra.backpack.models;

import java.io.Serializable;

public class EventModel implements Serializable {
    public String when;
    public String where;
    public String personImage;
    public String comment;
    public String name;
    public String state;
    public String placeName;
    public String placeLocation;
    public String timePassed;

    public EventModel(String when, String where, String personImage, String comment, String name, String state, String placeName, String placeLocation, String timePassed) {
        this.when = when;
        this.where = where;
        this.personImage = personImage;
        this.comment = comment;
        this.name = name;
        this.state = state;
        this.placeName = placeName;
        this.placeLocation = placeLocation;
        this.timePassed = timePassed;
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

    public String getTimePassed() {
        return timePassed;
    }

    public void setTimePassed(String timePassed) {
        this.timePassed = timePassed;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getPersonImage() {
        return personImage;
    }

    public void setPersonImage(String personImage) {
        this.personImage = personImage;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
