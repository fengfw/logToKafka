package com.fengfw.log2kafka;

import net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner;
import net.sourceforge.groboutils.junit.v1.TestRunnable;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class SendThreadTest{

    @Test
    public void sendTreadTest() throws Throwable {
        FileListener fileListener= Mockito.mock(FileListener.class);
        SendProcess sendProcess=Mockito.mock(SendProcess.class);
        SendThread sendThread=new SendThread(fileListener,sendProcess);
        RunThread runThread=new RunThread(sendThread);
        StopThread stopThread=new StopThread(sendThread);
        TestRunnable[] trs={runThread,stopThread};
        MultiThreadedTestRunner mttr=new MultiThreadedTestRunner(trs);
        mttr.runTestRunnables();
    }

    private static class RunThread extends TestRunnable {

        SendThread sendThread;

        private RunThread(SendThread sendThread) throws IOException {
            super();
            this.sendThread=sendThread;
        }

        @Override
        public void runTest() throws Throwable {
            sendThread.run();
        }
    }

    private static class StopThread extends TestRunnable {

        SendThread sendThread;

        private StopThread(SendThread sendThread) throws IOException {
            super();
            this.sendThread=sendThread;
        }

        @Override
        public void runTest() throws Throwable {
            Thread.sleep(100);
            sendThread.tostop();
        }
    }
}
