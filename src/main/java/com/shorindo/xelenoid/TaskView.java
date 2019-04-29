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
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.shorindo.xelenese.Xelenese;
import com.shorindo.xelenese.XeleneseException;
import com.shorindo.xelenese.task.Task;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.util.Callback;

/**
 * 
 */
public class TaskView extends TreeView<Task> {
    private static final WebLogger LOG = WebLogger.getLogger(TaskView.class);
    private Xelenese xelenese;

    public TaskView() {
        List<DOMCell> cellList = new ArrayList<DOMCell>();
        setCellFactory(new Callback<TreeView<Task>, TreeCell<Task>>() {
            @Override
            public TreeCell<Task> call(TreeView<Task> listView) {
                DOMCell cell = new DOMCell();
                cellList.add(cell);
                return cell;
            }
        });
    }

    public TaskView(TreeItem<Task> root) {
        super(root);
        root.setExpanded(true);
    }

    public void load(InputStream is) {
        try {
            xelenese = new Xelenese(is);
            setRoot(createTreeItem(xelenese.getRoot()));
        } catch (XeleneseException e) {
            e.printStackTrace();
        }
    }

    public TreeItem<Task> createTreeItem(Task task) {
        //LOG.debug("createTreeItem(" + node.getNodeName() + ")");
        TreeItem<Task> item = new TreeItem<Task>(task);
        for (Task child : task.getTaskList()) {
            item.getChildren().add(createTreeItem(child));
        }
        return item;
    }

    public void trim(Node node) {
        if (node.hasChildNodes()) {
            NodeList childNodes = node.getChildNodes();
            for (int i = childNodes.getLength() - 1; i >= 0; i--) {
                Node child = childNodes.item(i);
                if (child.getNodeType() == Node.TEXT_NODE) {
                    String value = child.getNodeValue();
                    if (value.matches("^(?m)\\s*$")) {
                        node.removeChild(child);
                    }
                } else {
                    trim(child);
                }
            }
        }
    }

    public void run() throws XeleneseException {
        xelenese.run();
    }

    public static class DOMCell extends TreeCell<Task> {

        public DOMCell() {
            setStyle("-fx-padding: 0px");
        }

        @Override
        protected void updateItem(Task item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
            } else {
                setText("<" + item.getTaskName() + ">");
            }
        }
    }
}
