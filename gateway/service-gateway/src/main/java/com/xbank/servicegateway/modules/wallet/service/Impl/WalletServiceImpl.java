package com.xbank.servicegateway.modules.wallet.service.Impl;

import com.xbank.servicegateway.modules.user.dto.StatusResponse;
import com.xbank.servicegateway.modules.user.dto.UserDto;
import com.xbank.servicegateway.modules.wallet.dto.CreateWalletResponse;
import com.xbank.servicegateway.modules.wallet.dto.GetWalletRequest;
import com.xbank.servicegateway.modules.wallet.dto.WalletDto;
import com.xbank.servicegateway.modules.wallet.service.WalletService;
import com.xbank.servicegateway.shared.mapper.UserMapper;
import com.xbank.servicegateway.shared.mapper.WalletMapper;
import com.xbank.servicegateway.shared.service.WalletClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import proto.wallet.proto.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class WalletServiceImpl implements WalletService {
    private final WalletClient walletClient;
    @Override
    public CreateWalletResponse createWallet(String userId) {
        var response = this.walletClient.createWallet(CreateWalletRequest.newBuilder().setUserId(userId).build());

        var walletData =  new CreateWalletResponse();
        walletData.setAccountNumber(response.getAccountNumber());

        return walletData;
    }

    public StatusResponse deleteWallet(String userId){
        this.walletClient.deleteWallet(DeleteWalletRequest.newBuilder().setId(userId).build());

        var status = new StatusResponse();

        status.setStatus("SUCCESS");

        return status;
    }

    @Override
    public WalletDto getWallet(GetWalletRequest payload){
        var response = this.walletClient.getWallet(GetSingleWalletRequest.newBuilder().setAccountNumber(payload.accountNumber).setId(payload.Id).build());

        WalletDto walletDto = new WalletDto();

        new WalletMapper().walletBuilder(walletDto, response);

        return walletDto;
    }

    @Override
    public List<WalletDto> getWallets(com.xbank.servicegateway.modules.wallet.dto.GetWalletsRequest payload, int page, int size){
            var dataFilter = GetWalletFilter.newBuilder().setWalletType(payload.getWalletType()).setUserId(payload.getUserId());

            var data = GetWalletsRequest.newBuilder().setPage(page).setSize(size).setFilter(dataFilter).build();

            var response = this.walletClient.getWallets(data);

            List<WalletDto> walletList = new ArrayList<>();
            var walletBuild = new WalletDto();

            response.getWalletsList().forEach((wallet)->{
                walletList.add(new WalletMapper().walletBuilder(walletBuild, wallet));
            });

            return walletList;
    }

    public void getBalance(){}

    public void fundWallet(){}

    public void fundTransfer(){}

    public void getSingleTransaction(){}

    public void getAllTransactions(){}
}

