package com.gmcc.msb.api.controller;

import com.gmcc.msb.api.entity.ApiGroup;
import com.gmcc.msb.api.service.ApiGroupService;
import com.gmcc.msb.api.service.ApiService;
import com.gmcc.msb.api.vo.response.ApiGroupResponse;
import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.common.vo.Response;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

public class ApiGroupControllerTest {

    @InjectMocks
    ApiGroupController apiGroupController;

    @Mock
    private ApiGroupService apiGroupService;

    @Mock
    private ApiService apiService;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = MsbException.class)
    public void testGetEmptyGroupId() {

        Integer groupId = 3;
        when(apiGroupService.findOne(groupId)).thenReturn(null);

        this.apiGroupController.get(3);
    }

    @Test
    public void testGet() {

        Integer groupId = 3;
        ApiGroup api = new ApiGroup();
        api.setId(1);
        api.setName("anme");

        when(apiGroupService.findOne(groupId)).thenReturn(api);
        when(apiService.findAllApisInGroup(groupId)).thenReturn(null);


        Response<ApiGroupResponse> resp = this.apiGroupController.get(3);
        assertEquals(api.getName(), resp.getContent().getName());
        assertEquals(api.getId(), resp.getContent().getId());
        assertTrue(resp.getContent().getApis().isEmpty());

    }


    @Test(expected = MsbException.class)
    public void testgetGroupApisEmptyRecord() {
        when(apiGroupService.findOne(3)).thenReturn(null);
        apiGroupController.getGroupApis(3);
    }

    @Test
    public void testgetGroupApis() {

        ApiGroup api = new ApiGroup();
        api.setId(3);
        api.setName("anme");


        when(apiGroupService.findOne(3)).thenReturn(api);
        when(apiService.findAllApisInGroup(3)).thenReturn(null);


        Response result = apiGroupController.getGroupApis(3);

        assertNotNull(result);
        assertNull(result.getContent());
    }


    @Test
    public void testGetAll() {

        Pageable pageable = new PageRequest(1, 2);
        List<ApiGroup> list = new ArrayList<>();
        ApiGroup apiGroup = new ApiGroup();
        apiGroup.setId(1);
        apiGroup.setName("hello");
        list.add(apiGroup);
        when(apiGroupService.find(pageable)).thenReturn(new PageImpl<>(list, pageable, 0));
        when(apiService.findAllApisInGroup(anyInt())).thenReturn(null);

        Response response = apiGroupController.getAll(pageable);

        assertNotNull(response);
        assertEquals(1, ((Page<ApiGroupResponse>) response.getContent()).getContent().size());
        assertEquals("hello", ((Page<ApiGroupResponse>) response.getContent()).getContent().get(0).getName());

    }

}
