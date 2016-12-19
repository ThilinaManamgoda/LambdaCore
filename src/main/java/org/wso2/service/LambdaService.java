package org.wso2.service;

import org.apache.log4j.Logger;
import org.wso2.core.Context;
import org.wso2.core.Deserializers.APICreateEventDeserializer;
import org.wso2.core.EventDeserializersManager;
import org.wso2.core.RequestHandler;
import org.wso2.core.models.APICreateEvent;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
    public Response runLambdaFunction(@PathParam("className") String className, Object payLoad) {
        Class aClass = null;
        Object aClassObj = null;

        try {
            aClass = Class.forName(className);
            logger.info("The class loaded successfully");

            aClassObj = aClass.newInstance();
        } catch (ClassNotFoundException e) {
            logger.error("Cannot load the Class !", e);
        } catch (InstantiationException  | IllegalAccessException e) {
            logger.error("Cannot create an instance of the Class !", e);
        }


        if(aClassObj instanceof RequestHandler){
            ((RequestHandler) aClassObj).handleRequest(new Context(), getEventDeserializersManager(),payLoad);
        }else{
            logger.error("Class is not implemented the RequestHandler Interface !");
        }
        return Response.ok().build();
    }
    private EventDeserializersManager getEventDeserializersManager(){

        EventDeserializersManager eventDeserializersManager = new EventDeserializersManager();
        eventDeserializersManager.registerDeserializer(APICreateEvent.class,new APICreateEventDeserializer());
        return eventDeserializersManager;
    }
}

