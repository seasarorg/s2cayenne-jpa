/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.cayenne.jpa.impl;

import java.lang.reflect.Field;
import java.sql.Connection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.cayenne.access.DataDomain;
import org.apache.cayenne.access.DataNode;
import org.apache.cayenne.jpa.Provider;
import org.apache.cayenne.jpa.ResourceLocalEntityManagerFactory;
import org.seasar.extension.jdbc.util.DataSourceUtil;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.DestroyMethod;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.jpa.Dialect;
import org.seasar.framework.jpa.DialectManager;
import org.seasar.framework.jpa.impl.TxScopedEntityManagerProxy;
import org.seasar.framework.util.tiger.ReflectionUtil;

/**
 * 
 * @author taedium
 */
public class S2CayenneDialect implements Dialect {

    private static Field emfField = getEmfField();

    private static Field domainField = getDomainField();

    @Binding(bindingType = BindingType.MUST)
    protected DialectManager dialectManager;

    @InitMethod
    public void initialize() {
        dialectManager.addDialect(Provider.class, this);
    }

    @DestroyMethod
    public void destroy() {
        dialectManager.removeDialect(Provider.class);
    }

    public Connection getConnection(final EntityManager em) {
        if (!TxScopedEntityManagerProxy.class.isInstance(em)) {
            return null;
        }
        EntityManagerFactory emf = ReflectionUtil.getValue(emfField, em);
        if (!ResourceLocalEntityManagerFactory.class.isInstance(emf)) {
            return null;
        }
        DataDomain domain = ReflectionUtil.getValue(domainField, emf);
        String name = domain.getName();
        DataNode dataNode = domain.getNode(name);
        DataSource dataSource = dataNode.getDataSource();
        return DataSourceUtil.getConnection(dataSource);
    }

    public void detach(EntityManager em, Object managedEntity) {
        throw new UnsupportedOperationException("detach(EntityManager, Object)");
    }

    private static Field getEmfField() {
        final Field field = ReflectionUtil
                .getDeclaredField(TxScopedEntityManagerProxy.class, "emf");
        field.setAccessible(true);
        return field;
    }

    private static Field getDomainField() {
        final Field field = ReflectionUtil.getDeclaredField(
                ResourceLocalEntityManagerFactory.class, "domain");
        field.setAccessible(true);
        return field;
    }
}
