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
package org.seasar.cayenne.jpa.util;

import java.lang.reflect.Field;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.apache.cayenne.jpa.JtaEntityManagerFactory;
import org.seasar.framework.container.annotation.tiger.DestroyMethod;
import org.seasar.framework.jpa.PersistenceUnitManager;
import org.seasar.framework.jpa.PersistenceUnitManagerLocater;
import org.seasar.framework.jpa.impl.PersistenceUnitManagerImpl;
import org.seasar.framework.jpa.impl.PersistenceUnitManagerImpl.ContextMap;
import org.seasar.framework.util.tiger.ReflectionUtil;

/**
 * 
 * @author taedium
 */
public class CayenneResourceDisposer {

	private static Field contextMapField = getContextMapField();

	private static Field entityManagerFactoriesField = getEntityManagerFactoriesField();

	private static Field transactionRegistryField = getTransactionRegistryField();

	@DestroyMethod
	public void dispose() {
		final PersistenceUnitManager manager = PersistenceUnitManagerLocater
				.getInstance();
		if (!PersistenceUnitManagerImpl.class.isInstance(manager)) {
			return;
		}
		ContextMap contextMap = ReflectionUtil.getValue(contextMapField,
				manager);
		if (contextMap == null) {
			return;
		}
		Map<String, EntityManagerFactory> entityManagerFactories = ReflectionUtil
				.getValue(entityManagerFactoriesField, contextMap);
		for (EntityManagerFactory emf : entityManagerFactories.values()) {
			if (emf instanceof JtaEntityManagerFactory) {
				ReflectionUtil.setValue(transactionRegistryField, emf, null);
			}
		}
	}

	private static Field getContextMapField() {
		Field field = ReflectionUtil.getDeclaredField(
				PersistenceUnitManagerImpl.class, "contextMap");
		field.setAccessible(true);
		return field;
	}

	private static Field getEntityManagerFactoriesField() {
		Field field = ReflectionUtil.getDeclaredField(ContextMap.class,
				"entityManagerFactories");
		field.setAccessible(true);
		return field;
	}

	private static Field getTransactionRegistryField() {
		Field field = ReflectionUtil.getDeclaredField(
				JtaEntityManagerFactory.class, "transactionRegistry");
		field.setAccessible(true);
		return field;
	}
}
