package tech.dut.safefood.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tech.dut.safefood.dto.request.ImageUploadDTO;
import tech.dut.safefood.dto.request.PreSignedURL;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import tech.dut.safefood.enums.CommonEnum;
import tech.dut.safefood.util.constants.Constants;

import static tech.dut.safefood.enums.CommonEnum.ImageFolderType.*;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    private final AwsStorageService awsStorageService;

    public List<PreSignedURL> uploadImagesWithPreSigned(List<ImageUploadDTO> imageUploadDTOs) {
        List<PreSignedURL> preSignedAWSResponse = new ArrayList<>();
        imageUploadDTOs.forEach(imageUploadDTO -> {
            String fileName = imageUploadDTO.getFileName().substring(imageUploadDTO.getFileName().lastIndexOf("/") + 1) + "_" + new Date().getTime() + "." + imageUploadDTO.getFileType();
            switch (imageUploadDTO.getImageFolderType()) {
                case PRODUCT:
                    fileName = Constants.AWS_PRODUCT_IMAGE_FOLDER + fileName;
                    break;
                case SHOP:
                    fileName = Constants.AWS_SHOP_IMAGE_FOLDER + fileName;
                    break;
                case VOUCHER:
                    fileName = Constants.AWS_VOUCHER_IMAGE_FOLDER + fileName;
                    break;
                case USER:
                    fileName = Constants.AWS_USER_IMAGE_FOLDER + fileName;
                    break;
                case NEWS:
                    fileName = Constants.AWS_NEWS_IMAGE_FOLDER + fileName;
                case COMMUNITY:
                    fileName = Constants.AWS_COMMUNITY_IMAGE_FOLDER  + fileName;
            }
            PreSignedURL preSignedAWS = this.awsStorageService.uploadPreSignedUrl(fileName);
            preSignedAWSResponse.add(preSignedAWS);
        });
        return preSignedAWSResponse;
    }
    public List<String> uploadFiles(List<MultipartFile> multipartFiles, String folderType) {
        List<String> response = new ArrayList<>();
        multipartFiles.forEach(multipartFile ->response.add(awsStorageService.uploadFile(multipartFile, folderType)));
        return response;
    }
}
