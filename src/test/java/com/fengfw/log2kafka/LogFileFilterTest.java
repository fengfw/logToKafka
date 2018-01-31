package com.fengfw.log2kafka;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LogFileFilterTest {

    @Test
    public void acceptTest(){
        LogFileFilter logFileFilter = Mockito.mock(LogFileFilter.class);
        File file1=new File("test_commit_file.txt");
        File file2=new File("listen_log.log");
        Mockito.when(logFileFilter.accept(file1)).thenReturn(false);
        Mockito.when(logFileFilter.accept(file2)).thenReturn(true);
        assertEquals(false, logFileFilter.accept(file1));
        assertEquals(true, logFileFilter.accept(file2));
    }


}
