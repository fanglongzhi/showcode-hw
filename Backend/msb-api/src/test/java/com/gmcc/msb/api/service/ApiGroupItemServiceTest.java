package com.gmcc.msb.api.service;

import com.gmcc.msb.api.repository.ApiGroupItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static junit.framework.TestCase.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class ApiGroupItemServiceTest {

    @InjectMocks
    ApiGroupItemService apiGroupItemService;


    @Mock
    ApiGroupItemRepository repository;


    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveListEmptyGroup() {
        apiGroupItemService.saveList(null, null);
    }
    @Test()
    public void testSaveListNullIds() {
        assertNull( apiGroupItemService.saveList(1, null));
    }

    @Test
    public void testSaveList() {
        when(repository.save(any(Iterable.class)))
                .thenReturn(null);
        assertNull(apiGroupItemService.saveList(1, Arrays.asList(1,2,3)));
    }
}
