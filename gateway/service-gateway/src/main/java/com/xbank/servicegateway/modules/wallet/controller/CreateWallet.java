package com.xbank.servicegateway.modules.wallet.controller;

import com.xbank.servicegateway.modules.wallet.dto.CreateWalletResponse;
import com.xbank.servicegateway.modules.wallet.service.WalletService;
import com.xbank.servicegateway.shared.utils.ApiResponse;
import com.xbank.servicegateway.shared.utils.ContextKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
public class CreateWallet {
    WalletService walletService;

    @PostMapping("/")
    public ResponseEntity<ApiResponse<CreateWalletResponse>> init(){
        String userId = ContextKeys.user.get().getId();
        CreateWalletResponse response = this.walletService.createWallet(userId);

        return new ResponseEntity<>(new ApiResponse<>("User created Successfully.", response), HttpStatus.CREATED);
    }
}
