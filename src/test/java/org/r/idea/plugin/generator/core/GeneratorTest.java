package org.r.idea.plugin.generator.core;

import static org.junit.Assert.*;

import org.junit.Test;
import org.r.idea.plugin.generator.core.config.Config;
import org.r.idea.plugin.generator.impl.config.ConfigImpl;
import org.r.idea.plugin.generator.impl.upload.DeliverymanImpl;

/**
 * @ClassName GeneratorTest
 * @Author Casper
 * @DATE 2019/6/24 14:12
 **/
public class GeneratorTest {

    @Test
    public void doGenerate() {

        String localFile = "/home/casper/tmp/api/api-doc.jar";
        String username = "root";
        String password = "jinbantest@2019";
        String host = "47.110.84.14";
        String remotePath = "/root/tmp/api-doc/";
        int port = 22;

        DeliverymanImpl deliveryman = new DeliverymanImpl(localFile, host, username, password, port, remotePath);

        deliveryman.doDeliver();


    }


}