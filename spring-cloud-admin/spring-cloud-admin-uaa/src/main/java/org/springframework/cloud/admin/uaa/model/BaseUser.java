package org.springframework.cloud.admin.uaa.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "base_user")
public class BaseUser {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 姓名
     */
    private String name;

    /**
     * 出生日期
     */
    private Date birthday;

    /**
     * 地址
     */
    private String address;

    /**
     * 电话号码
     */
    @Column(name = "tel_phone")
    private String telPhone;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 性别 1:男; 2:女
     */
    private Boolean sex;

    /**
     * 用户类型 
1:超级管理员; 2:租户管理员; 3:普通管理员
     */
    private Integer type;

    /**
     * 状态 1:正常；2:注销
     */
    private Integer status;

    /**
     * 描述
     */
    private String description;

    /**
     * 租户ID
     */
    @Column(name = "tenant_id")
    private String tenantId;

    /**
     * 记录创建的时间
     */
    @Column(name = "crt_time")
    private Date crtTime;

    /**
     * 记录创建者的用户名
     */
    @Column(name = "crt_user")
    private String crtUser;

    /**
     * 记录创建者的姓名
     */
    @Column(name = "crt_name")
    private String crtName;

    /**
     * 记录创建者的主机
     */
    @Column(name = "crt_host")
    private String crtHost;

    /**
     * 记录修改的时间
     */
    @Column(name = "upd_time")
    private Date updTime;

    /**
     * 记录修改者的用户名
     */
    @Column(name = "upd_user")
    private String updUser;

    /**
     * 记录修改者的姓名
     */
    @Column(name = "upd_name")
    private String updName;

    /**
     * 记录修改者的主机
     */
    @Column(name = "upd_host")
    private String updHost;

    /**
     * 获取ID
     *
     * @return id - ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置ID
     *
     * @param id ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取用户名
     *
     * @return user_name - 用户名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置用户名
     *
     * @param userName 用户名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取密码
     *
     * @return password - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取姓名
     *
     * @return name - 姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置姓名
     *
     * @param name 姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取出生日期
     *
     * @return birthday - 出生日期
     */
    public Date getBirthday() {
        return birthday;
    }

    /**
     * 设置出生日期
     *
     * @param birthday 出生日期
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /**
     * 获取地址
     *
     * @return address - 地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置地址
     *
     * @param address 地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取电话号码
     *
     * @return tel_phone - 电话号码
     */
    public String getTelPhone() {
        return telPhone;
    }

    /**
     * 设置电话号码
     *
     * @param telPhone 电话号码
     */
    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    /**
     * 获取电子邮箱
     *
     * @return email - 电子邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置电子邮箱
     *
     * @param email 电子邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取性别 1:男; 2:女
     *
     * @return sex - 性别 1:男; 2:女
     */
    public Boolean getSex() {
        return sex;
    }

    /**
     * 设置性别 1:男; 2:女
     *
     * @param sex 性别 1:男; 2:女
     */
    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    /**
     * 获取用户类型 
1:超级管理员; 2:租户管理员; 3:普通管理员
     *
     * @return type - 用户类型 
1:超级管理员; 2:租户管理员; 3:普通管理员
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置用户类型 
1:超级管理员; 2:租户管理员; 3:普通管理员
     *
     * @param type 用户类型 
1:超级管理员; 2:租户管理员; 3:普通管理员
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取状态 1:正常；2:注销
     *
     * @return status - 状态 1:正常；2:注销
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态 1:正常；2:注销
     *
     * @param status 状态 1:正常；2:注销
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取描述
     *
     * @return description - 描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置描述
     *
     * @param description 描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取租户ID
     *
     * @return tenant_id - 租户ID
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * 设置租户ID
     *
     * @param tenantId 租户ID
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * 获取记录创建的时间
     *
     * @return crt_time - 记录创建的时间
     */
    public Date getCrtTime() {
        return crtTime;
    }

    /**
     * 设置记录创建的时间
     *
     * @param crtTime 记录创建的时间
     */
    public void setCrtTime(Date crtTime) {
        this.crtTime = crtTime;
    }

    /**
     * 获取记录创建者的用户名
     *
     * @return crt_user - 记录创建者的用户名
     */
    public String getCrtUser() {
        return crtUser;
    }

    /**
     * 设置记录创建者的用户名
     *
     * @param crtUser 记录创建者的用户名
     */
    public void setCrtUser(String crtUser) {
        this.crtUser = crtUser;
    }

    /**
     * 获取记录创建者的姓名
     *
     * @return crt_name - 记录创建者的姓名
     */
    public String getCrtName() {
        return crtName;
    }

    /**
     * 设置记录创建者的姓名
     *
     * @param crtName 记录创建者的姓名
     */
    public void setCrtName(String crtName) {
        this.crtName = crtName;
    }

    /**
     * 获取记录创建者的主机
     *
     * @return crt_host - 记录创建者的主机
     */
    public String getCrtHost() {
        return crtHost;
    }

    /**
     * 设置记录创建者的主机
     *
     * @param crtHost 记录创建者的主机
     */
    public void setCrtHost(String crtHost) {
        this.crtHost = crtHost;
    }

    /**
     * 获取记录修改的时间
     *
     * @return upd_time - 记录修改的时间
     */
    public Date getUpdTime() {
        return updTime;
    }

    /**
     * 设置记录修改的时间
     *
     * @param updTime 记录修改的时间
     */
    public void setUpdTime(Date updTime) {
        this.updTime = updTime;
    }

    /**
     * 获取记录修改者的用户名
     *
     * @return upd_user - 记录修改者的用户名
     */
    public String getUpdUser() {
        return updUser;
    }

    /**
     * 设置记录修改者的用户名
     *
     * @param updUser 记录修改者的用户名
     */
    public void setUpdUser(String updUser) {
        this.updUser = updUser;
    }

    /**
     * 获取记录修改者的姓名
     *
     * @return upd_name - 记录修改者的姓名
     */
    public String getUpdName() {
        return updName;
    }

    /**
     * 设置记录修改者的姓名
     *
     * @param updName 记录修改者的姓名
     */
    public void setUpdName(String updName) {
        this.updName = updName;
    }

    /**
     * 获取记录修改者的主机
     *
     * @return upd_host - 记录修改者的主机
     */
    public String getUpdHost() {
        return updHost;
    }

    /**
     * 设置记录修改者的主机
     *
     * @param updHost 记录修改者的主机
     */
    public void setUpdHost(String updHost) {
        this.updHost = updHost;
    }
}