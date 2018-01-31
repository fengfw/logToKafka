package com.fengfw.log2kafka;

import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import java.io.File;

public class Log2KafkaTaskTest {

    @Test
    public void startMonitorAndSendTest() throws Exception {
        Configuration configuration= GetConfiguration.getConfiguration();
        FileListener fileListener=Mockito.mock(FileListener.class);
        SendThread sendThread=Mockito.mock(SendThread.class);
        SendProcess sendProcess=Mockito.mock(SendProcess.class);
        PowerMockito.whenNew(FileListener.class).withArguments(configuration).thenReturn(fileListener);
        PowerMockito.whenNew(SendProcess.class).withArguments(configuration).thenReturn(sendProcess);
        PowerMockito.whenNew(SendThread.class).withArguments(fileListener,sendProcess).thenReturn(sendThread);
        String relativePath=System.getProperty("user.dir");
        File monitorPath=new File(relativePath+"/src/test/resources/null");
        configuration.setMonitorPath(monitorPath.getAbsolutePath());
        Log2KafkaTask.startMonitorAndSend(configuration);

    }

}