package com.cz.android.sample.library.component.message;

import com.cz.android.sample.library.thread.WorkThread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SampleSystemConsole{
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private ReadStreamRunnable readStreamRunnable=null;

    public void setup(WorkThread<String> workThread){
        PipedOutputStream pipedOutputStream=null;
        PipedInputStream pipedInputStream = new PipedInputStream();
        try {
            pipedOutputStream = new PipedOutputStream(pipedInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // set up System.out
        System.setOut(new PrintStream(pipedOutputStream, true));
        readStreamRunnable=new ReadStreamRunnable(workThread,pipedInputStream);
        executorService.execute(readStreamRunnable);
    }

    public void stop(){
        if(null!=readStreamRunnable){
            readStreamRunnable.stop();
        }
    }

    private static class ReadStreamRunnable implements Runnable{
        private final PipedInputStream inputStream;
        private final WorkThread<String> workThread;
        private volatile boolean isRunning=true;

        public ReadStreamRunnable(WorkThread<String> workThread,PipedInputStream inputStream) {
            this.workThread=workThread;
            this.inputStream = inputStream;
        }

        public void stop(){
            isRunning=false;
            System.setOut(null);
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            final byte[] buf = new byte[1024];
            while (isRunning) {
                try(ObjectInputStream ois = new ObjectInputStream(inputStream)){
                    int len = ois.read(buf);
                    if (len == -1) {
                        continue;
                    }
                    String text = new String(buf, 0, len);
                    workThread.post(text);
                } catch (IOException e){
                    isRunning=false;
                    e.printStackTrace();
                }
            }
        }
    }

}
