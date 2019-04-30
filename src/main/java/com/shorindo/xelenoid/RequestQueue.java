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

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 
 */
public class RequestQueue {
    private static Map<String,Queue<Object[]>> queueMap = new ConcurrentHashMap<String,Queue<Object[]>>();

    public static synchronized void put(String key, Object[] values) {
        Queue<Object[]> queue = queueMap.get(key);
        if (queue == null) {
            queue = new ConcurrentLinkedQueue<Object[]>();
            queueMap.put(key, queue);
        }
        queue.add(values);
    }

    public static synchronized Object[] get(String key) {
        Object[] result = null;
        Queue<Object[]> queue = queueMap.get(key);
        if (queue != null) {
            result = queue.poll();
            if (queue.size() == 0) {
                queueMap.remove(key);
            }
        }
        return result;
    }
}
