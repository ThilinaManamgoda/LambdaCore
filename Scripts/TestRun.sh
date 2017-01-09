#!/usr/bin/env bash
export LAMBDA_CLASS=org.wso2.customer.App
java -cp ./../target/core-1.0-SNAPSHOT.jar:/../../Wso2FunctionClient/target/WSO2FunctionClient-1.0-SNAPSHOT.jar:./../../TestLambda/testingExeccution/target/testUpload-1.0-SNAPSHOT.jar org.wso2.core.service.LambdaServiceRunner
