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

/**
 * 
 */
public class XeleneseLogger {

    public static XeleneseLogger getLogger(Class<?> clazz) {
        return new XeleneseLogger(clazz);
    }

    private XeleneseLogger(Class<?> clazz) {
    }
    public void log(String level, Object message) {
        System.out.println("[" + level + "] " + message);
    }
    public void debug(Object message) {
        log("DEBUG", message);
    }
    public void info(Object message) {
        log("INFO", message);
    }
    public void warn(Object message) {
        log("WARN", message);
    }
    public void error(Object message) {
        log("ERROR", message);
    }
}
