package org.wso2.example;

import org.wso2.core.Context;
import org.wso2.core.RequestHandlerGeneric;
import org.wso2.core.models.APICreateEvent;

/**
 * Created by maanadev on 12/19/16.
 */
public class Customer implements RequestHandlerGeneric<APICreateEvent,Integer> {
    @Override
    public Integer handleRequest(Context context,APICreateEvent input) {
        System.out.println(input.toString());
        return null;
    }
}
