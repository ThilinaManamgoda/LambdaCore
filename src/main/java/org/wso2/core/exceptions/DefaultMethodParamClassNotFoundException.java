package org.wso2.core.exceptions;

/**
 * Created by maanadev on 12/20/16.
 */
public class DefaultMethodParamClassNotFoundException extends Throwable {
  public DefaultMethodParamClassNotFoundException(ClassNotFoundException e) {
        super(e);
    }
}
