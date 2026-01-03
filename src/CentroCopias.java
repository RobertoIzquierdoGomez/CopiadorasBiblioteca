public class CentroCopias {

    // Array que representa el estado de las copiadoras.
    // false = libre
    // true  = ocupada
    private boolean[] copiadorasOcupadas;

    // Constructor: crea el número de copiadoras indicado.
    // Por defecto, todas empiezan libres (false).
    public CentroCopias(int copiadoras){
        this.copiadorasOcupadas = new boolean[copiadoras];
    }

    /**
     * Busca la primera copiadora libre.
     * @return índice de la copiadora libre o -1 si no hay ninguna.
     *
     * synchronized porque accede a un recurso compartido
     * que usan varios hilos simultáneamente.
     */
    public synchronized int copiadoraLibre(){
        for(int i = 0; i < copiadorasOcupadas.length; i++){
            if(!copiadorasOcupadas[i]){
                return i;
            }
        }
        return -1;
    }

    /**
     * Método que simula el acceso a una copiadora.
     *
     * - Si no hay copiadoras libres, el hilo espera (wait).
     * - Cuando se despierta, vuelve a comprobar la condición.
     * - Cuando hay una libre, la marca como ocupada
     *   y devuelve su identificador.
     */
    public synchronized int imprimir() throws InterruptedException {

        // Mientras no haya copiadoras libres, el hilo espera.
        // Se usa while y no if para protegerse de despertares espurios.
        while(copiadoraLibre() < 0){
            wait();
        }

        // Se obtiene una copiadora libre y se reserva.
        int idCopiadora = copiadoraLibre();
        copiadorasOcupadas[idCopiadora] = true;

        // Se devuelve la copiadora asignada al alumno.
        return idCopiadora;
    }

    /**
     * Libera la copiadora que estaba usando un alumno.
     *
     * - Marca la copiadora como libre.
     * - Llama a notifyAll() para despertar a los alumnos
     *   que estén esperando una copiadora.
     */
    public synchronized void liberaCopiadora(Alumno a){
        System.out.println(
                "El alumno " + a.getId() +
                        " deja de utilizar la copiadora " + (a.getCopiadoraId() + 1)
        );

        copiadorasOcupadas[a.getCopiadoraId()] = false;

        // Despierta a todos los hilos en espera para que vuelvan
        // a comprobar si ya hay copiadoras disponibles.
        notifyAll();
    }
}
