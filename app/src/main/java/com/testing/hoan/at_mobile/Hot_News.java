package com.testing.hoan.at_mobile;

/**
 * Created by Hoan on 12/2/2016.
 */
public class Hot_News {
    private String imageUrl;
    private String title;
    public Hot_News(){

    }
    public void setImageUrl(String url){
        this.imageUrl=url;
    }
    public String getImageUrl(){
        return imageUrl;
    }
    public void setTitle(String title){
        this.title=title;
    }
    public String getTitle(){
        return this.title;
    }
}
