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

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 */
public class WebLogger {
    private Logger logger;

    public static WebLogger getLogger(Class<?> clazz) {
        return new WebLogger(LogManager.getLogger(clazz));
    }

    private WebLogger(Logger logger) {
        this.logger = logger;
    }
    public void log(WebLoggerLevel level, String message) {
        logger.log(level.getLevel(), message);
    }
    public void debug(String message) {
        log(WebLoggerLevel.DEBUG, message);
    }
    public void error(Throwable th) {
        log(WebLoggerLevel.ERROR, th.getMessage());
        th.printStackTrace();
    }

    public enum WebLoggerLevel {
        TRACE(Level.TRACE),
        DEBUG(Level.DEBUG),
        INFO(Level.INFO),
        WARN(Level.WARN),
        ERROR(Level.ERROR);

        private Level level;

        private WebLoggerLevel(Level level) {
            this.level = level;
        }

        public Level getLevel() {
            return level;
        }
    }
}
