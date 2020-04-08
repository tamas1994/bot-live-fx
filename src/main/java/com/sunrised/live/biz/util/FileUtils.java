package com.sunrised.live.biz.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * Created by tamas on 2019/10/15.
 */
@Slf4j
public class FileUtils {

    public static void saveInputStream2File(InputStream inputStream, String targetFilePath) {
        OutputStream output = null;
        try {
            output = new FileOutputStream(new File(targetFilePath));
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buf)) != -1) {
                output.write(buf, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    public static String getPath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    public static String readTextFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return "";
        }
        StringBuilder textBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            while (true) {
                String line = bufferedReader.readLine();
                if (line==null){
                    break;
                }
                textBuilder.append(line);

            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return textBuilder.toString();
    }

    public static void saveBytesToFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && !dir.isDirectory()) {//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static String getFileDirectoryPath(File file) {
        if (!file.exists()) {
            return null;
        }
        String directoryPath = file.getAbsolutePath().split(file.getName())[0];
        return directoryPath;
    }

    public static String getFileNameWithoutSufix(File file) {
        if (!file.exists()) {
            return null;
        }
        String fileName = file.getName();
        log.info("之前文件名：" + fileName);
        String fileNameWithoutSufix = fileName.split("[.]")[0];
        log.info("之后文件名：" + fileNameWithoutSufix);
        return fileNameWithoutSufix;
    }


    public static final boolean copyFile(File in, File out) {
        try {
            FileInputStream fis = new FileInputStream(in);
            FileOutputStream fos = new FileOutputStream(out);
            byte[] buf = new byte[1024];
            int i = 0;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
            fis.close();
            fos.close();
            return true;
        } catch (IOException ie) {
            ie.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static final long getFileSize(String path) {
        File f = new File(path);
        if (f.exists() && f.isFile()) {
            return f.length();
        } else {
            return 0;
        }

    }

}
