package com.consessa.inventory_management.service;

import com.consessa.inventory_management.model.Asset;
import com.consessa.inventory_management.model.AssetStatus;
import java.util.List;

public interface AssetService {
    List<Asset> getAllAssets();
    Asset getAssetById(Long id);
    void saveAsset(Asset asset);
    void deleteAsset(Long id);
    void issueAsset(Long id, String ownerName, String department);
    void returnAsset(Long id);
    List<Asset> searchAssets(String deviceType, String department, String condition, AssetStatus status);
}