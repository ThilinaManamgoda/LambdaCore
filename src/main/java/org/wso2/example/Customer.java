package org.wso2.example;

import org.wso2.core.Context;
import org.wso2.core.RequestHandler;
import org.wso2.core.models.APICreateEvent;

/**
 * Created by maanadev on 12/19/16.
 */
public class Customer implements RequestHandler<APICreateEvent,CustomResponse> {
    @Override
    public CustomResponse handleRequest(Context context, APICreateEvent input) {
        System.out.println("This is from interface: \n"+input.toString());
        return null;
    }



    public CustomResponse handleRequestMyWay(Context context, APICreateEvent input){
        System.out.println("This is from custom func: \n"+ input.toString());
        return null;
    }
}
