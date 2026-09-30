package evidencia;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

// una implementacion = una tarea = un hilo
// cada tarea tiene su propia copia de los datos
public class SortTask implements Runnable {
    // sirve para medir cpu y memoria del hilo
    private static final com.sun.management.ThreadMXBean TMX =
            (com.sun.management.ThreadMXBean) ManagementFactory.getThreadMXBean();

    // datos que necesita este hilo para trabajar
    private final Algorithm algorithm;
    private final Structure structure;
    private final int[] arrayData;                 // se usa si structure es ARREGLO
    private final ArrayList<Integer> listData;     // se usa si structure es ARRAYLIST
    private final ConcurrentHashMap<String, Result> results;
    private final CountDownLatch startGate;

    public SortTask(Algorithm algorithm, Structure structure, int[] arrayData,
                    ArrayList<Integer> listData,
                    ConcurrentHashMap<String, Result> results, CountDownLatch startGate) {
        this.algorithm = algorithm;
        this.structure = structure;
        this.arrayData = arrayData;
        this.listData = listData;
        this.results = results;
        this.startGate = startGate;
    }

    @Override
    public void run() {
        try {
            startGate.await();   // espera aqui para que los 12 arranquen juntos (no se mide)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        //solo el ordenamiento queda entre t0 y t1
        // se guarda cpu, memoria y hora ANTES de ordenar
        long cpu0 = TMX.getCurrentThreadCpuTime();
        long mem0 = TMX.getCurrentThreadAllocatedBytes();
        long t0 = System.nanoTime();

        if (structure == Structure.ARREGLO) {
            algorithm.sort(arrayData);
        } else {
            algorithm.sort(listData);
        }

        // se guarda tras ordenar
        long t1 = System.nanoTime();
        long mem1 = TMX.getCurrentThreadAllocatedBytes();
        long cpu1 = TMX.getCurrentThreadCpuTime();
        // aqui termina la parte medida

        // revisar si quedo ordenado (ya no se esta midiendo)
        boolean ok;
        if (structure == Structure.ARREGLO) {
            ok = SortVerifier.isSorted(arrayData);
        } else {
            ok = SortVerifier.isSorted(listData);
        }

        // de nanosegundos a milisegundos (1 ms = 1000000 ns)
        double wallMs = (t1 - t0) / 1000000.0;
        double cpuMs = (cpu1 - cpu0) / 1000000.0;
        long bytes = mem1 - mem0;

        Result r = new Result(algorithm, structure, wallMs, cpuMs, bytes, ok);
        results.put(r.key(), r);   // ConcurrentHashMap: seguro con 12 hilos escribiendo
    }
}
