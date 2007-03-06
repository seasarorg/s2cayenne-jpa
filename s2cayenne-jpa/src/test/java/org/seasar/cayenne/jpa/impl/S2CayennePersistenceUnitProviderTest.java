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

import javax.persistence.EntityManagerFactory;

import org.apache.cayenne.jpa.CayenneEntityManager;
import org.apache.cayenne.map.EntityResolver;
import org.seasar.cayenne.jpa.Department;
import org.seasar.cayenne.jpa.Employee;
import org.seasar.cayenne.jpa.entity.Department2;
import org.seasar.cayenne.jpa.entity.Employee2;
import org.seasar.cayenne.jpa.entity.aaa.Dummy;
import org.seasar.cayenne.jpa.entity.aaa.Dummy2;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.env.Env;
import org.seasar.framework.jpa.PersistenceUnitManager;

/**
 * 
 * @author nakamura
 */
public class S2CayennePersistenceUnitProviderTest extends S2TestCase {

	private EntityManagerFactory emf;

	public void setUpReadPersistenceXmlFile() throws Exception {
		Env.setFilePath("org/seasar/cayenne/jpa/impl/test1.txt");
		include(getClass().getName().replace('.', '/') + ".dicon");
	}

	public void testReadPersistenceXmlFile() throws Exception {
		CayenneEntityManager em = CayenneEntityManager.class.cast(emf
				.createEntityManager());
		EntityResolver resolver = em.getChannel().getEntityResolver();
		assertEquals(2, resolver.getObjEntities().size());
		assertNotNull(resolver.lookupObjEntity(Department.class));
		assertNotNull(resolver.lookupObjEntity(Employee.class));
	}

	public void setUpAddMappingFile() throws Exception {
		Env.setFilePath("org/seasar/cayenne/jpa/impl/test2.txt");
		include(getClass().getName().replace('.', '/') + ".dicon");
	}

	// マッピングファイルの定義を認識していないためテストが通らない
	public void ignore_testAddMappingFile() throws Exception {
		CayenneEntityManager em = CayenneEntityManager.class.cast(emf
				.createEntityManager());
		EntityResolver resolver = em.getChannel().getEntityResolver();
		assertEquals(3, resolver.getObjEntities().size());
		assertNotNull(resolver.lookupObjEntity(Department.class));
		assertNotNull(resolver.lookupObjEntity(Employee.class));
		assertNotNull(resolver.lookupObjEntity(Employee2.class));
	}

	public void setUpAddPersistenceClass() throws Exception {
		Env.setFilePath("org/seasar/cayenne/jpa/impl/test3.txt");
		include(getClass().getName().replace('.', '/') + ".dicon");
	}

	public void testAddPersistenceClass() throws Exception {
		CayenneEntityManager em = CayenneEntityManager.class.cast(emf
				.createEntityManager());
		EntityResolver resolver = em.getChannel().getEntityResolver();
		assertEquals(3, resolver.getObjEntities().size());
		assertNotNull(resolver.lookupObjEntity(Department.class));
		assertNotNull(resolver.lookupObjEntity(Employee.class));
		assertNotNull(resolver.lookupObjEntity(Department2.class));
	}

	public void setUpMappingFileAutoDetection() throws Exception {
		Env.setFilePath("org/seasar/cayenne/jpa/impl/test4.txt");
		include(getClass().getName().replace('.', '/') + ".dicon");
	}

	// マッピングファイルの定義を認識していないためテストが通らない
	public void ignore_testMappingFileAutoDetection() throws Exception {
		CayenneEntityManager em = CayenneEntityManager.class.cast(emf
				.createEntityManager());
		EntityResolver resolver = em.getChannel().getEntityResolver();
		assertEquals(3, resolver.getObjEntities().size());
		assertNotNull(resolver.lookupObjEntity(Department.class));
		assertNotNull(resolver.lookupObjEntity(Employee.class));
		assertNotNull(resolver.lookupObjEntity(Department2.class));
	}

	public void setUpMappingFileAutoDetectionSubPackage() throws Exception {
		Env.setFilePath("org/seasar/cayenne/jpa/impl/test4.txt");
		include(getClass().getName().replace('.', '/') + ".dicon");
	}

	// マッピングファイルの定義を認識していないためテストが通らない
	public void ignore_testMappingFileAutoDetectionSubPackage()
			throws Exception {
		PersistenceUnitManager pum = PersistenceUnitManager.class
				.cast(getComponent(PersistenceUnitManager.class));
		EntityManagerFactory emf = pum
				.getEntityManagerFactory("aaaPersistenceUnit");
		CayenneEntityManager em = CayenneEntityManager.class.cast(emf
				.createEntityManager());
		EntityResolver resolver = em.getChannel().getEntityResolver();
		assertEquals(3, resolver.getObjEntities().size());
		assertNotNull(resolver.lookupObjEntity(Dummy2.class));
	}

	public void setUpPersistenceClassAutoDetection() throws Exception {
		Env.setFilePath("org/seasar/cayenne/jpa/impl/test5.txt");
		include(getClass().getName().replace('.', '/') + ".dicon");
	}

	public void testPersistenceClassAutoDetection() throws Exception {
		CayenneEntityManager em = CayenneEntityManager.class.cast(emf
				.createEntityManager());
		EntityResolver resolver = em.getChannel().getEntityResolver();
		assertEquals(3, resolver.getObjEntities().size());
		assertNotNull(resolver.lookupObjEntity(Department.class));
		assertNotNull(resolver.lookupObjEntity(Employee.class));
		assertNotNull(resolver.lookupObjEntity(Department2.class));
	}

	public void setUpPersistenceClassAutoDetectionSubPackage() throws Exception {
		Env.setFilePath("org/seasar/cayenne/jpa/impl/test5.txt");
		include(getClass().getName().replace('.', '/') + ".dicon");
	}

	public void testPersistenceClassAutoDetectionSubPackage() throws Exception {
		PersistenceUnitManager pum = PersistenceUnitManager.class
				.cast(getComponent(PersistenceUnitManager.class));
		EntityManagerFactory emf = pum
				.getEntityManagerFactory("aaaPersistenceUnit");
		CayenneEntityManager em = CayenneEntityManager.class.cast(emf
				.createEntityManager());
		EntityResolver resolver = em.getChannel().getEntityResolver();
		assertEquals(3, resolver.getObjEntities().size());
		assertNotNull(resolver.lookupObjEntity(Dummy.class));
	}

}
