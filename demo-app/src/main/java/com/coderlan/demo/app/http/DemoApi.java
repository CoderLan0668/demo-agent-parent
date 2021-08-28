package com.coderlan.demo.app.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoApi {

    private static Logger logger = LoggerFactory.getLogger(DemoApi.class);

    @GetMapping("/test")
    public Object test() {
        logger.info("hello, {}", "world");
        Thread thread = new Thread(() -> {
            System.out.println("log in runnable");
        });
        thread.setName("shouldInstrument");
        thread.run();
        return "hello world";
    }
}
