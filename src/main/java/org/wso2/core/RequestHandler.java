package org.wso2.core;

import com.google.gson.JsonElement;

/**
 * Created by maanadev on 12/15/16.
 */
public interface RequestHandler{

    public Object handleRequest(Context context, EventDeserializersManager deserializer, JsonElement input);
}
