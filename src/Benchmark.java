import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Benchmark {
    private static final int[] SIZES = {100, 1000, 10000};
    private static final int WARMUP_ROUNDS = 2;
    private static final int MEASURED_ROUNDS = 7;

    public static void main(String[] args) throws IOException {
        String outputName = args.length > 0 ? args[0] : "results.csv";
        try (PrintWriter output = new PrintWriter(new FileWriter(outputName))) {
            output.println("elements,bst_insert_ms,bst_search_ms,bst_delete_ms,hash_insert_ms,hash_search_ms,hash_delete_ms");

            for (int size : SIZES) {
                for (int i = 0; i < WARMUP_ROUNDS; i++) {
                    measure(size, i);
                }

                double[][] times = new double[6][MEASURED_ROUNDS];
                for (int i = 0; i < MEASURED_ROUNDS; i++) {
                    double[] result = measure(size, i + WARMUP_ROUNDS);
                    for (int operation = 0; operation < result.length; operation++) {
                        times[operation][i] = result[operation];
                    }
                }

                double[] medians = new double[6];
                for (int operation = 0; operation < medians.length; operation++) {
                    medians[operation] = median(times[operation]);
                }

                output.printf(java.util.Locale.ROOT, "%d,%.6f,%.6f,%.6f,%.6f,%.6f,%.6f%n",
                        size, medians[0], medians[1], medians[2], medians[3], medians[4], medians[5]);
                System.out.printf(java.util.Locale.ROOT,
                        "%d values: BST %.6f/%.6f/%.6f ms; hash table %.6f/%.6f/%.6f ms%n",
                        size, medians[0], medians[1], medians[2], medians[3], medians[4], medians[5]);
            }
        }
        System.out.println("Saved results in " + outputName);
    }

    private static double[] measure(int size, int round) {
        List<Integer> values = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            values.add(i);
        }
        Collections.shuffle(values, new Random(100 + size + round));

        BST bst = new BST();
        long start = System.nanoTime();
        for (int value : values) {
            if (!bst.insert(value)) {
                throw new IllegalStateException("BST insert failed");
            }
        }
        double bstInsert = elapsedMillis(start);

        HashTable table = new HashTable(size * 2 + 1);
        start = System.nanoTime();
        for (int value : values) {
            if (!table.insert(value)) {
                throw new IllegalStateException("Hash table insert failed");
            }
        }
        double hashInsert = elapsedMillis(start);

        start = System.nanoTime();
        boolean bstFound = true;
        for (int value : values) {
            bstFound &= bst.contains(value);
        }
        double bstSearch = elapsedMillis(start);
        if (!bstFound) {
            throw new IllegalStateException("BST search failed");
        }

        start = System.nanoTime();
        boolean hashFound = true;
        for (int value : values) {
            hashFound &= table.contains(value);
        }
        double hashSearch = elapsedMillis(start);
        if (!hashFound) {
            throw new IllegalStateException("Hash table search failed");
        }

        start = System.nanoTime();
        boolean bstDeleted = true;
        for (int value : values) {
            bstDeleted &= bst.delete(value);
        }
        double bstDelete = elapsedMillis(start);
        if (!bstDeleted) {
            throw new IllegalStateException("BST deletion failed");
        }

        start = System.nanoTime();
        boolean hashDeleted = true;
        for (int value : values) {
            hashDeleted &= table.delete(value);
        }
        double hashDelete = elapsedMillis(start);
        if (!hashDeleted) {
            throw new IllegalStateException("Hash table deletion failed");
        }

        return new double[]{bstInsert, bstSearch, bstDelete, hashInsert, hashSearch, hashDelete};
    }

    private static double elapsedMillis(long start) {
        return (System.nanoTime() - start) / 1_000_000.0;
    }

    private static double median(double[] values) {
        double[] sorted = values.clone();
        java.util.Arrays.sort(sorted);
        return sorted[sorted.length / 2];
    }
}
