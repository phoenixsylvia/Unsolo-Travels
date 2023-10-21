package com.interswitch.Unsolorockets.controllers;

import com.interswitch.Unsolorockets.dtos.UpdateTransferChargeDto;
import com.interswitch.Unsolorockets.exceptions.UserNotFoundException;
import com.interswitch.Unsolorockets.service.TransferChargeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.interswitch.Unsolorockets.models.enums.Role.ADMIN_PREAUTHORIZE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transfer-charge")
public class TransferChargeController {

    private final TransferChargeService transferChargeService;

    @PutMapping("/update")
    @PreAuthorize(ADMIN_PREAUTHORIZE)
    public ResponseEntity<?> setTransferCharge(@RequestBody UpdateTransferChargeDto updateTransferChargeDto) throws UserNotFoundException {
        return ResponseEntity.ok(transferChargeService.updateCharge(updateTransferChargeDto));
    }

    @GetMapping
    @PreAuthorize(ADMIN_PREAUTHORIZE)
    public ResponseEntity<?> getTransferCharge() {
        return ResponseEntity.ok((transferChargeService.getCharge()));
    }
}
