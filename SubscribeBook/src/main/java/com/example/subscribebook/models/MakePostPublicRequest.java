package com.example.subscribebook.models;

public class MakePostPublicRequest {

    Integer urlId;

    public MakePostPublicRequest() {
    }

    public MakePostPublicRequest(Integer urlId) {
        this.urlId = urlId;
    }

    public Integer getUrlId() {
        return urlId;
    }

    public void setUrlId(Integer urlId) {
        this.urlId = urlId;
    }
}
