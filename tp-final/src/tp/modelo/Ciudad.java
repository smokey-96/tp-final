package tp.modelo;
public class Ciudad {
    private  Sector[][] sectores;

    public Ciudad(int filas,int columnas){
        if(filas<=0||columnas<=0)throw new IllegalArgumentException("Dimensiones inválidas");

        sectores = new Sector[filas][columnas];

        //se puebla la matriz con caminos libres
        for (int f=0; f<filas; f++) {
            for (int c=0; c<columnas; c++) {
                sectores[f][c] = new Sector(Sector.LIBRE, 0);
            }
        }
    }

    public int getFilas() {
        return this.sectores.length;
    }

    public int getColumnas() {
        return this.sectores[0].length;
    }

    public boolean posicionValida(int fila,int columna) {
       boolean valido = false;

       if (fila>=0 && fila<this.getFilas() && columna>=0 && columna<this.getColumnas()) {
            valido = true;
       }

       return valido;
    }

    public Sector getSector(int fila,int columna) {
        if(!posicionValida(fila,columna))throw new IndexOutOfBoundsException("Posición inválida");
        return sectores[fila][columna];
    }

    public void modificar(int fila,int columna,Sector sector) {
        if(!posicionValida(fila, columna)) throw new IndexOutOfBoundsException("Posición inválida");
        this.sectores[fila][columna] = sector;
    }

    public boolean esTransitable(int fila,int columna){
        //fuera de rango o muro devuelve false.
        boolean transitable = false;

        if (this.posicionValida(fila, columna) && sectores[fila][columna]!=null) {
            transitable = true;
        }

        return transitable;
    }

    public int contarTesoros() {
        int tesoros = contarEnMatriz(Sector.TESORO);
        return tesoros;
    }

    public int contarTrampas() {
        int trampas = contarEnMatriz(Sector.TRAMPA);
        return trampas;
    }

    public int contarTransitables() {
        int contador = 0;
        
        for (int f=0; f<this.getFilas(); f++) {
            for(int c=0; c<this.getColumnas(); c++) {
                if (this.sectores[f][c]!=null) {
                    contador++;
                }
            }
        }

        return contador;
    }

    public int[] tesorosPorFila() {
        int[] tesorosFila = contarEnFila(Sector.TESORO);
        return tesorosFila;
    }

    public int[] tesorosPorColumna() {
        int[] tesorosCol = contarEnColumna(Sector.TESORO);
        return tesorosCol;
    }

    public int[] trampasPorFila() {
        int[] trampasFila = contarEnFila(Sector.TRAMPA);
        return trampasFila;
    }

    public int[] trampasPorColumna() {
        int[] trampasCol = contarEnColumna(Sector.TRAMPA);
        return trampasCol;
    }

    /** En caso de empate devolver el índice menor; si no hay tesoros, devolver 0. */
    public int filaConMasTesoros() {
        int fila = 0;
        int[] tesoros = tesorosPorFila();

        for (int i=0; i<tesoros.length; i++) {
            if (tesoros[fila] < tesoros[i]) {
                fila = i;
            }
        }

        return fila;
    }

    public int columnaConMasTesoros() {
        int columna = 0;
        int[] tesoros = tesorosPorColumna();

        for (int i=0; i<tesoros.length; i++) {
            if (tesoros[columna] < tesoros[i]) {
                columna = i;
            }
        }

        return columna;
    }
    

    public void  mostrarMapa() {
        for (int f=0; f<this.getFilas(); f++) {
            for (int c=0; c<this.getColumnas(); c++) {
                if (this.sectores[f][c]!=null) {
                    System.out.print(this.sectores[f][c].toString());
                } else {
                    System.out.print("⬛");
                }
            }
            System.out.println();
        }
    }

    public Ciudad clonar(){
        //Retorna una ciudad que es una copia de la ciudad actual.
        Ciudad salida = new Ciudad(this.getFilas(), this.getColumnas());

        for (int f=0; f<salida.getFilas(); f++) {
            for (int c=0; c<salida.getColumnas(); c++) {
                if (this.sectores[f][c]!=null) {
                    Sector original = this.sectores[f][c];
                    salida.sectores[f][c] = new Sector(original.getTipo(), original.getValor());
                } else {
                    salida.sectores[f][c] = null;
                }
            }
        }

        return salida;
    }

    public void informeGeneral() {
        System.out.println("La ciudad tiene "+this.getFilas()+" filas y "+this.getColumnas()+" columnas.");
        System.out.println("Hay "+this.contarTransitables()+" sectores transitables, de los cuales");
        System.out.println(this.contarTesoros()+" son tesoros y "+this.contarTrampas()+" son trampas");
        System.out.println("La fila con más tesoros es la "+this.filaConMasTesoros()+", y la columna");
        System.out.println("con más tesoros es la "+this.columnaConMasTesoros());
    }

    /** --------------- METODOS AUXILIARES PRIVADOS --------------- */

    private int contarEnMatriz(int tipoSector) {
        int contador = 0;

        for (int f=0; f<this.getFilas(); f++) {
            for (int c=0; c<this.getColumnas(); c++) {
                if (this.sectores[f][c]!=null && this.sectores[f][c].getTipo() == tipoSector) {
                    contador++;
                }
            }
        }

        return contador;
    }

    private int[] contarEnFila(int tipo) {
        int[] salida = new int[this.getFilas()];

        for (int f=0; f<this.getFilas(); f++) {
            for (int c=0; c<this.getColumnas(); c++) {
                if (this.sectores[f][c]!=null && this.sectores[f][c].getTipo()==tipo) {
                    salida[f]++;
                }
            }
        }

        return salida;
    }

    private int[] contarEnColumna(int tipo) {
        int[] salida = new int[this.getColumnas()];

        for (int c=0; c<this.getColumnas(); c++) {
            for (int f=0; f<this.getFilas(); f++) {
                if (this.sectores[f][c]!=null && this.sectores[f][c].getTipo()==tipo) {
                    salida[c]++;
                }
            }
        }

        return salida;
    }
}