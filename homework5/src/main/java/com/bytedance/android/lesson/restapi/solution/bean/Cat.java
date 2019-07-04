package com.bytedance.android.lesson.restapi.solution.bean;

import java.util.List;

/**
 * @author Xavier.S
 * @date 2019.01.17 18:08
 */
public class Cat {

    // TODO-C1 (1) Implement your Cat Bean here according to the response json
    private List<String> breeds;

    private String id;

    private String url;

    private int width;

    private int height;

    public void setBreeds(List<String> breeds){
        this.breeds = breeds;
    }
    public List<String> getBreeds(){
        return this.breeds;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public String getUrl(){
        return this.url;
    }
    public void setWidth(int width){
        this.width = width;
    }
    public int getWidth(){
        return this.width;
    }
    public void setHeight(int height){
        this.height = height;
    }
    public int getHeight(){
        return this.height;
    }

}
