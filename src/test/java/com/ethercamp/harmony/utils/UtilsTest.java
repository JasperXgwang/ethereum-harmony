package com.ethercamp.harmony.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.spongycastle.util.encoders.Hex;

/**
 * User: jasperxgwang
 * Date: 2018-7-11 10:17
 */
@Slf4j(topic = "UtilsTest")
public class UtilsTest {

    @Test
    public void HexTest() {
        byte[] data = "jasper".getBytes();
        String hexStr = Hex.toHexString(data);
        // 16 进制
        log.info("hexStr:{}", hexStr);
    }
}
