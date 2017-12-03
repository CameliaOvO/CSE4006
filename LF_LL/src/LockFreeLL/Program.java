package LockFreeLL;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Program {
    static int INPUT_SIZE = 100000;
    static int NUM_OF_THREAD = 1;

    public static void main(String[] args) {

        LF_LL<Integer> list = new LF_LL<>();
        Random random = new Random();
        ArrayList<Integer> a = new ArrayList<>();
        for (int i = 0; i < INPUT_SIZE*2; i++) {
            a.add(random.nextInt(INPUT_SIZE*3));
        }
        Collections.shuffle(a);

        final long startTime = System.currentTimeMillis();

        ExecutorService executorService = Executors.newFixedThreadPool(NUM_OF_THREAD);
        System.out.println("Number of Thread : " + NUM_OF_THREAD);

        for (int i = 0; i < INPUT_SIZE*2; i++) {
            int finalI = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (finalI < INPUT_SIZE){
                    list.insert(a.get(finalI));}
                    else{
                        if(finalI % 10 < 0){
                            list.insert(a.get(finalI));
                        }else{
                            list.search(a.get(finalI));
                        }
                    }

                }
            };
            executorService.execute(runnable);
        }

        executorService.shutdown();
        while (!executorService.isTerminated()) {
        }
        final long endTime = System.currentTimeMillis();
        System.out.println("Execution Time : "+ (endTime - startTime) + "ms");
    }


}
