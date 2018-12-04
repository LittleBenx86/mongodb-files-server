package site.amcu.filesserver.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import site.amcu.filesserver.entity.File;

/**
 * @Description:    File存储仓库
 * @Author: Ben-Zheng
 * @Date: 2018/12/03 16:43
 */
public interface FileRepository extends MongoRepository<File, String> {

}
