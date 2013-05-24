/*****************************************************************
 *   Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 ****************************************************************/
package org.apache.cayenne.access.jdbc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.cayenne.access.ResultIterator;

/**
 * A ResultIterator over a collection of objects.
 * 
 * @since 3.2
 */
public class CollectionResultIterator<T> implements ResultIterator<T> {

    protected Iterator<T> iterator;

    public CollectionResultIterator(Collection<T> c) {
        this.iterator = c.iterator();
    }

    public Iterator<T> iterator() {
        checkIterator();
        return iterator;
    }

    public List<T> allRows() {

        List<T> list = new ArrayList<T>();
        for (T t : this) {
            list.add(t);
        }

        return list;
    }

    public boolean hasNextRow() {
        checkIterator();
        return iterator.hasNext();
    }

    public T nextRow() {
        checkIterator();
        return iterator.next();
    }

    public void skipRow() {
        checkIterator();
        iterator.next();
    }

    public void close() {
        iterator = null;
    }

    protected void checkIterator() {
        if (iterator == null) {
            throw new IllegalStateException("Iterator is closed");
        }
    }

}
