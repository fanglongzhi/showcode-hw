package com.gmcc.msb.msbsystem.service;

import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.common.property.UserContextHolder;
import com.gmcc.msb.msbsystem.entity.module.Module;
import com.gmcc.msb.msbsystem.entity.org.DataOrg;
import com.gmcc.msb.msbsystem.entity.org.DataOrgUser;
import com.gmcc.msb.msbsystem.entity.user.User;
import com.gmcc.msb.msbsystem.repository.org.DataOrgRepository;
import com.gmcc.msb.msbsystem.repository.org.DataOrgUserRepository;
import com.gmcc.msb.msbsystem.vo.req.org.CreateDataOrgParam;
import com.gmcc.msb.msbsystem.vo.req.org.CreateDataOrgUserParam;
import com.gmcc.msb.msbsystem.vo.req.org.ModifyDataOrgParam;
import com.gmcc.msb.msbsystem.vo.resp.DataOrgRespVo;
import com.gmcc.msb.msbsystem.vo.resp.ModuleRespVo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @program: msb-system
 * @description: 数据组管理处理类
 * @author: zhifanglong
 * @create: 2018-10-24 15:49
 */
@Service
public class DataOrgService {
    @Autowired
    private DataOrgRepository dataOrgRepository;
    @Autowired
    private DataOrgUserRepository dataOrgUserRepository;

    private static final String ORG_NAME_EXIST = "0007-10001";

    private static final String PARENT_ORG_NOT_EXIST = "0007-10002";

    private static final String ORG_NOT_EXIST = "0007-10003";

    private static final String ORG_HAS_USER = "0007-10004";

    private static final String MISSING_USER_LIST = "0007-00011";

    private static final String WRONG_MAIN_FLAG = "0007-10005";

    private static final String ORG_HAS_CHILDREN = "0007-10006";

    private static final String WRONG_ORG_QUERY_FLAG = "0007-10007";

    private static final String ORG_HAS_APP = "0007-10008";

    private static final String ORG_HAS_SERVICE = "0007-10009";


    private Set<String> mainFlagMap = Sets.newHashSet("1", "0");

    private static final String ORG_MAIN_Y="1";

    private static final String ORG_MAIN_N = "0";

    private static final String ORG_MAIN_ALL="2";

    private static final String ORG_MAIN_Y_O="3";


    public List<DataOrgUser> findUserMainDataOrg(String userId) {
        List<DataOrgUser> users = dataOrgUserRepository.findByUserIdAndMainFlag(userId, ORG_MAIN_Y);
        return users;
    }

    public List<Long> findOrgIdsOfUser(String userId) {
        return dataOrgUserRepository.findByUserId(userId);
    }

    public List<Long> findOrgChildrenList(List<Long> parent) {
        String regexp = "";
        for (Object id : parent) {
            regexp += "(," + id + ",)|(^"+id+",)|";
        }
        regexp = regexp.substring(0, regexp.lastIndexOf("|"));

        List<Long> result =  dataOrgRepository.findAllOrgIdChildren(regexp);

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public DataOrg createDataOrg(CreateDataOrgParam param) {
        if (dataOrgRepository.countByOrgName(param.getOrgName()) > 0) {
            throw new MsbException(ORG_NAME_EXIST, "数据组已经存在");
        }

        DataOrg parent = dataOrgRepository.findOne(param.getParent());


        if (parent == null) {
            throw new MsbException(PARENT_ORG_NOT_EXIST, "父组不存在");
        }

        DataOrg dataOrg = new DataOrg();
        dataOrg.setOrgName(param.getOrgName());
        dataOrg.setParent(param.getParent());
        dataOrg.setParentList(parent.getParentList() + parent.getId() + ",");

        return dataOrgRepository.save(dataOrg);
    }

    @Transactional(rollbackFor = Exception.class)
    public DataOrg modifyDataOrg(Integer id, ModifyDataOrgParam param) {
        DataOrg dataOrg = dataOrgRepository.findOne(id);
        if (dataOrg == null) {
            throw new MsbException(ORG_NOT_EXIST, "组不存在");
        }
        if (dataOrgRepository.countByOrgName(param.getOrgName(), id) > 0) {
            throw new MsbException(ORG_NAME_EXIST, "数据组已经存在");
        }

        dataOrg.setOrgName(param.getOrgName());

        return dataOrgRepository.save(dataOrg);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteDataOrg(Integer id) {
        if(dataOrgRepository.findOne(id)==null){
            throw new MsbException(ORG_NOT_EXIST);
        }
        if (dataOrgUserRepository.countByOrgId(id) > 0) {
            throw new MsbException(ORG_HAS_USER, "数据组中含有用户，不能删除");
        }
        String regex = "(," + id + ",)|(^"+id+",)";
        List<DataOrg> children = dataOrgRepository.findChildren(regex);

        if(children!=null&&children.size()>0){
            throw new MsbException(ORG_HAS_CHILDREN, "数据组中含有子组，不能删除");
        }

        if(dataOrgRepository.countAppInDataOrg(id)>0){
            throw new MsbException(ORG_HAS_APP,"数据组中含有应用，不能删除");
        }

        if(dataOrgRepository.countServiceInDataOrg(id)>0){
            throw new MsbException(ORG_HAS_SERVICE,"数据组中含有服务，不能删除");
        }


        dataOrgRepository.delete(id);
    }

    public DataOrgRespVo findDataOrgTree() {
        Long orgId = UserContextHolder.getContext().getOrgId();
        if (orgId == null) {
            return null;
        }
        DataOrg parent = dataOrgRepository.findOne(orgId.intValue());
        if (parent == null) {
            throw new MsbException(ORG_NOT_EXIST, "组不存在");
        }

        String regex = parent.getId() + ",";
        List<DataOrg> children = dataOrgRepository.findChildren(regex);

        DataOrgRespVo root = new DataOrgRespVo();
        root.setData(parent);
        root.setId(parent.getId());
        root.setLabel(parent.getOrgName());

        Map<Integer, DataOrgRespVo> treeMap = new HashMap<>();
        treeMap.put(root.getId(), root);

        for (DataOrg m : children) {
            DataOrgRespVo respVo = new DataOrgRespVo();
            respVo.setLabel(m.getOrgName());
            respVo.setData(m);
            respVo.setId(m.getId());
            treeMap.put(m.getId(), respVo);
        }

        for (DataOrg m : children) {
            DataOrgRespVo ch = treeMap.get(m.getId());
            DataOrgRespVo vo = treeMap.get(m.getParent());
            if (vo != null) {
                vo.getChildren().add(ch);
                vo.setLeaf(false);
            }
        }

        return root;
    }
    @Transactional(rollbackFor = Exception.class)
    public void createDataOrgUser(CreateDataOrgUserParam param) {
        if (param.getUserIdList() == null || param.getUserIdList().size() == 0) {
            throw new MsbException(MISSING_USER_LIST, "用户列表不能为空");
        }
        if (!mainFlagMap.contains(param.getMainFlag())) {
            throw new MsbException(WRONG_MAIN_FLAG, "主组标志不正确");
        }

        if (dataOrgRepository.findOne(param.getOrgId()) == null) {
            throw new MsbException(ORG_NOT_EXIST, "组不存在");
        }


        //1.添加主组
         if(ORG_MAIN_Y.equals(param.getMainFlag())){
            addMainDataOrgForUser(param);
         }else{
             //2.添加可见组
           addDataOrgForUser(param);
         }

    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteDataOrgUser(CreateDataOrgUserParam param) {
        if (param.getUserIdList() == null || param.getUserIdList().size() == 0) {
            throw new MsbException(MISSING_USER_LIST, "用户列表不能为空");
        }
        if (!mainFlagMap.contains(param.getMainFlag())) {
            throw new MsbException(WRONG_MAIN_FLAG, "主组标志不正确");
        }

        if (dataOrgRepository.findOne(param.getOrgId()) == null) {
            throw new MsbException(ORG_NOT_EXIST, "组不存在");
        }

        List<String> userIdList = param.getUserIdList();
        Integer orgId = param.getOrgId();

        if(ORG_MAIN_Y.equals(param.getMainFlag())){
            dataOrgUserRepository.deleteByUserId(userIdList,ORG_MAIN_Y);
        }else{
            dataOrgUserRepository.deleteByUserId(userIdList,ORG_MAIN_N);
        }

    }

    public List<User> findUserList(Integer orgId,String type){
        if(ORG_MAIN_Y.equals(type)||ORG_MAIN_N.equals(type)) {
            return dataOrgUserRepository.findUserList(orgId, type);
        }else if(ORG_MAIN_ALL.equals(type)){
            return dataOrgUserRepository.findUserListMain();
        }else if(ORG_MAIN_Y_O.equals(type)){
            Long mainOrg = UserContextHolder.getContext().getOrgId();
            List<Long> orgList =  findOrgChildrenList(Lists.newArrayList(mainOrg));
            List<Integer> orgIdList = Lists.newArrayList();
            orgIdList.add(mainOrg.intValue());

            for(Object id:orgList){
                orgIdList.add(Integer.valueOf(id.toString()));
            }
            return dataOrgUserRepository.findChildrenUserListMain(
                   orgIdList);
        }else{
            throw new MsbException(WRONG_ORG_QUERY_FLAG);
        }
    }

    private void addMainDataOrgForUser(CreateDataOrgUserParam param) {
        List<String> userIdList = param.getUserIdList();

        dataOrgUserRepository.deleteByUserId(userIdList,ORG_MAIN_Y);

        List<DataOrgUser> dataOrgUserList = Lists.newArrayList();

        Date currDate = new Date();
        for(String userId:userIdList){
            DataOrgUser du = new DataOrgUser();
            du.setOrgId(param.getOrgId());
            du.setMainFlag(ORG_MAIN_Y);
            du.setUserId(userId);
            du.setCreateTime(currDate);

            dataOrgUserList.add(du);
        }

        dataOrgUserRepository.save(dataOrgUserList);
    }

    private void addDataOrgForUser(CreateDataOrgUserParam param){
        List<String> userIdList = param.getUserIdList();
        List<String> hasUserIdList = dataOrgUserRepository.findUserIdInOrg(param.getOrgId());

        List<String> noExistUserIdList = Lists.newArrayList();
        if(hasUserIdList!=null&&hasUserIdList.size()>0) {
            for (String userId : userIdList) {
                if (!hasUserIdList.contains(userId)) {
                    noExistUserIdList.add(userId);
                }
            }
        }else{
            noExistUserIdList.addAll(userIdList);
        }

        List<DataOrgUser> dataOrgUserList = Lists.newArrayList();

        Date currDate = new Date();
        for(String userId:noExistUserIdList){
            DataOrgUser du = new DataOrgUser();
            du.setOrgId(param.getOrgId());
            du.setMainFlag(ORG_MAIN_N);
            du.setUserId(userId);
            du.setCreateTime(currDate);

            dataOrgUserList.add(du);
        }

        dataOrgUserRepository.save(dataOrgUserList);
    }

}
