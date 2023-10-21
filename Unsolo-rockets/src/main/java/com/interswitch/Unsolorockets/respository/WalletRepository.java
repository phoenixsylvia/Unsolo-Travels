package com.interswitch.Unsolorockets.respository;

import com.interswitch.Unsolorockets.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
   Optional<Wallet> findByUserId(long userId);

    Optional<Wallet> findWalletByWalletId(String walletId);
}
