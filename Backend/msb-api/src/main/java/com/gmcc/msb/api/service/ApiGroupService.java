package com.gmcc.msb.api.service;

import com.gmcc.msb.api.entity.ApiGroup;
import com.gmcc.msb.api.entity.ApiGroupItem;
import com.gmcc.msb.api.repository.ApiGroupRepository;
import com.gmcc.msb.api.vo.request.ApiGroupRequest;
import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.common.property.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yuan Chunhai
 */
@Service
public class ApiGroupService {

    @Autowired
    private ApiGroupRepository apiGroupRepository;

    @Autowired
    private ApiGroupItemService apiGroupItemService;

    @Autowired
    private AppOrderApiService appOrderApiService;

    @Transactional
    public ApiGroup add(ApiGroupRequest request) {

        List<ApiGroup> find = apiGroupRepository.findAllByNameEquals(request.getName());
        if (find != null && !find.isEmpty()) {
            throw new MsbException("0001-10132");
        }

        ApiGroup apiGroup = new ApiGroup();
        apiGroup.setName(request.getName());
        ApiGroup saved = apiGroupRepository.save(apiGroup);

        List<Integer> items = request.getApis();
        if (items != null) {
            for (Integer itemId : items) {
                if (itemId != null) {
                    apiGroupItemService.save(new ApiGroupItem(saved.getId(), itemId));
                }
            }
        }
        return saved;
    }

    @Transactional
    public void update(Integer groupId, ApiGroupRequest vo) {
        ApiGroup saved = this.updateName(groupId, vo.getName());
        this.apiGroupItemService.delList(groupId);
        this.apiGroupItemService.saveList(saved.getId(), vo.getApis());
    }

    public ApiGroup updateName(Integer groupId, String name) {
        ApiGroup findOne = this.findOne(groupId);
        if (findOne == null) {
            throw new MsbException("0001-10014");
        }
        findOne.setName(name);

        List<ApiGroup> find = apiGroupRepository.findAllByNameEquals(name);
        if (find != null && !find.isEmpty()) {
            for (ApiGroup apiGroup : find) {
                if (!apiGroup.getId().equals(groupId)){
                    throw new MsbException("0001-10133");
                }
            }
        }

        return this.apiGroupRepository.save(findOne);
    }

    public ApiGroup findOne(Integer id) {
        Assert.notNull(id, "0001-00020");
        return this.apiGroupRepository.findOne(id);
    }


    public void delete(Integer groupId) {
        ApiGroup findOne = this.findOne(groupId);
        if (findOne == null) {
            throw new MsbException("0001-10134");
        }

        if (this.apiGroupItemService.groupHasItems(groupId)) {
            throw new MsbException("0001-10135");
        }

        this.apiGroupRepository.delete(groupId);
    }

    public Page<ApiGroup> find(Pageable pageable) {
        List<Long> orgList = UserContextHolder.getContext().getDataOrgList();
        if(orgList.size()==0){

            return new PageImpl<ApiGroup>(new ArrayList<>(),pageable,0);
        }
        return this.apiGroupRepository.findAllByConditions(orgList,pageable);
    }

    public Page<ApiGroup> findAll(Pageable pageable) {

        return this.apiGroupRepository.findAll(pageable);
    }


}
