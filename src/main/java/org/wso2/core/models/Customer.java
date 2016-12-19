package org.wso2.core.models;

import org.wso2.core.Context;
import org.wso2.core.RequestHandlerGeneric;

/**
 * Created by maanadev on 12/19/16.
 */
public class Customer implements RequestHandlerGeneric<Input,Integer> {
    @Override
    public Integer handleRequest(Context context,Input input) {
        System.out.println(input.getNum());
        return null;
    }
}
