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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.apache.log4j.Logger;
import org.wso2.function.Context;
import org.wso2.function.RequestHandler;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

import static org.wso2.core.service.LambdaServiceConstant.CONTEXT_PARAM_INDEX;
import static org.wso2.core.service.LambdaServiceConstant.DEFAULT_PARAM_COUNT;

/**
 * This Util class has following capabilities
 * <p>
 * * Get the class when the class name is passed as a String
 * * Check whether the provided interface is Default Interface or not
 * * Cast Json payload to desired java object
 * * Find default Interface in given class
 * *
 */
public class LambdaUtil {

    final static Logger logger = Logger.getLogger(LambdaUtil.class);


    /**
     * Load the Class which contains the Lambda Function
     *
     * @return
     */
    public static Class getClassfromName(String className) {
        Class aclass = null;
        try {
            aclass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            logger.error("Couldn't load the Lambda Class: " + className, e);
        }
        logger.info(className + " class is loaded successfully");
        return aclass;
    }

    /**
     * Check whether the interface described by the argument is  org.wso2.core.RequestHandler
     *
     * @param parameterizedTypeInterface Interface to be validated
     * @return
     */
    public static boolean isDefaultInterface(ParameterizedType parameterizedTypeInterface) {
        Type ainterface = parameterizedTypeInterface.getRawType();
        return (ainterface == RequestHandler.class);
    }

    /**
     * Find the Parameterized  default interface
     *
     * @param aclass Class which implements the org.wso2.core.RequestHandler
     * @return ParameterizedType type representation of the org.wso2.core.RequestHandler interface
     */
    public static ParameterizedType findDefaultInterface(Class aclass) {
        Type[] genericInterfaces = aclass.getGenericInterfaces();

        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType && isDefaultInterface((ParameterizedType) genericInterface)) {
                return (ParameterizedType) genericInterface;
            }
        }
        return null;
    }


    /**
     * This method is called when the LAMBDA_FUNCTION_NAME is defined. It will return the method, which matches the given name and the syntax[ output-type FUNCTION_NAME(org.wso2.core.Context context, input-type input)]
     *
     * @param declaredMethods Array of methods
     * @param funcName        Name of the Lambda function to be found
     * @return Desired method if it's found otherwise null
     */
    public static Method findLambdaFunc(Method[] declaredMethods, String funcName) {

        return Arrays.stream(declaredMethods)
                .parallel()
                .filter(method -> {
                    logger.info("validating Method: " + method.getName());
                    return method.getName().equals(funcName) && isMethodValid(method);
                })
                .findFirst()
                .orElse(null);

    }


    /**
     * Checking following conditions,
     * --whether the first parameter of the given method is org.wso2.core.Context class type or not
     * --Is the parameter count is equal to 2 or not
     * <p>
     * SYNTAX: output-type FUNCTION_NAME(org.wso2.core.Context context, input-type input)
     *
     * @param method Method object to be checked
     * @return Method that match the given conditions
     */
    public static boolean isMethodValid(Method method) {

        Type[] paramTypes = getParamClassesOfMethod(method);
        Type paramType = paramTypes[CONTEXT_PARAM_INDEX];

        return (paramTypes.length == DEFAULT_PARAM_COUNT) && (paramType == Context.class);
    }


    public static Type[] getParamClassesOfMethod(Method method) {
        Type parameterTypes[] = method.getGenericParameterTypes();
        return parameterTypes;
    }

    /**
     * Extract the reflect.Type representation of the generic parameters in a given Generic parameterized Interface
     * <p>
     *
     * @param ParameterizedTypeInterface Parameterized Default Interface Type obj
     * @return array of  Types of Parameters
     */
    public static Type[] getParamTypesOfInterface(ParameterizedType ParameterizedTypeInterface) {

        Type[] genericTypes = ParameterizedTypeInterface.getActualTypeArguments();
        return genericTypes;
    }

    /**
     * Cast given json data to given Class type
     *
     * @param input  Serialized data
     * @param aclass The class which the data is to be deserialized
     * @return The deserialized object
     */
    public static <T> T fromJsonTo(JsonElement input, Class<T> aclass) {

        Gson gson = new Gson();
        return gson.fromJson(input, aclass);
    }

    /**
     * Cast given json data to given Class type
     *
     * @param input Serialized data
     * @param type  The Type representation of the class which the data is to be deserialized
     * @return The deserialized object
     */
    public static <T> T fromJsonTo(JsonElement input, Type type) {

        Gson gson = new Gson();
        return gson.fromJson(input, type);
    }

    /**
     * Construct the object Context which contains runtime info
     *
     * @param aClass the Class the Lambda function is implemented
     * @return
     */

    public static Context getContext(Class aClass) {

        ContextImpl contextImpl = new ContextImpl(aClass);
        return contextImpl;

    }
}
