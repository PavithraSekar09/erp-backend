package com.example.erp.finance.repository;

import com.example.erp.finance.entity.FinanceAsset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinanceAssetRepository extends JpaRepository<FinanceAsset, Long> {
}