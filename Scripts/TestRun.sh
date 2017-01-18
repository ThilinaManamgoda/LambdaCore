#!/usr/bin/env bash
export LAMBDA_CLASS=org.wso2.impl.LambdaApp
#export LAMBDA_FUNCTION_NAME=handleRequestMyWay
java -cp ./../target/core-1.0-SNAPSHOT.jar:./../../TestLambda/testExecution/target/testExecution-1.0.0.jar org.wso2.core.service.LambdaServiceRunner
