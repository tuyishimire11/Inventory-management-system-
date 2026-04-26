package com.consessa.inventory_management;

import com.consessa.inventory_management.model.AppUser;
import com.consessa.inventory_management.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryManagementApplication.class, args);
    }

    @Bean
    public CommandLineRunner seedDatabase(AppUserRepository appUserRepository) {
        return args -> {
            if (!appUserRepository.existsById("24RP09087")) {
                appUserRepository.save(new AppUser("24RP09087", "24RP04278", "SYSADMIN"));
                System.out.println("✅ SysAdmin user seeded.");
            }
        };
    }
}