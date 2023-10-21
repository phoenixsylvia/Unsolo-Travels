package com.interswitch.Unsolorockets.service.impl;

import com.interswitch.Unsolorockets.dtos.TransferChargeDto;
import com.interswitch.Unsolorockets.dtos.UpdateTransferChargeDto;
import com.interswitch.Unsolorockets.exceptions.UserNotFoundException;
import com.interswitch.Unsolorockets.models.TransferCharge;
import com.interswitch.Unsolorockets.respository.TransferChargeRepository;
import com.interswitch.Unsolorockets.service.TransferChargeService;
import com.interswitch.Unsolorockets.utils.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.interswitch.Unsolorockets.utils.UserUtil.getLoggedInUser;

@Service
@RequiredArgsConstructor
public class TransferChargeServiceImpl implements TransferChargeService {

    private final TransferChargeRepository transferChargeRepository;


    @Override
    public TransferChargeDto updateCharge(UpdateTransferChargeDto updateTransferChargeDto) throws UserNotFoundException {
        CustomUser user = getLoggedInUser();
        TransferCharge transferCharge = transferChargeRepository.findById(1L).orElse(new TransferCharge());
        transferCharge.setCharge(updateTransferChargeDto.getCharge());
        transferCharge.setUserId(user.getId());
        transferChargeRepository.save(transferCharge);
        return TransferChargeDto.builder()
                .charge(updateTransferChargeDto.getCharge())
                .build();
    }

    @Override
    public TransferChargeDto getCharge() {
        Optional<TransferCharge> transferCharge = transferChargeRepository.findById(1L);
        return TransferChargeDto.builder()
                .charge(transferCharge.get().getCharge())
                .build();
    }
}
