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

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;

import org.apache.cayenne.jpa.JpaUnit;
import org.apache.cayenne.jpa.Provider;
import org.seasar.cayenne.jpa.S2CayenneConfiguration;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.DestroyMethod;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.jpa.PersistenceUnitManager;
import org.seasar.framework.jpa.PersistenceUnitProvider;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ClassTraversal.ClassHandler;
import org.seasar.framework.util.ResourceTraversal.ResourceHandler;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * 
 * @author nakamura
 */
public class S2CayennePersistenceUnitProvider extends Provider implements
		PersistenceUnitProvider {

	private static final Logger logger = Logger
			.getLogger(S2CayennePersistenceUnitProvider.class);

	@Binding(bindingType = BindingType.MUST)
	protected PersistenceUnitManager persistenceUnitManager;

	@Binding(bindingType = BindingType.MUST)
	private S2CayenneConfiguration configuration;

	private JpaUnit jpaUnit;

	@InitMethod
	public void register() {
		persistenceUnitManager.addProvider(this);
	}

	@DestroyMethod
	public void unregister() {
		persistenceUnitManager.removeProvider(this);
	}

	public PersistenceUnitInfo getPersistenceUnitInfo() {
		return jpaUnit;
	}

	public EntityManagerFactory createEntityManagerFactory(final String unitName) {
		jpaUnit = loadUnit(unitName);
		if (jpaUnit == null) {
			return null;
		}

		final Properties properties = jpaUnit.getProperties();
		for (Map.Entry<?, ?> property : defaultProperties.entrySet()) {
			if (!properties.containsKey(property.getKey())) {
				properties.put(property.getKey(), property.getValue());
			}
		}

		final String providerName = jpaUnit.getPersistenceProviderClassName();
		if (providerName != null
				&& !providerName.equals(Provider.class.getName())) {
			return null;
		}

		configure(jpaUnit, unitName);
		return createContainerEntityManagerFactory(jpaUnit, null);
	}

	@Override
	protected JpaUnit loadUnit(final String unitName) {
		final JpaUnit jpaUnit = super.loadUnit(unitName);
		return new S2CayennePersistenceUnitInfo(jpaUnit);
	}

	protected void configure(final JpaUnit jpaUnit, final String unitName) {
		final ClassLoader originalLoader = Thread.currentThread()
				.getContextClassLoader();
		Thread.currentThread().setContextClassLoader(
				jpaUnit.getNewTempClassLoader());
		try {
			addMappingFiles(unitName, jpaUnit);
			addAnnotatedClasses(unitName, jpaUnit);
		} finally {
			Thread.currentThread().setContextClassLoader(originalLoader);
		}
	}

	protected void addMappingFiles(final String unitName, final JpaUnit jpaUnit) {
		configuration.detectMappingFiles(unitName, new MappingFileHandler(
				unitName, jpaUnit));
	}

	protected void addAnnotatedClasses(final String unitName,
			final JpaUnit jpaUnit) {
		configuration.detectPersistenceClasses(unitName,
				new PersistenceClassHandler(unitName, jpaUnit));
	}

	public static class MappingFileHandler implements ResourceHandler {

		protected final String unitName;

		protected final JpaUnit jpaUnit;

		public MappingFileHandler(final String unitName, final JpaUnit jpaUnit) {
			this.unitName = unitName;
			this.jpaUnit = jpaUnit;
		}

		public void processResource(final String path, final InputStream is) {
			if (logger.isDebugEnabled()) {
				logger.log("DCYNJPA0001", new Object[] { path, unitName });
			}
			jpaUnit.addMappingFileName(path);
		}
	}

	public static class PersistenceClassHandler implements ClassHandler {

		protected final String unitName;

		protected final JpaUnit jpaUnit;

		protected final Set<String> packageNames = CollectionsUtil.newHashSet();

		public PersistenceClassHandler(final String unitName,
				final JpaUnit jpaUnit) {
			this.unitName = unitName;
			this.jpaUnit = jpaUnit;
		}

		public void processClass(final String packageName,
				final String shortClassName) {
			final String className = ClassUtil.concatName(packageName,
					shortClassName);
			if (logger.isDebugEnabled()) {
				logger.log("DCYNJPA0002", new Object[] { className, unitName });
			}
			jpaUnit.addManagedClassName(className);
		}
	}
}
