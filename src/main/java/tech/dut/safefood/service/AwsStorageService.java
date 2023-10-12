package tech.dut.safefood.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;
import tech.dut.safefood.dto.request.PreSignedURL;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

@Configuration
public class AwsStorageService {
    @Autowired
    private AmazonS3 s3client;

    @Value("${app.s3.aws.bucket-name}")
    private String bucketName;

    @Value("${app.pre-signed-expired}")
    private int preSignedExpired;

    @Value("${app.s3.aws.endpointUrl}")
    private String endpointUrl;

    public PreSignedURL uploadPreSignedUrl(String fileName) {

        PreSignedURL preSignedAWS = new PreSignedURL();
        Date date = new Date();
        date = DateUtils.addMinutes(date, this.preSignedExpired);
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(this.bucketName, fileName);
        generatePresignedUrlRequest.setMethod(HttpMethod.PUT);
        generatePresignedUrlRequest.setExpiration(date);
        URL url = this.s3client.generatePresignedUrl(generatePresignedUrlRequest);
        preSignedAWS.setPreSignedURL(url);
        String urlRes = url.getProtocol() + "://" + endpointUrl  + url.getPath();
        preSignedAWS.setUrl(urlRes);
        return preSignedAWS;
    }

    private String uploadFileTos3bucket(String fileName, File file) {
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return s3client.getUrl(bucketName, fileName).toString();
    }

    public String uploadFile(MultipartFile multipartFile, String folderType) {

        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile, folderType);
            fileUrl = uploadFileTos3bucket(fileName, file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String generateFileName(MultipartFile multiPart, String folderType) {
        return "photo/" + folderType.toLowerCase(Locale.ROOT) + "/" +
                new Date().getTime() + "-" +
                multiPart.getOriginalFilename().replace(" ", "_");
    }
}
