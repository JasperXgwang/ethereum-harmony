package com.ethercamp.harmony.address;

import lombok.extern.slf4j.Slf4j;
import org.ethereum.crypto.ECKey;
import org.junit.Test;
import org.spongycastle.util.encoders.Hex;

/**
 * User: jasperxgwang
 * Date: 2018-7-10 14:45
 */
@Slf4j(topic = "ECKeyTest")
public class ECKeyTest {


    @Test
    public void createAddress() {
        ECKey ecKey = new ECKey();
        //公钥哈希
        byte[] address = ecKey.getAddress();
        //16进制转换
        String addressStr = Hex.toHexString(address);
        log.info("address:{} pubKey:{} privateKey:{}", addressStr, Hex.toHexString(ecKey.getPubKey()), Hex.toHexString(ecKey.getPrivKeyBytes()));
    }
}
