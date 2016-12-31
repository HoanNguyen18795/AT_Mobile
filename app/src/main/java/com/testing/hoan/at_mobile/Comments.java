package com.testing.hoan.at_mobile;

/**
 * Created by Hoan on 12/29/2016.
 */

public class Comments {
    private String userName;
    private String comment;

    public void setUserName(String newUsername){
        userName=newUsername;
    }
    public String getUserName(){
        return userName;
    }
    public void setComment(String newComment){
        comment=newComment;
    }
    public String getComment(){
        return comment;
    }
}
