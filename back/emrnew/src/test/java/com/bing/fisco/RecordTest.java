package com.bing.fisco;

import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class RecordTest {
    // 获取配置文件路径
    public final String configFile = "src/main/resources/config-example.toml";

    @Test
    public void test() throws Exception {
        // 初始化BcosSDK对象
        BcosSDK sdk = BcosSDK.build(configFile);
        // 获取Client对象，此处传入的群组ID为1
        Client client = sdk.getClient(Integer.valueOf(1));
        // 构造AssembleTransactionProcessor对象，需要传入client对象，CryptoKeyPair对象和abi、binary文件存放的路径。abi和binary文件需要在上一步复制到定义的文件夹中。
        CryptoKeyPair keyPair = client.getCryptoSuite().createKeyPair();
        AssembleTransactionProcessor transactionProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(client, keyPair, "src/main/resources/abi/", "");

        //同步方式发送交易
        // 创建调用交易函数的参数
//        String recordId = "4";
//        String description = "description4";
//        String remark = "remark4";
//
//        List<Object> params = new ArrayList<>();
//        params.add(recordId);
//        params.add(description);
//        params.add(remark);
//
//        // 调用record合约，调用函数名为『addRecord』，函数参数类型为params
//        TransactionResponse transactionResponse = transactionProcessor.sendTransactionAndGetResponseByContractLoader("record", "0xc0c669591f444b440b8c053ff977df2f34c2681f", "addRecord", params);
//
//        // 打印返回值
//        List<Object> returnValues = transactionResponse.getReturnObject();
//        if (returnValues != null) {
//            for (Object value : returnValues) {
//                System.out.println("上链返回值："+value.toString());
//            }
//        }


        //调用合约查询接口
        List<Object> params2 = new ArrayList<>();
        params2.add("13");
        // 调用record合约的『getRecord』函数，参数为recordid
        TransactionResponse transactionResponse2 = transactionProcessor.sendTransactionAndGetResponseByContractLoader("record", "0xc0c669591f444b440b8c053ff977df2f34c2681f", "getRecord", params2);
        // 打印返回值
        List<Object> returnValues2 = transactionResponse2.getReturnObject();
        if (returnValues2 != null) {
            // 检查返回值的长度是否正确
            if (returnValues2.size() == 2) {
                String description = (String) returnValues2.get(0);
                String remark = (String) returnValues2.get(1);
                System.out.println("Description: " + description);
                System.out.println("Remark: " + remark);
            } else {
                System.out.println("返回值长度不正确");
            }
        }
    }
}
