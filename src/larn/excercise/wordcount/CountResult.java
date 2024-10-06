package larn.excercise.wordcount;
import java.util.HashMap;
import java.util.Map;

public class CountResult {

	private long wordCount;
	private Map<String, Long> wordCountMap = new HashMap<String, Long>();

	public CountResult(long wordCount, Map<String, Long> wordCountMap) {
		this.wordCount = wordCount;
		this.wordCountMap = wordCountMap;
	}

	public long getWordCount() {
		return wordCount;
	}

	public Map<String, Long> getWordCountMap() {
		return wordCountMap;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (wordCount ^ (wordCount >>> 32));
		result = prime * result + ((wordCountMap == null) ? 0 : wordCountMap.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CountResult other = (CountResult) obj;
		if (wordCount != other.wordCount)
			return false;
		if (wordCountMap == null) {
			if (other.wordCountMap != null)
				return false;
		} else if (!wordCountMap.equals(other.wordCountMap))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Result [wordCount=" + wordCount + ", wordCountMap=" + wordCountMap + "]";
	}

}
