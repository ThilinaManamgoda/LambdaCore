package org.wso2.service;

import org.apache.log4j.Logger;
import org.wso2.msf4j.MicroservicesRunner;

/**
 * Created by maanadev on 12/15/16.
 */
public class LambdaServiceRunner {
    final static Logger logger = Logger.getLogger(LambdaServiceRunner.class);

    public static void main(String[] args) {
        logger.info("Starting Server");
        new MicroservicesRunner()
                .deploy(new LambdaService())
                .start();
        logger.info("Started !!");
    }
}
