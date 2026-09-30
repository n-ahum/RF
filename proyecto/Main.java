package evidencia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] args) {

        // scanner para leer lo que escribe el usuario
        Scanner sc = new Scanner(System.in);

        System.out.println("--------------------------------------------------");
        // again decide si el menu se repite
        boolean again = true;
        while (again) {
            System.out.println();
            System.out.println("[1] 12 algoritmos");
            System.out.println("[2] reto opcional");

            // pide la opcion del menu
            int option = readInt(sc, "opcion: ", 1);
            if (option > 2) {
                System.out.println("Opcion invalida");
                continue;
            }
            System.out.println("--------------------------------------------------");


            // n es cuantos numeros
            int n = readInt(sc, "cuantos elementos vas a ordenar? ", 1);

            // 1 = aleatorios 2 = solo del 1 al 5
            int dataType = readInt(sc, "datos: 1) aleatorios  2) solo del 1 al 5 : ", 1);
            boolean only1to5 = false;
            String dataMode = "aleatorios";
            if (dataType == 2) {
                only1to5 = true;
                dataMode = "solo 1-5";
            }
            // se crean solo una vez, los alg usan copias
            int[] original = DataGenerator.generate(n, only1to5);

            // opcion 2 es el reto
            if (option == 2) {
                long limit = readInt(sc, "tiempo limite en milisegundos: ", 1);
                TimeLimitMode.run(original, limit);
            } else {
                int runs = readInt(sc, "cuantas corridas? ", 1);
                compare(n, dataMode, runs, original);
            }

            System.out.print("\nrepetir con otra prueba? (si/no): ");
            // preguntar
            String answer = sc.next();
            again = answer.equalsIgnoreCase("si");
        }
        System.out.println("cerrando.");
    }

    private static void compare(int n, String dataMode, int runs, int[] original) {
        // para la tabla de promedios: llave -> [tiempo, cpu, bytes, veces ordenado]
        Map<String, double[]> sums = new HashMap<String, double[]>();
        // repite la comparacion las veces que pidio el usuario
        for (int run = 1; run <= runs; run++) {
            // aqui se crean y corren los 12 hilos
            ConcurrentHashMap<String, Result> results = SortRunner.runAll(original);
            List<Result> list = new ArrayList<Result>(results.values());
            ReportPrinter.printResults(n, dataMode, run, runs, list);

            for (Result r : list) {
                // busca lo acumulado,, si no existe, se crea
                double[] s = sums.get(r.key());
                if (s == null) {
                    s = new double[4];
                    sums.put(r.key(), s);
                }
                // s[0] = suma de tiempo, s[1] = suma de cpu, s[2] = suma de bytes
                // s[3] = cuantas veces quedo bien ordenado
                s[0] = s[0] + r.wallMs();
                s[1] = s[1] + r.cpuMs();
                s[2] = s[2] + r.allocatedBytes();
                if (r.sorted()) {
                    s[3] = s[3] + 1;
                }
            }
        }
        // el promedio solo tiene sentido si hubo mas de una corrida
        if (runs > 1) {
            ReportPrinter.printAverages(n, runs, sums);
        }
    }


    private static int readInt(Scanner sc, String prompt, int min) {
        // repite hasta que escriba un numero valido
        while (true) {
            System.out.print(prompt);
            String line = sc.next();
            try {
                // pasa el texto a numero (si no es numero da error y se atrapa abajo)
                int v = Integer.parseInt(line.trim());
                if (v >= min) return v;
            } catch (NumberFormatException ignored) { }
            System.out.println("Opcion invalida");
        }
    }
}
