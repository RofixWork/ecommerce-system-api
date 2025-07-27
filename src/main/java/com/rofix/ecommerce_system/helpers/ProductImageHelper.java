package com.rofix.ecommerce_system.helpers;

import com.rofix.ecommerce_system.config.AppConstants;
import com.rofix.ecommerce_system.exception.base.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductImageHelper {
    public void checkFileExtension(MultipartFile file) {
        if (!file.isEmpty() && !AppConstants.ALLOWED_FILE_MIME_TYPES.contains(file.getContentType())) {
            log.error("The uploaded file is not a valid image. Accepted types are: {}", AppConstants.ALLOWED_FILE_MIME_TYPES);
            throw new BadRequestException("The uploaded file is not a valid image. Accepted types are: " + AppConstants.ALLOWED_FILE_MIME_TYPES);
        }
    }
}
