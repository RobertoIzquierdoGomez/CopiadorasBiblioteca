public class Alumno implements Runnable {

    // Referencia al centro de copias compartido.
    private CentroCopias cc;

    // Identificador del alumno.
    private int id;

    // Copiadora que tiene asignada actualmente.
    private int copiadoraId;

    // Número de copias realizadas por el alumno.
    private int cantidadCopias;

    // Constructor del alumno.
    public Alumno(CentroCopias cc, int id){
        this.cc = cc;
        this.id = id;
        cantidadCopias = 0;
    }

    /**
     * Método run() del hilo.
     *
     * El alumno:
     * - Estudia
     * - Va al centro de copias
     * - Espera si no hay copiadoras
     * - Imprime
     * - Libera la copiadora
     *
     * El bucle continúa hasta que el hilo es interrumpido
     * por el hilo principal (main).
     */
    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()){
            estudia();
            System.out.println("El alumno " + id + " va al centro de copias");

            try {
                // Solicita una copiadora (puede bloquearse).
                copiadoraId = cc.imprimir();

                cantidadCopias++;

                System.out.println(
                        "El alumno " + id +
                                " está imprimiendo en copiadora " + (copiadoraId + 1)
                );

                // Simula el tiempo de impresión.
                Thread.sleep(500);

                // Libera la copiadora tras imprimir.
                cc.liberaCopiadora(this);

                // Simula tiempo antes de volver a estudiar.
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                // Si el hilo es interrumpido:
                // - libera la copiadora si la estaba usando
                // - muestra el número de copias realizadas
                // - termina el hilo limpiamente
                cc.liberaCopiadora(this);
                System.out.println("El alumno " + id + " ha imprimido " + cantidadCopias);
                return;
            }
        }
    }

    // Simula el tiempo de estudio del alumno.
    public void estudia(){
        System.out.println("El alumno " + id + " está estudiando");
    }

    // Getters
    public int getCopiadoraId() {
        return copiadoraId;
    }

    public void setCopiadoraId(int copiadoraId) {
        this.copiadoraId = copiadoraId;
    }

    public int getId() {
        return id;
    }
}
