package com.m2micro.m2mfa.base.node;

import lombok.Data;

/**
 * @Auther: liaotao
 * @Date: 2018/12/19 16:10
 * @Description: 字典下拉选项
 */
@Data
public class SelectNode {
    public String id;
    public String name;
    public String value;

    public SelectNode(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public SelectNode(String id, String name, String value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public SelectNode() {
        super();
    }
}
