package org.wso2.core.service;

import org.wso2.msf4j.MicroservicesRunner;


/**
 * Created by maanadev on 12/15/16.
 */
public class LambdaServiceRunner {

    public static void main(String[] args) {
        new MicroservicesRunner()
                .deploy(new LambdaService())
                .start();
    }
}
