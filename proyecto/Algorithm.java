package evidencia;

import java.util.ArrayList;

// los 6 metodos
public enum Algorithm {
    BURBUJA("Burbuja"),
    SELECCION("Seleccion"),
    INSERCION("Insercion"),
    SHELL("Shell"),
    MERGE("Merge"),
    QUICK("Quick");

    // nombre
    public final String label;

    Algorithm(String label) {
        this.label = label;
    }

    // ordena
    public void sort(int[] a) {

        switch (this) {
            case BURBUJA:
                ArraySorters.bubble(a);
                break;
            case SELECCION:
                ArraySorters.selection(a);
                break;
            case INSERCION:
                ArraySorters.insertion(a);
                break;
            case SHELL:
                ArraySorters.shell(a);
                break;
            case MERGE:
                ArraySorters.merge(a);
                break;
            case QUICK:
                ArraySorters.quick(a);
                break;
        }
    }

    // ordena un ArrayList
    public void sort(ArrayList<Integer> l) {
        // igual que arriba
        switch (this) {
            case BURBUJA:
                ListSorters.bubble(l);
                break;
            case SELECCION:
                ListSorters.selection(l);
                break;
            case INSERCION:
                ListSorters.insertion(l);
                break;
            case SHELL:
                ListSorters.shell(l);
                break;
            case MERGE:
                ListSorters.merge(l);
                break;
            case QUICK:
                ListSorters.quick(l);
                break;
        }
    }
}
