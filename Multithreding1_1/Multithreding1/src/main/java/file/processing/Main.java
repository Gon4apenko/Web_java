package file.processing;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.isNull;

public class Main {
    private static final String REGEX = "^\\d+$";
    private static final File OUTPUT_FILE = new File("C:\\Users\\gonch\\Desktop\\Multithreding1_1\\Multithreding1\\src\\main\\resources\\output.txt");
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();


    public static void main(String[] args) {
        String path = "C:\\Users\\gonch\\Desktop\\Multithreding1_1\\Multithreding1\\src\\main\\resources\\Task";
        File file = new File(path);

        if (file.isDirectory() && !isNull(file.listFiles())) {
            for (File file1 : file.listFiles()) {
                EXECUTOR.submit(new TaskProcessor(file1, OUTPUT_FILE, REGEX));
            }

            try {
                EXECUTOR.shutdown();
                EXECUTOR.awaitTermination(10L, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            outputResult();
        } else {
            System.out.println("Given path isn't a directory");
        }
    }

    private static void outputResult() {
        try {
            System.out.println("---------------------------------------");
            Files.readAllLines(OUTPUT_FILE.toPath(), StandardCharsets.UTF_8).forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
