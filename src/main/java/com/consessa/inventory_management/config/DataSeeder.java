package com.consessa.inventory_management.config;

import com.consessa.inventory_management.model.AppUser;
import com.consessa.inventory_management.model.Asset;
import com.consessa.inventory_management.model.AssetStatus;
import com.consessa.inventory_management.model.AuditLog;
import com.consessa.inventory_management.repository.AppUserRepository;
import com.consessa.inventory_management.repository.AssetRepository;
import com.consessa.inventory_management.repository.AuditLogRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataSeeder implements CommandLineRunner {

    private final AssetRepository assetRepository;
    private final AuditLogRepository auditLogRepository;
    private final AppUserRepository appUserRepository;

    public DataSeeder(AssetRepository assetRepository,
                      AuditLogRepository auditLogRepository,
                      AppUserRepository appUserRepository) {
        this.assetRepository = assetRepository;
        this.auditLogRepository = auditLogRepository;
        this.appUserRepository = appUserRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Seed admin user if not exists (though already seeded)
        if (!appUserRepository.existsById("24RP09087")) {
            appUserRepository.save(new AppUser("24RP09087", "24RP04278", "SYSADMIN"));
            System.out.println("Admin user seeded.");
        }

        // Seed assets only if table is empty
        if (assetRepository.count() == 0) {
            Asset a1 = new Asset();
            a1.setDeviceType("Laptop");
            a1.setSerialNumber("SN001");
            a1.setModelSpecifications("Dell XPS 15");
            a1.setCondition("New");
            a1.setStatus(AssetStatus.AVAILABLE);
            assetRepository.save(a1);

            Asset a2 = new Asset();
            a2.setDeviceType("Laptop");
            a2.setSerialNumber("SN002");
            a2.setModelSpecifications("HP EliteBook");
            a2.setOwnerName("TUYISHIMIRE Fabrigas");
            a2.setDepartment("IT");
            a2.setCondition("Good");
            a2.setIssueDate(LocalDate.now());
            a2.setStatus(AssetStatus.ASSIGNED);
            assetRepository.save(a2);

            Asset a3 = new Asset();
            a3.setDeviceType("Projector");
            a3.setSerialNumber("SN003");
            a3.setModelSpecifications("Epson EB-FH06");
            a3.setCondition("Used");
            a3.setStatus(AssetStatus.AVAILABLE);
            assetRepository.save(a3);

            Asset a4 = new Asset();
            a4.setDeviceType("Phone");
            a4.setSerialNumber("SN004");
            a4.setModelSpecifications("iPhone 14");
            a4.setOwnerName("NYIRABIZIMANA Consessa");
            a4.setDepartment("Sales");
            a4.setCondition("New");
            a4.setIssueDate(LocalDate.now());
            a4.setStatus(AssetStatus.ASSIGNED);
            assetRepository.save(a4);

            System.out.println("✅ 4 sample assets seeded.");

            auditLogRepository.save(new AuditLog("CREATE", "Asset created: SN001", a1.getId(), "system"));
            auditLogRepository.save(new AuditLog("CREATE", "Asset created: SN002", a2.getId(), "system"));
            auditLogRepository.save(new AuditLog("CREATE", "Asset created: SN003", a3.getId(), "system"));
            auditLogRepository.save(new AuditLog("CREATE", "Asset created: SN004", a4.getId(), "system"));
            auditLogRepository.save(new AuditLog("ISSUE", "Asset issued to TUYISHIMIRE Fabrigas (IT)", a2.getId(), "24RP04278"));
            auditLogRepository.save(new AuditLog("ISSUE", "Asset issued to NYIRABIZIMANA Consessa (Sales)", a4.getId(), "24RP09087"));
        }
    }
}