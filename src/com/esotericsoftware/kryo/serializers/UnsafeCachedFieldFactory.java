/* Copyright (c) 2008, Nathan Sweet
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 * - Neither the name of Esoteric Software nor the names of its contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.esotericsoftware.kryo.serializers;

import java.lang.reflect.Field;

import com.esotericsoftware.kryo.serializers.FieldSerializer.CachedField;
import com.esotericsoftware.kryo.serializers.FieldSerializer.CachedFieldFactory;
import com.esotericsoftware.kryo.serializers.UnsafeCacheFields.UnsafeBooleanField;
import com.esotericsoftware.kryo.serializers.UnsafeCacheFields.UnsafeByteField;
import com.esotericsoftware.kryo.serializers.UnsafeCacheFields.UnsafeCharField;
import com.esotericsoftware.kryo.serializers.UnsafeCacheFields.UnsafeDoubleField;
import com.esotericsoftware.kryo.serializers.UnsafeCacheFields.UnsafeFloatField;
import com.esotericsoftware.kryo.serializers.UnsafeCacheFields.UnsafeIntField;
import com.esotericsoftware.kryo.serializers.UnsafeCacheFields.UnsafeLongField;
import com.esotericsoftware.kryo.serializers.UnsafeCacheFields.UnsafeObjectField;
import com.esotericsoftware.kryo.serializers.UnsafeCacheFields.UnsafeShortField;
import com.esotericsoftware.kryo.serializers.UnsafeCacheFields.UnsafeStringField;

class UnsafeCachedFieldFactory implements CachedFieldFactory {
	public CachedField createCachedField (Class fieldClass, Field field, FieldSerializer ser) {
		CachedField cachedField;
		// Use Unsafe-based serializers
		if (fieldClass.isPrimitive()) {
			if (fieldClass == boolean.class)
				cachedField = new UnsafeBooleanField(field);
			else if (fieldClass == byte.class)
				cachedField = new UnsafeByteField(field);
			else if (fieldClass == char.class)
				cachedField = new UnsafeCharField(field);
			else if (fieldClass == short.class)
				cachedField = new UnsafeShortField(field);
			else if (fieldClass == int.class)
				cachedField = new UnsafeIntField(field);
			else if (fieldClass == long.class)
				cachedField = new UnsafeLongField(field);
			else if (fieldClass == float.class)
				cachedField = new UnsafeFloatField(field);
			else if (fieldClass == double.class)
				cachedField = new UnsafeDoubleField(field);
			else {
				cachedField = new UnsafeObjectField(ser);
			}
		} else if (fieldClass == String.class
			&& (!ser.kryo.getReferences() || !ser.kryo.getReferenceResolver().useReferences(String.class))) {
			cachedField = new UnsafeStringField(field);
		} else {
			cachedField = new UnsafeObjectField(ser);
		}
		return cachedField;
	}
}
