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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import net.arnx.jsonic.JSON;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.Logs;

import com.shorindo.xelenese.XeleneseLogger;

/**
 * 
 */
public class MockDriver implements WebDriver {
    private static final XeleneseLogger LOG = XeleneseLogger.getLogger(MockDriver.class);
    private Options options = new MockOptions();
    private String currentUrl;
    private String title;

    /**
     * 
     */
    public MockDriver() {
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
        LOG.debug("getTitle()");
        return title;
    }

    @Override
    public List<WebElement> findElements(By by) {
        LOG.debug("findElements(" + by + ")");
        return null;
    }

    @Override
    public WebElement findElement(By by) {
        LOG.debug("findElement(" + by + ")");
        return new MockElement();
    }

    @Override
    public String getPageSource() {
        LOG.debug("getPageSource()");
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
        LOG.debug("getWindowHandles()");
        return null;
    }

    @Override
    public String getWindowHandle() {
        LOG.debug("getWindowHandle()");
        return null;
    }

    @Override
    public TargetLocator switchTo() {
        LOG.debug("switchTo()");
        return null;
    }

    @Override
    public Navigation navigate() {
        LOG.debug("navigate()");
        return new MockNavigation();
    }

    @Override
    public Options manage() {
        LOG.debug("manage()");
        return options;
    }

    private static class MockNavigation implements Navigation {
        private static final XeleneseLogger LOG = XeleneseLogger.getLogger(MockNavigation.class);

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

    public static class MockOptions implements Options {
        private static final XeleneseLogger LOG = XeleneseLogger.getLogger(MockOptions.class);
        private Set<Cookie> cookies = new HashSet<Cookie>();
        private Timeouts timeouts = new MockTimeouts();
        private ImeHandler ime = new MockImeHandler();
        private Window window = new MockWindow();
        private Logs logs = new MockLogs();

        @Override
        public void addCookie(Cookie cookie) {
            LOG.debug("addCookie(" + cookie + ")");
            cookies.add(cookie);
        }

        @Override
        public void deleteCookieNamed(String name) {
            LOG.debug("deleteCookieNamed(" + name + ")");
            cookies.remove(getCookieNamed(name));
        }

        @Override
        public void deleteCookie(Cookie cookie) {
            LOG.debug("deleteCookie(" + cookie + ")");
            cookies.remove(cookie);
        }

        @Override
        public void deleteAllCookies() {
            LOG.debug("deleteAllCookies()");
            cookies.removeAll(cookies);
        }

        @Override
        public Set<Cookie> getCookies() {
            LOG.debug("getCookies()");
            return cookies;
        }

        @Override
        public Cookie getCookieNamed(String name) {
            LOG.debug("getCookieNamed(" + name + ")");
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    cookies.remove(cookie);
                    return cookie;
                }
            }
            return null;
        }

        @Override
        public Timeouts timeouts() {
            LOG.debug("timeouts()");
            return timeouts;
        }

        @Override
        public ImeHandler ime() {
            LOG.debug("ime()");
            return ime;
        }

        @Override
        public Window window() {
            LOG.debug("window()");
            return window;
        }

        @Override
        public Logs logs() {
            LOG.debug("logs()");
            return logs;
        }
    }

    private static class MockTimeouts implements Timeouts {

        @Override
        public Timeouts implicitlyWait(long time, TimeUnit unit) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Timeouts setScriptTimeout(long time, TimeUnit unit) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Timeouts pageLoadTimeout(long time, TimeUnit unit) {
            // TODO Auto-generated method stub
            return null;
        }
        
    }

    private static class MockWindow implements Window {
        private static final XeleneseLogger LOG = XeleneseLogger.getLogger(MockWindow.class);
        private Dimension size = new Dimension(1024, 768);
        private Point position = new Point(0, 0);

        @Override
        public void setSize(Dimension targetSize) {
            LOG.debug("setSize(" + targetSize + ")");
            this.size = targetSize;
        }

        @Override
        public void setPosition(Point targetPosition) {
            LOG.debug("setPosition(" + targetPosition + ")");
            this.position = targetPosition;
        }

        @Override
        public Dimension getSize() {
            LOG.debug("getSize)");
            return size;
        }

        @Override
        public Point getPosition() {
            LOG.debug("getPosition()");
            return position;
        }

        @Override
        public void maximize() {
            LOG.debug("maxmize()");
        }

        @Override
        public void fullscreen() {
            LOG.debug("fullscreen()");
        }
    }

    private static class MockImeHandler implements ImeHandler {
        private static final XeleneseLogger LOG = XeleneseLogger.getLogger(MockImeHandler.class);

        @Override
        public List<String> getAvailableEngines() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getActiveEngine() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean isActivated() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void deactivate() {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void activateEngine(String engine) {
            // TODO Auto-generated method stub
            
        }
        
    }

    private static class MockLogs implements Logs {
        private static final XeleneseLogger LOG = XeleneseLogger.getLogger(MockLogs.class);

        @Override
        public LogEntries get(String logType) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Set<String> getAvailableLogTypes() {
            // TODO Auto-generated method stub
            return null;
        }
        
    }

    private static class MockElement implements WebElement {
        private static XeleneseLogger LOG = XeleneseLogger.getLogger(MockElement.class);
        private String tagName = getClass().getSimpleName();

        @Override
        public <X> X getScreenshotAs(OutputType<X> target)
                throws WebDriverException {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void click() {
            LOG.debug("click()");
        }

        @Override
        public void submit() {
            LOG.debug("submit()");
        }

        @Override
        public void sendKeys(CharSequence... keysToSend) {
            LOG.debug("sendKeys(" + JSON.encode(keysToSend) + ")");
        }

        @Override
        public void clear() {
            LOG.debug("clear()");
        }

        @Override
        public String getTagName() {
            LOG.debug("getTagName()");
            return tagName;
        }

        @Override
        public String getAttribute(String name) {
            LOG.debug("getAttribute(" + name + ")");
            return null;
        }

        @Override
        public boolean isSelected() {
            LOG.debug("isSelected()");
            return false;
        }

        @Override
        public boolean isEnabled() {
            LOG.debug("isEnabled()");
            return false;
        }

        @Override
        public String getText() {
            LOG.debug("getText()");
            return null;
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
        public boolean isDisplayed() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public Point getLocation() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Dimension getSize() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Rectangle getRect() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getCssValue(String propertyName) {
            // TODO Auto-generated method stub
            return null;
        }

        public String toString() {
            return getTagName();
        }
    }
}
