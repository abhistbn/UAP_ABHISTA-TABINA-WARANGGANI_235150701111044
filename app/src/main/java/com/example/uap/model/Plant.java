package com.example.uap.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Plant implements Serializable {
    @SerializedName("id")
    private Integer id;

    @SerializedName("plant_name")
    private String name;

    @SerializedName("price")
    private String price;
    @SerializedName("description")
    private String description;

    private String image;

    public Plant(String name, String price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}