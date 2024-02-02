package com.Insurance.entity;

import java.io.Serializable;
import java.util.Date;

public class Baoxian implements Serializable {
    /**
     * 主键
     *
     * @mbg.generated
     */
    private Integer id;

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
     * 发行日期
     *
     * @mbg.generated
     */
    private Date releasedate;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Date getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(Date releasedate) {
        this.releasedate = releasedate;
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
        sb.append(", releasedate=").append(releasedate);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}