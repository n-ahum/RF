package evidencia;

// los 6 metodos para arreglos (int[])
public final class ArraySorters {
    private ArraySorters() {}

    private static void swap(int[] a, int i, int j) {
        int aux = a[i];
        a[i] = a[j];
        a[j] = aux;
    }

    // burbuja
    // compara vecinos y los cambia si estan alreves. el mayor va quedando al final
    // si una pasada no cambia nada, ya termino
    public static void bubble(int[] a) {
        // limit baja en cada pasada porque los ultimos ya estan en su lugar
        int limit = a.length - 1;
        // swapped avisa si hubo cambios en la pasada
        boolean swapped = true;
        while (swapped && limit > 0) {
            swapped = false;
            for (int i = 0; i < limit; i++) {
                if (a[i] > a[i + 1]) {
                    swap(a, i, i + 1);
                    swapped = true;
                }
            }
            limit--;
        }
    }

    // seleccion
    // busca el menor del resto y lo pone en la posicion i.
    public static void selection(int[] a) {
        int n = a.length;
        for (int i = 0; i < n - 1; i++) {
            // min guarda la posicion del menor que va encontrando
            int min = i;
            for (int j = i + 1; j < n; j++) {
                if (a[j] < a[min]) {
                    min = j;
                }
            }
            if (min != i) {
                swap(a, i, min);
            }
        }
    }

    // insercion
    // toma a[i], mueve a la derecha los mayores y lo mete en el hueco.
    public static void insertion(int[] a) {
        for (int i = 1; i < a.length; i++) {
            // key es el valor que se va a insertar
            int key = a[i];
            int j = i - 1;
            while (j >= 0 && a[j] > key) {
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = key;
        }
    }

    // shell
    // insert con saltos (gap), empieza en N/2 y se divide entre 2 hasta llegar a 1
    public static void shell(int[] a) {
        int n = a.length;
        // gap es la distancia entre los elementos que se comparan
        for (int gap = n / 2; gap > 0; gap = gap / 2) {
            for (int i = gap; i < n; i++) {
                int temp = a[i];
                int j = i;
                while (j >= gap && a[j - gap] > temp) {
                    a[j] = a[j - gap];
                    j = j - gap;
                }
                a[j] = temp;
            }
        }
    }

    // merge
    // crea un arreglo auxiliar (mas memoria)
    public static void merge(int[] a) {
        if (a.length < 2) {
            return;
        }
        // arreglo auxiliar del mismo tamano
        int[] temp = new int[a.length];
        mergeSort(a, temp, 0, a.length - 1);
    }

    // a: divide en dos mitades, ordena y las mezcla
    private static void mergeSort(int[] a, int[] temp, int lo, int hi) {
        if (lo >= hi) {
            return;   // un solo elemento ya esta ordenado
        }
        // mid es la mitad de este pedazo
        int mid = (lo + hi) / 2;
        mergeSort(a, temp, lo, mid);
        mergeSort(a, temp, mid + 1, hi);
        mergeHalves(a, temp, lo, mid, hi);
    }

    // b: mezcla las dos mitades ordenadas, toma el menor
    private static void mergeHalves(int[] a, int[] temp, int lo, int mid, int hi) {
        int i = lo;
        int j = mid + 1;
        int k = lo;
        while (i <= mid && j <= hi) {
            if (a[i] <= a[j]) {
                temp[k] = a[i];
                i++;
            } else {
                temp[k] = a[j];
                j++;
            }
            k++;
        }
        while (i <= mid) {
            temp[k] = a[i];
            i++;
            k++;
        }
        while (j <= hi) {
            temp[k] = a[j];
            j++;
            k++;
        }
        // copia lo mezclado de vuelta original
        for (k = lo; k <= hi; k++) {
            a[k] = temp[k];
        }
    }

    // quick
    public static void quick(int[] a) {
        if (a.length < 2) {
            return;
        }
        quick(a, 0, a.length - 1);
    }

    // pivote = el valor de en medio. i avanza mientras sea menor al pivote
    // j retrocede mientras sea mayor, y se intercambian
    //  al cruzarse se repite en cada lado
    private static void quick(int[] a, int lo, int hi) {
        int i = lo;
        int j = hi;
        // el pivote es el valor de en medio
        int pivot = a[(lo + hi) / 2];
        while (i <= j) {
            while (a[i] < pivot) {
                i++;
            }
            while (a[j] > pivot) {
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
