#!/usr/bin/env bash
export LAMBDA_CLASS=org.wso2.customer.App
java -cp ./../target/core-1.0-SNAPSHOT.jar:$1 org.wso2.core.service.LambdaServiceRunner