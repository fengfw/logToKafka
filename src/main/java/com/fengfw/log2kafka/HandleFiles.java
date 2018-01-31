package com.fengfw.log2kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class HandleFiles {
    private static Logger systemLogger= LoggerFactory.getLogger("systemLog");
    private static ArrayList<File> arrayList=new ArrayList<>();

    //1 获取近几天生成的路径
    public File[] getDirectoryInSomeDays(File file,int recoveryDays){
        File[] files=new File[recoveryDays+1];
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMdd");
        long intervalDay=24*60*60;
        files[0]=file;
        String dayBeforeTime;
        try {
            for(int i=1;i<=recoveryDays;i++){
                Date date=dateFormat.parse(files[i-1].getName());
                long dayBefore=date.getTime()-intervalDay;
                dayBeforeTime=dateFormat.format(dayBefore);
                files[i]=new File(files[i-1].getParent()+File.separator+dayBeforeTime);
            }
        } catch (ParseException e) {
            systemLogger.info("Exception ", e);
        }
        return files;
    }

    //2 获取近几天已经提交的日志文件的集合
    public Set<File> getCommitSetInSomeDays(File file,int recoveryDays){
        Set<File> set=new HashSet<>();
        if(!file.exists()){
            return set;
        }
        ArrayList<File> list=getFileInLog(file);
        set.addAll(list);
        File dayBefore=file;
        for(int i=1;i<=recoveryDays;i++){
            File tmpFile=getDayBeforeFile(dayBefore);
            if(tmpFile!=null){
                dayBefore=tmpFile;
                list=getFileInLog(dayBefore);
                set.addAll(list);
            }
        }
        return set;
    }

    //3 在commitFile日志文件中将文件路径提取出来
    public ArrayList<File> getFileInLog(File file){
        ArrayList<File> arrayList=new ArrayList<>();
        if(!file.exists()){
            return arrayList;
        }
        try {
            RandomAccessFile raf=new RandomAccessFile(file.getAbsolutePath(),"r");
            String line;
            while((line=raf.readLine())!= null) {
                String path=line.split("\\s+")[6];
                arrayList.add(new File(path));
            }
            raf.close();
        } catch (IOException e) {
            systemLogger.info("Exception ", e);
        }
        return arrayList;
    }

    //4 递归遍历获取日志文件列表
    public ArrayList<File> getCatalogFileList(File file){
        if(!file.exists()){
            return arrayList;
        }
        File[] files=file.listFiles();
        if(files!=null){
            for (File f : files) {
                if (f.isDirectory()) {
                    getCatalogFileList(f);
                } else {
                    arrayList.add(f);
                }
            }
        }
        return arrayList;
    }

    //5 获取前一天的日志文件
    public File getDayBeforeFile(File file){
        if(!file.exists()){
            return null;
        }
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMdd");
        String[] strings=file.getName().split("\\.");
        String dayBefore;
        long intervalDay=24*60*60;
        Date date=new Date();
        if(strings.length==3){
            try {
                date=dateFormat.parse(strings[0]);
            } catch (ParseException e) {
                systemLogger.info("Exception ", e);
            }
        }
        long dayBeforeTime=date.getTime()-intervalDay;
        dayBefore=dateFormat.format(dayBeforeTime);
        String parentPath=file.getParent();
        String filePath;
        if(strings.length==2){
            filePath=parentPath+File.separator+dayBefore+"."+file.getName();
        }else{
            filePath=parentPath+File.separator+dayBefore+"."+strings[1]+"."+strings[2];
        }
        File dayBeforeFile=new File(filePath);
        if(dayBeforeFile.exists()){
            return dayBeforeFile;
        }else {
            return null;
        }
    }

}
