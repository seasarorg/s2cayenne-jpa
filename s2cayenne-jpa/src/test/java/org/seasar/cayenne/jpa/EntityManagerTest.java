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
package org.seasar.cayenne.jpa;

import javax.persistence.EntityManager;
import javax.transaction.TransactionManager;

import org.seasar.extension.unit.S2TestCase;

/**
 * 
 * @author taedium
 */
public class EntityManagerTest extends S2TestCase {

	private EntityManager em;

	private TransactionManager tm;

	@Override
	protected void setUp() throws Exception {
		include("javaee5.dicon");
		include("jpa.dicon");
	}

	public void testLookup() throws Exception {
		assertNotNull("1", em);
	}

	public void testFind() throws Exception {
		assertNotNull("1", em.find(Department.class, 10));
	}

	public void testUpdateTx() throws Exception {
		Department dept = em.find(Department.class, 10);
		dept.setDname(dept.getDname() + 2);
		System.out.println(tm.getStatus());
		em.flush();
	}
}