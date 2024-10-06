package rackspace.excercise.wordcount;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProcessingTask implements Callable<CountResult> {
	private Path file = null;

	public ProcessingTask(Path file) {
		this.file = file;
	}

	public CountResult call() throws FileNotFoundException {
		List<String> words = new ArrayList<>();
		long maxCount = 0;
		Map<String, Long> wordCountMap = null;

		try (Scanner sc = new Scanner(new FileInputStream(file.toFile()))) {
			while (sc.hasNext()) {
				words.add(sc.next());
			}
			wordCountMap = words.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
			maxCount = Collections.max(wordCountMap.values());
			String maxWord = Utils.getKey(wordCountMap, maxCount);

			System.out.println("----------------------------------");
			System.out.println("File Processed: " + file.getFileName());
			System.out.println("Total words: " + words.size());
			System.out.println("Most used Word " + maxWord + " appeard " + maxCount + " times");
			System.out.println("---------------------------------");

		}
		CountResult result = new CountResult(words.size(), wordCountMap);
		return result;
	}
}