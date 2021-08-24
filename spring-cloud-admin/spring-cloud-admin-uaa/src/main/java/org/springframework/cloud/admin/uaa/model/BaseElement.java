package org.springframework.cloud.admin.uaa.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "base_element")
public class BaseElement {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 编码
     */
    private String code;

    /**
     * 类型 1:button; 2:uri
     */
    private Byte type;

    /**
     * 名称
     */
    private String name;

    /**
     * URI
     */
    private String uri;

    /**
     * 关联的菜单ID
     */
    @Column(name = "menu_id")
    private Integer menuId;

    /**
     * 请求类型
     */
    private String method;

    /**
     * 描述
     */
    private String description;

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
     * 获取编码
     *
     * @return code - 编码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置编码
     *
     * @param code 编码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取类型 1:button; 2:uri
     *
     * @return type - 类型 1:button; 2:uri
     */
    public Byte getType() {
        return type;
    }

    /**
     * 设置类型 1:button; 2:uri
     *
     * @param type 类型 1:button; 2:uri
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     * 获取名称
     *
     * @return name - 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取URI
     *
     * @return uri - URI
     */
    public String getUri() {
        return uri;
    }

    /**
     * 设置URI
     *
     * @param uri URI
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * 获取关联的菜单ID
     *
     * @return menu_id - 关联的菜单ID
     */
    public Integer getMenuId() {
        return menuId;
    }

    /**
     * 设置关联的菜单ID
     *
     * @param menuId 关联的菜单ID
     */
    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    /**
     * 获取请求类型
     *
     * @return method - 请求类型
     */
    public String getMethod() {
        return method;
    }

    /**
     * 设置请求类型
     *
     * @param method 请求类型
     */
    public void setMethod(String method) {
        this.method = method;
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