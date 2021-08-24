package org.springframework.cloud.admin.uaa.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "base_resource_authority")
public class BaseResourceAuthority {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 角色ID
     */
    @Column(name = "role_id")
    private Integer roleId;

    /**
     * 资源ID
     */
    @Column(name = "resource_id")
    private Integer resourceId;

    /**
     * 资源类型 1:menu; 2:element
     */
    @Column(name = "resource_type")
    private Byte resourceType;

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
     * 获取角色ID
     *
     * @return role_id - 角色ID
     */
    public Integer getRoleId() {
        return roleId;
    }

    /**
     * 设置角色ID
     *
     * @param roleId 角色ID
     */
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    /**
     * 获取资源ID
     *
     * @return resource_id - 资源ID
     */
    public Integer getResourceId() {
        return resourceId;
    }

    /**
     * 设置资源ID
     *
     * @param resourceId 资源ID
     */
    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * 获取资源类型 1:menu; 2:element
     *
     * @return resource_type - 资源类型 1:menu; 2:element
     */
    public Byte getResourceType() {
        return resourceType;
    }

    /**
     * 设置资源类型 1:menu; 2:element
     *
     * @param resourceType 资源类型 1:menu; 2:element
     */
    public void setResourceType(Byte resourceType) {
        this.resourceType = resourceType;
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
}