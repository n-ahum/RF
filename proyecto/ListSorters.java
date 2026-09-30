package evidencia;

import java.util.ArrayList;

// los mismos 6 metodos pero para ArrayList<Integer>
// cambia que se usa get(i) y set(i, valor) en vez de a[i]

public final class ListSorters {
    private ListSorters() {}

    private static void swap(ArrayList<Integer> a, int i, int j) {
        // guarda un valor para no perderlo al cambiar
        int aux = a.get(i);
        a.set(i, a.get(j));
        a.set(j, aux);
    }

    // burbuja
    public static void bubble(ArrayList<Integer> a) {
        int limit = a.size() - 1;
        boolean swapped = true;
        while (swapped && limit > 0) {
            swapped = false;
            for (int i = 0; i < limit; i++) {
                if (a.get(i) > a.get(i + 1)) {
                    swap(a, i, i + 1);
                    swapped = true;
                }
            }
            limit--;
        }
    }

    // seleccion
    public static void selection(ArrayList<Integer> a) {
        int n = a.size();
        for (int i = 0; i < n - 1; i++) {
            int min = i;
            for (int j = i + 1; j < n; j++) {
                if (a.get(j) < a.get(min)) {
                    min = j;
                }
            }
            if (min != i) {
                swap(a, i, min);
            }
        }
    }

    // insercion
    public static void insertion(ArrayList<Integer> a) {
        for (int i = 1; i < a.size(); i++) {
            int key = a.get(i);
            int j = i - 1;
            while (j >= 0 && a.get(j) > key) {
                a.set(j + 1, a.get(j));
                j--;
            }
            a.set(j + 1, key);
        }
    }

    // shell
    public static void shell(ArrayList<Integer> a) {
        int n = a.size();
        for (int gap = n / 2; gap > 0; gap = gap / 2) {
            for (int i = gap; i < n; i++) {
                int temp = a.get(i);
                int j = i;
                while (j >= gap && a.get(j - gap) > temp) {
                    a.set(j, a.get(j - gap));
                    j = j - gap;
                }
                a.set(j, temp);
            }
        }
    }

    // merge
    public static void merge(ArrayList<Integer> a) {
        int n = a.size();
        if (n < 2) {
            return;
        }
        // lista auxiliar (memoria extra) se llena con ceros
        ArrayList<Integer> temp = new ArrayList<Integer>();
        for (int i = 0; i < n; i++) {
            temp.add(0);
        }
        mergeSort(a, temp, 0, n - 1);
    }

    private static void mergeSort(ArrayList<Integer> a, ArrayList<Integer> temp, int lo, int hi) {
        if (lo >= hi) {
            return;
        }
        int mid = (lo + hi) / 2;
        mergeSort(a, temp, lo, mid);
        mergeSort(a, temp, mid + 1, hi);
        mergeHalves(a, temp, lo, mid, hi);
    }

    private static void mergeHalves(ArrayList<Integer> a, ArrayList<Integer> temp,
                                    int lo, int mid, int hi) {
        int i = lo;
        int j = mid + 1;
        int k = lo;
        while (i <= mid && j <= hi) {
            if (a.get(i) <= a.get(j)) {
                temp.set(k, a.get(i));
                i++;
            } else {
                temp.set(k, a.get(j));
                j++;
            }
            k++;
        }
        while (i <= mid) {
            temp.set(k, a.get(i));
            i++;
            k++;
        }
        while (j <= hi) {
            temp.set(k, a.get(j));
            j++;
            k++;
        }
        for (k = lo; k <= hi; k++) {
            a.set(k, temp.get(k));
        }
    }

    // quick
    public static void quick(ArrayList<Integer> a) {
        if (a.size() < 2) {
            return;
        }
        quick(a, 0, a.size() - 1);
    }

    private static void quick(ArrayList<Integer> a, int lo, int hi) {
        int i = lo;
        int j = hi;
        // el pivote es el valor de en medio
        int pivot = a.get((lo + hi) / 2);
        while (i <= j) {
            while (a.get(i) < pivot) {
                i++;
            }
            while (a.get(j) > pivot) {
                j--;
            }
            if (i <= j) {
                swap(a, i, j);
                i++;
                j--;
            }
        }
        if (lo < j) {
            quick(a, lo, j);
        }
        if (i < hi) {
            quick(a, i, hi);
        }
    }
}
