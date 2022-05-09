package com.datnt.textart.model;

import java.io.Serializable;

public class PicModel implements Serializable {

    private String id;
    private String title;
    private String date_add;
    private String bucket;
    private String height;
    private String width;
    private String size;
    private String path;
    private String uri;
    private boolean isCheck;

    public PicModel(String id, String title, String date_add, String bucket, String height, String width, String size, String path, String uri, boolean isCheck) {
        this.id = id;
        this.title = title;
        this.date_add = date_add;
        this.bucket = bucket;
        this.height = height;
        this.width = width;
        this.size = size;
        this.path = path;
        this.uri = uri;
        this.isCheck = isCheck;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate_add() {
        return date_add;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public void setDate_add(String date_add) {
        this.date_add = date_add;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
