package evidencia;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

// crea los 12 hilos, los inicia juntos, espera a que todos terminen y regresa los resultados
public final class SortRunner {
    private SortRunner() {}

    public static ConcurrentHashMap<String, Result> runAll(int[] original) {
        // aqui guardan sus resultados los 12 hilos
        ConcurrentHashMap<String, Result> results = new ConcurrentHashMap<String, Result>();
        //los hilos esperan aqui hasta que se abre
        CountDownLatch startGate = new CountDownLatch(1);
        List<Thread> threads = new ArrayList<Thread>();

        //6 algoritmos con 2 estructuras, cada uno con su copia
        for (Algorithm alg : Algorithm.values()) {
            for (Structure st : Structure.values()) {
                int[] arr = null;
                ArrayList<Integer> list = null;
                if (st == Structure.ARREGLO) {
                    arr = DataGenerator.copyArray(original);
                } else {
                    list = DataGenerator.toList(original);
                }
                // la tarea lleva el algoritmo, la estructura y su propia copia
                SortTask task = new SortTask(alg, st, arr, list, results, startGate);
                // el hilo se llama por ejemplo Quick-Array
                threads.add(new Thread(task, alg.label + "-" + st.label));
            }
        }

        //los hilos arrancan pero esperan en la compuerta
        for (Thread t : threads) {
            t.start();
        }

        //se abre la compuerta y los 12 empiezan al mismo tiempo
        startGate.countDown();

        //main espera a que todos los hilos terminen
        for (Thread t : threads) {
            try {
                // main espera aqui hasta que ese hilo termine
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return results;
    }
}
