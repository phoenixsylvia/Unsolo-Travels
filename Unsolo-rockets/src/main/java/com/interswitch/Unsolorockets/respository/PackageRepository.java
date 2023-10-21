package com.interswitch.Unsolorockets.respository;

import com.interswitch.Unsolorockets.models.Package;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PackageRepository extends JpaRepository<Package, Long> {

    Optional<Package> findByTitle(String title);
}
