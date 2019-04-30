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
package com.shorindo.xelenese;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 */
public class XeleneseLogger {
    private Logger logger;

    public static XeleneseLogger getLogger(Class<?> clazz) {
        return new XeleneseLogger(LogManager.getLogger(clazz));
    }

    private XeleneseLogger(Logger logger) {
        this.logger = logger;
    }

    private void log(XeleneseLoggerLevel level, String message, Object...args) {
        logger.log(level.getLevel(), message, args);
    }

    private void log(XeleneseLoggerLevel level, String message, Throwable th, Object...args) {
        logger.log(level.getLevel(), message, th, args);
    }

    public void debug(String message, Object...args) {
        log(XeleneseLoggerLevel.DEBUG, message, args);
    }

    public void info(String message, Object...args) {
        log(XeleneseLoggerLevel.INFO, message, args);
    }

    public void warn(String message, Object...args) {
        log(XeleneseLoggerLevel.WARN, message, args);
    }

    public void error(String message, Object...args) {
        log(XeleneseLoggerLevel.ERROR, message, args);
    }

    public void error(Throwable th) {
        log(XeleneseLoggerLevel.ERROR, th.getMessage(), th);
        th.printStackTrace(System.out);
    }

    private enum XeleneseLoggerLevel {
        TRACE(Level.TRACE),
        DEBUG(Level.DEBUG),
        INFO(Level.INFO),
        WARN(Level.WARN),
        ERROR(Level.ERROR);

        private Level level;

        private XeleneseLoggerLevel(Level level) {
            this.level = level;
        }

        public Level getLevel() {
            return level;
        }
    }
}
