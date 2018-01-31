package com.fengfw.log2kafka;


public class Configuration {

    private String monitorPath;
    String getMonitorPath(){ return monitorPath; }
    void setMonitorPath(String monitorPath){ this.monitorPath=monitorPath; }

    private String topic;
    String getTopic(){ return topic; }
    void setTopic(String topic){ this.topic=topic; }

    private long monitorInteval;
    long getMonitorInteval(){ return monitorInteval;}
    void setMonitorInteval(long monitorInteval){ this.monitorInteval=monitorInteval;}

    private int threadNumsToSend;
    int getThreadNumsToSend(){ return threadNumsToSend;}
    void setThreadNumsToSend(int threadNumsToSend){ this.threadNumsToSend=threadNumsToSend;}

    private String kafkaBroker;
    String getKafkaBroker(){ return kafkaBroker;}
    void setKafkaBroker(String kafkaBroker){ this.kafkaBroker=kafkaBroker;}

    private String kafkaBrokerList;
    String getKafkaBrokerList(){ return kafkaBrokerList;}
    void setKafkaBrokerList(String kafkaBrokerList){ this.kafkaBrokerList=kafkaBrokerList;}

    private String kafkaAcks;
    String getKafkaAcks(){ return kafkaAcks;}
    void setKafkaAcks(String kafkaAcks){ this.kafkaAcks=kafkaAcks;}

    private String kafkaAcksValue;
    String getKafkaAcksValue(){ return kafkaAcksValue;}
    void setKafkaAcksValue(String kafkaAcksValue){ this.kafkaAcksValue=kafkaAcksValue;}

    private String kafkaSerializer;
    String getKafkaSerializer(){return kafkaSerializer;}
    void setKafkaSerializer(String kafkaSerializer){ this.kafkaSerializer=kafkaSerializer;}

    private String kafkaSerializerValue;
    String getKafkaSerializerValue(){ return kafkaSerializerValue;}
    void setKafkaSerializerValue(String kafkaSerializerValue){ this.kafkaSerializerValue=kafkaSerializerValue;}

    private int batch;
    int getBatch(){ return batch;}
    void setBatch(int batch){ this.batch=batch;}

    private int resendFrequency;
    int getResendFrequency(){ return resendFrequency;}
    void setResendFrequency(int resendFrequency){ this.resendFrequency=resendFrequency;}

    private int recoveryDays;
    int getRecoveryDays(){ return recoveryDays;}
    void setRecoveryDays(int recoveryDays){ this.recoveryDays=recoveryDays;}

    public String commitFilePath;
    String getCommitFilePath(){ return commitFilePath;}
    void setCommitFilePath(String commitFilePath){ this.commitFilePath=commitFilePath;}

}
