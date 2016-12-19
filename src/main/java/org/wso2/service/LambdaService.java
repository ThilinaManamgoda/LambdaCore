package org.wso2.service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.apache.log4j.Logger;
import org.wso2.core.Context;
import org.wso2.core.Deserializers.APICreateEventDeserializer;
import org.wso2.core.EventDeserializersManager;
import org.wso2.core.LambdaServiceConstant;
import org.wso2.core.RequestHandlerGeneric;
import org.wso2.core.models.APICreateEvent;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by maanadev on 12/15/16.
 */
@Path("/lambda")
public class LambdaService {

    final static Logger logger = Logger.getLogger(LambdaService.class);
    final private static String LAMBDA_CLASS = System.getenv(LambdaServiceConstant.LAMBDA_CLASS_ENV);
    final private static String LAMBDA_FUNCTION_NAME= System.getenv(LambdaServiceConstant.LAMBDA_FUNCTION_NAME_ENV);



    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response runLambdaFunction(JsonElement payLoad) {
        Class lambdaFuncClass = null;
        Object lambdaFuncClassObj = null;
        Class paramClass = null;
        Object response = null;

        logger.info("Lambda Class: "+LAMBDA_CLASS+" Lambda Function: "+LAMBDA_FUNCTION_NAME);

        try {
            lambdaFuncClass = Class.forName(LAMBDA_CLASS);
            logger.info("The class loaded successfully");

            lambdaFuncClassObj = lambdaFuncClass.newInstance();
            Method declaredMethods[] = lambdaFuncClass.getDeclaredMethods();

            //TODO Create a Function for finding the correct function in the given Class


            if (LAMBDA_FUNCTION_NAME == null) {
                for (Method method : declaredMethods) {

                    if (method.getName().equals(LambdaServiceConstant.DEFAULT_METHOD_NAME)) {

                        paramClass = getInputParameterClass(method);


                        if (lambdaFuncClassObj instanceof RequestHandlerGeneric) {

                            response = ((RequestHandlerGeneric) lambdaFuncClassObj).handleRequest(new Context(), castPayload(payLoad, paramClass));
                        } else {
                            logger.error("Class is not implemented the RequestHandler Interface !");
                        }

                        break;
                    }
                }
            } else {
                for (Method method : declaredMethods) {

                    if (method.getName().equals(LAMBDA_FUNCTION_NAME)) {

                        paramClass = getInputParameterClass(method);

                        response = method.invoke(lambdaFuncClassObj, new Context(), castPayload(payLoad, paramClass));
                        break;
                    }
                }
            }

        } catch (ClassNotFoundException e) {
            logger.error("Cannot load the Class !", e);
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("Cannot create an instance of the Class !", e);
        } catch (InvocationTargetException e) {
            logger.error("Cannot Invoke the function !", e);
        }

        if (response == null) {
            return Response.ok(Response.status(Response.Status.ACCEPTED)).build();

        } else {
            return Response.ok(Response.status(Response.Status.ACCEPTED)).entity(response).build();

        }

    }

    private Class<?> getInputParameterClass(Method method) throws ClassNotFoundException {
        return Class.forName(method.getParameterTypes()[LambdaServiceConstant.DEFAULT_PARAM_INDEX].getTypeName());
    }

    private EventDeserializersManager getEventDeserializersManager() {

        EventDeserializersManager eventDeserializersManager = new EventDeserializersManager();
        eventDeserializersManager.registerDeserializer(APICreateEvent.class, new APICreateEventDeserializer());
        return eventDeserializersManager;
    }

    private <T> T castPayload(JsonElement input, Class<T> aclass) {

        Gson gson = new Gson();
        return gson.fromJson(input, aclass);
    }
/*

    public static void main(String[] args) {
        Class aClass = null;
        Object aClassObj = null;
        Class paramClass = null;

        try {
            aClass = Class.forName("org.wso2.example.Customer");
            logger.info("The class loaded successfully");

            Method declaredMethods[] = aClass.getDeclaredMethods();
            System.out.println(declaredMethods[0].getName());
            //TODO Create a Function for finding the correct function in the given Class

            // For demonstrating purpose let's think there is only one function
          //  if(declaredMethods[0].getName().equals(methodName))
                paramClass = Class.forName(declaredMethods[0].getParameterTypes()[1].getTypeName());

            aClassObj = aClass.newInstance();

        } catch (ClassNotFoundException e) {
            logger.error("Cannot load the Class !", e);
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("Cannot create an instance of the Class !", e);
        }

    }*/
}

