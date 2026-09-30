package evidencia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

// toda la salida en consola del modo comparacion
public final class ReportPrinter {
    private ReportPrinter() {}

    public static void printResults(int n, String dataMode, int run, int totalRuns,
                                    List<Result> results) {
        // copia de la lista para no modificar la original
        List<Result> sorted = new ArrayList<Result>(results);
        Collections.sort(sorted);   // el mas rapido primero

        System.out.println();
        // el numero de corrida solo sale si hay varias
        String title = "RESULTADOS DE ORDENAMIENTO";
        if (totalRuns > 1) {
            title = title + "  (corrida " + run + "/" + totalRuns + ")";
        }
        System.out.println(title);
        System.out.println("Elementos: " + n + "   Datos: " + dataMode);
        System.out.println();
        System.out.printf("%-6s %-12s %-12s %-14s %-10s %-14s %s%n",
                "Pos.", "Algoritmo", "Estructura", "Tiempo (ms)", "CPU (ms)", "Memoria (KB)", "Ordeno?");

        // pos es el lugar en la tabla (1 = el mas rapido)
        int pos = 1;
        for (Result r : sorted) {
            // pasa true/false a texto para la tabla
            String ok = "No";
            if (r.sorted()) {
                ok = "Si";
            }
            System.out.printf("%-6d %-12s %-12s %-14.2f %-10.2f %-14.1f %s%n",
                    pos, r.algorithm().label, r.structure().label,
                    r.wallMs(), r.cpuMs(), r.allocatedBytes() / 1024.0, ok);
            pos++;
        }

        // el primero de la lista ordenada es el mas rapido
        Result best = sorted.get(0);
        System.out.println();
        System.out.println("Implementacion con menor tiempo registrado: "
                + best.algorithm().label + " (" + best.structure().label + ")");
        System.out.println("Nota: Tiempo = reloj real (incluye esperar turno de CPU con 12 hilos);"
                + " CPU = tiempo que el hilo realmente ejecuto.");
    }

    // promedios de varias corridas
    // sums.get(llave) = {suma de tiempo, suma de cpu, suma de bytes, veces que quedo ordenado}
    public static void printAverages(int n, int runs, Map<String, double[]> sums) {
        // se arma una lista con los promedios para poder ordenarla
        List<Result> averages = new ArrayList<Result>();
        // recorre las 12 combinaciones de algoritmo y estructura
        for (Algorithm a : Algorithm.values()) {
            for (Structure s : Structure.values()) {
                String key = a.name() + "|" + s.name();
                double[] v = sums.get(key);
                double avgWall = v[0] / runs;
                double avgCpu = v[1] / runs;
                long avgBytes = (long) (v[2] / runs);
                averages.add(new Result(a, s, avgWall, avgCpu, avgBytes, true));
            }
        }
        // ordena los promedios del mas rapido al mas lento
        Collections.sort(averages);

        System.out.println();
        System.out.println("PROMEDIO DE " + runs + " CORRIDAS  (Elementos: " + n + ")");
        System.out.println();
        System.out.printf("%-6s %-12s %-12s %-14s %-10s %-14s %s%n",
                "Pos.", "Algoritmo", "Estructura", "Prom. (ms)", "CPU (ms)", "Memoria (KB)", "Ordeno");

        int pos = 1;
        for (Result r : averages) {
            // en cuantas corridas quedo bien ordenado
            int timesOk = (int) sums.get(r.key())[3];
            System.out.printf("%-6d %-12s %-12s %-14.2f %-10.2f %-14.1f %s%n",
                    pos, r.algorithm().label, r.structure().label,
                    r.wallMs(), r.cpuMs(), r.allocatedBytes() / 1024.0,
                    timesOk + "/" + runs);
            pos++;
        }
    }
}
