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
package org.seasar.cayenne.jpa.unit;

import java.util.Collection;
import java.util.Map;

import org.seasar.cayenne.jpa.metadata.CayenneEntityDesc;
import org.seasar.framework.jpa.metadata.EntityDesc;
import org.seasar.framework.jpa.metadata.EntityDescFactory;
import org.seasar.framework.jpa.unit.EntityReader;
import org.seasar.framework.jpa.unit.EntityReaderProvider;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * Cayenne用の{@link CayenneEntityReader}を提供するコンポーネントの実装クラスです。
 * 
 * @author taedium
 */
public class CayenneEntityReaderProvider implements EntityReaderProvider {

    public EntityReader createEntityReader(final Object entity) {
        if (entity == null) {
            return null;
        }
        final CayenneEntityDesc entityDesc = getEntityDesc(entity.getClass());
        if (entityDesc == null) {
            return null;
        }
        return new CayenneEntityReader(entity, entityDesc);
    }

    public EntityReader createEntityReader(final Collection<?> entities) {
        if (entities == null) {
            return null;
        }

        final Collection<Object> newEntities = flatten(entities);
        if (newEntities.isEmpty()) {
            return null;
        }

        final Map<Class<?>, CayenneEntityDesc> entityDescs = CollectionsUtil
                .newHashMap();
        for (final Object entity : newEntities) {
            final Class<?> entityClass = entity.getClass();
            if (entityDescs.containsKey(entityClass)) {
                continue;
            }
            final CayenneEntityDesc entityDesc = getEntityDesc(entityClass);
            if (entityDesc == null) {
                return null;
            }
            entityDescs.put(entityClass, entityDesc);
        }
        return new CayenneEntityCollectionReader(newEntities, entityDescs);
    }

    /**
     * <code>entities</code>に要素として含まれるオブジェクト配列を平坦化し、新しいコレクションに詰めて返します。
     * <p>
     * <code>entities</code>の要素がオブジェクト配列の場合、その配列の要素を新しいコレクションに詰めます。
     * <code>entities</code>の要素がオブジェクト配列でない場合はその要素を新しいコレクションに詰めます。
     * </p>
     * 
     * @param entities
     *            エンティティもしくはエンティティのオブジェクト配列のコレクション
     * @return 要素のオブジェクト配列が平坦化されている新しいコレクション
     */
    protected Collection<Object> flatten(final Collection<?> entities) {
        Collection<Object> newEntities = CollectionsUtil.newArrayList(entities
                .size());
        for (final Object element : entities) {
            if (element instanceof Object[]) {
                for (final Object nested : Object[].class.cast(element)) {
                    newEntities.add(nested);
                }
            } else {
                newEntities.add(element);
            }
        }
        return newEntities;
    }

    /**
     * エンティティのクラスに対応するエンティティ定義を返します。
     * 
     * @param entityClass
     *            エンティティのクラス
     * @return エンティティ定義
     */
    protected CayenneEntityDesc getEntityDesc(final Class<?> entityClass) {
        final EntityDesc entityDesc = EntityDescFactory
                .getEntityDesc(entityClass);
        if (entityDesc == null || !(entityDesc instanceof CayenneEntityDesc)) {
            return null;
        }
        return CayenneEntityDesc.class.cast(entityDesc);
    }
}
