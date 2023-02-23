package app.project.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Jimmy
 */
public interface OssService {

    Object getSTS();

    String uploadFile(MultipartFile file);

    Boolean removeOssFile(List<String> objectNames);

}
