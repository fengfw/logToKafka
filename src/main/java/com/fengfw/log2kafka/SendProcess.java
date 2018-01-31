package com.fengfw.log2kafka;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class SendProcess {
    private static Logger systemLogger= LoggerFactory.getLogger("systemLog");
    private static Logger commitLogger= LoggerFactory.getLogger("commitFile");

    private Configuration configuration;

    private final String topic;
    private final Producer<String, String> producer;
    private int failOpenResendTimes=0;
    private int failReadResendTimes=0;

    public SendProcess(Configuration configuration) {
        this.configuration=configuration;
        topic=configuration.getTopic();

        Properties kafkaBrokerProperties = new Properties();
        kafkaBrokerProperties.put(configuration.getKafkaBroker(),configuration.getKafkaBrokerList());
        //"1"即为producer在leader已成功收到的数据并得到确认后发送下一条message
        kafkaBrokerProperties.put(configuration.getKafkaAcks(),configuration.getKafkaAcksValue());
        ////配置value的序列化类
        kafkaBrokerProperties.put(configuration.getKafkaSerializer(),configuration.getKafkaSerializerValue());
        ProducerConfig config = new ProducerConfig(kafkaBrokerProperties);
        this.producer = new Producer<>(config);
    }

    public void send(File file,long startLine) {
        long count=0;
        List<KeyedMessage<String, String>> list=new ArrayList<>();
        try {
            FileInputStream fis=new FileInputStream(file);
            BufferedReader br=new BufferedReader(new InputStreamReader(fis));
            String line;
            int readRows=0;
            while ((line = br.readLine()) != null) {
                count++;
                if(count>=startLine){
                    list.add(new KeyedMessage<>(topic, line));
                    readRows++;
                    if(readRows==configuration.getBatch()){
//                        producer.send(list);
                        systemLogger.info("CumulativeNumber "+file.getAbsolutePath()+" count:"+count);
                        readRows=0;
                        list.clear();
                    }
                }
            }
            if(list.size()!=0){
//                producer.send(list);
                systemLogger.info("CumulativeNumber "+file.getAbsolutePath()+" count:"+count);
                systemLogger.info("CommitToKafka " +file.getAbsolutePath());
                commitLogger.info("CommitToKafka " +file.getAbsolutePath());
            }else{
                systemLogger.info("CommitToKafka " +file.getAbsolutePath());
                commitLogger.info("CommitToKafka " +file.getAbsolutePath());
            }
            br.close();
            count=0;
        } catch (FileNotFoundException e) {
            //FileInputStream异常
            failOpenResendTimes++;
            try {
                TimeUnit.SECONDS.sleep(6);
            } catch (InterruptedException e1) {
                systemLogger.info("Exception ", e1);
            }
            if(failOpenResendTimes<=configuration.getResendFrequency()){
                send(file,startLine);
            }else{
                systemLogger.error("FailToOpenFile "+file.getAbsolutePath());
            }
            systemLogger.info("Exception ", e);
//            throw e;
        } catch (IOException e) {
            //readLine异常
            long restartLine=count-count%configuration.getBatch()+1;
            failReadResendTimes++;
            try {
                TimeUnit.SECONDS.sleep(6);
            } catch (InterruptedException e1) {
                systemLogger.info("Exception ", e1);
            }
            if(failReadResendTimes<=configuration.getResendFrequency()){
                send(file,restartLine);
            }else{
                systemLogger.error("FailToReadFile "+file.getAbsolutePath());
            }
            systemLogger.info("Exception ", e);
//            throw e;
        }
    }

}
