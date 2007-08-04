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
package org.seasar.cayenne.jpa.metadata;

import java.lang.reflect.Field;

import javax.persistence.EntityManagerFactory;

import org.apache.cayenne.access.DataDomain;
import org.apache.cayenne.jpa.ResourceLocalEntityManagerFactory;
import org.apache.cayenne.map.EntityResolver;
import org.apache.cayenne.map.ObjEntity;
import org.seasar.framework.jpa.metadata.EntityDesc;
import org.seasar.framework.jpa.metadata.EntityDescProvider;
import org.seasar.framework.util.tiger.ReflectionUtil;

/**
 * 
 * @author taedium
 */
public class CayenneEntityDescProvider implements EntityDescProvider {

	private static Field domainField = getDomainField();

	public EntityDesc createEntityDesc(final EntityManagerFactory emf,
			final Class<?> entityClass) {
		if (!ResourceLocalEntityManagerFactory.class.isInstance(emf)) {
			return null;
		}
		final DataDomain domain = ReflectionUtil.getValue(domainField, emf);
		final EntityResolver resolver = domain.getEntityResolver();
		final ObjEntity objEntity = resolver.lookupObjEntity(entityClass);
		if (objEntity != null) {
			return new CayenneEntityDesc(entityClass, objEntity, resolver);
		}
		return null;
	}

	private static Field getDomainField() {
		final Field field = ReflectionUtil.getDeclaredField(
				ResourceLocalEntityManagerFactory.class, "domain");
		field.setAccessible(true);
		return field;
	}

}
