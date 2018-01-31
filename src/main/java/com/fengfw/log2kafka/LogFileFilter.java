package com.fengfw.log2kafka;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileFilter;

public class LogFileFilter implements FileFilter {

    @Override
    public boolean accept(File pathname) {
        //对感兴趣的文件监听,要返回true
        String extension= FilenameUtils.getExtension(pathname.getAbsolutePath());
        return extension.equals("log");
    }
}
