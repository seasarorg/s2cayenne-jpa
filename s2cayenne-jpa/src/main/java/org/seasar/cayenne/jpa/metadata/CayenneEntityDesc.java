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

import java.util.LinkedList;
import java.util.Map;

import org.apache.cayenne.map.Attribute;
import org.apache.cayenne.map.EmbeddedAttribute;
import org.apache.cayenne.map.EntityResolver;
import org.apache.cayenne.map.ObjAttribute;
import org.apache.cayenne.map.ObjEntity;
import org.apache.cayenne.map.ObjRelationship;
import org.apache.cayenne.map.Relationship;
import org.apache.cayenne.reflect.ClassDescriptor;
import org.apache.cayenne.reflect.Property;
import org.seasar.framework.jpa.metadata.AttributeDesc;
import org.seasar.framework.jpa.metadata.EntityDesc;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * 
 * @author taedium
 */
public class CayenneEntityDesc implements EntityDesc {

	protected final Class<?> entityClass;

	protected final ObjEntity objEntity;

	protected final EntityResolver entityResolver;

	protected final String entityName;

	protected final ClassDescriptor classDescriptor;

	protected final String[] attributeNames;

	protected final CayenneAttributeDesc[] attributeDescs;

	protected final Map<String, CayenneAttributeDesc> attributeDescMap = CollectionsUtil
			.newHashMap();

	public CayenneEntityDesc(final Class<?> entityClass,
			final ObjEntity objEntity, final EntityResolver entityResolver) {
		this.entityClass = entityClass;
		this.objEntity = objEntity;
		this.entityResolver = entityResolver;
		this.entityName = objEntity.getName();
		this.classDescriptor = entityResolver.getClassDescriptor(entityName);
		this.attributeDescs = createAttributeDescs();
		this.attributeNames = createAttributeNames();
	}

	public AttributeDesc getAttributeDesc(final String attributeName) {
		return attributeDescMap.get(attributeName);
	}

	public AttributeDesc[] getAttributeDescs() {
		return attributeDescs;
	}

	public String[] getAttributeNames() {
		return attributeNames;
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public String getEntityName() {
		return entityName;
	}

	public AttributeDesc getIdAttributeDesc() {
		return attributeDescs[0];
	}

	protected CayenneAttributeDesc[] createAttributeDescs() {
		final LinkedList<CayenneAttributeDesc> list = CollectionsUtil
				.newLinkedList();
		for (final Object each : objEntity.getAttributes()) {
			final Attribute attribute = Attribute.class.cast(each);
			final CayenneAttributeDesc attributeDesc = createAttributeDesc(attribute);
			if (attributeDesc.isId()) {
				list.addFirst(attributeDesc);
			} else {
				list.add(attributeDesc);
			}
		}
		for (final Object each : objEntity.getRelationships()) {
			final Relationship realtionship = Relationship.class.cast(each);
			list.add(createAttributeDesc(realtionship));
		}
		return list.toArray(new CayenneAttributeDesc[list.size()]);
	}

	protected CayenneAttributeDesc createAttributeDesc(final Attribute attribute) {
		final Property property = classDescriptor.getProperty(attribute
				.getName());
		if (ObjAttribute.class.isAssignableFrom(attribute.getClass())) {
			final ObjAttribute target = ObjAttribute.class.cast(attribute);
			return new CayenneAttributeDesc(target, property);
		}
		if (EmbeddedAttribute.class.isAssignableFrom(attribute.getClass())) {
			final EmbeddedAttribute target = EmbeddedAttribute.class
					.cast(attribute);
			return new CayenneAttributeDesc(target, property);
		}
		throw new IllegalStateException(); // TODO
	}

	protected CayenneAttributeDesc createAttributeDesc(
			final Relationship relationship) {
		final Property property = classDescriptor.getProperty(relationship
				.getName());
		if (ObjRelationship.class.isAssignableFrom(relationship.getClass())) {
			final ObjRelationship rel = ObjRelationship.class
					.cast(relationship);
			return new CayenneAttributeDesc(entityResolver, rel, property);
		}
		throw new IllegalStateException(); // TODO
	}

	protected String[] createAttributeNames() {
		final String[] attributeNames = new String[attributeDescs.length];
		for (int i = 0; i < attributeNames.length; i++) {
			attributeNames[i] = attributeDescs[i].getName();
			attributeDescMap.put(attributeNames[i], attributeDescs[i]);
		}
		return attributeNames;
	}
}
