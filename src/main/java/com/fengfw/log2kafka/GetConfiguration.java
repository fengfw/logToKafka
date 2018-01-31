package com.fengfw.log2kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class GetConfiguration {

    private static Logger systemLogger= LoggerFactory.getLogger("systemLog");

    public static Configuration getConfiguration(){
        Configuration configuration=new Configuration();
        Properties properties=new Properties();
        try {
            FileInputStream fileInputStream=new FileInputStream(System.getProperty("user.dir")
                    +"/src/main/resources/conf.properties");
            properties.load(fileInputStream);
            fileInputStream.close();
        } catch (IOException e) {
            systemLogger.info("ConfigurationException ",e);
        }
        configuration.setMonitorPath(properties.getProperty("monitorPath"));
        configuration.setTopic(properties.getProperty("topic"));
        configuration.setMonitorInteval(Long.parseLong(properties.getProperty("monitorInteval")));
        configuration.setThreadNumsToSend(Integer.valueOf(properties.getProperty("threadNumsToSend")));
        configuration.setKafkaBroker(properties.getProperty("kafkaBroker"));
        configuration.setKafkaBrokerList(properties.getProperty("kafkaBrokerList"));
        configuration.setKafkaAcks(properties.getProperty("kafkaAcks"));
        configuration.setKafkaAcksValue(properties.getProperty("kafkaAcksValue"));
        configuration.setKafkaSerializer(properties.getProperty("kafkaSerializer"));
        configuration.setKafkaSerializerValue(properties.getProperty("kafkaSerializerValue"));
        configuration.setBatch(Integer.valueOf(properties.getProperty("batch")));
        configuration.setResendFrequency(Integer.valueOf(properties.getProperty("resendFrequency")));
        configuration.setRecoveryDays(Integer.valueOf(properties.getProperty("recoveryDays")));
        String relativePath=System.getProperty("user.dir");
        configuration.setCommitFilePath(relativePath+properties.getProperty("commitFilePath"));
        return configuration;
    }

}
