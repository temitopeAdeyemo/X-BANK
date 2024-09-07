package com.xbank.servicegateway.modules.wallet.controller;

import com.xbank.servicegateway.modules.wallet.dto.BalanceResponse;
import com.xbank.servicegateway.modules.wallet.dto.GetWalletRequest;
import com.xbank.servicegateway.modules.wallet.service.WalletService;
import com.xbank.servicegateway.shared.utils.ApiResponse;
import com.xbank.servicegateway.shared.utils.ContextKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/wallet")
public class WalletBalance {
    public final WalletService walletService;

    @GetMapping("/balance")
    public ResponseEntity<ApiResponse<BalanceResponse>> getBalance(@RequestParam @Valid @ModelAttribute GetWalletRequest data){
        var response = this.walletService.getBalance(data, ContextKeys.user.get().getId());

        return new ResponseEntity<>(new ApiResponse<>("Balance fetched successfully.", response), HttpStatus.OK);
    }

}
