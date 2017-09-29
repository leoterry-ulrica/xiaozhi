/*package com.dist.bdf.base.utils.collectors;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
*//**
 *  兼容key覆盖，或者null
 * @author weifj
 *
 *//*
public final class DistCollectors {

	public static <T, K, V> Collector<T, ?, Map<K, V>> toMap(Function<T, K> f1, Function<T, V> f2) {
		return new ForceToMapCollector<T, K, V>(f1, f2);
	}
}
*/