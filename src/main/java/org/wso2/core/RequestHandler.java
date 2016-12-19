package org.wso2.core;

/**
 * Created by maanadev on 12/15/16.
 */
public interface RequestHandler{

    /**
     *
     * @param context
     * @param deserializer
     * @param input
     * @return
     */

    public Object handleRequest(Context context, EventDeserializersManager deserializer, Object input);
}
