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

import java.net.URL;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.Logs;

/**
 * 
 */
public class DummyDriver implements WebDriver {
    private static final XeleneseLogger LOG = XeleneseLogger.getLogger(DummyDriver.class);
    private String currentUrl;
    private String title;

    /**
     * 
     */
    public DummyDriver() {
    }

    @Override
    public void get(String url) {
        LOG.debug("get(" + url + ")");
        currentUrl = url;
    }

    @Override
    public String getCurrentUrl() {
        LOG.debug("getCurrentUrl() -> " + currentUrl);
        return currentUrl;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public List<WebElement> findElements(By by) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public WebElement findElement(By by) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getPageSource() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void close() {
        LOG.debug("close()");
    }

    @Override
    public void quit() {
        LOG.debug("quit()");
    }

    @Override
    public Set<String> getWindowHandles() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getWindowHandle() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TargetLocator switchTo() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Navigation navigate() {
        return new DummyNavigation();
    }

    @Override
    public Options manage() {
        // TODO Auto-generated method stub
        return null;
    }

    public static class DummyNavigation implements Navigation {
        @Override
        public void back() {
            LOG.debug("back()");
        }

        @Override
        public void forward() {
            LOG.debug("forward()");
        }

        @Override
        public void to(String url) {
            LOG.debug("to(" + url + ")");
        }

        @Override
        public void to(URL url) {
            LOG.debug("to(" + url + ")");
        }

        @Override
        public void refresh() {
            LOG.debug("refresh()");
        }
    }

    public static class DummyOptions implements Options {

        @Override
        public void addCookie(Cookie cookie) {
        }

        @Override
        public void deleteCookieNamed(String name) {
        }

        @Override
        public void deleteCookie(Cookie cookie) {
        }

        @Override
        public void deleteAllCookies() {
        }

        @Override
        public Set<Cookie> getCookies() {
            return null;
        }

        @Override
        public Cookie getCookieNamed(String name) {
            return null;
        }

        @Override
        public Timeouts timeouts() {
            return null;
        }

        @Override
        public ImeHandler ime() {
            return null;
        }

        @Override
        public Window window() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Logs logs() {
            // TODO Auto-generated method stub
            return null;
        }
        
    }
}
