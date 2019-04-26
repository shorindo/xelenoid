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
package com.shorindo.xelenoid;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * 
 */
public class WebBrowser extends Application implements WebDriver {
    private static final WebLogger LOG = WebLogger.getLogger(WebBrowser.class);

    /**
     * 
     */
    public static void main(String[] args) {
        launch(WebBrowser.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        NetworknManager.init();

        InputStream is = getClass().getResourceAsStream("xelenoid.fxml");
        Parent root = (Parent)new FXMLLoader().load(is);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.addEventHandler(WindowEvent.WINDOW_SHOWN, new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                LOG.debug("WINDOW_SHOWN");
            }
        });
        stage.show();
    }

    /*==========================================================================
     * WebDriver implementation
     */
    @Override
    public void get(String url) {
        // TODO Auto-generated method stub
        LOG.debug("get(" + url + ")");
    }

    @Override
    public String getCurrentUrl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getTitle() {
        // TODO Auto-generated method stub
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
    public String getPageSource() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void quit() {
        // TODO Auto-generated method stub
        
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
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.openqa.selenium.WebDriver#manage()
     */
    @Override
    public Options manage() {
        // TODO Auto-generated method stub
        return null;
    }

//    public class OnLoadListener implements EventHandler<WebEvent<String>> {
//        @Override
//        public void handle(WebEvent<String> event) {
//            print(event);
//        }
//    }

//    public class WebEventHandler implements EventHandler<MouseEvent> {
//        @Override
//        public void handle(MouseEvent event) {
//            print(event);
//        }
//    }
//
//    private static void print(Object arg) {
//        System.out.println(arg);
//    }
}
