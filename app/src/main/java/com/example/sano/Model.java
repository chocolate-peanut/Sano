package com.example.sano;

public class Model {

    private String Content;
    private String CreatedDate;

    public Model(){}
    public Model(String cContent, String createdDate){
        this.Content = Content;
        this.CreatedDate = createdDate;
    }

    public String getContent(){
        return Content;
    }

    public void setContent(String content){
        this.Content = content;
    }

    public String getCreatedDate(){
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate){
        this.CreatedDate = createdDate;
    }

}