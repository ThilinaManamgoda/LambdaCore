package org.wso2.core.models;

/**
 * Created by maanadev on 12/15/16.
 */
public class APICreateEvent {



    private String apiName;
    private String user;
    private String [] subscribers;

    public void setSubscribers(String[] subscribers) {
        this.subscribers = subscribers.clone();
    }

    public String[] getSubscribers() {
        return subscribers.clone();
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getApiName() {
        return apiName;
    }

    public String getUser() {
        return user;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer("Api Name: "+ apiName+"\nUser: "+user+"\nsubscribers: ");
        for (String sub:subscribers) {
            buffer.append(sub+" ");
        }
        return buffer.toString();
    }

    public APICreateEvent(){}

}
