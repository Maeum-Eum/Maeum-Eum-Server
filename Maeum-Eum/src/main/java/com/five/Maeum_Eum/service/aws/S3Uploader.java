package com.five.Maeum_Eum.service.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.five.Maeum_Eum.exception.CustomException;
import com.five.Maeum_Eum.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class S3Uploader {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.region.static}")
    private String region;

    //Multipart를 통해 전송된 파일을 업로드하는 메소드
    public String uploadImage(MultipartFile file) throws IllegalAccessException {

        String fileName = createFileName(file.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            uploadFile(inputStream, objectMetadata, fileName);
        } catch(IOException e) {
            throw new IllegalAccessException(String.format("파일 변환 중 에러가 발생하였습니다.(%s)", file.getOriginalFilename()));
        }

        System.out.println("[파일 경로] : " + getFileUrl(fileName));
        return getFileUrl(fileName);
    }

    //기존 확장자명을 유지한 채, 유니크한 파일의 이름을 생성하는 로직
    private String createFileName(String originalFileName) throws IllegalAccessException {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    //파일의 확장자명을 가져오는 로직
    private String getFileExtension(String fileName) throws IllegalAccessException {

        String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        if (!extension.equals(".jpg") && !extension.equals(".png")) {
            throw new CustomException(ErrorCode.INVALID_FILE, extension);
        }

        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalAccessException(String.format("잘못된 형식의 파일(%s) 입니다", fileName));
        }
    }

    public  void  uploadFile(InputStream inputStream, ObjectMetadata objectMeTadata, String fileName){
        amazonS3.putObject(new PutObjectRequest(bucket,fileName,inputStream,objectMeTadata).withCannedAcl(CannedAccessControlList.PublicRead));
    }

    public  String getFileUrl(String fileName){
        return amazonS3.getUrl(bucket,fileName).toString();
    }

    //file 삭제
    public void fileDelete(String fileUrl) {
        String prefix = String.format("https://%s.s3.%s.amazonaws.com/", bucket, region);
        String fileKey = fileUrl.replace(prefix, "");
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucket,fileKey));
        } catch (AmazonServiceException e) {
            System.out.println(e.getErrorMessage());
        }
    }
}