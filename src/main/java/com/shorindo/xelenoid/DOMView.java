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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.util.Callback;

/**
 * 
 */
public class DOMView extends TreeView<Node> {
    private static final WebLogger LOG = WebLogger.getLogger(DOMView.class);

    public DOMView() {
        List<DOMCell> cellList = new ArrayList<DOMCell>();
        setCellFactory(new Callback<TreeView<Node>, TreeCell<Node>>() {
            @Override
            public TreeCell<Node> call(TreeView<Node> listView) {
                DOMCell cell = new DOMCell();
                cellList.add(cell);
                return cell;
            }
        });
//        setStyle("-fx-padding: 0px");
    }

    public DOMView(TreeItem<Node> root) {
        super(root);
        root.setExpanded(true);
    }

    public void load(InputStream is) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(is);
            Node node = document.getDocumentElement();
            trim(node);
            setRoot(createTreeItem(node));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TreeItem<Node> createTreeItem(Node node) {
        //LOG.debug("createTreeItem(" + node.getNodeName() + ")");
        TreeItem<Node> item = new TreeItem<Node>(node);
        if (node.hasChildNodes()) {
            NodeList childNodes = node.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node child = childNodes.item(i);
                item.getChildren().add(createTreeItem(child));
            }
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

    public static class DOMCell extends TreeCell<Node> {

        public DOMCell() {
            setStyle("-fx-padding: 0px");
        }

        @Override
        protected void updateItem(Node item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
            } else {
                setText("<" + item.getNodeName() + ">");
            }
        }
    }
}
