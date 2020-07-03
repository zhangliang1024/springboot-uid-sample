package com.zhliang.uid.sample.controller;

import com.pzy.zhliang.uid.UidContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @类描述：测试不同策略获取ID
 * @创建人：zhiang
 * @创建时间：2020/6/28 18:50
 * @version：V1.0
 */
@RestController
@RequestMapping("uid")
public class UidContextController {

    @Autowired
    private UidContext context;

    //baiud snowflake
    @GetMapping
    public void context() {
        System.out.println(context.getUID());
        System.out.println("test: " +context.getUID("order"));
        System.out.println(context.getUID("qwer"));
        System.out.println("test: " +context.getUID("test"));
    }

    //segment step
    @GetMapping("leaf")
    public void segmenContext() {
        //System.out.println(context.getUID()); //leaf strategy prefix is empty
        System.out.println("test:" +context.getUID("order"));
    }



}
