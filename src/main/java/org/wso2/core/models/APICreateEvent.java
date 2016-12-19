package org.wso2.core.models;

/**
 * Created by maanadev on 12/15/16.
 */
public class APICreateEvent {



    private String apiName;
    private String user;
    private String [] subscribers;

    public void setSubscribers(String[] subscribers) {
        this.subscribers = subscribers;
    }

    public String[] getSubscribers() {
        return subscribers;
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
        String apim = "Api Name: "+ apiName+"\nUser: "+user+"\nsubscribers: ";
        for (String sub:subscribers) {
            apim+=sub+" ";
        }
        return apim;
    }

    public APICreateEvent(){}

}
