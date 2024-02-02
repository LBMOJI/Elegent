package com.Insurance.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Acception implements Serializable {
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
     * 办理人
     *
     * @mbg.generated
     */
    private String person;

    /**
     * 联系电话
     *
     * @mbg.generated
     */
    private String phone;

    /**
     * 0表示拒绝理赔，1表示审核中，2表示理赔成功
     *
     * @mbg.generated
     */
    private String status;

    /**
     * 业务员
     *
     * @mbg.generated
     */
    private String sname;

    /**
     * 业务员联系电话
     *
     * @mbg.generated
     */
    private String sphone;

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
     * 车辆图片
     *
     * @mbg.generated
     */
    private String img;

    /**
     * 业务员id
     *
     * @mbg.generated
     */
    private Integer sid;

    /**
     * 办理人id
     *
     * @mbg.generated
     */
    private Integer uid;

    /**
     * 赔款
     *
     * @mbg.generated
     */
    private BigDecimal payout;

    /**
     * 申请时间
     *
     * @mbg.generated
     */
    private Date applytime;

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

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSphone() {
        return sphone;
    }

    public void setSphone(String sphone) {
        this.sphone = sphone;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public BigDecimal getPayout() {
        return payout;
    }

    public void setPayout(BigDecimal payout) {
        this.payout = payout;
    }

    public Date getApplytime() {
        return applytime;
    }

    public void setApplytime(Date applytime) {
        this.applytime = applytime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", bname=").append(bname);
        sb.append(", person=").append(person);
        sb.append(", phone=").append(phone);
        sb.append(", status=").append(status);
        sb.append(", sname=").append(sname);
        sb.append(", sphone=").append(sphone);
        sb.append(", cname=").append(cname);
        sb.append(", cph=").append(cph);
        sb.append(", type=").append(type);
        sb.append(", img=").append(img);
        sb.append(", sid=").append(sid);
        sb.append(", uid=").append(uid);
        sb.append(", payout=").append(payout);
        sb.append(", applytime=").append(applytime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}