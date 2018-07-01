package com.example.aleksandra.backpack;

import java.io.Serializable;

public class CommentModel implements Serializable {
    public String name;
    public String state;
    public String image;
    public String comment;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public CommentModel(String name, String state, String comment) {
        this.name = name;
        this.state = state;
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
