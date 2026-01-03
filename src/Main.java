import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        // Centro de copias compartido por todos los alumnos.
        // El número indica cuántas copiadoras hay disponibles.
        CentroCopias cc = new CentroCopias(2);

        // Lista para guardar los hilos de los alumnos
        // y poder controlarlos (interrumpir y hacer join).
        ArrayList<Thread> alumnos = new ArrayList<>();

        // Creamos los alumnos (Runnable) y los envolvemos en hilos.
        // No se arrancan todavía, solo se crean.
        for(int i = 1; i <= 5; i++){
            Alumno a = new Alumno(cc, i);
            alumnos.add(new Thread(a));
        }

        // Arrancamos todos los hilos de alumnos.
        // A partir de aquí, cada alumno empieza su ciclo de estudio e impresión.
        for(Thread alumno : alumnos){
            alumno.start();
        }

        // El hilo principal (main) duerme durante 20 segundos.
        // Esto define el tiempo TOTAL de ejecución de la simulación.
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Pasados los 20 segundos, el main pide a todos los hilos
        // que terminen su ejecución mediante interrupt().
        for(Thread alumno : alumnos){
            alumno.interrupt();
        }

        // Esperamos a que todos los hilos terminen correctamente.
        // join() asegura que el programa no finaliza
        // hasta que todos los alumnos han acabado.
        for(Thread alumno : alumnos){
            try {
                alumno.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
