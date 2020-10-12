package cn.inps.dsf;

import cn.inps.dsf.server.NettyServiceServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;

@SpringBootApplication
public class DsfApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(DsfApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE);//不启动web服务
        app.run(args);

    }
    @Async
    @Override
    public void run(String... args) throws Exception {
        new NettyServiceServer().bind();
    }
}