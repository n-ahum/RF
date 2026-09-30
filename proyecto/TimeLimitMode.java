package evidencia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

// reto - cada implementacion (en su hilo) ordena cuantas colecciones nuevas pueda
// antes del tiempo limite, las copias no cuentan en el tiempo medido
public final class TimeLimitMode {
    private TimeLimitMode() {}

    // guarda cuantas colecciones ordeno una implementacion y el promedio por coleccion
    private static class Score implements Comparable<Score> {
        Algorithm algorithm;
        Structure structure;
        int count;
        double avgMs;

        Score(Algorithm algorithm, Structure structure, int count, double avgMs) {
            this.algorithm = algorithm;
            this.structure = structure;
            this.count = count;
            this.avgMs = avgMs;
        }

        // el que ordeno mas colecciones va primero
        @Override
        public int compareTo(Score other) {
            return Integer.compare(other.count, this.count);
        }
    }

    // lo que hace cada hilo en este modo
    private static class LimitTask implements Runnable {
        Algorithm alg;
        Structure st;
        int[] original;
        long limitMs;
        CountDownLatch gate;
        ConcurrentHashMap<String, Score> scores;

        LimitTask(Algorithm alg, Structure st, int[] original, long limitMs,
                  CountDownLatch gate, ConcurrentHashMap<String, Score> scores) {
            this.alg = alg;
            this.st = st;
            this.original = original;
            this.limitMs = limitMs;
            this.gate = gate;
            this.scores = scores;
        }

        @Override
        public void run() {
            try {
                gate.await();
            } catch (InterruptedException e) {
                return;
            }

            // hora limite = ahora + el tiempo que dio el usuario
            long deadline = System.nanoTime() + limitMs * 1000000L;
            int count = 0;
            long totalNs = 0;

            // sigue ordenando colecciones nuevas hasta que se acabe el tiempo
            while (System.nanoTime() < deadline) {
                // copia nueva cada vez (la copia no se mide)
                if (st == Structure.ARREGLO) {
                    int[] copy = DataGenerator.copyArray(original);
                    long t0 = System.nanoTime();
                    alg.sort(copy);
                    totalNs = totalNs + (System.nanoTime() - t0);
                } else {
                    ArrayList<Integer> copy = DataGenerator.toList(original);
                    long t0 = System.nanoTime();
                    alg.sort(copy);
                    totalNs = totalNs + (System.nanoTime() - t0);
                }
                count++;
            }

            // promedio por coleccion en milisegundos
            double avg = 0;
            if (count > 0) {
                avg = totalNs / (double) count / 1000000.0;
            }
            scores.put(alg.name() + "|" + st.name(), new Score(alg, st, count, avg));
        }
    }

    public static void run(int[] original, long limitMs) {
        ConcurrentHashMap<String, Score> scores = new ConcurrentHashMap<String, Score>();
        CountDownLatch gate = new CountDownLatch(1);
        List<Thread> threads = new ArrayList<Thread>();

        for (Algorithm alg : Algorithm.values()) {
            for (Structure st : Structure.values()) {
                LimitTask task = new LimitTask(alg, st, original, limitMs, gate, scores);
                threads.add(new Thread(task, alg.label + "-" + st.label));
            }
        }

        for (Thread t : threads) {
            t.start();
        }
        gate.countDown();
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // se ordena para que el que hizo mas colecciones salga primero
        List<Score> list = new ArrayList<Score>(scores.values());
        Collections.sort(list);

        System.out.println();
        System.out.println("RETO: colecciones ordenadas en " + limitMs + " ms  (Elementos por coleccion: "
                + original.length + ")");
        System.out.println();
        System.out.printf("%-6s %-12s %-12s %-14s %s%n",
                "Pos.", "Algoritmo", "Estructura", "Colecciones", "Prom./colec (ms)");

        int pos = 1;
        for (Score s : list) {
            System.out.printf("%-6d %-12s %-12s %-14d %.2f%n",
                    pos, s.algorithm.label, s.structure.label, s.count, s.avgMs);
            pos++;
        }

        Score best = list.get(0);
        System.out.println();
        System.out.println("Mas colecciones completadas: " + best.algorithm.label
                + " (" + best.structure.label + ") con " + best.count);
    }
}
