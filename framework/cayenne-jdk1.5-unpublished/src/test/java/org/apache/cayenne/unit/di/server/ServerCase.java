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
package org.apache.cayenne.unit.di.server;

import javax.sql.DataSource;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.access.DataNode;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.cayenne.dba.DbAdapter;
import org.apache.cayenne.di.Binder;
import org.apache.cayenne.di.DIBootstrap;
import org.apache.cayenne.di.Injector;
import org.apache.cayenne.di.Module;
import org.apache.cayenne.di.spi.DefaultScope;
import org.apache.cayenne.test.jdbc.DBHelper;
import org.apache.cayenne.unit.AccessStackAdapter;
import org.apache.cayenne.unit.CayenneResources;
import org.apache.cayenne.unit.di.DICase;
import org.apache.cayenne.unit.di.UnitTestLifecycleManager;

public class ServerCase extends DICase {

    // known runtimes... unit tests may reuse these with @UseServerRuntime annotation or
    // can define their own on the fly (TODO: how would that work with the global schema
    // setup?)
    public static final String INHERTITANCE_SINGLE_TABLE1_PROJECT = "cayenne-inheritance-single-table1.xml";
    public static final String INHERTITANCE_VERTICAL_PROJECT = "cayenne-inheritance-vertical.xml";
    public static final String QUOTED_IDENTIFIERS_PROJECT = "cayenne-quoted-identifiers.xml";
    public static final String TESTMAP_PROJECT = "cayenne-testmap.xml";

    private static final Injector injector;

    static {

        // TODO: andrus 6/14/2010 - this should probably also be DI driven
        final CayenneResources resources = CayenneResources.getResources();

        Module module = new Module() {

            public void configure(Binder binder) {
                DefaultScope testScope = new DefaultScope();

                // these are the objects injectable in unit tests that subclass from
                // ServerCase. Note that ServerRuntimeProvider creates ServerRuntime
                // instances complete with their own DI injectors, independent from the
                // unit test injector. ServerRuntime injector contents are customized
                // inside ServerRuntimeProvider.

                // singleton objects
                binder.bind(UnitTestLifecycleManager.class).toInstance(
                        new ServerCaseLifecycleManager(testScope));

                binder.bind(DataSource.class).toProviderInstance(
                        new CayenneResourcesDataSourceProvider(resources));
                binder.bind(DbAdapter.class).toProviderInstance(
                        new CayenneResourcesDbAdapterProvider(resources));
                binder.bind(AccessStackAdapter.class).toProviderInstance(
                        new CayenneResourcesAccessStackAdapterProvider(resources));
                binder.bind(DataNode.class).toProvider(ServerCaseDataNodeProvider.class);
                binder.bind(DataChannelQueryInterceptor.class).to(
                        ServerCaseDataChannelQueryInterceptor.class);

                // test-scoped objects
                binder
                        .bind(ServerCaseProperties.class)
                        .to(ServerCaseProperties.class)
                        .in(testScope);
                binder.bind(ServerRuntime.class).toProviderInstance(
                        new ServerRuntimeProvider(resources)).in(testScope);
                binder.bind(ObjectContext.class).toProvider(
                        ServerCaseObjectContextProvider.class).in(testScope);
                binder.bind(DataContext.class).toProvider(
                        ServerCaseDataContextProvider.class).in(testScope);
                binder
                        .bind(DBHelper.class)
                        .toProvider(FlavoredDBHelperProvider.class)
                        .in(testScope);
            }
        };

        injector = DIBootstrap.createInjector(module);
    }

    @Override
    protected Injector getUnitTestInjector() {
        return injector;
    }
}
