package com.fengfw.log2kafka;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class FileListenerTest {
    public static FileListener fileListener;

    @BeforeClass
    public static void initial() throws ParseException {
        Configuration configuration=GetConfiguration.getConfiguration();
        String relativePath=System.getProperty("user.dir");
        File monitorPath=new File(relativePath+"/src/test/resources/test_bid_deal");
        configuration.setMonitorPath(monitorPath.getAbsolutePath());
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMdd");
        Date date=dateFormat.parse("20171211");
        int interval= (int) (((new Date()).getTime()-date.getTime())/(24*60*60))+1;
        configuration.setRecoveryDays(interval);
        fileListener=new FileListener(configuration);
    }

    @Test
    public void  getQueueHeadFileTest() throws IOException {
        File directory=new File("tmp");
        fileListener.onDirectoryCreate(directory);
        File file1=new File("tmp/1234.log");
        File file2=new File("tmp/345.txt");
        fileListener.onFileCreate(file1);
        fileListener.onFileCreate(file2);
        assertEquals(4,fileListener.waitQueue.size());
        fileListener.getQueueHeadFile();
        assertEquals(3,fileListener.waitQueue.size());
    }

}
