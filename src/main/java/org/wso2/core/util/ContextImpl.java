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
package org.wso2.core.util;


import org.wso2.function.Context;

import static org.wso2.core.service.LambdaServiceConstant.LAMBDA_APPLICATION_NAME;
import static org.wso2.core.service.LambdaServiceConstant.TENANT;

public class ContextImpl implements Context {

    private final static String VAL_LAMBDA_APPLICATION_NAME = System.getenv(LAMBDA_APPLICATION_NAME);
    private final static String VAL_TENANT = System.getenv(TENANT);
    private static Context contextImpl;

    /**
     * Construct the object Context which contains runtime info
     *
     * @return
     */
    public static Context getContext() {
        if(contextImpl == null){
            contextImpl = new ContextImpl();
        }
        return contextImpl;
    }
    @Override
    public String getFunctionName() {
        return VAL_LAMBDA_APPLICATION_NAME;
    }

    @Override
    public String getTenant() {
        return VAL_TENANT;
    }


}
