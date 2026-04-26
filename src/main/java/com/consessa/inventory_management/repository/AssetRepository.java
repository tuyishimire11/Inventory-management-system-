package com.consessa.inventory_management.repository;

import com.consessa.inventory_management.model.Asset;
import com.consessa.inventory_management.model.AssetStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    List<Asset> findByDeviceTypeContainingIgnoreCase(String deviceType);
    List<Asset> findByDepartmentContainingIgnoreCase(String department);
    List<Asset> findByConditionIgnoreCase(String condition);
    List<Asset> findByStatus(AssetStatus status);

    @Query("SELECT a FROM Asset a WHERE " +
           "(:deviceType IS NULL OR LOWER(a.deviceType) LIKE LOWER(CONCAT('%', :deviceType, '%'))) AND " +
           "(:department IS NULL OR LOWER(a.department) LIKE LOWER(CONCAT('%', :department, '%'))) AND " +
           "(:condition IS NULL OR LOWER(a.condition) = LOWER(:condition)) AND " +
           "(:status IS NULL OR a.status = :status)")
    List<Asset> search(@Param("deviceType") String deviceType,
                       @Param("department") String department,
                       @Param("condition") String condition,
                       @Param("status") AssetStatus status);
}