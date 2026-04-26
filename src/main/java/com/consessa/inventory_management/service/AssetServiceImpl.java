package com.consessa.inventory_management.service;

import com.consessa.inventory_management.model.Asset;
import com.consessa.inventory_management.model.AssetStatus;
import com.consessa.inventory_management.repository.AssetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;
    private final AuditService auditService;

    public AssetServiceImpl(AssetRepository assetRepository, AuditService auditService) {
        this.assetRepository = assetRepository;
        this.auditService = auditService;
    }

    @Override
    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    @Override
    public Asset getAssetById(Long id) {
        return assetRepository.findById(id).orElse(null);
    }

    @Override
    public void saveAsset(Asset asset) {
        boolean isNew = asset.getId() == null;
        if (asset.getStatus() == null) {
            asset.setStatus(AssetStatus.AVAILABLE);
        }
        assetRepository.save(asset);
        if (isNew) {
            auditService.log("CREATE", "Asset created: " + asset.getSerialNumber(), asset.getId());
        } else {
            auditService.log("UPDATE", "Asset updated: " + asset.getSerialNumber(), asset.getId());
        }
    }

    @Override
    @Transactional
    public void deleteAsset(Long id) {
        Asset asset = getAssetById(id);
        if (asset != null) {
            auditService.log("DELETE", "Asset deleted: " + asset.getSerialNumber(), id);
            assetRepository.deleteById(id);
        }
    }

    @Override
    public void issueAsset(Long id, String ownerName, String department) {
        Asset asset = getAssetById(id);
        if (asset != null && asset.getReturnDate() == null && asset.getStatus() != AssetStatus.ASSIGNED) {
            asset.setOwnerName(ownerName);
            asset.setDepartment(department);
            asset.setIssueDate(LocalDate.now());
            asset.setReturnDate(null);
            asset.setStatus(AssetStatus.ASSIGNED);
            assetRepository.save(asset);
            auditService.log("ISSUE", "Asset issued to " + ownerName + " (" + department + ")", id);
        }
    }

    @Override
    public void returnAsset(Long id) {
        Asset asset = getAssetById(id);
        if (asset != null && asset.getIssueDate() != null && asset.getReturnDate() == null) {
            asset.setReturnDate(LocalDate.now());
            asset.setStatus(AssetStatus.AVAILABLE);
            assetRepository.save(asset);
            auditService.log("RETURN", "Asset returned by " + asset.getOwnerName(), id);
        }
    }

    @Override
    public List<Asset> searchAssets(String deviceType, String department, String condition, AssetStatus status) {
        if ((deviceType == null || deviceType.isEmpty()) &&
            (department == null || department.isEmpty()) &&
            (condition == null || condition.isEmpty()) &&
            status == null) {
            return getAllAssets();
        }
        return assetRepository.search(deviceType, department, condition, status);
    }
}