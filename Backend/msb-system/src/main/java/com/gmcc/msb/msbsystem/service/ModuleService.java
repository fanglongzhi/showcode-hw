package com.gmcc.msb.msbsystem.service;

import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.common.property.UserContextHolder;
import com.gmcc.msb.msbsystem.entity.module.Module;
import com.gmcc.msb.msbsystem.entity.user.User;
import com.gmcc.msb.msbsystem.repository.ApiRepository;
import com.gmcc.msb.msbsystem.repository.module.ModuleRepository;
import com.gmcc.msb.msbsystem.repository.role.RoleAuthRepository;
import com.gmcc.msb.msbsystem.vo.req.ModuleVo;
import com.gmcc.msb.msbsystem.vo.resp.ModuleRespVo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gmcc.msb.msbsystem.common.CommonConstant.*;

/**
 * @program: msb-system
 * @description: 模块业务逻辑类
 * @author: zhifanglong
 * @create: 2018-10-11 10:48
 */
@Service
public class ModuleService {
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private RoleAuthRepository roleAuthRepository;
    @Autowired
    private ApiRepository apiRepository;

    @Transactional(rollbackFor = Exception.class)
    public Module createModule(ModuleVo moduleVo) {
        if (moduleRepository.countByKey(moduleVo.getKey()) != 0) {
            throw new MsbException(ERROR_MODULE_KEY_DUPLICATE);
        }
        if (moduleRepository.countByModuleName(moduleVo.getModuleName()) != 0) {
            throw new MsbException(ERROR_MODULE_NAME_DUPLICATE);
        }
        Module module = new Module(moduleVo);
        module.setCreateTime(new Date());
        module.setUpdateTime(module.getCreateTime());

        Module perModule = moduleRepository.save(module);

        module.setId(perModule.getId());

        return module;

    }

    @Transactional(rollbackFor = Exception.class)
    public Module deleteModule(Long id) {
        Module module = moduleRepository.findOne(id);
        if (module == null) {
            throw new MsbException(ERROR_MODULE_NOT_EXIST);
        }
        if (roleAuthRepository.countByAuthId(id) > 0) {
            throw new MsbException(ERROR_MODULE_HAS_ROLE);
        }

        moduleRepository.delete(id);

        return module;
    }

    public List<Module> findAllModule() {
        List<Module> moduleList = Lists.newArrayList(
                moduleRepository.findAll(new Sort(Sort.Direction.ASC, "id"))
        );

        moduleList.stream().filter(module -> module.getApis() != null
                && !(module.getApis().isEmpty()))
                .forEach(module -> {
                    String apis = module.getApis();
                    List<Integer> idList = Lists.newArrayList();
                    String[] apiArray = apis.split(",");
                    for (String id : apiArray) {
                        idList.add(Integer.valueOf(id));
                    }
                    module.setApiList(apiRepository.findByIdIn(idList));
                });

        return moduleList;
    }

    @Transactional(rollbackFor = Exception.class)
    public Module modifyModule(ModuleVo moduleVo) {
        Module module = moduleRepository.findOne(moduleVo.getId());
        if (module == null) {
            throw new MsbException(ERROR_MODULE_NOT_EXIST);
        }
        if (moduleRepository.countByKey(moduleVo.getKey(), moduleVo.getId()) != 0) {
            throw new MsbException(ERROR_MODULE_KEY_DUPLICATE);
        }
        if (moduleRepository.countByModuleName(moduleVo.getModuleName(), moduleVo.getId()) != 0) {
            throw new MsbException(ERROR_MODULE_NAME_DUPLICATE);
        }
        module.setUpdateTime(new Date());

        module.setKey(moduleVo.getKey());
        module.setApis(moduleVo.buildApiList());
        module.setModuleName(moduleVo.getModuleName());

        moduleRepository.save(module);

        return module;

    }

    @Transactional(rollbackFor = Exception.class)
    public Module modifyModuleStatus(Long id, String status) {
        Module module = moduleRepository.findOne(id);
        if (module == null) {
            throw new MsbException(ERROR_MODULE_NOT_EXIST);
        }
        module.setUpdateTime(new Date());
        module.setStatus(status);
        moduleRepository.updateModuleStatus(id,status,new Date(), UserContextHolder.getContext().getUserId());

        return module;

    }
    @Transactional(rollbackFor = Exception.class)
    public void modifyModuleStatusBatch(List<Long> ids,String status) {
        Iterable<Module> moduleList = moduleRepository.findAll(ids);
        if (moduleList == null) {
            throw new MsbException(ERROR_MODULE_NOT_EXIST);
        }
        List<Module> modules = Lists.newArrayList(moduleList);
        for(Module module:modules){
            module.setStatus(status);
            module.setUpdateBy(UserContextHolder.getContext().getUserId());
        }

        moduleRepository.save(modules);

    }

    public List<User> findUserListInModule(Long moduleId) {
        return moduleRepository.findUserByModuleId(moduleId);
    }

    public ModuleRespVo findAllModuleTree() {
        List<Module> mlist = findAllModule();
        Map<Long, ModuleRespVo> treeMap = new HashMap<>();
        ModuleRespVo root = new ModuleRespVo();
        root.setId(0L);
        root.setLabel("root");

        treeMap.put(root.getId(), root);
        for (Module m : mlist) {
            ModuleRespVo respVo = new ModuleRespVo();
            respVo.setLabel(m.getModuleName());
            respVo.setData(m);
            respVo.setId(m.getId());
            treeMap.put(m.getId(), respVo);
        }

        for (Module m : mlist) {
            ModuleRespVo ch = treeMap.get(m.getId());
            ModuleRespVo vo = treeMap.get(m.getParentId());
            if (vo != null) {
                vo.getChildren().add(ch);
                vo.setLeaf(false);
            }
        }

        return root;

    }


}
