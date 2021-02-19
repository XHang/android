package com.example.service;

import android.os.Environment;

import com.example.util.DateUtil;
import com.music.exception.LogException;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class logService {

    public static String LogFolder = "log";

    public static String AppFolder = "myMusic";

    public static String InfoFile = "info";

    //映射到内存的日志字节，5mb大小
    private static int mappingMember = 200;

    private MappedByteBuffer buffer;
    public static logService Log = new logService();

    public logService() {

        resetBuffer();

    }

    private void resetBuffer() {
        File file = getFile("");
        try {
            //初始构建5mb内存的日志文件
            if (this.buffer != null) {
                this.buffer.force();
            }
            this.buffer = new RandomAccessFile(file, "rw").getChannel().map(FileChannel.MapMode.READ_WRITE, 0, mappingMember);
        } catch (Exception e) {
            throw new LogException(e, "初始化日志文件失败，原因");
        }
    }


    private File getFile(String suffix) {
        String fileName = InfoFile;
        if (suffix != "") {
            fileName = fileName + "-" + suffix;
        }
        fileName = fileName + ".log";
        File file = new File(String.format("%s/%s/%s/%s", Environment.getExternalStorageDirectory(), LogFolder, AppFolder, fileName));
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new LogException(e, "创建日志文件失败");
            }
            return file;
        }
        return getFile(DateUtil.format(new Date(), "yyyyMMddHHmmss"));
    }


    public void Info(String msg, Object... args) {
        msg = String.format("info:"+msg, args) + "\r\n";
        log(msg);

    }

    private void log(String msg) {
        System.out.println(msg);
        byte[] bytes = new byte[0];
        bytes = msg.getBytes(StandardCharsets.UTF_8);
        if (this.buffer.position() + bytes.length >= this.buffer.limit()) {
            this.resetBuffer();
        }
        this.buffer.put(bytes);
    }

    public void Error(Throwable e,String msg, Object... args) {
        msg = String.format("error:"+msg+",cause:"+e !=null?e.getMessage():"", args) + "\r\n";
        System.out.println(msg);
        log(msg);
    }


}
