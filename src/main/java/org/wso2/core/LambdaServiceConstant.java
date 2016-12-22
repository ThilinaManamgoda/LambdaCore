
/*
 *
 *  *  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *  *
 *  *  Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License at
 *  *
 *  *  http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *  Unless required by applicable law or agreed to in writing, software
 *  *  distributed under the License is distributed on an "AS IS" BASIS,
 *  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  See the License for the specific language governing permissions and
 *  *  limitations under the License.
 *
 */

package org.wso2.core;

/**
 *
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
