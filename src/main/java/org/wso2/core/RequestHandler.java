package org.wso2.core;

/**
 * Created by maanadev on 12/19/16.
 */
public interface RequestHandler<I,O> {

    public O handleRequest(Context context,I input);
}
