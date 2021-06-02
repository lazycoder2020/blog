package com.blog.demo.service.impl;

import com.blog.demo.service.TestInterface;
import org.springframework.stereotype.Service;

@Service
public class TestInterfaceImpl implements TestInterface {
    @Override
    public void myName() {
        System.out.println("my name is john");
    }
}
