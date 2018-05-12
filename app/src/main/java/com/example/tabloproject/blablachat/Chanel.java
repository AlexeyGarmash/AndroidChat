package com.example.tabloproject.blablachat;

/**
 * Created by Tablo Project on 09.04.2018.
 */

public class Chanel {
    private String name;
    private String type;
    private int imageId;



    public Chanel(String name, String type, int imageId){
        this.name = name;
        this.type = type;
        this.imageId = imageId;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
