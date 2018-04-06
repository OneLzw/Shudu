package com.type.bean;

import java.util.Date;

public class History {
    private Integer id;

    private Integer perfect;

    private String originalData;

    private String completeData;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPerfect() {
        return perfect;
    }

    public void setPerfect(Integer perfect) {
        this.perfect = perfect;
    }

    public String getOriginalData() {
        return originalData;
    }

    public void setOriginalData(String originalData) {
        this.originalData = originalData == null ? null : originalData.trim();
    }

    public String getCompleteData() {
        return completeData;
    }

    public void setCompleteData(String completeData) {
        this.completeData = completeData == null ? null : completeData.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}