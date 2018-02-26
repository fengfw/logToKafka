package com.fengfw.log2kafka;

import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import java.io.*;
import java.util.Arrays;
import java.util.List;



//@RunWith(Parameterized.class)
public class SendProcessTest {
//    private static SendProcess sendProcess;
    private static String relativePath;

    @BeforeClass
    public static void initial(){
        relativePath=System.getProperty("user.dir");
//        Configuration configuration=GetConfiguration.getConfiguration();
//        configuration.setTopic("");
//        sendProcess=new SendProcess(configuration);
    }

//    @Parameterized.Parameters
//    public static List<String> getParams(){
//        return Arrays.asList("hello");
//    }

//    @Rule
//    public ExpectedException thrown= ExpectedException.none();

    @Test
    public void sendTest() throws Exception {
        //模拟则报空指针，若用已有配置，则测试会向kafka发送东西  有问题，还需修改！！！
        Configuration configuration=Mockito.mock(Configuration.class);
        
        ProducerConfig producerConfig=Mockito.mock(ProducerConfig.class);
        Producer producer=Mockito.mock(Producer.class);
        SendProcess sendProcess=Mockito.mock(SendProcess.class);
        PowerMockito.whenNew(Producer.class).withArguments(producerConfig).thenReturn(producer);
        PowerMockito.whenNew(SendProcess.class).withArguments(configuration).thenReturn(sendProcess);

        BufferedReader bufferedReader=Mockito.mock(BufferedReader.class);
        FileInputStream fileInputStream=Mockito.mock(FileInputStream.class);
        File file1=new File("201712111042.000.bid-deal.log");
        PowerMockito.whenNew(FileInputStream.class).withAnyArguments().thenReturn(fileInputStream);
        PowerMockito.whenNew(BufferedReader.class).withArguments(new InputStreamReader(fileInputStream))
                .thenReturn(bufferedReader);
        PowerMockito.doThrow(new FileNotFoundException()).when(fileInputStream).read();
        PowerMockito.doThrow(new IOException()).when(bufferedReader).readLine();
//        sendProcess.send(file1,0);

        File file2=new File(relativePath+"/src/test/resources/test_bid_deal/20171211/201712111042.000.bid-deal.log");
        (new SendProcess(configuration)).send(file2,0);
//        bufferedReader.close();

    }

//    @Test
//    public void sendTestExpection1() throws IOException {
//        File file=new File(monitorPath+"/20171211/201712111043.000.bid-deal.l");
//        thrown.expect(java.io.FileNotFoundException.class);
//        kafkaProducer.send(file,0);
//    }

}
