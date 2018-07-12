/*
 * Copyright 2015, 2016 Ether.Camp Inc. (US)
 * This file is part of Ethereum Harmony.
 *
 * Ethereum Harmony is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Ethereum Harmony is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Ethereum Harmony.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.ethercamp.harmony;

import com.ethercamp.harmony.config.EthereumHarmonyConfig;
import lombok.extern.slf4j.Slf4j;
import org.ethereum.Start;
import org.ethereum.config.SystemProperties;
import org.ethereum.facade.Ethereum;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;

//@Configuration：指出该类是 Bean 配置的信息源，相当于XML中的<beans></beans>，一般加在主类上。
//@EnableAutoConfiguration：让 SpringBoot 根据应用所声明的依赖来对 Spring 框架进行自动配置，由于 spring-boot-starter-web 添加了Tomcat和Spring MVC，所以auto-configuration将假定你正在开发一个web应用并相应地对Spring进行设置
//@ComponentScan：表示将该类自动发现（扫描）并注册为Bean，可以自动收集所有的Spring组件（@Component , @Service , @Repository , @Controller 等），包括@Configuration类。
//@SpringBootApplication： @EnableAutoConfiguration、@ComponentScan和@Configuration的合集。
//@EnableTransactionManagement：启用注解式事务。
//EnableScheduling 注解的作用是发现注解@Scheduled的任务并后台执行。

@SpringBootApplication
@EnableScheduling
@Import({EthereumHarmonyConfig.class})
@Slf4j(topic = "Application")
public class Application {

    /**
     * Does one of:
     * - start Harmony peer;
     * - perform action and exit on completion.
     *
     * vm option
     * -Dethereumj.conf.res=private.conf -Ddatabase.name=database-private -DnetworkProfile=private
     */
    public static void main(String[] args) throws Exception {
        // Overriding mine.start to get control of its startup
        // in {@link com.ethercamp.harmony.service.PrivateMinerService}
        SystemProperties.getDefault().overrideParams("mine.start", "false");
        final List<String> actions = asList("importBlocks");

        final Optional<String> foundAction = asList(args).stream()
                .filter(arg -> actions.contains(arg))
                .findFirst();

        if (foundAction.isPresent()) {
            foundAction.ifPresent(action -> System.out.println("Performing action: " + action));
            Start.main(args);
            // system is expected to exit after action performed
        } else {
            if (!SystemProperties.getDefault().blocksLoader().equals("")) {
                SystemProperties.getDefault().setSyncEnabled(false);
                SystemProperties.getDefault().setDiscoveryEnabled(false);
            }

            ConfigurableApplicationContext context = SpringApplication.run(new Object[]{Application.class}, args);

            Ethereum ethereum = context.getBean(Ethereum.class);

            if (!SystemProperties.getDefault().blocksLoader().equals("")) {
                ethereum.getBlockLoader().loadBlocks();
            }
        }
    }
}
