package com.gmcc.msb.api.service;

import com.gmcc.msb.api.entity.AppOrderApi;
import com.gmcc.msb.api.repository.AppOrderApiRepository;
import com.gmcc.msb.common.property.UserContextHolder;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class AppOrderApiServiceTest {
    @Mock
    private AppOrderApiRepository appOrderApiRepository;

    @InjectMocks
    private AppOrderApiService service;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_findAll(){
        int id = 1;
        AppOrderApi appOrderApi = new AppOrderApi();
        appOrderApi.setId(id);
        ArrayList<AppOrderApi> list = new ArrayList<>();
        list.add(appOrderApi);
        when(appOrderApiRepository.findAll())
                .thenReturn(list);
        List<AppOrderApi> all = service.findAll();
        assertTrue(all != null);
        assertTrue(all.size()>0);
    }

    @Test
    public void test_repository(){
        int id =1;
        List<Map<String, Object>> mapList = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        map.put("id",id);
        mapList.add(map);
        when(appOrderApiRepository.getAll(Lists.newArrayList(1l))).thenReturn(mapList);
        UserContextHolder.getContext().setDataOrgList(Lists.newArrayList(1L));
        List<Map<String, Object>> all = service.getAll(null);
        assertTrue(all!=null);
        assertTrue(all.get(0).get("id").equals(id));
    }

    @Test
    public void  test_delete(){
        service.delete(1);
        verify(appOrderApiRepository,times(1)).delete(1);
    }

    @Test
    public void test_sava(){
        int id = 1;
        AppOrderApi appOrderApi = new AppOrderApi();
        appOrderApi.setId(id);
        when(appOrderApiRepository.save(appOrderApi)).thenReturn(appOrderApi);
        AppOrderApi sava = service.save(appOrderApi);
        verify(appOrderApiRepository,times(1)).save(appOrderApi);
    }

    @Test
    public void test_findOne(){
        int id = 1;
        AppOrderApi appOrderApi = new AppOrderApi();
        appOrderApi.setId(id);
        when(appOrderApiRepository.findOne(id)).thenReturn(appOrderApi);
        AppOrderApi one = service.findOne(id);
        verify(appOrderApiRepository,times(1)).findOne(id);
        assertTrue(one.getId()==id);
    }

    @Test
    public void test_findAppIdAndTypeId(){
        int id = 1;
        int appId = 11;
        int typeId = 111;
        AppOrderApi appOrderApi = new AppOrderApi();
        appOrderApi.setId(id);
        appOrderApi.setAppId(appId);
        appOrderApi.setApiId(typeId);

        ArrayList<AppOrderApi> list = new ArrayList<>();
        list.add(appOrderApi);
        when(appOrderApiRepository.findAllByAppIdAndApiId(appId,typeId)).thenReturn(list);
        List<AppOrderApi> appIdAndTypeId = service.findAppIdAndTypeId(appId, typeId);
        verify(appOrderApiRepository).findAllByAppIdAndApiId(appId,typeId);
        assertTrue(appIdAndTypeId.get(0).getId()==id);
    }
}
