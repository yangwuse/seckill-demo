package com.xxxx.seckill.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xpath.internal.operations.Mod;
import com.wf.captcha.ArithmeticCaptcha;
import com.xxxx.seckill.entity.User;
import com.xxxx.seckill.rabbimq.Sender;
import com.xxxx.seckill.utils.JsonUtil;
import com.xxxx.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.codec.cbor.Jackson2CborEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yangwu
 * @since 2022-03-30
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private Sender sender;

    private static int cnt = 0;

    // 用户信息（测试）
    @RequestMapping("/info")
    @ResponseBody
    public RespBean info(User user) {
        System.out.println("user " + cnt++);
        return RespBean.success(user);
    }

    // // 测试 RabbitMQ 发送消息
    // @RequestMapping("/mq")
    // @ResponseBody
    // public void mq() {
    //     sender.send("Hello");
    // }
    //
    // // Fanout 模式
    // @RequestMapping("/mq/fanout")
    // @ResponseBody
    // public void mq01() {
    //     sender.send("Hello");
    // }
    //
    // // Direct 模式
    // @RequestMapping("/mq/direct01")
    // @ResponseBody
    // public void mq02() {
    //     sender.send01("Hello, Red");
    // }
    //
    // @RequestMapping("/mq/direct02")
    // @ResponseBody
    // public void mq03() {
    //     sender.send02("Hello, Green");
    // }
    //
    // // Topic 模式
    // @RequestMapping("/mq/topic01")
    // @ResponseBody
    // public void mq04() {
    //     sender.send03("msg");
    // }
    //
    // @RequestMapping("/mq/topic02")
    // @ResponseBody
    // public void mq05() {
    //     sender.send04("msg");
    // }

    @RequestMapping("/hello/{name}")
    @ResponseBody
    public String hello(@CookieValue("ticket") String session,
                        @RequestHeader("Accept-Language") String language,
                        @PathVariable("name") String name,
                        @RequestParam("age") int age,
                        HttpServletRequest request) {
        ModelMap map = new ModelMap();
        map.addAttribute("ticket", session);
        map.addAttribute("language", language);
        map.addAttribute("name", name);
        map.addAttribute("age", age);
        ModelMap map1 = new ModelMap();
        map1.addAttribute("AuthType", request.getAuthType());
        map1.addAttribute("ContextPath", request.getContextPath());
        map1.addAttribute("Cookies", request.getCookies());
        Enumeration<String> headerNames = request.getHeaderNames();
        int i = 0;
        while (headerNames.hasMoreElements()) {
            String headName = headerNames.nextElement();
            map1.addAttribute("head" + i++, request.getHeader(headName));
        }
        map1.addAttribute("HttpServletMapping", request.getHttpServletMapping());
        map1.addAttribute("method", request.getMethod());
        map1.addAttribute("QueryString", request.getQueryString());
        map1.addAttribute("PathInfo", request.getPathInfo());
        map1.addAttribute("RequestURL", request.getRequestURL());
        map1.addAttribute("RequestURI", request.getRequestURI());
        map1.addAttribute("ServletPath", request.getServletPath());
        map1.addAttribute("UserPrincipal", request.getUserPrincipal());
        map1.addAttribute("RemoteUser", request.getRemoteUser());
        map.addAttribute("request", map1);

        return JsonUtil.object2JsonStr(map);
    }

    @GetMapping("/user1")
    @ResponseBody
    public String user(User user) {
        return JsonUtil.object2JsonStr(user);
    }

    @GetMapping("/img")
    public void img(OutputStream out) throws IOException {
        ClassPathResource resource = new ClassPathResource("/img.png");
        FileCopyUtils.copy(resource.getInputStream(), out);
    }

    @GetMapping("/img2")
    @ResponseBody
    public byte[] img() throws IOException {
        ClassPathResource resource = new ClassPathResource("/img.png");
        byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
        return bytes;
    }

    @GetMapping("/request")
    @ResponseBody
    public String echo(@RequestBody String request, User user) {
        System.out.println(request);
        return request;
    }

    // SpringMVC 在调用方法时将提前创建的 Model 对象传入方法
    @GetMapping("/model")
    public void model(Model model) {
        System.out.println(model);
    }

    // SpringMVC 重定向 即发送一个新的请求
    @GetMapping("/redirect")
    public String redirect() {
        return "redirect:http://www.github.com";
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public String exceptionHandler(RuntimeException exception, HttpServletRequest request) {
        return exception.getMessage();
    }

    @GetMapping("/error")
    public void error(User user) {
       // int a = 1 / 0;
        Integer.parseInt("1.0");
    }

}

