package file.processing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static java.util.Objects.isNull;

public class TaskProcessor implements Runnable {

    private final File file;
    private final File outputFile;
    private final String regex;

    public TaskProcessor(File file, File outputFile, String regex) {
        this.file = file;
        this.outputFile = outputFile;
        this.regex = regex;
    }

    @Override
    public void run() {
        try {
            recurringSearch(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void recurringSearch(File file) throws FileNotFoundException {
        if (file.isDirectory() && !isNull(file.listFiles())) {
            System.out.println("In folder " + file.getName());
            for (File file1 : file.listFiles()) {
                recurringSearch(file1);
            }
            System.out.println("Quit folder " + file.getName());
        } else {
            verifyAndRecordCondition(file);
        }
    }

    private void verifyAndRecordCondition(File file) {
        synchronized (outputFile) {
            System.out.println("    Scanning " + file.getName());

            int countDigit = 0;
            try (Scanner input = new Scanner(file);
                 FileWriter writer = new FileWriter(outputFile, true)) {
                while (input.hasNext()) {
                    if (input.next().trim().matches(regex)) {
                        countDigit++;
                    }
                }

                if (countDigit > 0) {
                    writer.write(String.format("%s -> %d occurrences\n", file.getName(), countDigit));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("    Finished");
        }
    }
}
