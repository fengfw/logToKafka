package com.fengfw.log2kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

class SendThread implements Runnable{
    private static Logger systemLogger= LoggerFactory.getLogger("systemLog");
    private FileListener fileListener;
    private SendProcess sendProcess;
    private volatile boolean flag;

    public SendThread(FileListener fileListener,SendProcess sendProcess){
        this.fileListener=fileListener;
        this.sendProcess=sendProcess;
    }
    @Override
    public void run() {
        try {
            flag=true;
            while (flag){
                File file=fileListener.getQueueHeadFile();
                if(file!=null){
                    sendProcess.send(file,0);
                    TimeUnit.SECONDS.sleep(3);
                }else {
                    TimeUnit.SECONDS.sleep(10);
                }
            }
        } catch (InterruptedException e) {
            systemLogger.info("Exception ", e);
        }
    }

    public void tostop(){
        flag=false;
    }

}
