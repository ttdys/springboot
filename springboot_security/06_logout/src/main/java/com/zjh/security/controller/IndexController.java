package com.zjh.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @GetMapping("index")

    public Object index(){
        return SecurityContextHolder.getContext().getAuthentication();
    }
    @GetMapping("/auth/admin")
    @PreAuthorize("hasAuthority('admin')")
    public String authenticationTest() {
        return "您拥有admin权限，可以查看";
    }
    @GetMapping("/signout/success")
    public String logoutSuccess() {
        System.out.println("退出成功，请重新登录");
        return "退出成功，请重新登录";
    }
}
