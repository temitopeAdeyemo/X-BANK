package com.xbank.servicegateway.modules.wallet.service.Impl;

import com.xbank.servicegateway.modules.user.dto.StatusResponse;
import com.xbank.servicegateway.modules.wallet.dto.*;
import com.xbank.servicegateway.modules.wallet.dto.CreateWalletResponse;
import com.xbank.servicegateway.modules.wallet.service.WalletService;
import com.xbank.servicegateway.shared.Exceptions.SystemBusyException;
import com.xbank.servicegateway.shared.mapper.WalletMapper;
import com.xbank.servicegateway.shared.service.RedisService;
import com.xbank.servicegateway.shared.service.WalletClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import proto.wallet.proto.*;
import proto.wallet.proto.GetWalletsRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Service
public class WalletServiceImpl implements WalletService {
    private final WalletClient walletClient;
    private final RedisService<String> redisService;

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
    public WalletDto getWallet(GetWalletRequest payload, String userId ){
        var response = this.walletClient.getWallet(GetSingleWalletRequest.newBuilder()
                .setAccountNumber(payload.accountNumber)
                .setId(userId)
                .build());

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

    @Override
    public BalanceResponse getBalance(GetWalletRequest payload, String userId){

        var response = this.walletClient.getBalance(GetBalanceRequest.newBuilder()
                .setAccountNumber(payload.accountNumber)
                .setId(userId)
                .build());

        var balanceData = new BalanceResponse();

        balanceData.setAccountBalance(response.getBalance());

        return balanceData;
    }

    @Override
    public InitTransactionResponse fundWallet(FundWalletRequest payload, String userId){
        var key = "trx/init"+"/"+userId;

        var openedSession = redisService.get(key);

        if(openedSession != null) throw new SystemBusyException("System busy with a request. Try again later.");

        redisService.set(key, "1", 1, TimeUnit.MINUTES);

        var response = this.walletClient.fundWallet(proto.transaction.proto.FundWalletRequest.newBuilder()
                .setUserId(userId)
                .setAmount(payload.getAmount())
                .build());

        var transactionData = new InitTransactionResponse();

        transactionData.setStatus(response.getStatus().toString());
        transactionData.setReferenceNumber(response.getTrfRef());

        redisService.remove(key);

        return transactionData;
    }

    @Override
    public InitTransactionResponse fundTransfer(FundTransferRequest payload, String userId){
        var key = "trx/init"+"/"+userId;

        var openedSession = redisService.get(key);

        if(openedSession != null) throw new SystemBusyException("System busy with a request. Try again later.");

        var response = this.walletClient.fundTransfer(proto.transaction.proto.FundTransferRequest.newBuilder()
                        .setReceiverAccountNumber(payload.getReceiverAccountNumber())
                        .setSenderId(userId)
                        .setAmount(payload.getAmount())
                        .setSourceCode(payload.getSourceCode())
                .build());

        var transactionData = new InitTransactionResponse();

        transactionData.setStatus(response.getStatus().toString());
        transactionData.setReferenceNumber(response.getTrfRef());

        redisService.remove(key);

        return transactionData;
    }

    public void getSingleTransaction(){}

    public void getAllTransactions(){}
}

