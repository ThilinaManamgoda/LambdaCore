package org.wso2.service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.apache.log4j.Logger;
import org.wso2.core.Context;
import org.wso2.core.Deserializers.APICreateEventDeserializer;
import org.wso2.core.EventDeserializersManager;
import org.wso2.core.RequestHandlerGeneric;
import org.wso2.core.models.APICreateEvent;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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

    static {

    }

    @POST
    @Path("/{className}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response runLambdaFunctionWithInterface(@PathParam("className") String className, JsonElement payLoad) {
        Class aClass = null;
        Object aClassObj = null;
        Class paramClass = null;

        try {
            aClass = Class.forName(className);
            logger.info("The class loaded successfully");

            Method declaredMethods[] = aClass.getDeclaredMethods();

            //TODO Create a Function for finding the correct function in the given Class

            // For demonstrating purpose let's think there is only one function

            paramClass = Class.forName(declaredMethods[0].getParameterTypes()[1].getTypeName());

            aClassObj = aClass.newInstance();

        } catch (ClassNotFoundException e) {
            logger.error("Cannot load the Class !", e);
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("Cannot create an instance of the Class !", e);
        }


        if (aClassObj instanceof RequestHandlerGeneric) {
            //TODO Implement Context class

            ((RequestHandlerGeneric) aClassObj).handleRequest(new Context(), castPayload(payLoad, paramClass));
        } else {
            logger.error("Class is not implemented the RequestHandler Interface !");
        }

        return Response.ok().build();
    }

    @Path("/{className}/{methodName}")
    public Response runLambdaFunctionWithMethodName(@PathParam("className") String className, @PathParam("methodName") String methodName, JsonElement payLoad) {
        Class aClass = null;
        Object aClassObj = null;
        Class paramClass = null;

        try {
            aClass = Class.forName(className);
            logger.info("The class loaded successfully");

            Method declaredMethods[] = aClass.getDeclaredMethods();

            //TODO Create a Function for finding the correct function in the given Class

            // For demonstrating purpose let's think there is only one function
            for (Method method : declaredMethods) {

                if (method.getName().equals(methodName)) {
                    paramClass = Class.forName(method.getParameterTypes()[1].getTypeName());

                    method.invoke(aClass.newInstance(),new Context(),castPayload(payLoad, paramClass));
                    break;
                }
            }

        } catch (ClassNotFoundException e) {
            logger.error("Cannot load the Class !", e);
        } catch (InstantiationException | IllegalAccessException e) {
        } catch (InvocationTargetException e) {
            logger.error("Cannot create an instance of the Class !", e);
            logger.error("Cannot invoke the Function !", e);
        }



        return Response.ok().build();
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
            aClass = Class.forName("org.wso2.core.models.Customer");
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

