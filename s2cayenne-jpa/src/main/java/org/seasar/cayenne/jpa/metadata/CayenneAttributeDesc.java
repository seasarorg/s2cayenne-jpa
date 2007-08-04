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

import java.sql.Types;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.persistence.TemporalType;

import org.apache.cayenne.map.EmbeddableAttribute;
import org.apache.cayenne.map.EmbeddedAttribute;
import org.apache.cayenne.map.EntityResolver;
import org.apache.cayenne.map.ObjAttribute;
import org.apache.cayenne.map.ObjRelationship;
import org.apache.cayenne.reflect.ClassDescriptor;
import org.apache.cayenne.reflect.Property;
import org.seasar.framework.jpa.metadata.AttributeDesc;
import org.seasar.framework.jpa.util.TemporalTypeUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * 
 * @author taedium
 */
public class CayenneAttributeDesc implements AttributeDesc {

	protected final Property property;

	protected final String name;

	protected Class<?> type;

	protected Class<?> elementType;

	protected final int sqlType;

	protected TemporalType temporalType;

	protected final boolean id;

	protected final boolean association;

	protected final boolean collection;

	protected final boolean component;

	protected final boolean version;

	protected AttributeDesc[] childAttributeDescs = new AttributeDesc[] {};

	protected Map<String, AttributeDesc> childAttributeDescMap = CollectionsUtil
			.newHashMap();

	protected CayenneAttributeDesc(final Property property, final String name,
			final int sqlType, final boolean id, final boolean version,
			final boolean association, final boolean collection,
			final boolean component) {
		this.property = property;
		this.name = name;
		this.sqlType = sqlType;
		this.id = id; // TODO
		this.version = version; // TODO
		this.association = association;
		this.collection = collection;
		this.component = component;
	}

	public CayenneAttributeDesc(final ObjAttribute attribute,
			final Property property) {
		this(property, attribute.getName(), attribute.getDbAttribute()
				.getType(), attribute.getDbAttribute().isPrimaryKey(), false,
				false, false, false);
		this.type = attribute.getJavaClass();
		if (type == Date.class || type == Calendar.class) {
			this.temporalType = TemporalTypeUtil
					.fromSqlTypeToTemporalType(sqlType);
		}
	}

	public CayenneAttributeDesc(final EmbeddedAttribute attribute,
			final Property property) {
		this(property, attribute.getName(), Types.OTHER, false, false, false,
				false, true);
		this.type = attribute.getJavaClass();
		for (final Object each : attribute.getAttributes()) {
			final EmbeddableAttribute embeddableAttribute = EmbeddableAttribute.class
					.cast(each);
			// TODO
		}
	}

	public CayenneAttributeDesc(final EntityResolver entityResolver,
			final ObjRelationship relationship, final Property property) {
		this(property, relationship.getName(), Types.OTHER, false, false, true,
				relationship.isToMany(), false);
		final String targetEntityName = relationship.getTargetEntityName();
		final ClassDescriptor classDescriptor = entityResolver
				.getClassDescriptor(targetEntityName);
		this.type = Collection.class;// TODO
		if (collection) {
			this.elementType = classDescriptor.getObjectClass();
		}
	}

	public AttributeDesc getChildAttributeDesc(final String name) {
		return childAttributeDescMap.get(name);
	}

	public AttributeDesc[] getChildAttributeDescs() {
		return childAttributeDescs;
	}

	public Class<?> getElementType() {
		return elementType;
	}

	public String getName() {
		return name;
	}

	public int getSqlType() {
		return sqlType;
	}

	public TemporalType getTemporalType() {
		return temporalType;
	}

	public Class<?> getType() {
		return type;
	}

	public Object getValue(final Object entity) {
		return property.readProperty(entity);
	}

	public boolean isAssociation() {
		return association;
	}

	public boolean isCollection() {
		return collection;
	}

	public boolean isComponent() {
		return component;
	}

	public boolean isId() {
		return id;
	}

	public boolean isVersion() {
		return version;
	}

	public void setValue(final Object entity, final Object value) {
		property.writeProperty(entity, getValue(entity), value);
	}

}
