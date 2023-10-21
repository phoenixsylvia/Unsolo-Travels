package com.interswitch.Unsolorockets;

import com.interswitch.Unsolorockets.models.TransferCharge;
import com.interswitch.Unsolorockets.respository.TransferChargeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.math.BigDecimal;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class UserServiceApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(UserServiceApplication.class, args);

        /*
         * To add a default transfer charge to the database. This is done to avoid error in case a user tries
         * to make a transfer while charges has not been set.
         * default value is set to 0.0 naira
         **/

        TransferChargeRepository transferChargeRepository = context.getBean(TransferChargeRepository.class);

        if (transferChargeRepository.findAll().isEmpty()) {
            TransferCharge transferCharge = new TransferCharge();
            transferCharge.setCharge(BigDecimal.ZERO);
            transferCharge.setUserId(1); // id of the super Admin since super admin is created when the application is ran
            transferChargeRepository.save(transferCharge);
            log.info("Created default transfer charge");
        }
    }
}