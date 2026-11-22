package tp.algoritmos;
import tp.modelo.Expedicion;
public final class Ordenamientos {
    private Ordenamientos(){}
    public static final int PUNTAJE=1,TESOROS=2,TIEMPO=3,CELDAS=4;
    /** Compara según criterio: negativo si a va antes que b, positivo si va después.
     * Empates: id ascendente para obtener orden determinista.
     */
    public static int comparar(Expedicion a,Expedicion b,int criterio){
        int salida = 0;

        if (criterio == PUNTAJE) {
            if (a.getPuntaje() > b.getPuntaje()) {
                salida = -1;
            } else if (a.getPuntaje() < b.getPuntaje()) {
                salida = 1;
            }
        } else if (criterio == TESOROS) {
            if (a.getTesorosRecuperados() > b.getTesorosRecuperados()) {
                salida = -1;
            } else if (a.getTesorosRecuperados() < b.getTesorosRecuperados()) {
                salida = 1;
            }
        } else if (criterio == TIEMPO) {
            if (a.getTiempoNanos() > b.getTiempoNanos()) {
                salida = 1;
            } else if (a.getTiempoNanos() < b.getTiempoNanos()) {
                salida = -1;
            }
        } else if (criterio == CELDAS) {
            if (a.getCeldasVisitadas() > b.getCeldasVisitadas()) {
                salida = 1;
            } else if (a.getCeldasVisitadas() < b.getCeldasVisitadas()) {
                salida = -1;
            }
        }

        if (salida == 0) {
            if (a.getId() > b.getId()) {
                salida = 1;
            } else if (a.getId() < b.getId()){
                salida = -1;
            }
        }

        return salida;
    }

    // algoritmo de fuerza bruta (inserción)
    public static void ordenarFuerzaBruta(Expedicion[] datos,int criterio){
        for (int i = 1; i < datos.length; i++) {
            Expedicion exp = datos[i];
            int j = i - 1;
            while (j >= 0 && comparar(datos[j], exp, criterio) > 0) {
                datos[j + 1] = datos[j];
                j--;
            }
            datos[j + 1] = exp;
        }
    }
    
    // algoritmo de divide y vencerás (mergeSort)
    public static void ordenarDivideVenceras(Expedicion[] datos,int criterio){
        mergeSort(datos, 0, datos.length-1, criterio);
    }

    private static void mergeSort(Expedicion[] datos, int inicio, int fin, int criterio) {
        int medio;
        if (inicio < fin) {
            medio = (inicio + fin) / 2;
            mergeSort(datos, inicio, medio, criterio);
            mergeSort(datos, medio+1, fin, criterio);
            merge(datos, inicio, medio, fin, criterio);
        }
    }

    private static void merge(Expedicion[] datos, int inicio, int medio, int fin, int criterio) {
        int tamIzq = medio - inicio + 1;
        int tamDer = fin - medio;
        Expedicion[] izq = new Expedicion[tamIzq];
        Expedicion[] der = new Expedicion[tamDer];
        int i = 0, j = 0, k = inicio;

        for (int a=0; a<tamIzq; a++) {
            izq[a] = datos[inicio + a];
        }

        for (int b=0; b<tamDer; b++) {
            der[b] = datos[medio + 1 + b];
        }

        while (i<tamIzq && j<tamDer) {
            if (comparar(izq[i], der[j], criterio) <= 0) {
                datos[k] = izq[i];
                i++;
            } else {
                datos[k] = der[j];
                j++;
            }
            k++;
        }

        while (i<tamIzq) {
            datos[k] = izq[i];
            i++;
            k++;
        }

        while (j<tamDer) {
            datos[k] = der[j];
            j++;
            k++;
        }
    }
}
