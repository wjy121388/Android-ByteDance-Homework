package com.bytedance.android.lesson.restapi.solution.bean;

import java.util.List;

/**
 * @author Xavier.S
 * @date 2019.01.20 14:17
 */
public class FeedResponse {

    // TODO-C2 (2) Implement your FeedResponse Bean here according to the response json
    private List<Feed> feeds;

    private boolean success;

    public void setFeeds(List<Feed> feeds){
        this.feeds = feeds;
    }
    public List<Feed> getFeeds(){
        return this.feeds;
    }
    public void setSuccess(boolean success){
        this.success = success;
    }
    public boolean getSuccess(){
        return this.success;
    }

}
