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
package com.shorindo.selenade;

import java.io.InputStream;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEvent;
import javafx.stage.Stage;

/**
 * 
 */
public class WebBrowser extends Application {

    /**
     * 
     */
    public static void main(String[] args) {
        launch(WebBrowser.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        InputStream is = getClass().getResourceAsStream("jfxdriver.fxml");
        Parent root = (Parent)new FXMLLoader().load(is);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public class OnLoadListener implements EventHandler<WebEvent<String>> {
        @Override
        public void handle(WebEvent<String> event) {
            print(event);
        }
    }

    public class WebEventHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            print(event);
        }
    }

    private static void print(Object arg) {
        System.out.println(arg);
    }
}
