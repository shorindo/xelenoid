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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 
 */
public class InputStreamInterceptor extends InputStream {
    private static final WebLogger LOG = WebLogger.getLogger(InputStreamInterceptor.class);
    private InputStream is;
    private ByteArrayOutputStream bb;

    public InputStreamInterceptor(InputStream is) {
        this.is = is;
        this.bb = new ByteArrayOutputStream();
    }

    @Override
    public int read() throws IOException {
        int ch = is.read();
        this.bb.write(ch);
        return ch;
    }

    public byte[] getBytes() {
        return this.bb.toByteArray();
    }
}
