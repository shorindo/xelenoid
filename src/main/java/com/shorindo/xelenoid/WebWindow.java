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

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import net.arnx.jsonic.JSON;

import com.shorindo.xelenese.XeleneseException;
import com.sun.javafx.webkit.WebConsoleListener;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebErrorEvent;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * 
 */
public class WebWindow implements Initializable {
    private static final WebLogger LOG = WebLogger.getLogger(WebWindow.class);
    private static final String HOME = "http://shorindo.com";
    @FXML private WebView webView;
    @FXML private TextField locationBar;
    @FXML private TaskView suiteView;
    @FXML private WebView runnerView;
    @FXML private ConsoleView consoleView;
    private WebEngine webEngine;

    /**
     * 
     */
    public WebWindow() {
    }

    /**
     * 
     */
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        WebConsoleListener.setDefaultListener((webView, message, lineNumber, sourceId) -> {
            LOG.debug("console.log(" + message + ")@" + sourceId);
            consoleView.log(message);
        });
        consoleView.attach(webView);

//        webView.addEventHandler(WindowEvent.WINDOW_SHOWN, new EventHandler<WindowEvent>() {
//            @Override
//            public void handle(WindowEvent event) {
//                LOG.debug("INDOW_SHOWN");
//            }
//        });

//        webView.getScene().widthProperty().addListener(new ChangeListener<Number>() {
//            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
//                LOG.debug("width:" + oldSceneWidth + "->" + newSceneWidth);
//            }
//        });

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

        suiteView.load(getClass().getResourceAsStream("sample.xml"));
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
        webEngine.setOnError(new EventHandler<WebErrorEvent>() {
            @Override
            public void handle(WebErrorEvent event) {
                LOG.debug(event.toString());
            }
        });
        webEngine.load(HOME);
    }

    /**
     * 
     */
    private void onLoadBefore() {
        locationBar.setText(webEngine.getLocation());
    }

    /**
     * 
     */
    private void onLoadAfter() {
        Object[] result = RequestQueue.get(webEngine.getLocation());
        LOG.debug("#onLoadAfter() -> queue[" + webEngine.getLocation() + "]=" + JSON.encode(result));
        if (result == null) {
            return;
        } else if ("401".equals(result[0])) {
            showAuthDialog((String)result[1]);
        } else {
            locationBar.setText(webEngine.getLocation());
            webEngine.setUserStyleSheetLocation(getClass().getResource("style.css").toString());
            webEngine.executeScript(loadResource("script.js"));
            ((Stage)webView.getScene().getWindow()).setTitle(webEngine.getTitle());
        }
    }

    /**
     * 
     * @param fileName
     * @return
     */
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

    /**
     * 
     * @param realm
     */
    private void showAuthDialog(String realm) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("basicauth.fxml"));
            loader.load();
            Parent root = loader.getRoot();
            Scene scene = new Scene(root);
            Stage dialog = new Stage(StageStyle.UTILITY);
            dialog.setScene(scene);
            dialog.initOwner(webView.getScene().getWindow());
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setResizable(false);
            dialog.setTitle("Select an Option");
            dialog.showAndWait();
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    private void snapShot() {
        try{
            File outFile = new File("test.png");
            LOG.debug("start snapShot:" + outFile.getAbsolutePath());
            WritableImage img = webView.snapshot(new SnapshotParameters(), null);
            ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", outFile);
            LOG.debug("end snapShot:" + outFile.getAbsolutePath());
        } catch(Exception ex) {
            LOG.error(ex);
        }
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
