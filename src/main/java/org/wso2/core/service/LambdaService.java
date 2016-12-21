package org.wso2.core.service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.apache.log4j.Logger;
import org.wso2.core.Context;
import org.wso2.core.RequestHandler;
import org.wso2.core.exceptions.CustomMethodParamClassNotFoundException;
import org.wso2.core.exceptions.DefaultMethodParamClassNotFoundException;

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

import static org.wso2.core.LambdaServiceConstant.*;


/**
 * Created by maanadev on 12/15/16.
 */
@Path("/lambda")
public class LambdaService {
    final static Logger logger = Logger.getLogger(LambdaService.class);


    final private static String LAMBDA_CLASS = System.getenv(LAMBDA_CLASS_ENV);
    final private static String LAMBDA_FUNCTION_NAME = System.getenv(LAMBDA_FUNCTION_NAME_ENV);


    private static Class lambdaFuncClass = getLambdaFuncClass();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response runLambdaFunction(JsonElement payLoad) {
        Object lambdaFuncClassObj = null;
        Class paramClass = null;
        Object response = null;
        boolean internalServerError = false;

        logger.info("Lambda Class: " + LAMBDA_CLASS + " Lambda Function: " + LAMBDA_FUNCTION_NAME);

        try {


            lambdaFuncClassObj = lambdaFuncClass.newInstance();
            Method declaredMethods[] = lambdaFuncClass.getDeclaredMethods();


            if (LAMBDA_FUNCTION_NAME == null) {

                if (lambdaFuncClassObj instanceof RequestHandler) {

                    paramClass = getInputParameterClass(lambdaFuncClass)[DEFAULT_INTERFACE_INPUT_PARAM_INDEX];
                    response = ((RequestHandler) lambdaFuncClassObj).handleRequest(new Context(), castPayload(payLoad, paramClass));

                } else {
                    logger.error(LAMBDA_CLASS + " Class is not implemented the RequestHandler Interface !");
                    internalServerError = true;
                }


            } else {
                for (Method method : declaredMethods) {
                    logger.info("validating Method: " +method.getName());
                    if (method.getName().equals(LAMBDA_FUNCTION_NAME) && isMethodValid(method)) {
                        logger.info(LAMBDA_FUNCTION_NAME+" method is found");
                        paramClass = getInputParameterClass(method, CUSTOM_METHOD_INPUT_PARAM_INDEX);

                        response = method.invoke(lambdaFuncClassObj, new Context(), castPayload(payLoad, paramClass));
                        break;
                    }
                }
            }

        } catch (CustomMethodParamClassNotFoundException e) {
            logger.error("Couldn't load the Parameter Class of the " + LAMBDA_FUNCTION_NAME + " method input Parameter!", e);
            internalServerError = true;
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("Couldn't create an instance of the Class !", e);
            internalServerError = true;
        } catch (InvocationTargetException e) {
            logger.error("Couldn't Invoke the function !", e);
            internalServerError = true;
        } catch (DefaultMethodParamClassNotFoundException e) {
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

    private boolean isMethodValid(Method method) throws CustomMethodParamClassNotFoundException {
        Class paramClass = getInputParameterClass(method, CONTEXT_PARAM_INDEX);
        return paramClass == Context.class;
    }


    private static Class getLambdaFuncClass() {
        Class aclass = null;
        try {
            aclass = Class.forName(LAMBDA_CLASS);
        } catch (ClassNotFoundException e) {
            logger.error("Couldn't load the Lambda Function Class: " + LAMBDA_CLASS, e);
        }
        logger.info(LAMBDA_CLASS + " class is loaded successfully");
        return aclass;
    }

    /**
     * Extract the class of the parameter in a method
     * @param method
     * @param index index of the parameter. First parameter is 0 and so on.
     * @return
     * @throws CustomMethodParamClassNotFoundException
     */
    private Class<?> getInputParameterClass(Method method, int index) throws CustomMethodParamClassNotFoundException {

        try {
            return Class.forName(method.getParameterTypes()[index].getTypeName());
        } catch (ClassNotFoundException e) {
            throw new CustomMethodParamClassNotFoundException(e);
        }
    }

    /**
     * Extract Class of the parameters in given Generic Interface
     * @param aclass Default Interface class
     * @return array of  classes of Parameters
     * @throws DefaultMethodParamClassNotFoundException
     */
    private static Class<?>[] getInputParameterClass(Class aclass) throws DefaultMethodParamClassNotFoundException {
        Class[] paramClasses = new Class[DEFAULT_PARAM_COUNT];
        Type[] genericInterfaces = aclass.getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType) {
                Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
                int i = 0;
                for (Type genericType : genericTypes) {
                    try {
                        paramClasses[i] = Class.forName(genericType.getTypeName());
                    } catch (ClassNotFoundException e) {
                        throw new DefaultMethodParamClassNotFoundException(e);
                    }
                    i++;
                }
            }
        }

        return paramClasses;
    }

    /**
     * Cast given json data to given Class type
     * @param input Serialized data
     * @param aclass The class data is to be deserialized
     * @param <T>
     * @return
     */
    private <T> T castPayload(JsonElement input, Class<T> aclass) {

        Gson gson = new Gson();
        return gson.fromJson(input, aclass);
    }


}

