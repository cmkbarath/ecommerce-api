package com.barath.SpringEcom2.controller;

import com.barath.SpringEcom2.model.Product;
import com.barath.SpringEcom2.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductService proService;

    @GetMapping("products")
    public ResponseEntity<List<Product>> getAllProducts(){
        return new ResponseEntity<>(proService.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("product/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id){

        Optional<Product> product = proService.getProduct(id);
        return product
                .map(p-> new ResponseEntity<>(p,HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @GetMapping("product/{productId}/image")
    public ResponseEntity<byte[]> getImageById(@PathVariable int productId){

        Optional<Product> product = proService.getProduct(productId);
        return product
                .map(p -> new ResponseEntity<>(p.getImageData(), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("product")
    public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile){
        Product saveProduct = null;
        try {
            saveProduct = proService.addOrUpdateProduct(product, imageFile);
            return new ResponseEntity<>(saveProduct,HttpStatus.CREATED);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("product/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id, @RequestPart Product product,
                                                @RequestPart MultipartFile imageFile){
        Optional<Product> existing = proService.getProduct(id);
        if (existing.isEmpty()) {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
        try {
            proService.addOrUpdateProduct(product, imageFile);
            return new ResponseEntity<>("Updated", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id){
        Optional<Product> product = proService.getProduct(id);
        if (product.isPresent()) {
            proService.deleteProduct(id);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("products/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam String keyword){
        List<Product> productslist = proService.searchProduct(keyword);
        System.out.println("Searching with " +keyword);
        return new ResponseEntity<>(productslist,HttpStatus.OK);
    }

}
