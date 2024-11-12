public class Main {

    public static void main(String[] args) {
        ReadWriteLock rwl = new ReadWriteLock();

        // write threads
        int numWriters = 5;
        Thread[] writers = new Thread[numWriters];
        for(int i=0;i<numWriters;i++){
            final Long data = (long) i;
            writers[i] = new Thread(()-> rwl.Writer(data));
            writers[i].start();
        }

        // read threads
        int numReaders = 10;
        Thread [] readers = new Thread[numReaders];
        for(int i=0;i<numReaders;i++){
            readers[i] = new Thread(rwl::Reader);
            readers[i].start();
        }

        // more write threads
        Thread[] writers2 = new Thread[numWriters];
        for(int i=0;i<numWriters;i++){
            final Long data = (long) i;
            writers2[i] = new Thread(()-> rwl.Writer(data));
            writers2[i].start();
        }

        // more read threads
        Thread [] readers2 = new Thread[numReaders];
        for(int i=0;i<numReaders;i++){
            readers2[i] = new Thread(rwl::Reader);
            readers2[i].start();
        }

        for(int i=0;i<numReaders;i++){
            try{
                readers[i].join();
                readers2[i].join();
            }
            catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }

        for(int i=0;i<numWriters;i++){
            try{
                writers[i].join();
                writers2[i].join();
            }
            catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }

    }
}
