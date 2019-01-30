package com.gmcc.msb.msbsystem.entity.org;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

@Entity
@Table(name = "t_org")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Org {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer orgid;
    private String orgcode;
    private String orgname;
    private Integer parentorgid;
    @JsonIgnore
    private String orgaddr;

    @Transient
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Iterable<Org> children;

    public Integer getOrgid() {
        return orgid;
    }

    public void setOrgid(Integer orgid) {
        this.orgid = orgid;
    }

    public String getOrgcode() {
        return orgcode;
    }

    public void setOrgcode(String orgcode) {
        this.orgcode = orgcode;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    public Integer getParentorgid() {
        return parentorgid;
    }

    public void setParentorgid(Integer parentorgid) {
        this.parentorgid = parentorgid;
    }

    public String getOrgaddr() {
        return orgaddr;
    }

    public void setOrgaddr(String orgaddr) {
        this.orgaddr = orgaddr;
    }

    public Iterable<Org> getChildren() {
        return children;
    }

    public void setChildren(Iterable<Org> children) {
        this.children = children;
    }
}
