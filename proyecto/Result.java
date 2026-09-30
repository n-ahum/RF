package evidencia;

// los datos de una implementacion en una corrida
// implements Comparable: sirve para ordenar los resultados por tiempo
public class Result implements Comparable<Result> {
    private final Algorithm algorithm;
    private final Structure structure;
    private final double wallMs;         // tiempo real (System.nanoTime)
    private final double cpuMs;          // tiempo de cpu que uso el hilo
    private final long allocatedBytes;   // bytes que pidio el hilo mientras ordenaba
    private final boolean sorted;        // quedo ordenado?

    public Result(Algorithm algorithm, Structure structure, double wallMs,
                  double cpuMs, long allocatedBytes, boolean sorted) {
        this.algorithm = algorithm;
        this.structure = structure;
        this.wallMs = wallMs;
        this.cpuMs = cpuMs;
        this.allocatedBytes = allocatedBytes;
        this.sorted = sorted;
    }

    // getters: sirven para leer los datos privados desde otras clases
    public Algorithm algorithm() {
        return algorithm;
    }

    public Structure structure() {
        return structure;
    }

    public double wallMs() {
        return wallMs;
    }

    public double cpuMs() {
        return cpuMs;
    }

    public long allocatedBytes() {
        return allocatedBytes;
    }

    public boolean sorted() {
        return sorted;
    }

    // llave para el ConcurrentHashMap
    public String key() {
        return algorithm.name() + "|" + structure.name();
    }

    // menor tiempo primero
    @Override
    public int compareTo(Result other) {
        return Double.compare(this.wallMs, other.wallMs);
    }
}
