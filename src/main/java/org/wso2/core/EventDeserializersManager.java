package org.wso2.core;

import org.apache.log4j.Logger;
import org.wso2.core.Deserializers.Deserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by maanadev on 12/16/16.
 */
public class EventDeserializersManager {

    final static Logger logger = Logger.getLogger(EventDeserializersManager.class);

    private final  Map<Class, Deserializer> deserializers;

    public EventDeserializersManager() {
        deserializers = new HashMap<>();
    }

    /**
     *
     * @param tClass
     * @param deserializer
     */

    public void registerDeserializer(Class tClass, Deserializer deserializer) {
        deserializers.put(tClass, deserializer);
        logger.info(tClass.getName() + " Deserializer is added");
    }

    /**
     *
     *
     * @param tClass
     * @param input
     * @param <T>
     * @return
     */
    public <T> T deserialize(Class<T> tClass, Object input) {
        logger.info(" Deserializing " + tClass.getName());

        Deserializer deserializer = deserializers.get(tClass);
        T return_obj = null;
        try {
            return_obj = tClass.cast(deserializer.deserialize(input));
        } catch (ClassCastException e) {
            logger.error("Cannot cast the class. Check the Class parameter", e);
        }
        return return_obj;
    }
}
