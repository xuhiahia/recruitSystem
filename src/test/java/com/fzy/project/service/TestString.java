package com.fzy.project.service;

import cn.hutool.core.util.StrUtil;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class TestString {
    @Test
    public void test() {
        String originalFilename="D:\\abs\\bn.png";
        String suffix = StrUtil.subAfter(originalFilename, ".", true);
        System.out.println(suffix);
    }

    @Test
    public void test1(){

    }
}
