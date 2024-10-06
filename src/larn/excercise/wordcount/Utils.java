package larn.excercise.wordcount;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Utils {
	public static <K, V> K getKey(Map<K, V> map, V value) {
		return map.keySet().stream().filter(key -> value.equals(map.get(key))).findFirst().get();
	}

	/**
	 * 
	 * @param map1
	 * @param map2
	 * Merging maps by sum of values
	 */
	public static void mergeMap(Map<String, Long> map1, Map<String, Long> map2) {
		map2.forEach((key, value) -> map1.merge(key, value, (v1, v2) -> v1 + v2));
	}

	/**
	 * 
	 * @param totalWordCountMap
	 * @return
	 * Sorting map in reverse order
	 */
	public static Map<String, Long> sortMap(Map<String, Long> totalWordCountMap) {
		return totalWordCountMap.entrySet().stream().sorted(Entry.comparingByValue(Comparator.reverseOrder()))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}
}
