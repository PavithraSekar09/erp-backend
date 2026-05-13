package com.example.erp.product.controller;

import com.example.erp.product.entity.Product;
import com.example.erp.product.repository.ProductRepository;

import com.example.erp.finance.entity.FinanceAsset;
import com.example.erp.finance.repository.FinanceAssetRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    @Autowired
    private ProductRepository repo;

    @Autowired
    private FinanceAssetRepository assetRepo;

    // =========================
    // GET ALL PRODUCTS
    // =========================
    @GetMapping
    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    // =========================
    // ADD PRODUCT + ADD ASSET
    // =========================
    @PostMapping
    public Product addProduct(@RequestBody Product product) {

        Product savedProduct = repo.save(product);

        FinanceAsset asset = new FinanceAsset();
        asset.setAssetName(product.getName());
        asset.setAssetType("Inventory");
        asset.setPurchaseCost(product.getPrice() * product.getStock());
        asset.setPurchaseDate(LocalDate.now());
        asset.setStatus("Active");

        assetRepo.save(asset);

        return savedProduct;
    }

    // =========================
    // UPDATE PRODUCT + UPDATE ASSET
    // =========================
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id,
                                 @RequestBody Product updatedProduct) {

        Optional<Product> optional = repo.findById(id);

        if (optional.isPresent()) {

            Product product = optional.get();

            String oldName = product.getName();

            product.setName(updatedProduct.getName());
            product.setPrice(updatedProduct.getPrice());
            product.setStock(updatedProduct.getStock());

            Product savedProduct = repo.save(product);

            // Find matching asset by product name
            List<FinanceAsset> assets = assetRepo.findAll();

            for (FinanceAsset asset : assets) {
                if (asset.getAssetName().equals(oldName)
                        && asset.getAssetType().equals("Inventory")) {

                    asset.setAssetName(updatedProduct.getName());
                    asset.setPurchaseCost(
                            updatedProduct.getPrice() * updatedProduct.getStock()
                    );
                    asset.setPurchaseDate(LocalDate.now());

                    assetRepo.save(asset);
                    break;
                }
            }

            return savedProduct;
        }

        return null;
    }

    // =========================
    // DELETE PRODUCT + DELETE ASSET
    // =========================
    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {

        Optional<Product> optional = repo.findById(id);

        if (optional.isPresent()) {

            Product product = optional.get();

            String productName = product.getName();

            // Delete product
            repo.deleteById(id);

            // Delete matching asset
            List<FinanceAsset> assets = assetRepo.findAll();

            for (FinanceAsset asset : assets) {
                if (asset.getAssetName().equals(productName)
                        && asset.getAssetType().equals("Inventory")) {

                    assetRepo.deleteById(asset.getId());
                    break;
                }
            }

            return "Deleted Successfully";
        }

        return "Product Not Found";
    }
}