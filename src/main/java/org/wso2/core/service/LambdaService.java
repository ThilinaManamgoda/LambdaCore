package org.wso2.core.service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.apache.log4j.Logger;
import org.wso2.core.Context;
import org.wso2.core.LambdaServiceConstant;
import org.wso2.core.RequestHandler;

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


/**
 * Created by maanadev on 12/15/16.
 */
@Path("/lambda")
public class LambdaService {
    final static Logger logger = Logger.getLogger(LambdaService.class);


    final private static String LAMBDA_CLASS = System.getenv(LambdaServiceConstant.LAMBDA_CLASS_ENV);
    final private static String LAMBDA_FUNCTION_NAME = System.getenv(LambdaServiceConstant.LAMBDA_FUNCTION_NAME_ENV);

    private static Class lambdaFuncClass = getLambdaFuncClass();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response runLambdaFunction(JsonElement payLoad) {
        Object lambdaFuncClassObj = null;
        Class paramClass = null;
        Object response = null;

        logger.info("Lambda Class: " + LAMBDA_CLASS + " Lambda Function: " + LAMBDA_FUNCTION_NAME);

        try {


            lambdaFuncClassObj = lambdaFuncClass.newInstance();
            Method declaredMethods[] = lambdaFuncClass.getDeclaredMethods();


            if (LAMBDA_FUNCTION_NAME == null) {


                if (lambdaFuncClassObj instanceof RequestHandler) {

                    paramClass = getInputParameterClass(lambdaFuncClass)[0];
                    response = ((RequestHandler) lambdaFuncClassObj).handleRequest(new Context(), castPayload(payLoad, paramClass));
                    logger.info(response == null);

                } else {
                    logger.error("Class is not implemented the RequestHandler Interface !");
                    return Response.ok(Response.status(Response.Status.INTERNAL_SERVER_ERROR)).build();
                }


            } else {
                for (Method method : declaredMethods) {
                    //TODO need to implement finding function logic
                    if (method.getName().equals(LAMBDA_FUNCTION_NAME)) {

                        paramClass = getInputParameterClass(method);

                        response = method.invoke(lambdaFuncClassObj, new Context(), castPayload(payLoad, paramClass));
                        break;
                    }
                }
            }

        } catch (ClassNotFoundException e) {
            logger.error("Cannot load the Parameter Class !", e);
            return Response.ok(Response.status(Response.Status.INTERNAL_SERVER_ERROR)).build();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("Cannot create an instance of the Class !", e);
            return Response.ok(Response.status(Response.Status.INTERNAL_SERVER_ERROR)).build();
        } catch (InvocationTargetException e) {
            logger.error("Cannot Invoke the function !", e);
            return Response.ok(Response.status(Response.Status.INTERNAL_SERVER_ERROR)).build();
        }

        if (response == null) {
            return Response.ok(Response.status(Response.Status.ACCEPTED)).build();

        } else {
            return Response.ok(Response.status(Response.Status.ACCEPTED)).entity(response).build();

        }

    }

    private static Class getLambdaFuncClass() {
        Class aclass = null;
        try {
            aclass = Class.forName(LAMBDA_CLASS);
        } catch (ClassNotFoundException e) {
            logger.error("Cannot load the Lambda Function Class !", e);
        }
        logger.info("The class loaded successfully");
        return aclass;
    }

    private Class<?> getInputParameterClass(Method method) throws ClassNotFoundException {

        return Class.forName(method.getParameterTypes()[LambdaServiceConstant.DEFAULT_PARAM_INDEX].getTypeName());
    }

    private static Class<?>[] getInputParameterClass(Class aclass) throws ClassNotFoundException {
        Class[] paramClasses = new Class[2];
        Type[] genericInterfaces = aclass.getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType &&(((ParameterizedType) genericInterface).getRawType().getTypeName().equals(LambdaServiceConstant.DEFAULT_INTERFACE))) {
                Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
                int i = 0;
                for (Type genericType : genericTypes) {
                    paramClasses[i] = Class.forName(genericType.getTypeName());
                    i++;
                }
            }
        }

        return paramClasses;
    }

    private <T> T castPayload(JsonElement input, Class<T> aclass) {

        Gson gson = new Gson();
        return gson.fromJson(input, aclass);
    }


}

