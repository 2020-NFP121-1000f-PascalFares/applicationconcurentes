/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package executorexemple;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author pascalfares
 */
public class ExecutorExemple {
    public static void main(String[] args) throws ExecutionException {
        ExecutorService service = Executors.newFixedThreadPool(4);
        // création de 20 futures pour récupérer le résultat 
        // de nos tâches
        Future<Integer>[] futures = new Future[20];
        for (int i = 0; i < futures.length; i++) {
            // pour être utilisée dans une classe anonyme, 
            // cette variable doit être final, ce ne peut donc pas être i
            final int k = i;
            System.out.println("Lancement de " + k);
            futures[i] = service.submit(() -> {
                // simulation d'un traitement
                System.out.println("[" + k + "] thread : " + Thread.currentThread().getName());
                return k;
            });
        }
        // autres traitement dans le thread courant
        // tableau des résultats
        int[] result = new int[futures.length];
        // récupération des résultats par interrogation des
        // futures
        for (int i = 0; i < futures.length; i++) {
            try {
                // on donne 100ms à un future pour répondre
                result[i] = futures[i].get(100, TimeUnit.MILLISECONDS);
                System.out.println(result[i]);
                // gestion des exceptions
            } catch (TimeoutException | ExecutionException | InterruptedException e) {
                result[i] = -1;
            }
        }
        // fermeture du service
        // sans quoi la JVM reste active
        service.shutdown();
        System.out.println("Fin du traitement");

    }
}
