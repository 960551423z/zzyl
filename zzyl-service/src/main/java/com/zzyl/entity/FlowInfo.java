package com.zzyl.entity;

import lombok.Data;

import java.util.Date;

@Data
public class FlowInfo {
    private Long id;
    private String flowname;
    private String flowkey;
    private String filepath;
    /**
     * 1- 没有部署  0- 已经部署
     */
    private Integer state;
    private Date createtime;
}
