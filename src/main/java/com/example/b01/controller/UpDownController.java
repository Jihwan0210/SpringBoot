package com.example.b01.controller;


import com.example.b01.dto.upload.UploadFileDTO;
import com.example.b01.dto.upload.UploadResultDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@Log4j2
public class UpDownController {
    @Value("${file.upload.path}") //import시에 springframework으로 시작하는 value
    private String uploadPath;


    @Operation (description = "POST 방식으로 파일 업로드")
    @PostMapping(value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<UploadResultDTO> upload(UploadFileDTO uploadFileDTO) {
//    public String upload(UploadFileDTO uploadFileDTO) {
        log.info(uploadFileDTO);
        if(uploadFileDTO.getFiles() != null) {
            final List<UploadResultDTO> list = new ArrayList<>();
            uploadFileDTO.getFiles().forEach(multipartFile -> {
                String originalName = multipartFile.getOriginalFilename();
                log.info(originalName);

                String uuid = UUID.randomUUID().toString();
                Path savePath = Paths.get(uploadPath, uuid+"_" + originalName);

                boolean image = false;
                try {
                    multipartFile.transferTo(savePath); //실제 파일 저장
                    //이미지 파일의 종류라면
                    //MIME 타입이 "image/*"(예 : "image/jpg" , "image/jpeg","image/gif")로 시작하는 경우만 실행
                    if(Files.probeContentType(savePath).startsWith("image")) {
                        image = true;
                        File thumbFile = new File(uploadPath, "s_" + uuid + "_" + originalName);
                        Thumbnailator.createThumbnail(savePath.toFile() , thumbFile, 200 , 200);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                list.add(UploadResultDTO.builder()
                                .uuid(uuid)
                                .fileName(originalName)
                                .img(image).build());
//                log.info(multipartFile.getOriginalFilename());
            }); //end each
            return list;
        } //end if
        return null;
    }
    @Operation(description = "GET방식으로 업로드된 파일 조회")
    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName) {
        Resource resource = new FileSystemResource(uploadPath+File.separator+fileName);
        String resourceName = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();
        try{
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    @Operation(description = "DELETE 방식으로 파일 삭제")
    @DeleteMapping("/remove/{fileName}")
    public Map<String , Boolean> removeFile(@PathVariable String fileName) {
        Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);
        log.info(resource);
        String resourceName = resource.getFilename();
        Map<String ,Boolean> resultMap = new HashMap<>();
        boolean removed = false;
        try{
            String contentType = Files.probeContentType(resource.getFile().toPath());
            log.info(resource.getFile().toPath());
            removed = resource.getFile().delete();
            //섬네일이 존재한다면
            if(contentType.startsWith("image")){
                File thumbnailFile = new File (uploadPath+File.separator+"s_" + fileName);
                thumbnailFile.delete();
        }
        }catch(Exception e) {
            log.error(e);
        } //end catch
        resultMap.put("result",removed);
        return resultMap;
    }
}
