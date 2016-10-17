package net.virtualviking.dunesbuggy;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class RgraphApplication {

    public static void main(String[] args) {
    	System.setProperty("jsse.enableSNIExtension", "false");
        ApplicationContext ctx = SpringApplication.run(RgraphApplication.class, args);
    }
}