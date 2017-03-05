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

package org.wso2.core.service;

import com.google.gson.JsonElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wso2.function.Context;
import org.wso2.function.RequestHandler;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static org.wso2.core.service.LambdaServiceConstant.*;
import static org.wso2.core.util.ContextImpl.getContext;
import static org.wso2.core.util.LambdaUtil.*;

/**
 * This is the microservice which handles the logic for execution of the Lambda function.
 * At least the "LAMBDA_CLASS" environment variable  should be defined
 * Lambda Function is called via Default Interface method handleRequest or if it's custom method then via Reflection API
 * <p>
 * If the calling is done via Interface, Input parameter is determined through the parametrized Interface
 * If the calling is done via custom method,  Input parameter is determined through the method object
 */

@Path("/")
public class LambdaService {

    final static Logger logger = LogManager.getLogger(LambdaService.class);

    private static Class lambdaClass = null;
    private static Type paramType = null;
    private static Method method = null;
    final private static String LAMBDA_CLASS = System.getenv(LambdaServiceConstant.LAMBDA_CLASS);
    final private static String LAMBDA_FUNCTION_NAME = System.getenv(LambdaServiceConstant.LAMBDA_FUNCTION_NAME);
    final private static boolean isLambdaFunctionNameNULL = (LAMBDA_FUNCTION_NAME == null);


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response runLambdaFunction(JsonElement payLoad) {

        Object response = null;
        boolean internalServerError = false;
        logger.info("Lambda Class: {} Lambda Function: {} ", LAMBDA_CLASS, LAMBDA_FUNCTION_NAME);

        try {
            Object lambdaFuncClassObj = lambdaClass.newInstance();
            Context context = getContext();
            if (isLambdaFunctionNameNULL) {
                if (lambdaFuncClassObj instanceof RequestHandler) {
                    logger.info("Calling the Default method: {}", DEFAULT_METHOD_NAME);
                    response = ((RequestHandler) lambdaFuncClassObj).handleRequest(context, fromJsonTo(payLoad, paramType));
                } else {
                    logger.error(" {} Class is not implemented the RequestHandler Interface !", LAMBDA_CLASS);
                    internalServerError = true;
                }
            } else {
                logger.info("Calling the method: {}", LAMBDA_FUNCTION_NAME);
                response = method.invoke(lambdaFuncClassObj, context, fromJsonTo(payLoad, paramType));
            }
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("Couldn't create an instance of the Class {}!", LAMBDA_CLASS, e);
            internalServerError = true;
        } catch (InvocationTargetException e) {
            logger.error("Couldn't Invoke the {} function !", LAMBDA_FUNCTION_NAME, e);
            internalServerError = true;
        }
        if (internalServerError) {
            return Response.ok(Response.status(Response.Status.INTERNAL_SERVER_ERROR)).build();
        } else if (response == null) {
            return Response.ok(Response.status(Response.Status.ACCEPTED)).build();
        } else {
            return Response.ok(Response.status(Response.Status.ACCEPTED)).entity(response).build();
        }

    }

    /**
     * These code runs once when the class is loaded. This will initialize the needed parameters for the logic
     */
    static {

        lambdaClass = getClassfromName(LAMBDA_CLASS);
        if (lambdaClass != null) {
            logger.info("{} class is loaded successfully", LAMBDA_CLASS);
        }
        if (lambdaClass != null) {
            if (isLambdaFunctionNameNULL) {

                ParameterizedType defaultInterfaceParameterizedTypeObj = findDefaultInterface(lambdaClass);

                paramType = getParamTypesOfInterface(defaultInterfaceParameterizedTypeObj)[DEFAULT_INTERFACE_INPUT_PARAM_INDEX];

            } else {
                Method declaredMethods[] = lambdaClass.getDeclaredMethods();

                method = findLambdaFunc(declaredMethods, LAMBDA_FUNCTION_NAME);

                paramType = getParamClassesOfMethod(method)[CUSTOM_METHOD_INPUT_PARAM_INDEX];


            }

        }
    }
}
