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
import java.lang.reflect.Method;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;

import org.apache.cayenne.Persistent;
import org.apache.cayenne.jpa.JpaUnit;
import org.seasar.framework.exception.ClassNotFoundRuntimeException;
import org.seasar.framework.util.ClassLoaderUtil;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.InputStreamUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.tiger.ReflectionUtil;

/**
 * 
 * @author nakamura
 */
public class S2CayennePersistenceUnitInfo extends JpaUnit {

	private static Method defineClassMethod;

	static {
		defineClassMethod = ReflectionUtil
				.getDeclaredMethod(ClassLoader.class, "defineClass",
						String.class, byte[].class, int.class, int.class);
		defineClassMethod.setAccessible(true);
	}

	private JpaUnit jpaUnit;

	public S2CayennePersistenceUnitInfo(final JpaUnit jpaUnit) {
		this.jpaUnit = jpaUnit;
	}

	@Override
	public void addTransformer(final ClassTransformer transformer) {
		final ClassLoader loader = Persistent.class.getClassLoader();
		for (final String className : getManagedClassNames()) {
			if (isLoaded(loader, className)) {
				continue;
			}
			final String path = ClassUtil.getResourcePath(className);
			final InputStream is = ResourceUtil.getResourceAsStream(path);
			byte[] bytes = null;
			try {
				bytes = InputStreamUtil.getBytes(is);
			} finally {
				InputStreamUtil.close(is);
			}
			final byte[] transBytes = transform(transformer, loader, className,
					null, null, bytes);
			if (transBytes != null) {
				ReflectionUtil.invoke(defineClassMethod, loader, className,
						transBytes, 0, transBytes.length);
			}
		}
	}

	// TODO
	protected byte[] transform(final ClassTransformer transformer,
			final ClassLoader loader, final String className,
			final Class<?> classBeingRedefined,
			final ProtectionDomain protectionDomain,
			final byte[] classfileBuffer) {
		try {
			return transformer.transform(loader, className,
					classBeingRedefined, protectionDomain, classfileBuffer);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	// TODO
	protected boolean isLoaded(final ClassLoader loader, final String className) {
		try {
			return ClassLoaderUtil.findLoadedClass(loader, className) != null;
		} catch (final ClassNotFoundException e) {
			throw new ClassNotFoundRuntimeException(e);
		}
	}

	@Override
	public void addJarFileUrl(final String jarName) {
		getJpaUnit().addJarFileUrl(jarName);
	}

	@Override
	public void addManagedClassName(final String managedClassName) {
		getJpaUnit().addManagedClassName(managedClassName);
	}

	@Override
	public void addMappingFileName(final String mappingFileName) {
		getJpaUnit().addMappingFileName(mappingFileName);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void addProperties(final Map properties) {
		getJpaUnit().addProperties(properties);
	}

	@Override
	public boolean excludeUnlistedClasses() {
		return getJpaUnit().excludeUnlistedClasses();
	}

	@Override
	public ClassLoader getClassLoader() {
		return getJpaUnit().getClassLoader();
	}

	@Override
	public String getDescription() {
		return getJpaUnit().getDescription();
	}

	@Override
	public List<URL> getJarFileUrls() {
		return getJpaUnit().getJarFileUrls();
	}

	@Override
	public DataSource getJtaDataSource() {
		return getJpaUnit().getJtaDataSource();
	}

	@Override
	public List<String> getManagedClassNames() {
		return getJpaUnit().getManagedClassNames();
	}

	@Override
	public List<String> getMappingFileNames() {
		return getJpaUnit().getMappingFileNames();
	}

	@Override
	public ClassLoader getNewTempClassLoader() {
		return getJpaUnit().getNewTempClassLoader();
	}

	@Override
	public DataSource getNonJtaDataSource() {
		return getJpaUnit().getNonJtaDataSource();
	}

	@Override
	public String getPersistenceProviderClassName() {
		return getJpaUnit().getPersistenceProviderClassName();
	}

	@Override
	public String getPersistenceUnitName() {
		return getJpaUnit().getPersistenceUnitName();
	}

	@Override
	public URL getPersistenceUnitRootUrl() {
		return getJpaUnit().getPersistenceUnitRootUrl();
	}

	@Override
	public Properties getProperties() {
		return getJpaUnit().getProperties();
	}

	@Override
	public PersistenceUnitTransactionType getTransactionType() {
		return getJpaUnit().getTransactionType();
	}

	@Override
	public void putProperty(final String key, final String value) {
		getJpaUnit().putProperty(key, value);
	}

	@Override
	public void setClassLoader(final ClassLoader classLoader) {
		getJpaUnit().setClassLoader(classLoader);
	}

	@Override
	public void setDescription(final String description) {
		getJpaUnit().setDescription(description);
	}

	@Override
	public void setExcludeUnlistedClasses(final boolean excludeUnlistedClasses) {
		getJpaUnit().setExcludeUnlistedClasses(excludeUnlistedClasses);
	}

	@Override
	public void setPersistenceUnitName(final String persistenceUnitName) {
		getJpaUnit().setPersistenceUnitName(persistenceUnitName);
	}

	@Override
	public void setPersistenceUnitRootUrl(final URL persistenceUnitRootUrl) {
		getJpaUnit().setPersistenceUnitRootUrl(persistenceUnitRootUrl);
	}

	protected JpaUnit getJpaUnit() {
		return jpaUnit;
	}

}
