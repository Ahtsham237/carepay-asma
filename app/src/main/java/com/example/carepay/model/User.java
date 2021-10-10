
package com.example.carepay.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

@IgnoreExtraProperties
public class User implements Parcelable  {

    @SerializedName("name")
    @Expose
    private String name = "";
    @SerializedName("profilePicUrl")
    @Expose
    private String profilePicUrl = "";
    @SerializedName("lastSeen")
    @Expose
    private String lastSeen = "";
    @SerializedName("isTyping")
    @Expose
    private Boolean isTyping = false;
    @SerializedName("status")
    @Expose
    private String status = "";

    @SerializedName("uid")
    @Expose
    private String uid = "";
    @SerializedName("email")
    @Expose
    private String email = "";
    @SerializedName("number")
    @Expose
    private String number = "";
    @SerializedName("role")
    @Expose
    private String role = "";
    private String account_number = "";

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    HashMap<String,String> follower;
    HashMap<String,String> following;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public Boolean getIsTyping() {
        return isTyping;
    }

    public void setIsTyping(Boolean isTyping) {
        this.isTyping = isTyping;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.profilePicUrl);
        dest.writeString(this.lastSeen);
        dest.writeValue(this.isTyping);
        dest.writeString(this.status);
        dest.writeString(this.uid);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.name = in.readString();
        this.profilePicUrl = in.readString();
        this.lastSeen = in.readString();
        this.isTyping = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.status = in.readString();
        this.uid= in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public Boolean getTyping() {
        return isTyping;
    }

    public void setTyping(Boolean typing) {
        isTyping = typing;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public HashMap<String,String> getFollower() {
        return follower;
    }

    public void setFollower(HashMap<String,String> follower) {
        this.follower = follower;
    }

    public HashMap<String, String> getFollowing() {
        return following;
    }

    public void setFollowing(HashMap<String, String> following) {
        this.following = following;
    }
}
