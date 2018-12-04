package site.amcu.filesserver.entity;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description:    文件类
 *                      (文件服务器存放的文件对象)
 * @Author: Ben-Zheng
 * @Date: 2018/12/03 16:26
 */
@Document
public class File implements Serializable {

    /** 文件资源的主键 */
    @Id
    private String id;

    /** 文件名称 */
    private String name;

    /** 文件类型 */
    private String contentType;

    /** 文件大小 */
    private Long size;

    /** 文件的上传时间 */
    private Date uploadDate;

    /** 文件的md5值 */
    private String md5;

    /** 文件的二进制内容 */
    private Binary content;

    /** 文件的路径 */
    private String path;

    protected File() {

    }

    public File(String name, String contentType, Long size, Binary content) {
        this.name = name;
        this.contentType = contentType;
        this.size = size;
        this.uploadDate = new Date();
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Binary getContent() {
        return content;
    }

    public void setContent(Binary content) {
        this.content = content;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }

        if(null == obj || this.getClass() != obj.getClass()) {
            return false;
        }

        File fileInfo = (File) obj;

        return java.util.Objects.equals(this.size, fileInfo.getSize()) &&
                java.util.Objects.equals(this.name, fileInfo.getName()) &&
                java.util.Objects.equals(this.contentType, fileInfo.getContentType()) &&
                java.util.Objects.equals(this.uploadDate, fileInfo.getUploadDate()) &&
                java.util.Objects.equals(this.md5, fileInfo.getMd5()) &&
                java.util.Objects.equals(this.id, fileInfo.getId());
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(
                this.name,
                this.contentType,
                this.size,
                this.uploadDate,
                this.md5,
                this.id);
    }

    @Override
    public String toString() {
        return "File{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", contentType='" + contentType + '\'' +
                ", size=" + size +
                ", uploadDate=" + uploadDate +
                ", md5='" + md5 + '\'' +
                ", content=" + content +
                ", path='" + path + '\'' +
                '}';
    }
}
