package com.yuu.client;


import com.yuu.annotation.RpcReference;
import com.yuu.api.Hello;
import com.yuu.api.HelloService;
import org.springframework.stereotype.Component;


/**
 * @author smile2coder
 */
@Component
public class HelloController {

    @RpcReference(version = "version2", group = "test2")
    private HelloService helloService;

    public void test() throws InterruptedException {
        String hello = this.helloService.hello(new Hello("111", "222"));
        //如需使用 assert 断言，需要在 VM options 添加参数：-ea
        assert "Hello description is 222".equals(hello);
        Thread.sleep(12000);
        for (int i = 0; i < 10; i++) {
            System.out.println(helloService.hello(new Hello("111", "222")));
        }
    }
}
