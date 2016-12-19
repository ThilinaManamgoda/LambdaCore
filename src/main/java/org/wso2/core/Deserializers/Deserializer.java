package org.wso2.core.Deserializers;

/**
 * Created by maanadev on 12/16/16.
 */
public interface Deserializer <T>{
    /**
     *
     * @param input
     * @return
     */

    public T deserialize(Object input);
}
