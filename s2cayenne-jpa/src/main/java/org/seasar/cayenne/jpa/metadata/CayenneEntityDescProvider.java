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

import javax.persistence.EntityManagerFactory;

import org.apache.cayenne.jpa.CayenneEntityManager;
import org.apache.cayenne.map.EntityResolver;
import org.apache.cayenne.map.ObjEntity;
import org.seasar.framework.container.annotation.tiger.DestroyMethod;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.jpa.metadata.EntityDesc;
import org.seasar.framework.jpa.metadata.EntityDescFactory;
import org.seasar.framework.jpa.metadata.EntityDescProvider;

/**
 * 
 * @author nakamura
 */
public class CayenneEntityDescProvider implements EntityDescProvider {

	protected EntityManagerFactory emf;

	public CayenneEntityDescProvider(final EntityManagerFactory emf) {
		this.emf = emf;
	}

	@InitMethod
	public void register() {
		EntityDescFactory.addProvider(this);
	}

	@DestroyMethod
	public void unregister() {
		EntityDescFactory.removeProvider(this);
	}

	public EntityDesc createEntityDesc(final Class<?> entityClass) {
		final CayenneEntityManager em = CayenneEntityManager.class.cast(emf
				.createEntityManager());
		final EntityResolver resolver = em.getChannel().getEntityResolver();
		final ObjEntity objEntity = resolver.lookupObjEntity(entityClass);
		if (objEntity != null) {
			return new CayenneEntityDesc(entityClass, objEntity, resolver);
		}
		return null;
	}

}
