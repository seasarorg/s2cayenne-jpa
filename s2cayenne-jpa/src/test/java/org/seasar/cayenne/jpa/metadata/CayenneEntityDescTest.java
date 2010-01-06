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
package org.seasar.cayenne.jpa.metadata;

import java.sql.Types;
import java.util.Date;
import java.util.Set;

import javax.persistence.TemporalType;

import org.seasar.cayenne.jpa.Address;
import org.seasar.cayenne.jpa.Department;
import org.seasar.cayenne.jpa.Employee;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.jpa.metadata.AttributeDesc;
import org.seasar.framework.jpa.metadata.EntityDesc;
import org.seasar.framework.jpa.metadata.EntityDescFactory;

/**
 * 
 * @author taedium
 */
public class CayenneEntityDescTest extends S2TestCase {

	@Override
	protected void setUp() throws Exception {
		include("javaee5.dicon");
		include("jpa.dicon");
	}

	public void test() throws Exception {
		EntityDesc entityDesc = EntityDescFactory
				.getEntityDesc(Department.class);
		assertNotNull(entityDesc);
	}

	public void ignore_testDepartment() throws Exception {
		Department dept = new Department();
		EntityDesc entityDesc = EntityDescFactory
				.getEntityDesc(Department.class);
		assertNotNull(entityDesc);
		assertEquals("Dept", entityDesc.getEntityName());
		assertEquals("deptno", entityDesc.getIdAttributeDesc().getName());
		String[] propNames = entityDesc.getAttributeNames();
		assertNotNull(propNames);
		assertEquals(6, propNames.length);
		assertNotNull(entityDesc.getAttributeDesc("deptno"));
		assertNotNull(entityDesc.getAttributeDesc("dname"));
		assertNotNull(entityDesc.getAttributeDesc("loc"));
		assertNotNull(entityDesc.getAttributeDesc("versionNo"));
		assertNotNull(entityDesc.getAttributeDesc("active"));
		assertNotNull(entityDesc.getAttributeDesc("employees"));

		AttributeDesc attribute = entityDesc.getAttributeDesc("deptno");
		assertEquals("deptno", attribute.getName());
		assertEquals(int.class, attribute.getType());
		assertEquals(Types.INTEGER, attribute.getSqlType());
		assertNull(attribute.getTemporalType());
		assertTrue(attribute.isId());
		assertFalse(attribute.isAssociation());
		assertFalse(attribute.isCollection());
		assertFalse(attribute.isComponent());
		assertFalse(attribute.isVersion());
		attribute.setValue(dept, 100);
		assertEquals(100, dept.getDeptno());

		attribute = entityDesc.getAttributeDesc("dname");
		assertEquals("dname", attribute.getName());
		assertEquals(String.class, attribute.getType());
		assertEquals(Types.VARCHAR, attribute.getSqlType());
		assertNull(attribute.getTemporalType());
		assertFalse(attribute.isId());
		assertFalse(attribute.isAssociation());
		assertFalse(attribute.isCollection());
		assertFalse(attribute.isComponent());
		assertFalse(attribute.isVersion());

		attribute = entityDesc.getAttributeDesc("loc");
		assertEquals("loc", attribute.getName());
		assertEquals(String.class, attribute.getType());
		assertEquals(Types.VARCHAR, attribute.getSqlType());
		assertNull(attribute.getTemporalType());
		assertFalse(attribute.isId());
		assertFalse(attribute.isAssociation());
		assertFalse(attribute.isCollection());
		assertFalse(attribute.isComponent());
		assertFalse(attribute.isVersion());

		attribute = entityDesc.getAttributeDesc("versionNo");
		assertEquals("versionNo", attribute.getName());
		assertEquals(int.class, attribute.getType());
		assertEquals(Types.INTEGER, attribute.getSqlType());
		assertNull(attribute.getTemporalType());
		assertFalse(attribute.isId());
		assertFalse(attribute.isAssociation());
		assertFalse(attribute.isCollection());
		assertFalse(attribute.isComponent());
		assertTrue(attribute.isVersion());

		attribute = entityDesc.getAttributeDesc("active");
		assertEquals("active", attribute.getName());
		assertEquals(boolean.class, attribute.getType());
		assertEquals(Types.BIT, attribute.getSqlType());
		assertNull(attribute.getTemporalType());
		assertFalse(attribute.isId());
		assertFalse(attribute.isAssociation());
		assertFalse(attribute.isCollection());
		assertFalse(attribute.isComponent());
		assertFalse(attribute.isVersion());

		attribute = entityDesc.getAttributeDesc("employees");
		assertEquals("employees", attribute.getName());
		assertEquals(Set.class, attribute.getType());
		assertEquals(Employee.class, attribute.getElementType());
		assertEquals(Types.OTHER, attribute.getSqlType());
		assertNull(attribute.getTemporalType());
		assertFalse(attribute.isId());
		assertTrue(attribute.isAssociation());
		assertTrue(attribute.isCollection());
		assertFalse(attribute.isComponent());
		assertFalse(attribute.isVersion());
	}

	public void ignore_testEmployee() throws Exception {
		EntityDesc entityDesc = EntityDescFactory.getEntityDesc(Employee.class);
		assertNotNull(entityDesc);
		assertEquals("Emp", entityDesc.getEntityName());
		assertEquals("empno", entityDesc.getIdAttributeDesc().getName());
		String[] propNames = entityDesc.getAttributeNames();
		assertNotNull(propNames);
		assertEquals(10, propNames.length);
		assertEquals("empno", propNames[0]);
		assertEquals("ename", propNames[1]);
		assertEquals("job", propNames[2]);
		assertEquals("mgr", propNames[3]);
		assertEquals("hiredate", propNames[4]);
		assertEquals("sal", propNames[5]);
		assertEquals("comm", propNames[6]);
		assertEquals("tstamp", propNames[7]);
		assertEquals("department", propNames[8]);
		assertEquals("address", propNames[9]);

		AttributeDesc[] attributes = entityDesc.getAttributeDescs();

		AttributeDesc attribute = attributes[0];
		assertEquals("empno", attribute.getName());
		assertEquals(Long.class, attribute.getType());
		assertEquals(Types.BIGINT, attribute.getSqlType());
		assertNull(attribute.getTemporalType());
		assertTrue(attribute.isId());
		assertFalse(attribute.isAssociation());
		assertFalse(attribute.isCollection());
		assertFalse(attribute.isComponent());
		assertFalse(attribute.isVersion());

		attribute = attributes[1];
		assertEquals("ename", attribute.getName());
		assertEquals(String.class, attribute.getType());
		assertEquals(Types.VARCHAR, attribute.getSqlType());
		assertNull(attribute.getTemporalType());
		assertFalse(attribute.isId());
		assertFalse(attribute.isAssociation());
		assertFalse(attribute.isCollection());
		assertFalse(attribute.isComponent());
		assertFalse(attribute.isVersion());

		attribute = attributes[2];
		assertEquals("job", attribute.getName());
		assertEquals(String.class, attribute.getType());
		assertEquals(Types.VARCHAR, attribute.getSqlType());
		assertNull(attribute.getTemporalType());
		assertFalse(attribute.isId());
		assertFalse(attribute.isAssociation());
		assertFalse(attribute.isCollection());
		assertFalse(attribute.isComponent());
		assertFalse(attribute.isVersion());

		attribute = attributes[3];
		assertEquals("mgr", attribute.getName());
		assertEquals(Short.class, attribute.getType());
		assertEquals(Types.SMALLINT, attribute.getSqlType());
		assertNull(attribute.getTemporalType());
		assertFalse(attribute.isId());
		assertFalse(attribute.isAssociation());
		assertFalse(attribute.isCollection());
		assertFalse(attribute.isComponent());
		assertFalse(attribute.isVersion());

		attribute = attributes[4];
		assertEquals("hiredate", attribute.getName());
		assertEquals(Date.class, attribute.getType());
		assertEquals(Types.DATE, attribute.getSqlType());
		assertEquals(TemporalType.DATE, attribute.getTemporalType());
		assertFalse(attribute.isId());
		assertFalse(attribute.isAssociation());
		assertFalse(attribute.isCollection());
		assertFalse(attribute.isComponent());
		assertFalse(attribute.isVersion());

		attribute = attributes[5];
		assertEquals("sal", attribute.getName());
		assertEquals(Float.class, attribute.getType());
		assertEquals(Types.FLOAT, attribute.getSqlType());
		assertNull(attribute.getTemporalType());
		assertFalse(attribute.isId());
		assertFalse(attribute.isAssociation());
		assertFalse(attribute.isCollection());
		assertFalse(attribute.isComponent());
		assertFalse(attribute.isVersion());

		attribute = attributes[6];
		assertEquals("comm", attribute.getName());
		assertEquals(Float.class, attribute.getType());
		assertEquals(Types.FLOAT, attribute.getSqlType());
		assertNull(attribute.getTemporalType());
		assertFalse(attribute.isId());
		assertFalse(attribute.isAssociation());
		assertFalse(attribute.isCollection());
		assertFalse(attribute.isComponent());
		assertFalse(attribute.isVersion());

		attribute = attributes[7];
		assertEquals("tstamp", attribute.getName());
		assertEquals(Date.class, attribute.getType());
		assertEquals(Types.TIMESTAMP, attribute.getSqlType());
		assertEquals(TemporalType.TIMESTAMP, attribute.getTemporalType());
		assertFalse(attribute.isId());
		assertFalse(attribute.isAssociation());
		assertFalse(attribute.isCollection());
		assertFalse(attribute.isComponent());
		assertFalse(attribute.isVersion());

		attribute = attributes[8];
		assertEquals("department", attribute.getName());
		assertEquals(Department.class, attribute.getType());
		assertEquals(Types.OTHER, attribute.getSqlType());
		assertNull(attribute.getTemporalType());
		assertFalse(attribute.isId());
		assertTrue(attribute.isAssociation());
		assertFalse(attribute.isCollection());
		assertFalse(attribute.isComponent());
		assertFalse(attribute.isVersion());

		attribute = attributes[9];
		assertEquals("address", attribute.getName());
		assertEquals(Address.class, attribute.getType());
		assertEquals(Types.OTHER, attribute.getSqlType());
		assertNull(attribute.getTemporalType());
		assertFalse(attribute.isId());
		assertFalse(attribute.isAssociation());
		assertFalse(attribute.isCollection());
		assertTrue(attribute.isComponent());
		assertFalse(attribute.isVersion());

		AttributeDesc[] children = attribute.getChildAttributeDescs();
		assertNotNull(children);
		assertEquals(2, children.length);

		attribute = children[0];
		assertEquals("city", attribute.getName());
		assertEquals(String.class, attribute.getType());
		assertEquals(Types.VARCHAR, attribute.getSqlType());
		assertNull(attribute.getTemporalType());
		assertFalse(attribute.isId());
		assertFalse(attribute.isAssociation());
		assertFalse(attribute.isCollection());
		assertFalse(attribute.isComponent());
		assertFalse(attribute.isVersion());

		attribute = children[1];
		assertEquals("zip", attribute.getName());
		assertEquals(String.class, attribute.getType());
		assertEquals(Types.VARCHAR, attribute.getSqlType());
		assertNull(attribute.getTemporalType());
		assertFalse(attribute.isId());
		assertFalse(attribute.isAssociation());
		assertFalse(attribute.isCollection());
		assertFalse(attribute.isComponent());
		assertFalse(attribute.isVersion());
	}
}
