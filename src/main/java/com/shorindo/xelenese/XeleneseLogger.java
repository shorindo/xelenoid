/*
 * Copyright 2018 Shorindo, Inc.
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
package com.shorindo.xelenese;

import java.text.MessageFormat;

/**
 * 
 */
public class XeleneseLogger {
    private Class<?> clazz;

    public static XeleneseLogger getLogger(Class<?> clazz) {
        return new XeleneseLogger(clazz);
    }

    private XeleneseLogger(Class<?> clazz) {
        this.clazz = clazz;
    }
    public void log(String level, Object message) {
        System.out.println("[" + level + "] " + clazz.getSimpleName() + " - " + message);
    }
    public void debug(String message, Object...args) {
        log("WARN", MessageFormat.format(message, args));
    }
    public void info(String message, Object...args) {
        log("WARN", MessageFormat.format(message, args));
    }
    public void warn(String message, Object...args) {
        log("WARN", MessageFormat.format(message, args));
    }
    public void error(String message, Object...args) {
        log("ERROR", MessageFormat.format(message, args));
    }
    public void error(Throwable th) {
        log("ERROR", th.getMessage());
        th.printStackTrace(System.out);
    }
}
