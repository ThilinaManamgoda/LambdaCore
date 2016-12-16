package org.wso2.core;

import com.google.gson.JsonElement;
import org.wso2.core.models.APIM_Create;

/**
 * Created by maanadev on 12/16/16.
 */
public class CustomerImpl implements RequestHandler {

    @Override
    public Object handleRequest(Context context, EventDeserializersManager deserializer, JsonElement input) {

        System.out.println(deserializer.deserialize(APIM_Create.class,input));
        return null;
    }
}
