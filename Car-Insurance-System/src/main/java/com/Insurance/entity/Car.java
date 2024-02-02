package com.Insurance.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class Car implements Serializable {
    /**
     * 车牌号
     *
     * @mbg.generated
     */
    private String cph;

    /**
     * 车辆类型
     *
     * @mbg.generated
     */
    private String ctype;

    /**
     * 汽车名称
     *
     * @mbg.generated
     */
    private String cname;

    /**
     * 持车人
     *
     * @mbg.generated
     */
    private Integer uid;

    /**
     * 上传审核状态
     *
     * @mbg.generated
     */
    private String status;

    /**
     * 购置价
     *
     * @mbg.generated
     */
    private BigDecimal price;

    /**
     * 汽车图片
     *
     * @mbg.generated
     */
    private String cimg;

    private User user;

    private static final long serialVersionUID = 1L;

    public String getCph() {
        return cph;
    }

    public void setCph(String cph) {
        this.cph = cph;
    }

    public String getCtype() {
        return ctype;
    }

    public void setCtype(String ctype) {
        this.ctype = ctype;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCimg() {
        return cimg;
    }

    public void setCimg(String cimg) {
        this.cimg = cimg;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", cph=").append(cph);
        sb.append(", ctype=").append(ctype);
        sb.append(", cname=").append(cname);
        sb.append(", uid=").append(uid);
        sb.append(", status=").append(status);
        sb.append(", price=").append(price);
        sb.append(", cimg=").append(cimg);
        sb.append(", user=").append(user);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}