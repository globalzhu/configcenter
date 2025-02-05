/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-22 15:48 创建
 */
package org.antframework.configcenter;

import cn.hutool.core.util.RandomUtil;
import com.be.gaia.mpc.core.ecc.CurveType;
import com.be.gaia.mpc.core.ecc.EccCryptorFactory;
import com.be.gaia.mpc.core.ecc.IEccCryptor;
import org.antframework.boot.lang.AntBootApplication;
import org.antframework.boot.lang.Apps;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.boot.SpringApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 程序启动入口
 */
@AntBootApplication(appId = "configcenter")
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 604800)
public class Main {
    public static void main(String[] args) {
        Apps.setProfileIfAbsent("dev");
        SpringApplication.run(Main.class, args);
    }
}
