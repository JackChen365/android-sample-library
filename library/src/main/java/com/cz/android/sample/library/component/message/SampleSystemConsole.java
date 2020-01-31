package com.cz.android.sample.library.component.message;

import android.util.Log;

import com.cz.android.sample.library.thread.WorkThread;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 *
 */
public class SampleSystemConsole{
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);
    private ReadStreamRunnable readStreamRunnable;
    private ReadStreamRunnable errorReadStreamRunnable;

    public SampleSystemConsole(WorkThread<String> workThread) {
        PipedOutputStream pipedOutputStream=null;
        PipedOutputStream pipedErrorOutputStream=null;
        PipedInputStream pipedInputStream = new PipedInputStream();
        PipedInputStream pipedErrorInputStream = new PipedInputStream();
        try {
            pipedOutputStream = new PipedOutputStream(pipedInputStream);
            pipedErrorOutputStream = new PipedOutputStream(pipedErrorInputStream);
        } catch (IOException e) {
        }
        // set up System.out
        System.setOut(new PrintStream(pipedOutputStream, true));
        // set up System.err
        System.setErr(new PrintStream(pipedErrorOutputStream, true));
        readStreamRunnable=new ReadStreamRunnable(workThread,pipedInputStream);
        errorReadStreamRunnable=new ReadStreamRunnable(workThread,pipedErrorInputStream);
        executorService.execute(readStreamRunnable);
        executorService.execute(errorReadStreamRunnable);
    }

    public void stop(){
        if(null!=readStreamRunnable){
            readStreamRunnable.stop();
        }
        if(null!=errorReadStreamRunnable){
            errorReadStreamRunnable.stop();
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
            System.out.println('\n');
            System.err.println('\n');
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String TAG="test";
        @Override
        public void run() {
            final byte[] buf = new byte[1024];
            while (isRunning) {
                try {
                    int len = inputStream.read(buf);
                    if (len == -1 || 1==len) {
                        continue;
                    }
                    String text = new String(buf, 0, len);
                    workThread.post(text.trim()+"\n");
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
