package com.alphasolutions.piauieventos.repository;


import com.alphasolutions.piauieventos.model.LocationCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationCategoryRepository extends JpaRepository<LocationCategory, Integer> {
    LocationCategory findByCategoryName(String category);
}
