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

/**
 * エンティティのコレクションをデータセットとして読み取るクラスです。
 * 
 * @author taedium
 */
public class CayenneEntityCollectionReader extends CayenneEntityReader {

	/** エンティティのクラスをキー、エンティティ定義を値としたマップ */
	protected Map<Class<?>, CayenneEntityDesc> entityDescs;

	/** 処理中のエンティティのクラス */
	protected Class<?> processingClass;

	/**
	 * インスタンスを構築します。
	 * 
	 * @param entities
	 *            エンティティのコレクション
	 * @param entityDescs
	 *            エンティティのクラスをキー、エンティティ定義をバリューとしたマップ
	 */
	public CayenneEntityCollectionReader(final Collection<?> entities,
			final Map<Class<?>, CayenneEntityDesc> entityDescs) {

		this.entityDescs = entityDescs;

		for (final Object entity : entities) {
			processingClass = entity.getClass();
			setupColumns();
			setupRow(entity);
			processingClass = null;
		}
	}

	@Override
	protected CayenneEntityDesc getEntityDesc() {
		return entityDescs.get(processingClass);
	}
}
