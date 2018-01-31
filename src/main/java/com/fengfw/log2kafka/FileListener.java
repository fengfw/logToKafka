package com.fengfw.log2kafka;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FileListener extends FileAlterationListenerAdaptor {
    private static Logger systemLogger= LoggerFactory.getLogger("systemLog");

    //    private  Queue<File> waitqueue=new LinkedList();
    BlockingQueue<File> waitQueue = new LinkedBlockingQueue<>(100);

    private static LogFileFilter logFileFilter;

    public FileListener(Configuration configuration){
        logFileFilter=new LogFileFilter();
        HandleFiles handleFiles=new HandleFiles();
        File monitor=new File(configuration.getMonitorPath());
        String commitFilePath=configuration.getCommitFilePath();
        File commitFile=new File(commitFilePath);
        int recoveryDays=configuration.getRecoveryDays();

        if(commitFile.length()==0 && handleFiles.getDayBeforeFile(commitFile)==null){
            ArrayList<File> fileList=handleFiles.getCatalogFileList(new File(configuration.getMonitorPath()));
            if(fileList!=null){
                for (File file:fileList) {
                    if (logFileFilter.accept(file)) {
                        waitQueue.add(file);
                        systemLogger.info("InitialWaitQueue " + file.getAbsolutePath());
                    }
                }
            }
        }else{
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMdd");
            Date date=new Date();
            String today=monitor.getAbsolutePath()+File.separator+dateFormat.format(date);
            File[] files=handleFiles.getDirectoryInSomeDays(new File(today),recoveryDays);
            ArrayList<File> fileList=new ArrayList<>();
            for (File file:files) {
                if (file.exists()) {
                    fileList.addAll(handleFiles.getCatalogFileList(file));
                }
            }
            Set<File> commitSet=handleFiles.getCommitSetInSomeDays(commitFile,recoveryDays);
            for (File file:fileList) {
                if (!commitSet.contains(file) && logFileFilter.accept(file)) {
                    waitQueue.add(file);
                    systemLogger.info("InitialWaitQueue " + file.getAbsolutePath());
                }
            }
        }
    }

    @Override
    public void onDirectoryCreate(File directory) {
        super.onDirectoryCreate(directory);
        systemLogger.info("CreateDirectory " +directory.getAbsolutePath());
    }

    @Override
    public void onFileCreate(File file) {
        super.onFileCreate(file);
        systemLogger.info("CreateFile "+file.getAbsolutePath());
        if(logFileFilter.accept(file)&&file.exists()){
            waitQueue.add(file);
            systemLogger.info("AddWaitQueue "+file.getAbsolutePath());
        }
    }

    synchronized File getQueueHeadFile(){
        File file=null;
        if(!waitQueue.isEmpty()){
            file=waitQueue.poll();
        }
        return file;
    }

}
