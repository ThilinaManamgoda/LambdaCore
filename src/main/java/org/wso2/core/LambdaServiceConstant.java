package org.wso2.core;

/**
 * Created by maanadev on 12/19/16.
 */
final public class LambdaServiceConstant {
    public static final String DEFAULT_METHOD_NAME = "handleRequest";
    public static final String LAMBDA_CLASS_ENV = "LAMBDA_CLASS";
    public static final String LAMBDA_FUNCTION_NAME_ENV = "LAMBDA_FUNCTION_NAME";
    public static final int CUSTOM_METHOD_INPUT_PARAM_INDEX = 1;
    public static final String DEFAULT_INTERFACE = "org.wso2.core.RequestHandler";
    public static final int DEFAULT_INTERFACE_INPUT_PARAM_INDEX = 0;
    public static final int CONTEXT_PARAM_INDEX = 0;
    public static final int DEFAULT_PARAM_COUNT = 2;
    private LambdaServiceConstant(){}
}
