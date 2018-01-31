package com.fengfw.log2kafka;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class HandleFilesTest {

    private static String relativePath;
    @BeforeClass
    public static void initial(){
        relativePath=System.getProperty("user.dir");
    }

    @Test
    public void getDirectoryInSomeDaysTest(){
        HandleFiles handleFiles=new HandleFiles();
        String today=relativePath+"/src/test/resources/test_bid_deal"+File.separator+"20180112";
        File[] compareFiles=new File[]{new File(relativePath+"/src/test/resources/test_bid_deal"+File.separator+"20180112"),
                new File(relativePath+"/src/test/resources/test_bid_deal"+File.separator+"20180111"),
                new File(relativePath+"/src/test/resources/test_bid_deal"+File.separator+"20180110"),
                new File(relativePath+"/src/test/resources/test_bid_deal"+File.separator+"20180109")};
        assertArrayEquals(compareFiles,handleFiles.getDirectoryInSomeDays(new File(today),3));
    }

    @Test
    public void getCommitSetInSomeDaysTest() throws IOException {
        HandleFiles handleFiles=new HandleFiles();
        File file=new File(relativePath+"/src/test/resources/test_commitFile.txt");
        FileWriter fileWriter= new FileWriter(file);
        fileWriter.write("");
        fileWriter.flush();
        File directory=new File(relativePath+"/src/test/resources/test_bid_deal/20171211");
        File[] files=directory.listFiles();
        if(files!=null){
            for(File file1:files){
                fileWriter.write("2018/01/15 10:37:16.073 INFO  commitFile.send -- CommitToKafka " +file1.getAbsolutePath()+System.lineSeparator());
            }
        }
        fileWriter.close();
        Set<File> compareSet=new HashSet<File>(){{add(new File(relativePath+"/src/test/resources/test_bid_deal/20171211/201712111040.000.bid-deal.log"));
                add(new File(relativePath+"/src/test/resources/test_bid_deal/20171211/201712111041.000.bid-deal.log"));
        add(new File(relativePath+"/src/test/resources/test_bid_deal/20171211/201712111042.000.bid-deal.log"));}};
        assertEquals(compareSet,handleFiles.getCommitSetInSomeDays(new File(relativePath+"/src/test/resources/test_commitFile.txt"),0));
    }

    @Test
    public void getCatalogFileListTest(){
        HandleFiles handleFiles=new HandleFiles();
        File file1=new File(relativePath+"/src/test/resources/null");
        assertEquals(new ArrayList<File>(),handleFiles.getCatalogFileList(file1));

        File file2=new File(relativePath+"/src/test/resources/test_bid_deal/20171211");
        ArrayList<File> list=new ArrayList<File>()
        {{add(new File(relativePath+"/src/test/resources/test_bid_deal/20171211/201712111040.000.bid-deal.log"));
                add(new File(relativePath+"/src/test/resources/test_bid_deal/20171211/201712111041.000.bid-deal.log"));
                add(new File(relativePath+"/src/test/resources/test_bid_deal/20171211/201712111042.000.bid-deal.log"));}};
        assertEquals(list,handleFiles.getCatalogFileList(file2));
    }

    @Test
    public void getDayBeforeFileTest(){
        HandleFiles handleFiles=new HandleFiles();
        File file1=new File(relativePath+"/src/test/resources/test_listen_file.txt");
        assertEquals(null,handleFiles.getDayBeforeFile(file1));

        File file2=new File(relativePath+"/src/test/resources/temp_log/20180105.systemLog.log");
        assertEquals("20180104.systemLog.log",handleFiles.getDayBeforeFile(file2).getName());
    }

}
