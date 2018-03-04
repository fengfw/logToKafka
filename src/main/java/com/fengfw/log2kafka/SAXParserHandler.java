//package com.ipinyou.log2kafka;
//
//import org.xml.sax.Attributes;
//import org.xml.sax.SAXException;
//import org.xml.sax.helpers.DefaultHandler;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.ArrayList;
//
//public class SAXParserHandler extends DefaultHandler {
//    private static Logger systemLogger= LoggerFactory.getLogger("systemLog");
//
//    private String value;
//    private String targetName;
//    private int flag=0;
//    private Configuration configuration = null;
//    private ArrayList<Configuration> configurationList = new ArrayList<>();
//
//    public ArrayList<Configuration> getConfigurationList() {
//        return configurationList;
//    }
//
//     @Override
//     public void startDocument() {
//         try {
//             super.startDocument();
//         } catch (SAXException e) {
//             systemLogger.info("Exception ", e);
//         }
//     }
//
//     @Override
//     public void endDocument() {
//         try {
//             super.endDocument();
//         } catch (SAXException e) {
//             systemLogger.info("Exception ", e);
//         }
//     }
//
//     @Override
//     public void startElement(String uri, String localName, String qName,Attributes attributes) {
//         //调用DefaultHandler类的startElement方法
//         try {
//             super.startElement(uri, localName, qName, attributes);
//             if (qName.equals("log2kafka")) {
//                 //创建一个configuration对象
//                 configuration=new Configuration();
//                 configuration.setId(attributes.getValue("id"));
//             }
//             if(!qName.equals(targetName)){
//                 targetName=qName;
//                 flag=1;
//             }
//         } catch (SAXException e) {
//             systemLogger.info("Exception ", e);
//         }
//     }
//
//     @Override
//     public void endElement(String uri, String localName, String qName) {
//         //调用DefaultHandler类的endElement方法
//         try {
//             super.endElement(uri, localName, qName);
//         } catch (SAXException e) {
//             systemLogger.info("Exception ", e);
//         }
//         //判断是否针对一个Configuration已经遍历结束
//         if (qName.equals("configuration")) {
//             configurationList.add(configuration);
//             configuration = null;
//         }else if (qName.equals("monitorPath")) {
//             configuration.setMonitorPath(value);
//         }else if (qName.equals("topic")) {
//             configuration.setTopic(value);
//         }else if (qName.equals("monitorInteval")) {
//             configuration.setMonitorInteval(Long.parseLong(value));
//         }else if (qName.equals("kafkaBroker")) {
//             configuration.setKafkaBroker(value);
//         }else if (qName.equals("kafkaBrokerList")) {
//             configuration.setKafkaBrokerList(value);
//         }else if(qName.equals("kafkaAcks")){
//             configuration.setKafkaAcks(value);
//         }else if(qName.equals("kafkaAcksValue")){
//             configuration.setKafkaAcksValue(value);
//         }else if(qName.equals("kafkaSerializer")){
//             configuration.setKafkaSerializer(value);
//         }else if(qName.equals("kafkaSerializerValue")){
//             configuration.setKafkaSerializerValue(value);
//         }else if(qName.equals("batch")){
//             configuration.setBatch(Integer.valueOf(value));
//         }
//     }
//
//     @Override
//     public void characters(char[] ch,int start,int length) {
//         try {
//             super.characters(ch,start,length);
//             //获取括号间的内容
//             if(flag==0){
//                 value=(value+(new String(ch,start,length)).trim()).replaceAll("[\n\\s\r\t]","");
//             }else {
//                 value=(new String(ch,start,length)).trim();
//                 flag=0;
//             }
//         } catch (SAXException e) {
//             systemLogger.info("Exception ", e);
//         }
//     }
//
//}
