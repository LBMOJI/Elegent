package com.Insurance.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Record implements Serializable {
    /**
     * 主键 保单号
     *
     * @mbg.generated
     */
    private String id;

    /**
     * 保险名称
     *
     * @mbg.generated
     */
    private String bname;

    /**
     * 保险内容
     *
     * @mbg.generated
     */
    private String detail;

    /**
     * 办理日期
     *
     * @mbg.generated
     */
    private Date starttime;

    /**
     * 结束日期
     *
     * @mbg.generated
     */
    private Date endtime;

    /**
     * 办理人姓名
     *
     * @mbg.generated
     */
    private String person;

    /**
     * 车辆名称
     *
     * @mbg.generated
     */
    private String cname;

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
    private String type;

    /**
     * 办理人id
     *
     * @mbg.generated
     */
    private Integer uid;

    /**
     * 办理人联系电话
     *
     * @mbg.generated
     */
    private String phone;

    /**
     * 保险id
     *
     * @mbg.generated
     */
    private Integer bid;

    /**
     * 保费
     *
     * @mbg.generated
     */
    private BigDecimal cost;

    /**
     * 保额
     *
     * @mbg.generated
     */
    private BigDecimal amount;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCph() {
        return cph;
    }

    public void setCph(String cph) {
        this.cph = cph;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", bname=").append(bname);
        sb.append(", detail=").append(detail);
        sb.append(", starttime=").append(starttime);
        sb.append(", endtime=").append(endtime);
        sb.append(", person=").append(person);
        sb.append(", cname=").append(cname);
        sb.append(", cph=").append(cph);
        sb.append(", type=").append(type);
        sb.append(", uid=").append(uid);
        sb.append(", phone=").append(phone);
        sb.append(", bid=").append(bid);
        sb.append(", cost=").append(cost);
        sb.append(", amount=").append(amount);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}