package evidencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

// crea los numeros originales y las copias
public final class DataGenerator {
    private static final Random RND = new Random();

    private DataGenerator() {}

    // only1to5 = true: todos los valores son de 1 a 5.
    public static int[] generate(int n, boolean only1to5) {
        // arreglo con n espacios
        int[] data = new int[n];
        for (int i = 0; i < n; i++) {
            if (only1to5) {
                data[i] = RND.nextInt(5) + 1;
            } else {
                data[i] = RND.nextInt(1000000);
            }
        }
        return data;
    }

    // copia independiente del arreglo
    public static int[] copyArray(int[] original) {
        // crea un arreglo nuevo con los mismos valores
        return Arrays.copyOf(original, original.length);
    }

    // copia independiente en un ArrayList
    public static ArrayList<Integer> toList(int[] original) {
        // lista nueva, se copia valor por valor
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < original.length; i++) {
            list.add(original[i]);
        }
        return list;
    }
}
