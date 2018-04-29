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

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import com.sun.javafx.scene.web.Debugger;
import com.sun.javafx.webkit.WebConsoleListener;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * 
 */
public class WebWindow implements Initializable {
    private static final WebLogger LOG = WebLogger.getLogger(WebWindow.class);
    private static final String HOME = "http://shorindo.com";
    @FXML private WebView webView;
    @FXML private TextField locationBar;
    @FXML private ListView<String> historyView;
    @FXML private ConsoleView consoleView;
    private WebEngine webEngine;

    public WebWindow() {
    }

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        WebConsoleListener.setDefaultListener((webView, message, lineNumber, sourceId) -> {
            consoleView.log(message);
        });
        consoleView.attach(webView);
        locationBar.setBackground(new Background(new BackgroundFill(
                new Color(0.9, 0.9, 0.9, 0.0),
                new CornerRadii(20.0),
                Insets.EMPTY)));
        locationBar.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                case ENTER:
                    webEngine.load(locationBar.getText());
                    break;
                default:
                }
            }
        });
        locationBar.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable,
                    Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    locationBar.selectAll();
                }
            }
        });
        webView.setFontScale(1.2);
        webEngine = webView.getEngine();
        webEngine.getLoadWorker().stateProperty().addListener(
              new ChangeListener<State>() {
                  @Override public void changed(ObservableValue ov, State oldState, State newState) {
                      switch (newState) {
                      case SCHEDULED:
                          onLoadBefore();
                          break;
                      case SUCCEEDED:
                          onLoadAfter();
                          break;
                      default:
                          break;
                      }
                  }
              });
        webEngine.setOnAlert(new EventHandler<WebEvent<String>>() {
            @Override
            public void handle(WebEvent<String> event) {
                Alert alert = new Alert(AlertType.INFORMATION, event.getData());
                alert.showAndWait();
            }
        });
        webEngine.load(HOME);
    }

    private void onLoadBefore() {
        locationBar.setText(webEngine.getLocation());
    }

    private void onLoadAfter() {
        locationBar.setText(webEngine.getLocation());
        webEngine.setUserStyleSheetLocation(getClass().getResource("style.css").toString());
        webEngine.executeScript(loadResource("script.js"));
        historyView.getItems().add(webEngine.getLocation());

        ((Stage)webView.getScene().getWindow()).setTitle(webEngine.getTitle());
    }

    private String loadResource(String fileName) {
        InputStream is = null;
        try {
            is = getClass().getResourceAsStream(fileName);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] b = new byte[2048];
            int len = 0;
            while ((len = is.read(b)) > 0) {
                baos.write(b, 0, len);
            }
            return new String(baos.toByteArray(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            dispose(is);
        }
        return "";
    }

    private void dispose(Closeable c) {
        if (c != null)
            try {
                c.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
