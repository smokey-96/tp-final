package tp.modelo;
/** null en la matriz significa muro; un Sector nunca es muro. */
public class Sector {
    public static final int INICIO=0, LIBRE=1, TESORO=2, TRAMPA=3; //final significa que es una constante
    private int tipo;
    private int valor; // Recompensa positiva del tesoro o penalización positiva de la trampa.
    
    public Sector(int tipo,int valor) {
        if (tipo<INICIO || tipo>TRAMPA || valor<0) throw new IllegalArgumentException("Datos inválidos");

        this.tipo = tipo;
        this.valor = valor;
    }

    public int getTipo() {
        return this.tipo;
    }

    public int getValor() {
        return this.valor;
    }

    private String getEmoji(){
        //Devuelve un simbolo para cada sector

        String simbolo = 
                 (this.getTipo() == 0) ? "🚩" :
                 (this.getTipo() == 1) ? "⬜" :
                 (this.getTipo() == 2) ? "💎" : "⚠️";
        return simbolo;
    }

    public boolean esTesoro() {
        return (this.tipo == TESORO);
    }

    public boolean esTrampa() {
        return (this.tipo == TRAMPA);
    }

    @Override
    public String toString() {
        return this.getEmoji();
    }
}