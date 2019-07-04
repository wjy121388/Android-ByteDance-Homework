package com.bytedance.android.lesson.restapi.solution.bean;

/**
 * @author Xavier.S
 * @date 2019.01.20 14:18
 */
public class Feed {

    // TODO-C2 (1) Implement your Feed Bean here according to the response json
    private String student_id;

    private String user_name;

    private String image_url;

    private String video_url;

    public void setStudent_id(String student_id){
        this.student_id = student_id;
    }
    public String getStudent_id(){
        return this.student_id;
    }
    public void setUser_name(String user_name){
        this.user_name = user_name;
    }
    public String getUser_name(){
        return this.user_name;
    }
    public void setImage_url(String image_url){
        this.image_url = image_url;
    }
    public String getImage_url(){
        return this.image_url;
    }
    public void setVideo_url(String video_url){
        this.video_url = video_url;
    }
    public String getVideo_url(){
        return this.video_url;
    }
}
