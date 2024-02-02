package com.Insurance.entity;

import java.io.Serializable;

public class Saler implements Serializable {
    /**
     * 主键
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * 姓名
     *
     * @mbg.generated
     */
    private String name;

    /**
     * 性别
     *
     * @mbg.generated
     */
    private String sex;

    /**
     * 登陆密码
     *
     * @mbg.generated
     */
    private String pwd;

    /**
     * 手机号
     *
     * @mbg.generated
     */
    private String phone;

    /**
     * 电子邮箱
     *
     * @mbg.generated
     */
    private String email;

    /**
     * 家庭住址
     *
     * @mbg.generated
     */
    private String address;

    /**
     * 就职状态,1为离职
     *
     * @mbg.generated
     */
    private Integer demission;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getDemission() {
        return demission;
    }

    public void setDemission(Integer demission) {
        this.demission = demission;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", sex=").append(sex);
        sb.append(", pwd=").append(pwd);
        sb.append(", phone=").append(phone);
        sb.append(", email=").append(email);
        sb.append(", address=").append(address);
        sb.append(", demission=").append(demission);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}