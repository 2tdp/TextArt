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

    public String getDate_add() {
        return date_add;
    }

    public String getBucket() {
        return bucket;
    }

    public String getHeight() {
        return height;
    }

    public String getWidth() {
        return width;
    }

    public String getSize() {
        return size;
    }

    public String getPath() {
        return path;
    }

    public String getUri() {
        return uri;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
