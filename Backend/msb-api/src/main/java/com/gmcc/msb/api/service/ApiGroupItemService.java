package com.gmcc.msb.api.service;

import com.gmcc.msb.api.entity.ApiGroupItem;
import com.gmcc.msb.api.repository.ApiGroupItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ApiGroupItemService {

    @Autowired
    private ApiGroupItemRepository repository;


    public boolean isApiInGroup(Integer api) {
        Assert.notNull(api, "0001-00018");
        return this.repository.countAllByApiIdEquals(api) > 0;
    }

    public Iterable<ApiGroupItem> groupItems(Integer groupId) {
        checkGroupId(groupId);
        return this.repository.findAllByGroupIdEquals(groupId);
    }

    public boolean groupHasItems(Integer groupId) {
        checkGroupId(groupId);
        return this.repository.countAllByGroupIdEquals(groupId) > 0 ;
    }

    public ApiGroupItem save(ApiGroupItem item) {
        return this.repository.save(item);
    }

    public Iterable<ApiGroupItem> saveList(Iterable<ApiGroupItem> list) {
        return this.repository.save(list);
    }

    public Iterable<ApiGroupItem> saveList(Integer groupId, Iterable<Integer> itemIds) {
        checkGroupId(groupId);
        if (itemIds == null){
            return null;
        }
        Iterator<Integer> it = itemIds.iterator();
        
        List<ApiGroupItem> list = new ArrayList<>();
        while (it.hasNext()) {
            list.add(new ApiGroupItem(groupId, it.next()));
        }
        return this.saveList(list);
    }


    public void delList(Integer groupId) {
        checkGroupId(groupId);
        this.repository.deleteAllByGroupIdEquals(groupId);
    }

    private void checkGroupId(Integer groupId) {
        Assert.notNull(groupId, "groupId不能为空");
    }

}
