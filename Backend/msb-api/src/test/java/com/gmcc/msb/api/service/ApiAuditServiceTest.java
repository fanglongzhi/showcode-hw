package com.gmcc.msb.api.service;

import com.gmcc.msb.api.entity.API;
import com.gmcc.msb.api.entity.ApiAudit;
import com.gmcc.msb.api.repository.ApiAuditRepository;
import com.gmcc.msb.common.property.UserContextHolder;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class ApiAuditServiceTest {


    @InjectMocks
    ApiAuditService apiAuditService;

    @Mock
    private ApiAuditRepository reporitory;

    @Mock
    private ServService servService;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreate() {
        API api = new API();
        api.setId(1);
        api.setServiceName("shlkjer");

        ApiAudit apiAudit = new ApiAudit();
        when(reporitory.save(any(ApiAudit.class)))
                .thenReturn(apiAudit);

        assertEquals(apiAudit, apiAuditService.create(api, 1));
    }


    @Test
    public void testFindall() {
        API api = new API();
        api.setId(1);
        api.setServiceName("shlkjer");

        ApiAudit apiAudit = new ApiAudit();
        PageRequest pageable = new PageRequest(1,2);
        List<ApiAudit> content = Arrays.asList(new ApiAudit());
        when(reporitory.findAllByConditions(Lists.newArrayList(1L),pageable))
                .thenReturn(new PageImpl<>(content));
        UserContextHolder.getContext().setDataOrgList(Lists.newArrayList(1L));
        assertEquals(1, apiAuditService.findAll(pageable).getTotalElements());
    }
}
