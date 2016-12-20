package org.wso2.core.exceptions;

/**
 * Created by maanadev on 12/20/16.
 */
public class CustomMethodParamClassNotFoundException extends Throwable {


    public CustomMethodParamClassNotFoundException(ClassNotFoundException e) {
        super(e);
    }
}
