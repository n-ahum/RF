package evidencia;

import java.util.ArrayList;

// revisa que la coleccion quedo ordenada
public final class SortVerifier {
    private SortVerifier() {}

    public static boolean isSorted(int[] a) {
        // si algun valor es mayor que el siguiente, no esta ordenado
        for (int i = 1; i < a.length; i++) {
            if (a[i - 1] > a[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean isSorted(ArrayList<Integer> a) {
        for (int i = 1; i < a.size(); i++) {
            if (a.get(i - 1) > a.get(i)) {
                return false;
            }
        }
        return true;
    }
}
