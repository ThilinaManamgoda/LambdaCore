package org.wso2.core.Deserializers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;
import org.wso2.core.models.APIM_Create;

/**
 * Created by maanadev on 12/16/16.
 */
public class APIM_Deserializer implements Deserializer<APIM_Create> {
    final private static String API_NAME = "apiName";
    final private static String USER_NAME = "user";
    final private static String SUBSCRIBERS = "subscribers";
    final static Logger logger = Logger.getLogger(APIM_Deserializer.class);

    @Override
    public APIM_Create deserialize(Object input) {

        JsonElement jsonElement = (JsonElement)input;

        JsonObject jsonPayload = jsonElement.getAsJsonObject();

        APIM_Create apim_create = new APIM_Create();

        apim_create.setApiName(jsonPayload.get(API_NAME).getAsString());
        apim_create.setUser(jsonPayload.get(USER_NAME).getAsString());
        JsonArray subsArray = jsonPayload.get(SUBSCRIBERS).getAsJsonArray();

        int arrayLength =subsArray.size();

        String [] array=new String[arrayLength];

        for (int count=0;count<arrayLength;count++) {
            array[count] = subsArray.get(count).getAsString();
        }

        apim_create.setSubscribers(array);

        return apim_create;
    }
}
