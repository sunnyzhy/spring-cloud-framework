package org.springframework.cloud.admin.uaa.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "base_data_group")
public class BaseDataGroup {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 序号
     */
    private Integer sort;

    /**
     * 名称
     */
    private String name;

    /**
     * 父级节点ID
     */
    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 分组类型
     */
    private String type;

    /**
     * 图标
     */
    private String icon;

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
     * 获取序号
     *
     * @return sort - 序号
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置序号
     *
     * @param sort 序号
     */
    public void setSort(Integer sort) {
        this.sort = sort;
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
     * 获取父级节点ID
     *
     * @return parent_id - 父级节点ID
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * 设置父级节点ID
     *
     * @param parentId 父级节点ID
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取分组类型
     *
     * @return type - 分组类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置分组类型
     *
     * @param type 分组类型
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取图标
     *
     * @return icon - 图标
     */
    public String getIcon() {
        return icon;
    }

    /**
     * 设置图标
     *
     * @param icon 图标
     */
    public void setIcon(String icon) {
        this.icon = icon;
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