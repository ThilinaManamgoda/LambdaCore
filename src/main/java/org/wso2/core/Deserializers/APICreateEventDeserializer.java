package org.wso2.core.Deserializers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;
import org.wso2.core.models.APICreateEvent;

/**
 * Created by maanadev on 12/16/16.
 */
public class APICreateEventDeserializer implements Deserializer<APICreateEvent> {

    final private static String API_NAME = "apiName";
    final private static String USER_NAME = "user";
    final private static String SUBSCRIBERS = "subscribers";

    final static Logger logger = Logger.getLogger(APICreateEventDeserializer.class);

    @Override
    public APICreateEvent deserialize(Object input) {

        JsonElement jsonElement = (JsonElement)input;

        JsonObject jsonPayload = jsonElement.getAsJsonObject();

        APICreateEvent API_createEvent = new APICreateEvent();

        API_createEvent.setApiName(jsonPayload.get(API_NAME).getAsString());
        API_createEvent.setUser(jsonPayload.get(USER_NAME).getAsString());
        JsonArray subsArray = jsonPayload.get(SUBSCRIBERS).getAsJsonArray();

        int arrayLength =subsArray.size();

        String [] array=new String[arrayLength];

        for (int count=0;count<arrayLength;count++) {
            array[count] = subsArray.get(count).getAsString();
        }

        API_createEvent.setSubscribers(array);

        return API_createEvent;
    }
}
