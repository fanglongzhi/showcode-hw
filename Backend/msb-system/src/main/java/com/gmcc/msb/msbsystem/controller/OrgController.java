package com.gmcc.msb.msbsystem.controller;

import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.msbsystem.common.resp.Result;
import com.gmcc.msb.msbsystem.entity.org.Org;
import com.gmcc.msb.msbsystem.entity.user.User;
import com.gmcc.msb.msbsystem.service.OrgService;
import com.gmcc.msb.msbsystem.vo.resp.OrgVo;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


@RestController
public class OrgController {

    Logger logger = LoggerFactory.getLogger(OrgController.class);

    @Autowired
    private OrgService orgService;

    @GetMapping("/org/{orgid}")
    public Result findOneWithChildren(@PathVariable Integer orgid) {
        Assert.notNull(orgid, "0007-00013");
        OrgVo rootVo = null;
        if (orgid.intValue() == 0) {
            orgid = 99999;
            rootVo = new OrgVo("0");
            rootVo.setData(new Org());
        }

        OrgVo orgVo = orgService.findOneWithChildren(orgid);
        if (orgVo == null) {
            throw new MsbException("0007-10032", "未找到记录");
        }

        if (rootVo != null) {
            rootVo.setChildren(Arrays.asList(orgVo));
            orgVo.setChildren(null);
            return Result.success(rootVo);
        }

        return Result.success(orgVo);
    }

    @GetMapping("/org/{orgid}/users")
    /**
     * 查询组织架构下的用户
     */
    public Result findUsersInOrg(@PathVariable Long orgid, Pageable pageable) {
        Page<User> users = orgService.findUsersInOrg(orgid, pageable);
        return Result.success(users);
    }

    @PostMapping("/org")
    public Result add(@RequestBody Org org) {
        Assert.notNull(org, "0007-00014");
        Assert.notNull(org.getOrgcode(), "0007-00015");
        Assert.notNull(org.getOrgname(), "0007-00016");
        Assert.notNull(org.getParentorgid(), "0007-00017");

        org.setOrgid(null);

        OrgVo saved = null;
        try {
            saved = orgService.save(org);
        } catch (Exception e) {
            logger.error("新增组织机构报错 {} ", ExceptionUtils.getMessage(e));
            throw new MsbException("0007-10033", "新增出错," + e.getMessage());
        }
        return Result.success(saved);
    }

    @PutMapping("/org/{orgid}")
    public Result update(@PathVariable Integer orgid, @RequestBody Org org) {
        Assert.notNull(orgid, "0007-00018");
        Assert.notNull(org.getOrgcode(), "0007-00015");
        Assert.notNull(org.getOrgname(), "0007-00016");
        Assert.notNull(org.getParentorgid(), "0007-00017");

        OrgVo saved;
        try {
            saved = orgService.update(orgid, org);
        } catch (Exception e) {
            logger.error("修改组织机构报错 {} ", ExceptionUtils.getMessage(e));
            throw new MsbException("0007-10034", "修改出错," + e.getMessage());
        }

        return Result.success(saved);
    }


    @DeleteMapping("/org/{orgid}")
    public Result delete(@PathVariable Integer orgid) {
        try {
            this.orgService.delete(orgid);
        } catch (EmptyResultDataAccessException e) {
        } catch (Exception e) {
            logger.error("删除组织机构报错 {} ", ExceptionUtils.getMessage(e));
            throw new MsbException("0007-10035", "删除出错," + e.getMessage());
        }
        return Result.success();
    }

    @GetMapping("/orgs")
    public Result find(@RequestParam(required = false) String name, Pageable pageable) {
        List<OrgVo> result = null;
        try {
            result = orgService.find(name, pageable);
        } catch (Exception e) {
            logger.error("查询失败，{}", e.getMessage());
            throw new MsbException("0007-10036", "查询失败，" + e.getMessage());
        }
        return Result.success(result);
    }


    @PostMapping("/orgs/sync")
    public synchronized Result sync() {
        orgService.sync();
        return Result.success();
    }

}
