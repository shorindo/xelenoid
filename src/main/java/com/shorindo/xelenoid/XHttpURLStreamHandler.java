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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import sun.net.www.protocol.http.Handler;
import sun.net.www.protocol.http.HttpURLConnection;

/**
 * 
 */
public class XHttpURLStreamHandler extends Handler {
    private static final WebLogger LOG = WebLogger.getLogger(XHttpURLStreamHandler.class);

    public XHttpURLStreamHandler() {
    }

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        switch (u.getProtocol()) {
        case "http":
            return new XHttpURLConnection(u, this);
        default:
            return null;
        }
    }

    public static class XHttpURLConnection extends HttpURLConnection {
        private URL url;
        private InputStreamInterceptor is;

        protected XHttpURLConnection(URL u, Handler handler) throws IOException {
            super(u, handler);
            this.url = u;
        }

        @Override
        public void connect() throws IOException {
            try {
                super.connect();
                is = new InputStreamInterceptor(super.getInputStream());
            } catch (IOException e) {
                is = new InputStreamInterceptor(super.getErrorStream());
            } finally {
                RequestQueue.put(url.toString(), new String[] {
                    String.valueOf(getResponseCode())
                });
            }
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return is;
        }

        @Override
        public InputStream getErrorStream() {
            return super.getErrorStream();
        }

        public byte[] getBytes() {
            return is.getBytes();
        }
    }
}
