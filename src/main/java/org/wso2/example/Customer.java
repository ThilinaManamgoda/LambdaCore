
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

package org.wso2.example;

import org.wso2.function.Context;
import org.wso2.function.RequestHandler;
import org.wso2.function.models.API_CREATION;

/**
 * This class presents two ways to create Lambda function
 * 1. Using the interface
 * 2. User defined Function (should be matched to the prototype)
 */
public class Customer implements RequestHandler<API_CREATION, CustomResponse> {
    @Override
    public CustomResponse handleRequest(Context context, API_CREATION input) {
        CustomResponse customResponse = new CustomResponse();
        customResponse.setApiCreateEvent(input);
        customResponse.setResponse("Lambda function works!: "+context.getFunctionName()+" "+ context.getTenant());
        return customResponse;
    }


    public CustomResponse handleRequestMyWay(Context context, API_CREATION input) {
        CustomResponse customResponse = new CustomResponse();
        customResponse.setApiCreateEvent(input);
        customResponse.setResponse("Lambda function works via custom function!");
        return customResponse;
    }


}
