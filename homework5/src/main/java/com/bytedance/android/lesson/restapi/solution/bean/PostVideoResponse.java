package com.bytedance.android.lesson.restapi.solution.bean;

/**
 * @author Xavier.S
 * @date 2019.01.18 17:53
 */
public class PostVideoResponse {

    // TODO-C2 (3) Implement your PostVideoResponse Bean here according to the response json
    private boolean success;

    private Item result;

    private String error;


    public void setSuccess(boolean success){
        this.success = success;
    }
    public boolean getSuccess(){
        return this.success;
    }
    public void setItem(Item item){
        this.result = item;
    }
    public Item getItem(){
        return this.result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public  class Item
    {
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


}

