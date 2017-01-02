
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

import org.wso2.core.Context;
import org.wso2.core.RequestHandler;
import org.wso2.core.models.APICreateEvent;

import java.util.List;

/**
 * This class presents two ways to create Lambda function
 *  1. Using the interface
 *  2. User defined Function (should be matched to the prototype)
 */
public class Customer implements RequestHandler<List, CustomResponse> {
    @Override
    public CustomResponse handleRequest(Context context, List input) {
        System.out.println("This is from interface: \n" + input.get(0).toString());
        return null;
    }


    public CustomResponse handleRequestMyWay(Context context, APICreateEvent input) {
        System.out.println("This is from custom func: \n" + input.toString());
        return null;
    }


}
