package com.example.uap.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PlantResponse {

    @SerializedName("data")
    private List<Plant> data;

    @SerializedName("message")
    private String message;

    public List<Plant> getData() {
        return data;
    }

    public void setData(List<Plant> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}