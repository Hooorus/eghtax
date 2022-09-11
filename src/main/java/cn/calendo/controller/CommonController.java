package cn.calendo.controller;

import cn.calendo.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * general file's download & upload
 */

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${img.path}")
    private String imgpath;

    /**
     * 上传文件,其中参数必须和前端设置的协调好，必须一样，否则接收不到
     *
     * @param addFile
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(@RequestPart("file") MultipartFile addFile) {
        //file是个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        log.info(addFile.toString());
        //获得原始文件名
        String originalFilename = addFile.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用uuid重新生成文件名防止文件名称重复
        String filename = UUID.randomUUID().toString() + substring;
        try {
            addFile.transferTo(new File(imgpath + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(filename);
    }

    /**
     * 文件回显，不需要返回值因为是io流
     *
     * @param response
     * @param name
     */
    @GetMapping("/download")
    public void download(HttpServletResponse response, String name) {
        try {
            //输入流，通过输入流读取文件内容
            FileInputStream inputStream = new FileInputStream(new File(imgpath + name));
            //输出流，通过输出流将文件写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            //关闭
            outputStream.close();
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
