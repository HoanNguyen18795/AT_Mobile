package com.testing.hoan.at_mobile;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hoan on 10/28/2016.
 */
public class Events implements Parcelable {
    private String id;
    private String title;
    private String imageUrl;
    private String body;
    public Events(){

    }

    protected Events(Parcel in) {
        id = in.readString();
        title = in.readString();
        imageUrl = in.readString();
        body=in.readString();
    }

    public static final Creator<Events> CREATOR = new Creator<Events>() {
        @Override
        public Events createFromParcel(Parcel in) {
            return new Events(in);
        }

        @Override
        public Events[] newArray(int size) {
            return new Events[size];
        }
    };

    private boolean isValid(String params){
        if(params==null){
            return false;
        }
        return true;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public void setBody(String newBody){
        body=newBody;
    }
    public String getBody(){
        return body;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(imageUrl);
        dest.writeString(body);
    }
}
