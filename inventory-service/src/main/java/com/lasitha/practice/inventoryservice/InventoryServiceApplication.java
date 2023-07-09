package com.lasitha.practice.inventoryservice;

import com.lasitha.practice.inventoryservice.model.Inventory;
import com.lasitha.practice.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
@SpringBootApplication
@EnableDiscoveryClient
public class InventoryServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(InventoryRepository inventoryRepository){
       return args -> {
           List<Inventory> inList = new ArrayList<>();

           inList.add(Inventory.builder()
                   .skuCode("101")
                   .quantity(10)
                   .build());

           inList.add(Inventory.builder()
                   .skuCode("102")
                   .quantity(5)
                   .build());

           inList.add(Inventory.builder()
                   .skuCode("103")
                   .quantity(0)
                   .build());

           inList.stream()
                   .filter(entity -> inventoryRepository.findBySkuCode(entity.getSkuCode()).isEmpty())
                   .forEach(inventoryRepository::save);
       };
    }
}
