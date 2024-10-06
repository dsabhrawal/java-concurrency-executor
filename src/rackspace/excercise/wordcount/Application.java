package rackspace.excercise.wordcount;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

/**
 * 
 * @author deepak This program splits the given file into multiple files based
 *         on given number of lines. Then each file is processed to get count of
 *         total words and the most used word and its occurrence
 *         
 *
 */
public class Application {

	private static final long MAX_LINES = 15000;
	private static final int THREAD_POOL = 5;
	static File filePath = new File("input.txt");
	static String outputPath = "output\\" + filePath.getName() + ".split";

	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
		LocalDateTime startTime = LocalDateTime.now();
		readAndSplitFile();
		List<Callable<CountResult>> tasks = collectProcessingTasks();
		executeTasks(tasks);
		LocalDateTime endTime = LocalDateTime.now();
		System.out.println("Time in miliseconds: " + Duration.between(startTime, endTime).toMillis());

	}

	private static void executeTasks(List<Callable<CountResult>> tasks)
			throws InterruptedException, ExecutionException {
		ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL);
		List<Future<CountResult>> results = executor.invokeAll(tasks);

		long totalWords = 0;
		Map<String, Long> totalWordCountMap = new HashMap<>();

		for (Future<CountResult> future : results) {
			totalWords += future.get().getWordCount();
			Utils.mergeMap(totalWordCountMap, future.get().getWordCountMap());
		}

		Map<String, Long> sortedMap = Utils.sortMap(totalWordCountMap);

		System.out.println("========================================");
		System.out.print("Total Word Count: " + totalWords + " \n");
		System.out.println("Top 5 words: ");
		sortedMap.entrySet().stream().limit(5).forEach(i -> System.out.println(i.getKey() + "      " + i.getValue()));
		System.out.println("=========================================");

		executor.shutdown();
	}

	private static List<Callable<CountResult>> collectProcessingTasks() throws IOException {
		List<Callable<CountResult>> tasks = new ArrayList<>();
		try (Stream<Path> paths = Files.walk(Paths.get("output"))) {
			paths.filter(Files::isRegularFile).forEach(f -> {
				tasks.add(new ProcessingTask(f));
			});
		}
		return tasks;
	}

	private static void readAndSplitFile() {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF8"))) {
			String line;
			int lineCount = 1;
			int fileCount = 1;
			StringBuilder sb = new StringBuilder("");
			while ((line = br.readLine()) != null) {
				if (lineCount % MAX_LINES == 0) {
					writeFile(fileCount, sb);
					sb = new StringBuilder("");
					fileCount++;
					lineCount = 0;
				}
				sb.append(line).append("\n");
				lineCount++;
			}
			writeFile(fileCount, sb);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	private static void writeFile(int fileCount, StringBuilder sb) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outputPath + fileCount)));
		int last = sb.lastIndexOf("\n");
		if (last >= 0) {
			sb.delete(last, sb.length());
		}
		bw.write(sb.toString());
		bw.close();
	}

}
