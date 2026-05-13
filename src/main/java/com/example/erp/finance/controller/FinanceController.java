package com.example.erp.finance.controller;

import com.example.erp.finance.entity.FinanceAsset;
import com.example.erp.finance.entity.Revenue;
import com.example.erp.finance.repository.FinanceAssetRepository;
import com.example.erp.finance.repository.RevenueRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin-finance")
@CrossOrigin(origins = "http://localhost:3000")
public class FinanceController {

    @Autowired
    private FinanceAssetRepository assetRepo;

    @Autowired
    private RevenueRepository revenueRepo;

    // ==========================
    // GET ALL ASSETS
    // ==========================
    @GetMapping("/assets")
    public List<FinanceAsset> getAssets() {
        return assetRepo.findAll();
    }

    // ==========================
    // ADD NEW ASSET
    // ==========================
    @PostMapping("/assets")
    public FinanceAsset addAsset(@RequestBody FinanceAsset asset) {
        return assetRepo.save(asset);
    }

    // ==========================
    // UPDATE ASSET
    // ==========================
    @PutMapping("/assets/{id}")
    public FinanceAsset updateAsset(@PathVariable Long id,
                                    @RequestBody FinanceAsset updatedAsset) {

        Optional<FinanceAsset> optional = assetRepo.findById(id);

        if (optional.isPresent()) {
            FinanceAsset asset = optional.get();

            asset.setAssetName(updatedAsset.getAssetName());
            asset.setAssetType(updatedAsset.getAssetType());
            asset.setPurchaseCost(updatedAsset.getPurchaseCost());
            asset.setPurchaseDate(updatedAsset.getPurchaseDate());
            asset.setStatus(updatedAsset.getStatus());

            return assetRepo.save(asset);
        }

        return null;
    }

    // ==========================
    // DELETE ASSET
    // ==========================
    @DeleteMapping("/assets/{id}")
    public String deleteAsset(@PathVariable Long id) {
        assetRepo.deleteById(id);
        return "Asset Deleted Successfully";
    }

    // ==========================
    // GET REVENUE
    // ==========================
    @GetMapping("/revenue")
    public List<Revenue> getRevenue() {
        return revenueRepo.findAll();
    }
}