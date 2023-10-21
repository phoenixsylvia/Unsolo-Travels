package com.interswitch.Unsolorockets.service;

import com.interswitch.Unsolorockets.dtos.TransferChargeDto;
import com.interswitch.Unsolorockets.dtos.UpdateTransferChargeDto;
import com.interswitch.Unsolorockets.exceptions.UserNotFoundException;

public interface TransferChargeService {

    TransferChargeDto updateCharge(UpdateTransferChargeDto updateTransferChargeDto) throws UserNotFoundException;

    TransferChargeDto getCharge();
}
