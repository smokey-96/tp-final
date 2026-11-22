package tp.app;
import tp.modelo.*;
import tp.datos.DatosIniciales;
import tp.algoritmos.*;
import java.util.Random;
import java.util.Scanner;
public class Main {
    public static void main(String[] args){
        Ciudad ciudad=crearCiudad();
        Explorador[] exploradores=crearExploradores();
        Scanner teclado=new Scanner(System.in);
        int opcion = -1;
        Expedicion[] expediciones = new Expedicion[exploradores.length];
        boolean arrayLleno = false;
        do{
            System.out.println("\n1. Mostrar Mapa  2. Informe  3. Explorar  4. Ordenar  0. Salir");
            System.out.print("Opción: ");
            if(!teclado.hasNextInt()){teclado.nextLine();continue;}
            opcion=teclado.nextInt();
            teclado.nextLine();
            switch(opcion){
                case 1 :
                    ciudad.mostrarMapa();
                    break;
                case 2 :
                    ciudad.informeGeneral();
                    break;
                case 3 :
                    for (int i=0; i<exploradores.length; i++) {
                        expediciones[i] = explorar(ciudad, exploradores[i], i+1);
                    }
                    mostrarExpediciones(exploradores, expediciones);
                    arrayLleno = true;
                    break;
                case 4 : 
                    if (!arrayLleno) {
                        System.out.println("Se necesita explorar primero");
                    } else {
                        ordenarExpediciones(teclado, expediciones);
                    }
                    break;
                
                case 0 : System.out.println("Fin");
                break;
                default: System.out.println("Opción inválida");
            }
        }while(opcion!=0);
    }


    // se llaman a los metodos proporcionados por DatosIniciales para poblar el programa
    public static Ciudad crearCiudad() {
        return DatosIniciales.crearCiudad();
    }

    public static Explorador[] crearExploradores() {
        return DatosIniciales.crearExploradores();
    }

    public static Expedicion explorar(Ciudad ciudad,Explorador explorador,int idExpedicion) {
        // La matriz de booleanos sirve como "bingo" para marcar sectores ya visitados
        // Se mide el tiempo de ejecución del algoritmo recursivo con System.nanoTime()
        // contadorVisitadas es un arreglo para así poder actualizarlo desde dentro del algoritmo (ya que no se puede retornar)
        boolean[][] visitadas = new boolean[ciudad.getFilas()][ciudad.getColumnas()];
        int[] contadorVisitadas = new int[1];
        explorador.reiniciarResultados();

        long inicio = System.nanoTime();
        int tesoros = explorarDesde(ciudad, explorador, visitadas, explorador.getFila(), explorador.getColumna(), contadorVisitadas);
        long tiempoNanos = System.nanoTime() - inicio;

        Expedicion salida = new Expedicion(idExpedicion, explorador.getId(), tesoros, contadorVisitadas[0], explorador.getPuntaje(), tiempoNanos);
        return salida;
    }
    
    /** El auxiliar debe incluir casos base y cuatro llamadas recursivas. */
    private static int explorarDesde(Ciudad ciudad,Explorador explorador,boolean[][] visitadas,int fila,int columna,int[] contadorVisitadas){
        int salida = 0;
        
        if (ciudad.esTransitable(fila, columna) && !visitadas[fila][columna]) {
            visitadas[fila][columna] = true; // Se marca el sector en el "bingo"
            contadorVisitadas[0]++; // La suma se ve reflejada afuera

            Sector actual = ciudad.getSector(fila, columna);
            if (actual.esTesoro()) {
                salida++;
                explorador.registrarTesoro(actual.getValor());
            } else if (actual.esTrampa()) {
                explorador.registrarTrampa(actual.getValor());
            }

            // Se intenta explorar hacia arriba, abajo, izquierda y derecha
            salida = salida + explorarDesde(ciudad, explorador, visitadas, fila-1, columna, contadorVisitadas);
            salida = salida + explorarDesde(ciudad, explorador, visitadas, fila+1, columna, contadorVisitadas);
            salida = salida + explorarDesde(ciudad, explorador, visitadas, fila, columna-1, contadorVisitadas);
            salida = salida + explorarDesde(ciudad, explorador, visitadas, fila, columna+1, contadorVisitadas);
        }

        return salida;
    }

    private static void mostrarExpediciones(Explorador[] exploradores, Expedicion[] expediciones) {
        for (int i=0; i<expediciones.length; i++) {
            System.out.println("Explorador: "+exploradores[i].getNombre()
                +", tesoros: "+expediciones[i].getTesorosRecuperados()
                +", sectores visitados: "+expediciones[i].getCeldasVisitadas()
                +", puntaje: "+expediciones[i].getPuntaje());
            System.out.println();
        }
    }

    private static void ordenarExpediciones(Scanner teclado, Expedicion[] expediciones) {
        int criterio = -1;
        int algoritmo = -1;
        Expedicion[] copia = clonarArreglo(expediciones);

        // Se usan las estructuras do-while y el hasNextInt() para validar los inputs del usuario
        do { 
            System.out.println("Elija el criterio:");
            System.out.println("1. Mayor puntaje obtenido");
            System.out.println("2. Mayor cantidad de tesoros recuperados");
            System.out.println("3. Menor tiempo de expedición");
            System.out.println("4. Menor cantidad de sectores recorridos");

            if (teclado.hasNextInt()) {
                criterio = teclado.nextInt();
                teclado.nextLine();
            } else {
                teclado.nextLine();
                System.out.println("Entrada inválida.");
            }
        } while (criterio < 1 || criterio > 4);

        do {
            System.out.println("Elija el algoritmo de ordenamiento:");
            System.out.println("1. Fuerza bruta");
            System.out.println("2. Divide y vencerás");

            if (teclado.hasNextInt()) {
                algoritmo = teclado.nextInt();
                teclado.nextLine();
            } else {
                teclado.nextLine();
                System.out.println("Entrada inválida.");
            }
        } while (algoritmo < 1 || algoritmo > 2);

        if (algoritmo == 1) {
            Ordenamientos.ordenarFuerzaBruta(copia, criterio);
        } else {
            Ordenamientos.ordenarDivideVenceras(copia, criterio);
        }

        mostrarExpOrdenadas(copia);
    }

    private static void mostrarExpOrdenadas(Expedicion[] copia) {
        System.out.println("Expediciones ordenadas:");
        for (int i=0; i<copia.length; i++) {
            System.out.println(copia[i].toString());
            System.out.println();
        }
    }

    // Se clona el arreglo de expediciones para no editar el original
    // como las expediciones solo se ordenan, no hace falta una copia profunda.
    private static Expedicion[] clonarArreglo(Expedicion[] entrada) {
        Expedicion[] aux = new Expedicion[entrada.length];

        for (int i=0; i<entrada.length; i++) {
            aux[i] = entrada[i];
        }

        return aux;
    }
}