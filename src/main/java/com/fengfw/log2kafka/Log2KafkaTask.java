package com.fengfw.log2kafka;

import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Log2KafkaTask {
    private static Logger systemLogger= LoggerFactory.getLogger("systemLog");

    public static void main(String args[]){
        //先提取配置文件
        Configuration configuration=GetConfiguration.getConfiguration();
        //开启监控
        if(configuration.getMonitorPath()!=null){
            startMonitorAndSend(configuration);
        }
    }

    public static void startMonitorAndSend(Configuration configuration){
        String monitorPath=configuration.getMonitorPath();
        long interval=configuration.getMonitorInteval();
        FileListener fileListener;
        FileAlterationMonitor monitor;
        FileAlterationObserver observer;
        try {
            observer = new FileAlterationObserver(monitorPath);
            //添加监听器
            fileListener=new FileListener(configuration);
            observer.addListener(fileListener);
            monitor=new FileAlterationMonitor(interval, observer);
            //监听开始
            monitor.start();
            SendProcess sendProcess=new SendProcess(configuration);
            Runnable sendThreadImpl=new SendThread(fileListener,sendProcess);
            Thread[] sendThread=new Thread[configuration.getThreadNumsToSend()];
            for(int i=0;i<sendThread.length;i++){
                sendThread[i]=new Thread(sendThreadImpl);
                sendThread[i].start();
            }
//            Thread handleErrorLogThread=new HandleErrorLogThread();
//            handleErrorLogThread.start();
        } catch (Exception e) {
            systemLogger.info("Exception ", e);
        }
    }

}

