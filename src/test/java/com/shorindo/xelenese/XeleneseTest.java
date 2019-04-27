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

import static org.junit.Assert.*;

import java.io.File;
import java.io.InputStream;

import javax.servlet.ServletContext;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.JarScanFilter;
import org.apache.tomcat.JarScanType;
import org.apache.tomcat.JarScanner;
import org.apache.tomcat.JarScannerCallback;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * 
 */
public class XeleneseTest {
    private static Tomcat tomcat;

    @BeforeClass
    public static void setUp() throws Exception {
//        System.setProperty("webdriver.gecko.driver", "./exe/geckodriver.exe");
//        DesiredCapabilities cap = DesiredCapabilities.firefox();
//        cap.setCapability("marionette", true);

        tomcat = new Tomcat();
        tomcat.setHostname("localhost");
        tomcat.setPort(8880);
        tomcat.getConnector().setURIEncoding("UTF-8");

        Context ctx = tomcat.addWebapp(null, "", new File("WebContent").getAbsolutePath());
        ctx.setAltDDName("WebContent/WEB-INF/web.xml");
        ctx.setJarScanner(new JarScanner() {
            @Override
            public void scan(JarScanType scanType, ServletContext context,
                    JarScannerCallback callback) {
            }

            @Override
            public JarScanFilter getJarScanFilter() {
                return null;
            }

            @Override
            public void setJarScanFilter(JarScanFilter jarScanFilter) {
            }
        });

        tomcat.start();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        tomcat.stop();
    }

    @Test
    public void test() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("xelenese-test.xml");
        try {
            Xelenese xelenese = new Xelenese(is);
            xelenese.run();
        } finally {
            is.close();
        }
    }

}
