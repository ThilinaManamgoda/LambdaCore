/*
 *
 *  *  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *  *
 *  *  Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License at
 *  *
 *  *  http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *  Unless required by applicable law or agreed to in writing, software
 *  *  distributed under the License is distributed on an "AS IS" BASIS,
 *  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  See the License for the specific language governing permissions and
 *  *  limitations under the License.
 *
 */
package org.wso2.core.interceptors;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wso2.msf4j.Interceptor;
import org.wso2.msf4j.Request;
import org.wso2.msf4j.Response;
import org.wso2.msf4j.ServiceMethodInfo;
import org.wso2.msf4j.security.JWTSecurityInterceptor;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Date;

public class LambdaJWTInterceptor implements Interceptor {
    private static final Logger log = LogManager.getLogger(JWTSecurityInterceptor.class);
    private static final String JWT_HEADER = "X-JWT-Assertion";
    private static final String AUTH_TYPE_JWT = "JWT";
    private static final String KEYSTORE = "wso2keystore.jks";
    private static final String ALIAS = "apicloudsign";
    private static final String KEYSTORE_PASSWORD = "zxcvbn";

    @Override
    public boolean preCall(Request request, Response response, ServiceMethodInfo serviceMethodInfo) throws Exception {
        log.info("Authentication precall");
        String jwtHeader = request.getHeader(JWT_HEADER);
        if(jwtHeader != null) {
            boolean isValidSignature = this.verifySignature(jwtHeader);
            if(isValidSignature) {
                return true;
            }
        }

        response.setHeader("WWW-Authenticate", "JWT");
        response.setStatus(javax.ws.rs.core.Response.Status.UNAUTHORIZED.getStatusCode());
        response.send();
        return false;
    }

    @Override
    public void postCall(Request request, int i, ServiceMethodInfo serviceMethodInfo) throws Exception {

    }

    private boolean verifySignature(String jwt) {
        try {
            SignedJWT e = SignedJWT.parse(jwt);
            if((new Date()).before(new Date(e.getJWTClaimsSet().getExpirationTimeClaim()))) {
                RSASSAVerifier verifier = new RSASSAVerifier((RSAPublicKey)this.getPublicKey(KEYSTORE, KEYSTORE_PASSWORD, ALIAS));
                return e.verify(verifier);
            }

            log.info("Token has expired");
        } catch (IOException | KeyStoreException | CertificateException | NoSuchAlgorithmException | UnrecoverableKeyException | JOSEException | ParseException var4) {
            log.error("Error occurred while JWT signature verification. JWT={}",jwt, var4);
        }

        return false;
    }

    private PublicKey getPublicKey(String keyStorePath, String keyStorePassword, String alias) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(keyStorePath);
        Throwable var5 = null;

        try {
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(inputStream, keyStorePassword.toCharArray());
            Key key = keystore.getKey(alias, keyStorePassword.toCharArray());
            if(key instanceof PrivateKey) {
               Certificate cert = keystore.getCertificate(alias);
                PublicKey var9 = cert.getPublicKey();
                return var9;
            }
        } catch (Throwable var19) {
            var5 = var19;
            throw var19;
        } finally {
            if(inputStream != null) {
                if(var5 != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable var18) {
                        var5.addSuppressed(var18);
                    }
                } else {
                    inputStream.close();
                }
            }

        }

        return null;
    }
}
