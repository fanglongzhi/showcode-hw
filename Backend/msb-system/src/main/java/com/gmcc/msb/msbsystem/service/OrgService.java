package com.gmcc.msb.msbsystem.service;

import com.gmcc.msb.msbsystem.entity.org.Org;
import com.gmcc.msb.msbsystem.entity.user.User;
import com.gmcc.msb.msbsystem.property.AppProperties;
import com.gmcc.msb.msbsystem.repository.org.OrgRepository;
import com.gmcc.msb.msbsystem.repository.user.UserRepository;
import com.gmcc.msb.msbsystem.vo.resp.OrgVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class OrgService {

    @Autowired
    private OrgRepository orgRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EOMSService eomsService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AppProperties appProperties;


    /**
     * 查询机构和子机构
     *
     * @param orgId
     * @return
     */
    public OrgVo findOneWithChildren(Integer orgId) {
        Assert.notNull(orgId, "0007-00018");
        Org org = orgRepository.findOne(orgId);
        OrgVo orgVo = null;
        if (org != null) {
            orgVo = orgToVo(org);

            Iterable<Org> children = orgRepository.findAllByParentorgid(orgId);

            List<OrgVo> childrenOrgvo = new ArrayList<>();
            if (children != null) {
                for (Org org1 : children) {
                    OrgVo child = orgToVo(org1);
                    if(orgRepository.countByParentorgid(org1.getOrgid())==0){
                        child.setLeaf(true);
                    }
                    childrenOrgvo.add(child);
                }
            }

            if(childrenOrgvo.size()==0){
                orgVo.setLeaf(true);
            }

            orgVo.setChildren(childrenOrgvo);
        }


        return orgVo;
    }

    public List<OrgVo> find(String name, Pageable pageable) throws Exception {

        Page<Org> page = null;
        if (StringUtils.isEmpty(name)) {
            page = orgRepository.findAll(pageable);
        } else {
            page = orgRepository.findAllByOrgnameContaining(name, pageable);
        }

        if (page.getContent() == null || page.getContent().size() == 0) {
            throw new Exception("0007-10037");
        }

        List<Org> all = new ArrayList<>();
        all.addAll(page.getContent());

        Iterable<Org> children = page.getContent();

        int loopCount = 0;
        while (children != null) {
            // 查询所有父级节点
            Set<Integer> ids = new HashSet<>();
            for (Org org : children) {
                if (org.getParentorgid() != null) {
                    ids.add(org.getParentorgid());
                }
            }

            if (ids.size() > 0) {
                Iterable<Org> parents = orgRepository.findAllByOrgidIn(ids);

                if (parents != null) {
                    for (Org o : parents) {
                        if (!all.contains(o)) {
                            all.add(o);
                        }
                    }
                    children = parents;
                } else {
                    children = null;
                }
            } else {
                children = null;
            }

            loopCount++;
            if (loopCount > 20) {
                //查询上一级，超过20次，一般不会有这么多层，可能数据出错，形成回路，
                // 跳出
                throw new Exception("0007-10038");
            }
        }


        // 以树形方式展现
        Set<Org> roots = new HashSet<>();
        for (Org org : all) {
            if (StringUtils.isEmpty(org.getParentorgid())) {
                roots.add(org);
            }
        }
        if (roots.isEmpty()) {
            throw new NullPointerException("0007-10039");
        }

        List<OrgVo> rootVos = new ArrayList<>();
        for (Org org : roots) {
            OrgVo rootVo = orgToVo(org);
            rootVo.setExpanded(true);
            this.setChildren(rootVo, all);
            rootVos.add(rootVo);
        }

        return rootVos;
    }

    public OrgVo orgToVo(Org org) {
        OrgVo orgVo = new OrgVo();
        orgVo.setLabel(org.getOrgname());
        orgVo.setData(org);
        return orgVo;
    }


    private void setChildren(OrgVo root, List<Org> all) {
        for (Org org : all) {
            if (org.getParentorgid() != null &&
                        root.getData().getOrgid() != null &&
                        org.getParentorgid().equals(root.getData().getOrgid())) {
                List<OrgVo> children = root.getChildren();
                if (children == null) {
                    children = new ArrayList<>();
                    root.setChildren(children);
                }
                OrgVo vo = orgToVo(org);
                vo.setExpanded(true);
                children.add(vo);
                setChildren(vo, all);
            }
        }
    }

    public OrgVo save(Org org) {
        Org parent = orgRepository.findOne(org.getParentorgid());
        if (parent == null) {
            throw new NullPointerException("0007-10040");
        }
        Org s = orgRepository.save(org);
        return this.orgToVo(s);
    }

    public OrgVo update(Integer orgid, Org org) {
        Org find = orgRepository.findOne(orgid);
        if (find == null) {
            throw new NullPointerException("0007-10041");
        }


        if (!StringUtils.isEmpty(org.getOrgname())) {
            find.setOrgname(org.getOrgname());
        }
        if (!StringUtils.isEmpty(org.getOrgcode())) {
            find.setOrgcode(org.getOrgcode());
        }
        if (!StringUtils.isEmpty(org.getParentorgid())) {
            find.setParentorgid(org.getParentorgid());
        }

        return this.save(find);
    }

    public void delete(Integer orgid) {
        this.orgRepository.delete(orgid);
    }


    public Page<User> findUsersInOrg(Long orgid, Pageable pageable) {
        Assert.notNull(orgid, "0007-10042");
        return userRepository.findAllByOrgId(orgid, pageable);
    }

    @Transactional
    public void saveAllFromSync(List<Org> all) {
        this.jdbcTemplate.update("DELETE FROM t_org");
        List<Object[]> batchArgs = new ArrayList<>();
        for (Org org : all) {
            batchArgs.add(new Object[]{
                    org.getOrgid(),
                    org.getOrgcode(),
                    org.getOrgname(),
                    org.getParentorgid(),
                    org.getOrgaddr()});
        }
        this.jdbcTemplate.batchUpdate(
                "INSERT INTO t_org(ORGID, ORGCODE, ORGNAME, PARENTORGID, ORGADDR) VALUE (?, ?, ?, ?, ?)",
                batchArgs);
    }

    public void sync() {
        String fields = appProperties.getReqEomsFields();
        this.eomsService.sync(fields);
    }
}
