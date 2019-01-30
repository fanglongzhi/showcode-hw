package com.gmcc.msb.api.service;

import com.gmcc.msb.api.entity.ApiGroup;
import com.gmcc.msb.api.entity.ApiGroupItem;
import com.gmcc.msb.api.repository.ApiGroupRepository;
import com.gmcc.msb.api.vo.request.ApiGroupRequest;
import com.gmcc.msb.common.exception.MsbException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.fail;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class ApiGroupServiceTest {


    @InjectMocks
    ApiGroupService apiGroupService;

    @Mock
    private ApiGroupRepository apiGroupRepository;

    @Mock
    private ApiGroupItemService apiGroupItemService;

    @Mock
    private AppOrderApiService appOrderApiService;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAdd() {

        ApiGroup savedApiGroup = new ApiGroup();
        savedApiGroup.setId(1);
        savedApiGroup.setName("test");

        when(apiGroupRepository.save(any(ApiGroup.class))).thenReturn(savedApiGroup);
        when(apiGroupItemService.save(any(ApiGroupItem.class))).thenReturn(new ApiGroupItem());

        ApiGroupRequest request = new ApiGroupRequest();
        request.setName("hello");
        request.setApis(Arrays.asList(1, 2, 3));
        ApiGroup result = apiGroupService.add(request);

        assertNotNull(result);


    }


    @Test(expected = MsbException.class)
    public void testUpdateRecordNotExists() {
        Integer id = 3;
        when(apiGroupRepository.findOne(id)).thenReturn(null);

        apiGroupService.update(id, new ApiGroupRequest());
    }


    @Test()
    public void testUpdate() {
        Integer id = 3;
        ApiGroup apiGroup = new ApiGroup();
        apiGroup.setId(3);
        apiGroup.setName("test");

        when(apiGroupRepository.findOne(id)).thenReturn(apiGroup);
        when(apiGroupRepository.save(any(ApiGroup.class))).thenReturn(apiGroup);
        when(apiGroupItemService.saveList(id, Arrays.asList(1, 2, 3, 4))).thenReturn(null);

        ApiGroupRequest request = new ApiGroupRequest();
        request.setName("test2");
        request.setApis(Arrays.asList(1, 2, 3, 4));
        apiGroupService.update(id, request);


    }


    @Test()
    public void testDeleteEmptyRecord() {
        Integer id = 3;
        ApiGroup apiGroup = new ApiGroup();
        apiGroup.setId(3);
        apiGroup.setName("test");

        when(apiGroupRepository.findOne(id)).thenReturn(null);

        try {
            apiGroupService.delete(id);
            fail();
        } catch (MsbException e) {
            assertThat(e.getMessage(), containsString("0001-10134"));
        }
    }

    @Test()
    public void testDeleteGroupHasItem() {
        Integer id = 3;
        ApiGroup apiGroup = new ApiGroup();
        apiGroup.setId(3);
        apiGroup.setName("test");

        when(apiGroupRepository.findOne(id)).thenReturn(apiGroup);
        when(apiGroupItemService.groupHasItems(id)).thenReturn(true);

        try {
            apiGroupService.delete(id);
            fail();
        } catch (MsbException e) {
            assertThat(e.getMessage(), containsString("0001-10135"));
        }
    }

    @Test()
    public void testDelete() {
        Integer id = 3;
        ApiGroup apiGroup = new ApiGroup();
        apiGroup.setId(3);
        apiGroup.setName("test");

        when(apiGroupRepository.findOne(id)).thenReturn(apiGroup);
        when(apiGroupItemService.groupHasItems(id)).thenReturn(false);

        apiGroupService.delete(id);
    }

}
