package com.zhixun.javacode.module;

public class FileInfo {
    private String fileName;//文件名
    private String fileType;//文件类型
    private String fileSize;//文件大小
    private String fileMD5;//MD5码
    private String fileVersionNO;//文件版本号

    public FileInfo() {
        super();
    }

    public FileInfo(String fileName, String fileType, String fileSize, String fileMD5, String fileVersionNO) {
        super();
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.fileMD5 = fileMD5;
        this.fileVersionNO = fileVersionNO;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileMD5() {
        return fileMD5;
    }

    public void setFileMD5(String fileMD5) {
        this.fileMD5 = fileMD5;
    }

    public String getFileVersionNO() {
        return fileVersionNO;
    }

    public void setFileVersionNO(String fileVersionNO) {
        this.fileVersionNO = fileVersionNO;
    }
}
