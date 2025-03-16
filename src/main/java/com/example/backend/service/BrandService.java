package com.example.backend.service;

import com.example.backend.dto.request.BrandDTO;
import com.example.backend.dto.response.BrandRes;
import com.example.backend.entity.Brand;
import com.example.backend.entity.Product_line;
import com.example.backend.repository.BrandRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BrandService {
    @Autowired
    private BrandRepo brandRepo;

    public BrandRes createBrand(BrandDTO brandDTO) {
        Brand brand = new Brand();
        brand.setName(brandDTO.getName());

        Brand result = brandRepo.save(brand);

        return new BrandRes(result.getId(), result.getName());
    }

    public BrandRes updateBrand(String id ,BrandDTO brandDTO) {
        Brand brand = brandRepo.findById(id)
                .orElseThrow(()-> new RuntimeException(("Brand not found")));

        brand.setName(brandDTO.getName());

        Brand updatedBrand = brandRepo.save(brand);

        return new BrandRes(updatedBrand.getId(), updatedBrand.getName());
    }

    public void deleteBrand(String id) {
        Brand brand = brandRepo.findById(id).orElseThrow(()-> new RuntimeException(("Brand not found")));
        brandRepo.delete(brand);
    }

    public BrandRes getBrandById(String id) {
        Brand brand = brandRepo.findById(id)
                .orElseThrow(()-> new RuntimeException(("Brand not found")));
        return new BrandRes(
                brand.getId(),
                brand.getName()
        );
    }

    public List<BrandRes> getAllBrands() {
        List<Brand> brands = brandRepo.findAll();

        return brands.stream()
                .map(brand -> new BrandRes(
                        brand.getId(),
                        brand.getName()
                )).collect(Collectors.toList());
    }

    public List<com.example.backend.dto.response.BrandDTO> getAllBrandsWithProductLines() {
        List<Object[]> results = brandRepo.findAllWithProductLinesNative();
        Map<String, com.example.backend.dto.response.BrandDTO> brandMap = new HashMap<>();

        for (Object[] row : results) {
            String brandId = (String) row[0];
            String brandName = (String) row[1];
            String productLineId = (String) row[2];
            String productLineName = (String) row[3];

            brandMap.putIfAbsent(brandId, new com.example.backend.dto.response.BrandDTO(brandName, new ArrayList<>()));

            if (productLineName != null) {
                brandMap.get(brandId).getProductLines().add(productLineName);
            }
        }
        return new ArrayList<>(brandMap.values());
    }
}
