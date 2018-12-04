package site.amcu.filesserver.controller;

import org.apache.commons.lang.StringUtils;
import org.bson.types.Binary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import site.amcu.filesserver.entity.File;
import site.amcu.filesserver.service.FileService;
import site.amcu.filesserver.utils.MD5Util;
import site.amcu.filesserver.vo.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @Description:    文件服务控制层
 * @Author: Ben-Zheng
 * @Date: 2018/12/03 18:22
 */
/** 允许所有域名访问 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class FileController {

    @Autowired
    private FileService fileService;

    @Value("${server.address}")
    private String serverAddress;

    @Value("${server.port}")
    private String serverPort;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/")
    public  String index(Model model) {
        model.addAttribute("files", fileService.listFilesByPage(0, 20));
        return "index";
    }

    /**
     * 分页查询文件
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/files/{pageIndex}/{pageSize}")
    @ResponseBody
    public List<File> listFileByPage(@PathVariable int pageIndex, @PathVariable int pageSize) {
        return fileService.listFilesByPage(pageIndex, pageSize);
    }

    /**
     * 获取文件信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Object> searchFile(@PathVariable String id) {

        File file = fileService.getFileById(id);

        if(file != null) {
            try {
                return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=" + new String(file.getName().getBytes("utf-8"), "ISO-8859-1"))
                            .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                            .header(HttpHeaders.CONTENT_LENGTH, file.getSize() + "")
                            .header("Connection", "close")
                            .body(file.getContent().getData());
            } catch (UnsupportedEncodingException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File decode error!");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File was not fount");
        }
    }

    /**
     * 在线查看图片
     * @param id
     * @return
     */
    @GetMapping("/view/{id}")
    @ResponseBody
    public ResponseEntity<Object> serveFileOnline(@PathVariable String id) {
        File file = fileService.getFileById(id);

        if(file != null) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "fileName=\"" + file.getName() + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, file.getContentType())
                    .header(HttpHeaders.CONTENT_LENGTH, file.getSize() + "")
                    .header("Connection", "close")
                    .body(file.getContent().getData());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File was not fount");
        }
    }

    /**
     * 本地上传接口
     * @param file
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

        try {
            File f = new File(file.getOriginalFilename(), file.getContentType(),
                    file.getSize(), new Binary(file.getBytes()));
            f.setMd5(MD5Util.getMD5(file.getInputStream()));
            fileService.saveFile(f);
        } catch (IOException | NoSuchAlgorithmException ex) {
            logger.error(ex.getMessage());
            redirectAttributes.addFlashAttribute("message", "Your " + file.getOriginalFilename() + " is wrong!");
            return "redirect:/";
        }

        redirectAttributes.addFlashAttribute("message", "You successfully upload " + file.getOriginalFilename() + "!");
        return "redirect:/";
    }

    /**
     * 跨域上传接口
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<Object> handleFileUpload(@RequestParam("file") MultipartFile file) {
        File returnFile = null;
        try {
            String fileName = file.getOriginalFilename().equals("blob") ?
                    System.currentTimeMillis() + "." + StringUtils.split(file.getContentType(),"/")[1] :
                    file.getOriginalFilename();
            File f = new File(fileName, file.getContentType(),
                    file.getSize(), new Binary(file.getBytes()));
            f.setMd5(MD5Util.getMD5(file.getInputStream()));
            returnFile = fileService.saveFile(f);
            String path = "//" + serverAddress + ":" + serverPort + "/view/" + returnFile.getId();
            return ResponseEntity.ok().body(new Response(true, "", path));
        } catch (IOException | NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    /**
     * 删除接口
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Object> deleteFile(@PathVariable String id) {
        try {
            fileService.removeFile(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("DELETE Success!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

}
