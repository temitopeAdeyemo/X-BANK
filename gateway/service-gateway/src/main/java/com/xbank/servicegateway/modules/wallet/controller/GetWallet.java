package com.xbank.servicegateway.modules.wallet.controller;

import com.xbank.servicegateway.modules.wallet.dto.GetWalletRequest;
import com.xbank.servicegateway.modules.wallet.dto.GetWalletsRequest;
import com.xbank.servicegateway.modules.wallet.dto.WalletDto;
import com.xbank.servicegateway.modules.wallet.service.WalletService;
import com.xbank.servicegateway.shared.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/wallet")
public class GetWallet {
    private final WalletService walletService;

    @GetMapping("/")
    ResponseEntity<ApiResponse<WalletDto>> getSingle(@ModelAttribute GetWalletRequest data){
        WalletDto response = this.walletService.getWallet(data);

        return new ResponseEntity<>(new ApiResponse<>("Success.", response),HttpStatus.OK);
    }

    @GetMapping("/many")
    ResponseEntity<ApiResponse<List<WalletDto>>> getMany(@ModelAttribute GetWalletsRequest data, @RequestParam int page, @RequestParam int size){
        var response = this.walletService.getWallets(data, page, size);

        return new ResponseEntity<>(new ApiResponse<>("Success.", response),HttpStatus.OK);
    }
}
