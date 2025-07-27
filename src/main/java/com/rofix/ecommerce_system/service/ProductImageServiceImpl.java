package com.rofix.ecommerce_system.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.rofix.ecommerce_system.config.AppConstants;
import com.rofix.ecommerce_system.dto.response.ProductImageResponseDTO;
import com.rofix.ecommerce_system.entity.Product;
import com.rofix.ecommerce_system.entity.ProductImage;
import com.rofix.ecommerce_system.exception.base.BadRequestException;
import com.rofix.ecommerce_system.repository.ProductImageRepository;
import com.rofix.ecommerce_system.helpers.EntityHelper;
import com.rofix.ecommerce_system.helpers.ProductImageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {
    private final EntityHelper entityHelper;
    private final Cloudinary cloudinary;
    private final ProductImageRepository productImageRepository;
    private final ModelMapper modelMapper;
    private final ProductImageHelper productImageHelper;

    @Override
    public ProductImageResponseDTO uploadProductImage(Long productId, MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("The uploaded file is empty");
        }

        productImageHelper.checkFileExtension(file);

        Product product = entityHelper.getProductOrThrow(productId);
        //upload
        Map<?, ?> uploadFile;
        try {
            Map<?, ?> infos = ObjectUtils.asMap("resource_type", "image",
                    "public_id", UUID.randomUUID().toString(),
                    "folder", AppConstants.CLOUDINARY_PRODUCTS_FOLDER,
                    "overwrite", true);
            uploadFile = cloudinary.uploader().upload(file.getBytes(), infos);
            log.info("Product image uploaded successfully: {}", uploadFile.get("secure_url"));

        } catch (IOException ex) {
            log.error("Failed Upload Product Image");
            throw new BadRequestException("Failed Upload Product Image");
        }
        log.info("Upload Product Image File Name: {}", uploadFile.get("secure_url"));

        ProductImage productImage = new ProductImage();
        productImage.setUrl((String) uploadFile.get("secure_url"));
        productImage.setProduct(product);
        productImage.setPublicId((String) uploadFile.get("public_id"));
        ProductImage savedProductImage = productImageRepository.save(productImage);

        log.info("Product Image Saved in DB: {}", savedProductImage.getUrl());

        return modelMapper.map(savedProductImage, ProductImageResponseDTO.class);
    }

    @Override
    public String deleteImageProduct(Long productId, Long productImageId) {
        Product product = entityHelper.getProductOrThrow(productId);
        ProductImage productImage = entityHelper.getProductImageOrThrow(productImageId);
        try {
            Map<?, ?> infos = ObjectUtils.asMap("invalidate", true);
            Map<?, ?> result = cloudinary.uploader().destroy(productImage.getPublicId(), infos);
            log.info("Result {}", result);
            log.info("Product Image Deleted From Cloudinary...");
        } catch (IOException ex) {
            log.error("Failed Delete Product Image From Cloudinary");
            throw new BadRequestException("Failed Delete Product Image");
        }
        productImageRepository.delete(productImage);
        log.info("Product Image Deleted Successfully");
        return "Image for product " + product.getId() + " deleted successfully.";
    }
}
