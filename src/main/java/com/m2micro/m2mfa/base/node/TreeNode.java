package com.m2micro.m2mfa.base.node;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: liaotao
 * @Date: 2018/12/19 16:08
 * @Description:    字典树形选项
 */
@Data
public class TreeNode {

    public String id;
    public String name;
    List<TreeNode> children = new ArrayList<>();

    public TreeNode(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public TreeNode() {
        super();
    }
}
