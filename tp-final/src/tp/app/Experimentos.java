package tp.app;
import tp.modelo.Expedicion;
import tp.datos.DatosIniciales;
import tp.algoritmos.Ordenamientos;

/** esta clase realiza los experimentos para el informe de forma automatica
se repite todo el proceso para cada cantidad de expediciones del arreglo (tamanio) */
public class Experimentos { 
    private Experimentos(){}

    private static final int[] tamanio = {100, 500, 1000, 2000, 5000};
    private static final int REPETICIONES = 5, INSERCION = 1, MERGE = 2;
    private static final long SEMILLA = 67;
    private static final int CRITERIO = Ordenamientos.PUNTAJE;

    public static void generarExperimentos() {
        calentamiento();

        for (int i=0; i<tamanio.length; i++) {
            // se crea un arreglo desordenado, una copia ordenada, y otra invertida
            Expedicion[] desordenado = DatosIniciales.crearExpediciones(tamanio[i], SEMILLA);

            Expedicion[] ordenado = clonarArreglo(desordenado);
            Ordenamientos.ordenarDivideVenceras(ordenado, CRITERIO);

            Expedicion[] invertido = invertirArreglo(ordenado);

            System.out.println("Tamaño del arreglo: "+tamanio[i]+" expediciones");
            System.out.println("Cantidad de ordenamientos: "+REPETICIONES);
            mostrarPromedios(desordenado, "desordenado");
            mostrarPromedios(ordenado, "ordenado");
            mostrarPromedios(invertido, "invertido");
            System.out.println();
        }
    }

    /** este metodo es una preparacion de la JVM para la medicion de promedios
    sin esto, el algoritmo arroja promedios muy inflados al principio */
    private static void calentamiento() {
        Expedicion[] test = DatosIniciales.crearExpediciones(1000, SEMILLA);
        medirPromedios(test, INSERCION);
        medirPromedios(test, MERGE);
    }

    private static void mostrarPromedios(Expedicion[] arreglo, String situacion) {
        System.out.println("Situación: arreglo " + situacion);
        System.out.printf("Promedio fuerza bruta (ms): %.4f%n", medirPromedios(arreglo, INSERCION));
        System.out.printf("Promedio divide y vencerás (ms): %.4f%n", medirPromedios(arreglo, MERGE));
        System.out.println();
    }

    /** este metodo mide los promedios en base a cinco pasadas por cada metodo de ordenamiento
    el criterio de ordenamiento siempre es el mismo (puntaje) para que tenga sentido */
    private static double medirPromedios(Expedicion[] arreglo, int algoritmo) {
        double total = 0;

        if (algoritmo == INSERCION) {
            for (int i=0; i<REPETICIONES; i++) {
                Expedicion[] copia = clonarArreglo(arreglo);

                long inicio = System.nanoTime();
                Ordenamientos.ordenarFuerzaBruta(copia, CRITERIO);
                long tiempo = System.nanoTime() - inicio;
                total = total + tiempo;
            }
        } else if (algoritmo == MERGE) {
            for (int j=0; j<REPETICIONES; j++) {
                Expedicion[] copia = clonarArreglo(arreglo);

                long inicio = System.nanoTime();
                Ordenamientos.ordenarDivideVenceras(copia, CRITERIO);
                long tiempo = System.nanoTime() - inicio;
                total = total + tiempo;
            }
        }

        double promedio = (total / REPETICIONES) / 1000000;
        return promedio;
    }

    private static Expedicion[] clonarArreglo(Expedicion[] entrada) {
        Expedicion[] aux = new Expedicion[entrada.length];

        for (int i=0; i<entrada.length; i++) {
            aux[i] = entrada[i];
        }

        return aux;
    }

    private static Expedicion[] invertirArreglo(Expedicion[] entrada) {
        Expedicion[] aux = new Expedicion[entrada.length];

        for (int i=0; i<entrada.length; i++) {
            aux[i] = entrada[entrada.length-1-i];
        }

        return aux;
    }
}