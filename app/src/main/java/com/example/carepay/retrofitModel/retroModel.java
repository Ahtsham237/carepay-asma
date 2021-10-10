package com.example.carepay.retrofitModel;
import com.google.gson.annotations.SerializedName;

public class retroModel {
    @SerializedName("error")
    boolean error;
    @SerializedName("error_msg")
    String error_msg;


    public boolean isError() {
        return error;
    }
    public String getError_msg() {
        return error_msg;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }


}
