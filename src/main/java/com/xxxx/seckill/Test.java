package com.xxxx.seckill;

import com.xxxx.seckill.config.WebConfig;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author yangWu
 * @date 2022/4/29 10:37 PM
 * @version 1.0
 */
public class Test {
    public Test() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static Test instance;

    public static Test getInstance() {
        if(instance == null) {
            synchronized (Test.class) {
                if (instance == null) {
                    instance = new Test();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            while (true) {
                Test t = getInstance();
                if (t == null )
                    System.out.println("t1 test == null break");
            }
        });
        Thread t2 = new Thread(() -> {
            while (true) {
                Test t = getInstance();
                if (t == null) {
                    System.out.println("t2 test == null break ");
                }
            }
        });
        t1.start();
        t2.start();
    }


}
