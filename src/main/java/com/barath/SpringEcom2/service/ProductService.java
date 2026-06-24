package com.barath.SpringEcom2.service;

import com.barath.SpringEcom2.model.Product;
import com.barath.SpringEcom2.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepo proRepo;

    public List<Product> getAllProducts() {
        return proRepo.findAll();
    }

    public Optional<Product> getProduct(int id) {
        return proRepo.findById(id);
    }


    public Product addOrUpdateProduct(Product product, MultipartFile image) throws IOException {
        product.setImageName(image.getOriginalFilename());
        product.setImageType(image.getContentType());
        product.setImageData(image.getBytes());

        return proRepo.save(product);
    }

    public void deleteProduct(int id){
        proRepo.deleteById(id);
    }

    public List<Product> searchProduct(String keyword) {
        return proRepo.searchProduct(keyword);
    }
}
