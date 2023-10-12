package tech.dut.safefood.controller.user;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.dut.safefood.dto.request.ImageUploadDTO;
import tech.dut.safefood.dto.request.PreSignedURL;
import tech.dut.safefood.dto.response.APIResponse;
import tech.dut.safefood.enums.CommonEnum;
import tech.dut.safefood.service.ImageUploadService;

import java.util.List;

@RestController
@RequestMapping("/users/image")
public class UserImageController {

    @Autowired
    private ImageUploadService imageUploadService;

    @ApiOperation(value = "Get pre-signed URL for upload images to s3")
    @PostMapping(path = "/pre-signed")
    public APIResponse<List<PreSignedURL>> uploadImages(
            @ApiParam(value = "List fileName", required = true) @RequestBody List<ImageUploadDTO> images) {
        return APIResponse.okStatus(this.imageUploadService.uploadImagesWithPreSigned(images));
    }

    @ApiOperation(value = "Upload image with s3")
    @PostMapping(path = "/upload")
    public APIResponse<List<String>> uploadFiles(
            @ApiParam(value = "List file") @RequestBody List<MultipartFile> files, @ApiParam("FolderType") @RequestParam CommonEnum.ImageFolderType imageFolderType) {
        return APIResponse.okStatus(this.imageUploadService.uploadFiles(files, imageFolderType.name()));
    }
}
