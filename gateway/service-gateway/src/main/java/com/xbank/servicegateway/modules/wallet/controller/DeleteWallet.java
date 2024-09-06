package com.xbank.servicegateway.modules.wallet.controller;

import com.xbank.servicegateway.modules.user.dto.StatusResponse;
import com.xbank.servicegateway.modules.wallet.service.WalletService;
import com.xbank.servicegateway.shared.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
public class DeleteWallet {
    private final WalletService walletService;

    @DeleteMapping("/")
    public ResponseEntity<ApiResponse<StatusResponse>> init(@PathVariable String id){
        var response = this.walletService.deleteWallet(id);

        return new ResponseEntity<>(new ApiResponse<>("User created Successfully.", response), HttpStatus.OK);
    }
}
