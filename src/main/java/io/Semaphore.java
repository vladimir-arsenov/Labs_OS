package io;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;

public class Semaphore {
    public static CyclicBarrier b;
    public static int stage = 0; // номер текущей точки
    public static Map<String, T> threads = new HashMap<>(); // для обращения к другим потокам

    public static void main(String[] args) {
        Resource r = new Resource();
        b = new CyclicBarrier(1);
        T t = new T("A");
        t.start();
        t.setRes(r);
    }
}

class T extends Thread {
    private volatile Resource resource;

    public T(String name) {
        super(name);
    }

    @Override
    public void run() {
        try {
            if (getName().equals("K")) { // завершающие действия при достижении последнего потока
                System.out.println("Поток К начался.");
                System.out.println("The End");
                System.exit(0);
            }

            System.out.println("Поток '" + getName() + "' начался и ожидает семафор.");
            while (resource == null) {
                Thread.onSpinWait();
            } // поток ждёт ресурс (семафор)
            System.out.print("");
            Semaphore.b.await(); // ждём пока все потоки получат ресурс (с семафором)

            System.out.println("Поток '" + getName() + "' захватывает семафор.");
            Semaphore.b.await(); // ждём пока все потоки приготовятся захватить семафор

            int n = resource.takeAndRelease(getName()); // поток захватывает семафор и выходит из него
            System.out.println("Переменная семафора равна: " + n + ". Поток '" + getName() + "' выходит из семафора.");
            Semaphore.b.await(); // ждём пока все потоки выйдут из семафора (достигнут следующей точки)

            if (n == 0) { // если это первый поток достигший семафора он должен породить потоки
                System.out.println();
                if (Semaphore.stage == 0) {
                    addNewThreads("C", "B", "I", "J");
                    setResourceToThreads(resource, "C", "B");
                    Semaphore.b = new CyclicBarrier(2);
                } else if (Semaphore.stage == 1) {
                    addNewThreads("D", "E", "F");
                    setResourceToThreads(resource, "D", "E", "F", "J");
                    Semaphore.b = new CyclicBarrier(4);
                } else if (Semaphore.stage == 2) {
                    addNewThreads("G", "H");
                    setResourceToThreads(resource, "G", "H", "I");
                    Semaphore.b = new CyclicBarrier(3);
                } else if (Semaphore.stage == 3) {
                    addNewThreads("K");
                }
                resource.reset(); // переустанавливаем переменную семафора
                Semaphore.stage++; // помечаем что началась следующая стадия
            }
        } catch (Exception e) {
            System.out.println("Broken");
        }
    }

    public void setRes(Resource resource) {
        this.resource = resource;
    }

    private void setResourceToThreads(Resource resource, String... names) {
        for(String name : names) {
            Semaphore.threads.get(name).setRes(resource);
        }
    }

    private void addNewThreads(String... names) {
        for(String name : names) {
            T t = new T(name);
            t.start();
            Semaphore.threads.put(name, t);
        }
    }

}

// класс в который оборачивается семафор, чтобы хранить переменную семафора
class Resource {
    private final java.util.concurrent.Semaphore semaphore = new java.util.concurrent.Semaphore(1, true);
    private int x = 0;

    public int takeAndRelease(String name) throws InterruptedException {
        semaphore.acquire(); // захват семафора при вызове из потока
        System.out.println("Поток '" + name + "' в семафоре.");
        semaphore.release();
        return x++;
    }

    public void reset() { x = 0; }
}