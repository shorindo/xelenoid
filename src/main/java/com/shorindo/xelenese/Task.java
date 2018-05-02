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

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public abstract class Task {
    private List<Task> taskList = new ArrayList<Task>();

    public Task() {
    }

    public abstract String getName();

    public List<Task> getTaskList() {
        return taskList;
    }

    public String toString() {
        return toString(0);
    }
    public String toString(int depth) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth * 2; i++) {
            sb.append(" ");
        }
        sb.append("<" + getName() + ">");
        for (Task task : taskList) {
            sb.append("\n" + task.toString(depth + 1));
        }
        return sb.toString();
    }
}
