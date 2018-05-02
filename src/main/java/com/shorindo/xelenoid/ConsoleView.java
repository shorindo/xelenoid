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

import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.util.Callback;

/**
 * 
 */
public class ConsoleView extends ListView<String> {
    private static final WebLogger LOG = WebLogger.getLogger(ConsoleView.class);
    private static WebView webView;

    public ConsoleView() {
        List<PromptListCell> cellList = new ArrayList<PromptListCell>();
        setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> listView) {
                PromptListCell cell = new PromptListCell();
                cellList.add(cell);
                return cell;
            }
        });
        focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable,
                    Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    for (PromptListCell cell : cellList) {
                        if (cell.isEditable()) {
                            cell.focus();
                            break;
                        }
                    }
                }
            }
        });
        getItems().add("");
    }

    public ConsoleView(ObservableList<String> list) {
        super(list);
    }

    public void log(String message) {
        getItems().add(getItems().size() - 1, message);
    }

    // FIXME
    public void attach(WebView webView) {
        ConsoleView.webView = webView;
    }

    public static class PromptListCell extends ListCell<String> {
        private HBox container;
        private Label label;
        private TextField textField;

        public PromptListCell() {
            label = new Label("js> ");
            textField = new TextField();
            textField.setEditable(true);
            textField.setBorder(Border.EMPTY);
            textField.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
            textField.setPadding(Insets.EMPTY);
            textField.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    switch (event.getCode()) {
                    case ENTER:
                        commitEdit(textField.getText());
                        textField.clear();
                        break;
                    case L:
                        if (event.isControlDown()) {
                            clear();
                        }
                        break;
                    default:
                    }
                }});
            HBox.setHgrow(textField, Priority.ALWAYS);
            container = new HBox();
            container.getChildren().add(label);
            container.getChildren().add(textField);
            backgroundProperty().set(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            setStyle("-fx-padding: 0px 5px 0px 5px");
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
                setEditable(false);
            } else if (getIndex() == getListView().getItems().size() - 1) {
                setText(null);
                setGraphic(container);
                setEditable(true);
                textField.requestFocus();
            } else {
                setText(item);
                setGraphic(null);
                setEditable(false);
            }
        }

        @Override
        public void commitEdit(String script) {
            super.commitEdit(script);
            getListView().getItems().add(
                    getListView().getItems().size() - 1,
                    "js> " + script);
            if (webView != null) {
                try {
                    Object result = webView.getEngine().executeScript(script);
                    getListView().getItems().add(
                            getListView().getItems().size() - 1,
                            result.toString());
                } catch (Exception e) {
                    getListView().getItems().add(
                            getListView().getItems().size() - 1,
                            e.getMessage());
                }
            }
        }

        public void focus() {
            textField.requestFocus();
        }

        public void clear() {
            getListView().getItems().clear();
            getListView().getItems().add("");
            focus();
        }
    }
}
