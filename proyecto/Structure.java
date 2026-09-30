package evidencia;

// las dos estructuras
public enum Structure {
    ARREGLO("Array"),
    ARRAYLIST("ArrayList");

    public final String label;

    Structure(String label) {
        this.label = label;
    }
}
