package site.amcu.filesserver.service;

import site.amcu.filesserver.entity.File;

import java.util.List;
import java.util.Optional;

/**
 * @Description:    File存储服务接口
 * @Author: Ben-Zheng
 * @Date: 2018/12/03 16:45
 */
public interface FileService {

    /**
     * 保存文件
     * @param file
     * @return
     */
    File saveFile(File file);

    /**
     * 删除文件
     * @param id
     */
    void removeFile(String id);

    /**
     * 根据id获取文件
     * @param id
     * @return
     */
    File getFileById(String id);

    /**
     * 分页查询，按上传时间降序
     * @param pageIndex
     * @param pageSize
     * @return
     */
    List<File> listFilesByPage(int pageIndex, int pageSize);

}
