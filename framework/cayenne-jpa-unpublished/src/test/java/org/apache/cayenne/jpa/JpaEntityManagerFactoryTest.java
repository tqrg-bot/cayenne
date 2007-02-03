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

package org.apache.cayenne.jpa;

import java.util.HashMap;

import junit.framework.TestCase;

public class JpaEntityManagerFactoryTest extends TestCase {

    public void testOpenClose() {
        JpaEntityManagerFactory f = new JpaEntityManagerFactory(
                null,
                new MockPersistenceUnitInfo());
        assertTrue(f.isOpen());

        f.close();

        assertFalse(f.isOpen());

        // check that all methods throw
        try {
            f.close();
            fail("Closed EntityManagerFactory is supposed to throw");
        }
        catch (IllegalStateException e) {
            // expected
        }

        try {
            f.createEntityManager();
            fail("Closed EntityManagerFactory is supposed to throw");
        }
        catch (IllegalStateException e) {
            // expected
        }

        try {
            f.createEntityManager(new HashMap());
            fail("Closed EntityManagerFactory is supposed to throw");
        }
        catch (IllegalStateException e) {
            // expected
        }
    }
}
