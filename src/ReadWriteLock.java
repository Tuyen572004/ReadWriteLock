import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class ReadWriteLock {
    final private Semaphore mutex; // for mutual exclusion of readCount
    final private Semaphore db; // for mutex of database access by writer and reader
    private int readCount; // number of readers accessing the database
    final private List<Object> database; // the database

    public ReadWriteLock() {
        mutex = new Semaphore(1);
        db = new Semaphore(1);
        readCount = 0;
        database = new ArrayList<>();
    }

    public void Writer(Object data) {
        try{
            db.acquire();
            System.out.println("Start writing thread with id: " + Thread.currentThread().getId());
            database.add(data);
        }
        catch(InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        finally{
            System.out.println("End writing thread with id: " + Thread.currentThread().getId());
            db.release();
        }
    }

    public void Reader(){
        try{
            mutex.acquire();
            if(readCount==0){
                db.acquire();
            }
            readCount++;
            mutex.release();

            System.out.println("Start reading thread with id: " + Thread.currentThread().getId());

            System.out.println("Reading data of thread with id: " + Thread.currentThread().getId() + " - data: " + database);

            System.out.println("End reading thread with id: " + Thread.currentThread().getId());

            mutex.acquire();
            readCount--;
            if(readCount==0){
                System.out.println("No more readers, releasing database access");
                db.release();
            }
            mutex.release();

        }
        catch(InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
