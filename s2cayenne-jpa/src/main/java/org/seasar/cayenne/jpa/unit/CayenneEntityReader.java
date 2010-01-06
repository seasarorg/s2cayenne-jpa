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

import org.seasar.cayenne.jpa.metadata.CayenneEntityDesc;
import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.impl.DataSetImpl;
import org.seasar.framework.jpa.unit.EntityReader;

/**
 * エンティティをデータセットとして読み取るクラスです。
 * 
 * @author taedium
 */
public class CayenneEntityReader implements EntityReader {

	private CayenneEntityDesc entityDesc;

	/** データセット */
	protected final DataSet dataSet = new DataSetImpl();

	/**
	 * インスタンスを構築します。
	 */
	protected CayenneEntityReader() {
	}

	/**
	 * インスタンスを構築します。
	 * 
	 * @param entity
	 *            エンティティ
	 * @param entityDesc
	 *            <code>entity</code>のエンティティ定義
	 */
	public CayenneEntityReader(final Object entity,
			final CayenneEntityDesc entityDesc) {

		this.entityDesc = entityDesc;
		setupColumns();
		setupRow(entity);
	}

	/**
	 * カラムを設定します。
	 */
	protected void setupColumns() {
		// TODO;
	}

	/**
	 * エンティティの属性に対応するカラムを設定します。
	 */
	protected void setupAttributeColumns() {
		// TODO;
	}

	/**
	 * 識別カラムを設定します。
	 */
	protected void setupDiscriminatorColumn() {
		// TODO;
	}

	/**
	 * 行を設定します。
	 * 
	 * @param entity
	 *            エンティティ
	 */
	protected void setupRow(final Object entity) {
		// TODO;
	}

	/**
	 * エンティティの属性値を行に設定します。
	 * 
	 * @param entity
	 *            エンティティ
	 * @param row
	 *            行
	 * @param tableName
	 *            テーブル名
	 */
	protected void setupAttributeValues(final Object entity, final DataRow row,
			final String tableName) {
		// TODO;
	}

	/**
	 * 識別値を行に設定します。
	 * 
	 * @param row
	 *            行
	 * @param tableName
	 *            テーブル名
	 */
	protected void setupDiscriminatorValue(final DataRow row,
			final String tableName) {
		// TODO;
	}

	/**
	 * エンティティ定義を返します。
	 * 
	 * @return エンティティ定義
	 */
	protected CayenneEntityDesc getEntityDesc() {
		return entityDesc;
	}

	public DataSet read() {
		return dataSet;
	}
}
