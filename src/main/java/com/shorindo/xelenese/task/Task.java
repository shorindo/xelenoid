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
package com.shorindo.xelenese.task;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;

import com.shorindo.xelenese.XeleneseException;
import com.shorindo.xelenese.XeleneseLogger;
import com.shorindo.xelenese.annotation.TaskName;

/**
 * 
 */
public abstract class Task {
    private static final XeleneseLogger LOG = XeleneseLogger.getLogger(Task.class);
    protected static final String ON_ERROR_IGNORE = "ignore";
    protected static final String ON_ERROR_FAILURE = "failure";
    protected static final String ON_ERROR_SUCCESS = "success";
    private Task parent;
    private String title;
    private StringBuilder text = new StringBuilder();
    private String onError = ON_ERROR_FAILURE;
    private List<Task> taskList = new ArrayList<Task>();

    /**
     * コンストラクタ
     * 
     * @param parent 親タスク
     */
    public Task(Task parent) {
        this.parent = parent;
        if (parent != null) {
            parent.getTaskList().add(this);
        }
    }

    /**
     * タスクを実行する
     * 
     * @param args タスクの引数
     * @return タスクが成功したらtrue、失敗したらfalseを返す
     * @throws XeleneseException
     */
    public abstract List<ExecutionError> execute(Object...args) throws XeleneseException;

    /**
     * タスクの設定値を検証する
     * 
     * @return 検証エラーのリスト。検証OKなら空のリストとなる。
     * @throws XeleneseException
     */
    public abstract List<ValidationError> validate() throws XeleneseException;

    public final String getTaskName() {
        TaskName taskName = getClass().getAnnotation(TaskName.class);
        if (taskName != null) {
            return taskName.value();
        } else {
            return null;
        }
    }

    public Task getParent() {
        return parent;
    }

    public void setParent(Task parent) {
        this.parent = parent;
    }

    public String getText() {
        return text.toString();
    }

    public void addText(String text) {
        this.text.append(text);
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public WebDriver getDriver() {
        Task parent = this;
        while (parent != null && !(parent instanceof SuiteTask)) {
            parent = parent.getParent();
        }
        return ((SuiteTask)parent).getDriver();
    }

    public final void setTitle(String title) {
        this.title = title;
    }

    public final String getTitle() {
        return title;
    }

    public void setOnError(String action) {
        this.onError = action;
    }

    public String getOnError() {
        return onError;
    }

    public String toString() {
        return toString(0);
    }

    private String toString(int depth) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth * 2; i++) {
            sb.append(" ");
        }
        sb.append("<" + getTaskName() + ">");
        for (Task task : taskList) {
            sb.append("\n" + task.toString(depth + 1));
        }
        return sb.toString();
    }
}
