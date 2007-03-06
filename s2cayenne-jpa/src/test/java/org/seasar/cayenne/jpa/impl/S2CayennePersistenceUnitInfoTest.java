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

import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.List;

import javax.persistence.spi.ClassTransformer;

import org.seasar.framework.unit.EasyMockTestCase;
import org.seasar.framework.unit.annotation.EasyMock;

import static org.easymock.EasyMock.*;

/**
 * 
 * @author nakamura
 */
public class S2CayennePersistenceUnitInfoTest extends EasyMockTestCase {

	@EasyMock
	private ClassTransformer transformer;

	public void testAddTransformer() throws Exception {
		S2CayennePersistenceUnitInfo info = new StubS2CayennePersistenceUnitInfo();
		info.addTransformer(transformer);
	}

	public void recordAddTransformer() throws Exception {
		expect(
				transformer.transform(isA(ClassLoader.class),
						eq("java.lang.String"), Class.class.cast(isNull()),
						ProtectionDomain.class.cast(isNull()),
						isA(byte[].class))).andReturn(null);
		expect(
				transformer.transform(isA(ClassLoader.class),
						eq("java.lang.Integer"), Class.class.cast(isNull()),
						ProtectionDomain.class.cast(isNull()),
						isA(byte[].class))).andReturn(null);
	}

	public static class StubS2CayennePersistenceUnitInfo extends
			S2CayennePersistenceUnitInfo {

		public StubS2CayennePersistenceUnitInfo() {
			super(null);
		}

		@Override
		public List<String> getManagedClassNames() {
			return Arrays.asList("java.lang.String", "java.lang.Integer");
		}

		@Override
		protected boolean isLoaded(final ClassLoader loader,
				final String className) {
			return false;
		}

	}
}
