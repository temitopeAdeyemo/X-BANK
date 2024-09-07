package com.xbank.servicegateway.modules.wallet.controller;

import com.xbank.servicegateway.modules.wallet.dto.FundTransferRequest;
import com.xbank.servicegateway.modules.wallet.dto.FundWalletRequest;
import com.xbank.servicegateway.modules.wallet.dto.InitTransactionResponse;
import com.xbank.servicegateway.modules.wallet.service.WalletService;
import com.xbank.servicegateway.shared.utils.ApiResponse;
import com.xbank.servicegateway.shared.utils.ContextKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/transaction/init")
public class TransactionInitiation {
    public final WalletService walletService;
    @PostMapping("/fund")
    public ResponseEntity<ApiResponse<InitTransactionResponse>> fundWallet(@RequestBody @Valid FundWalletRequest data){
        var response = this.walletService.fundWallet(data, ContextKeys.user.get().getId());

        return new ResponseEntity<>(new ApiResponse<>("Wallet funded successfully.", response), HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<InitTransactionResponse>> fundTransfer(@RequestBody @Valid FundTransferRequest data){
        var response = this.walletService.fundTransfer(data, ContextKeys.user.get().getId());

        return new ResponseEntity<>(new ApiResponse<>("Fund transferred successfully.", response), HttpStatus.OK);
    }
}
