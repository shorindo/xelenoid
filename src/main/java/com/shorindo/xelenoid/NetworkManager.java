/*
 * Copyright 2018-2019 Shorindo, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shorindo.xelenoid;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

/**
 * 
 */
public class NetworkManager {
    private static final WebLogger LOG = WebLogger.getLogger(NetworkManager.class);

    public static void init() {
        URL.setURLStreamHandlerFactory(new URLStreamHandlerFactory() {
            @Override
            public URLStreamHandler createURLStreamHandler(String protocol) {
                switch (protocol) {
                case "http":
                    return new XHttpURLStreamHandler();
                default:
                    return null;
                }
            }
        });
        Authenticator.setDefault(new Authenticator() {

            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                LOG.debug(this.getRequestingURL().toString());
                LOG.debug(this.getRequestingPrompt().toString());
//                if ("utage.org".equals(getRequestingHost())) {
//                    return new PasswordAuthentication("kazm", "7m2kvx".toCharArray());
//                } else {
//                    return null;
//                }
                return null;
            }
        });
    }
}
