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
import org.apache.log4j.Logger;
import org.wso2.core.exceptions.DefaultInterfaceParamClassNotFoundException;
import org.wso2.core.util.LambdaUtil;
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

import static org.wso2.core.service.LambdaServiceConstant.*;

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

    final static Logger logger = Logger.getLogger(LambdaService.class);


    final private static String LAMBDA_CLASS = System.getenv(LAMBDA_CLASS_ENV);
    final private static String LAMBDA_FUNCTION_NAME = System.getenv(LAMBDA_FUNCTION_NAME_ENV);


    private static Class lambdaClass = LambdaUtil.getClassfromName(LAMBDA_CLASS);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response runLambdaFunction(JsonElement payLoad) {
        Object response = null;
        boolean internalServerError = false;
        logger.info("Lambda Class: " + LAMBDA_CLASS + " Lambda Function: " + LAMBDA_FUNCTION_NAME);

        try {


            Object lambdaFuncClassObj = lambdaClass.newInstance();


            if (LAMBDA_FUNCTION_NAME == null) {


                if (lambdaFuncClassObj instanceof RequestHandler) {
                    ParameterizedType defaultInterfaceParameterizedTypeObj = LambdaUtil.findDefaultInterface(lambdaClass);
                    Class paramClass = LambdaUtil.getParamClassesOfInterface(defaultInterfaceParameterizedTypeObj)[DEFAULT_INTERFACE_INPUT_PARAM_INDEX];


                    response = ((RequestHandler) lambdaFuncClassObj).handleRequest(LambdaUtil.getContext(lambdaClass), LambdaUtil.fromJsonTo(payLoad, paramClass));
                } else {
                    logger.error(LAMBDA_CLASS + " Class is not implemented the RequestHandler Interface !");
                    internalServerError = true;
                }


            } else {
                Method declaredMethods[] = lambdaClass.getDeclaredMethods();

                Method method = LambdaUtil.findLambdaFunc(declaredMethods,LAMBDA_FUNCTION_NAME);

                Class paramClass = LambdaUtil.getParamClassesOfMethod(method)[CUSTOM_METHOD_INPUT_PARAM_INDEX];

                response = method.invoke(lambdaFuncClassObj, LambdaUtil.getContext(lambdaClass), LambdaUtil.fromJsonTo(payLoad, paramClass));

            }

        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("Couldn't create an instance of the Class !", e);
            internalServerError = true;
        } catch (InvocationTargetException e) {
            logger.error("Couldn't Invoke the function !", e);
            internalServerError = true;
        } catch (DefaultInterfaceParamClassNotFoundException e) {
            logger.error("Couldn't load the Parameter Class of the " + DEFAULT_METHOD_NAME + " method input Parameter!", e);
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





}

