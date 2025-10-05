package com.algaworks.ecommerce.concorrencia;

import org.junit.jupiter.api.Test;

public class ThreadTest {

    public static void log(Object obj, Object... args) {
        System.out.println(
                String.format("[LOG " + System.currentTimeMillis() + "] " + obj, args)
        );
    }

    private static void esperar(int segundos){
        try {
            Thread.sleep(segundos * 1000);
        } catch (InterruptedException e){
            System.out.println(e.getCause().getMessage());
        }
    }

    @Test
    public void entenderThreads () {
        Runnable runnable1 = () -> {
            log("Runnable 1 vai esperar 3 segundos.");
            esperar(3);
            log("Runnable 1 concluído.");
        };

        Runnable runnable2 = () -> {
            log("Runnable 2 vai esperar 3 segundos.");
            esperar(3);
            log("Runnable 2 concluído.");
        };

        Thread thread1 = new Thread(runnable1);
        Thread thread2 = new Thread(runnable2);

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
