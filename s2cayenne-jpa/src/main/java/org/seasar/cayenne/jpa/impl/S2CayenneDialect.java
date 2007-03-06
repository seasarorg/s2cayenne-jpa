/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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

import java.sql.Connection;

import javax.persistence.EntityManager;
import javax.persistence.spi.PersistenceUnitInfo;

import org.apache.cayenne.jpa.Provider;
import org.seasar.extension.jdbc.util.DataSourceUtil;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.DestroyMethod;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.jpa.Dialect;
import org.seasar.framework.jpa.DialectManager;

/**
 * 
 * @author nakamura
 */
public class S2CayenneDialect implements Dialect {

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
		final Object delegate = em.getDelegate();
		final S2CayennePersistenceUnitProvider provider = S2CayennePersistenceUnitProvider.class
				.cast(delegate);
		final PersistenceUnitInfo unitInfo = provider.getPersistenceUnitInfo();
		return DataSourceUtil.getConnection(unitInfo.getJtaDataSource());
	}

}
