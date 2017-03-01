
#!/usr/bin/env bash
export LAMBDA_CLASS=org.wso2.impl.LambdaApp
export LAMBDA_APPLICATION_NAME=testing
export LAMBDA_EVENT=APICREATE
export TENANT=maanadevwso2
#export LAMBDA_FUNCTION_NAME=handleRequestMyWay
java -cp ./../target/core-1.0-SNAPSHOT.jar:/home/maanadev/WSO2_CLOUD/TestLambda/LambdaSample/target/LambdaSample-1.0.0.jar org.wso2.core.service.LambdaServiceRunner
