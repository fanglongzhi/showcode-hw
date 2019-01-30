package com.gmcc.msb.msbbreak.service;

import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.common.property.UserContextHolder;
import com.gmcc.msb.msbbreak.common.BreakerParam;
import com.gmcc.msb.msbbreak.entity.BreakerStrategy;
import com.gmcc.msb.msbbreak.entity.BreakerStrategyApi;
import com.gmcc.msb.msbbreak.entity.ServiceApi;
import com.gmcc.msb.msbbreak.repository.BreakerStrategyApiRepository;
import com.gmcc.msb.msbbreak.repository.BreakerStrategyRepository;
import com.gmcc.msb.msbbreak.repository.ServiceApiRepository;
import com.gmcc.msb.msbbreak.repository.ServiceBreakerRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;
import java.util.Set;

import static com.gmcc.msb.msbbreak.common.CommonConstant.ERROR_API_NOT_EXIST;
import static com.gmcc.msb.msbbreak.common.CommonConstant.ERROR_DEFAULT_CAN_NOT_BIND;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BreakerServiceTest {
    @InjectMocks
    private BreakerService breakerService;
    @Mock
    private BreakerStrategyRepository breakerStrategyRepository;
    @Mock
    private ServiceBreakerRepository serviceBreakerRepository;
    @Mock
    private ServiceApiRepository serviceApiRepository;
    @Mock
    private BreakerStrategyApiRepository breakerStrategyApiRepository;
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAllServiceApiTest(){
        breakerService.findAllServiceApi(new ServiceApi());
        verify(serviceApiRepository,times(1)).findAll(any(Specification.class),any(Sort.class));
    }
    @Test
    public void findAllBreakerStrategyTest(){
        UserContextHolder.getContext().getDataOrgList().add(1l);
        breakerService.findAllBreakerStrategy(new BreakerStrategy());
        verify(breakerStrategyRepository,times(1)).findAll(any(Specification.class),any(Sort.class));
    }
    @Test
    public void findBreakerStrategyTest(){
        breakerService.findBreakerStrategy(1);
        verify(breakerStrategyRepository,times(1)).findBreakerStrategyByIdNoDefault(1);
    }
    @Test
    public void findServiceApiTest(){
        breakerService.findServiceApi(1);
        verify(serviceApiRepository,times(1)).findOne(1);
    }
    @Test
    public void findBreakerStrategyByApiIdTest(){
        breakerService.findBreakerStrategyByApiId(1);
        verify(breakerStrategyRepository,
                times(1)).findBreakerStrategyByApiId(1);
    }
    @Test
    public void enableBreakerTest(){
        breakerService.enableBreaker(1);
        verify(breakerStrategyRepository,
                times(1)).updateBreakerStatus(1,Boolean.TRUE);
        verify(serviceBreakerRepository,
                times(1)).
                updateStrategy("true",1,BreakerParam.ENABLE_BREAKER_TYPE);

    }

    @Test
    public void disableBreakerTest(){
        breakerService.disableBreaker(1);
        verify(breakerStrategyRepository,
                times(1)).updateBreakerStatus(1,false);
        verify(serviceBreakerRepository,
                times(1)).
                updateStrategy("false",1,BreakerParam.ENABLE_BREAKER_TYPE);

    }

    @Test
    public void deleteBreakerTest(){
        breakerService.deleteBreaker(1);
        verify(breakerStrategyRepository,
                times(1)).delete(1);
        verify(breakerStrategyApiRepository,
                times(1)).
                deleteByStrategyId(1);

        verify(serviceBreakerRepository,
                times(1)).
                deleteByStrategyId(1);
    }

    @Test
    public void createBreakerStrategyApi_fail_noExist(){
        expectedEx.expect(MsbException.class);
        expectedEx.expectMessage(ERROR_API_NOT_EXIST);
        breakerService.createBreakerStrategyApi(null,null);


    }

    @Test
    public void createBreakerStrategyApi_fail_default(){
        expectedEx.expect(MsbException.class);
        expectedEx.expectMessage(ERROR_DEFAULT_CAN_NOT_BIND);
        BreakerStrategy strategy = new BreakerStrategy();
        strategy.setIsDefault(true);
        breakerService.createBreakerStrategyApi(strategy,new ServiceApi());


    }

    @Test
    public void createBreakerStrategyApi(){
        BreakerStrategy strategy = new BreakerStrategy();
        strategy.setIsDefault(false);
        breakerService.createBreakerStrategyApi(strategy,new ServiceApi());

        verify(breakerStrategyApiRepository,
                times(1)).
                save(any(BreakerStrategyApi.class));
    }

    @Test
    public void testManageBreakStrategy_default() throws Exception{
        BreakerStrategy bs1 = new BreakerStrategy();
        bs1.setIsDefault(true);
        bs1.setStrategyName("DEFAULT");
        bs1.setTimeout(1000);
        bs1.setSleep(10000);
        bs1.setEnableBreaker(true);

        when(breakerStrategyRepository.findByIsDefault(true)).thenReturn(bs1);

        Map<String,Object> result = breakerService.manageBreakStrategy(bs1);
        Assert.assertNotNull(result);
        Map<String,String> proKeyMap = (Map<String,String>)result.get("key");
        Map<String,String> typeMap = (Map<String,String>)result.get("type");

        Assert.assertFalse(proKeyMap.isEmpty());
        Assert.assertFalse(typeMap.isEmpty());
        Assert.assertEquals(3,proKeyMap.entrySet().size());
        Assert.assertEquals(3,typeMap.entrySet().size());

        int num = 0;
        for(Map.Entry<String,String> entry:proKeyMap.entrySet()){
            if(entry.getKey().endsWith(BreakerParam.ENABLE_BREAKER_SUF)){
                Assert.assertEquals(bs1.getEnableBreaker().toString(),entry.getValue());
                Assert.assertEquals(BreakerParam.ENABLE_BREAKER_TYPE,typeMap.get(entry.getKey()));
                num++;
            }

            if(entry.getKey().endsWith(BreakerParam.SLEEP_SUF)){
                Assert.assertEquals(bs1.getSleep().toString(),entry.getValue());
                Assert.assertEquals(BreakerParam.SLEEP_TYPE,typeMap.get(entry.getKey()));
                num++;
            }

            if(entry.getKey().endsWith(BreakerParam.TIMEOUT_SUF)){
                Assert.assertEquals(bs1.getTimeout().toString(),entry.getValue());
                Assert.assertEquals(BreakerParam.TIMEOUT_TYPE,typeMap.get(entry.getKey()));
                num++;
            }
        }

        Assert.assertEquals(3,num);
    }

    @Test
    public void testManageBreakStrategy_no_default() throws Exception{
        BreakerStrategy bs1 = new BreakerStrategy();
        bs1.setId(1);
        bs1.setIsDefault(false);
        bs1.setStrategyName("TEST2");
        bs1.setEnableBreaker(true);
        bs1.setSleep(10000);
        bs1.setTimeout(1000);
        bs1.setFailRate(50);
        bs1.setRequestVolume(10);


        BreakerStrategy old = new BreakerStrategy();
        old.setId(1);
        old.setIsDefault(false);
        old.setStrategyName("TEST");

        when(breakerStrategyRepository.findOne(bs1.getId())).thenReturn(old);

        Map<String,Object> result = breakerService.manageBreakStrategy(bs1);
        Assert.assertNotNull(result);
        Map<String,String> proKeyMap = (Map<String,String>)result.get("update");
        Set<String> deleteMap = (Set<String>)result.get("delete");

        Assert.assertFalse(proKeyMap.isEmpty());
        Assert.assertTrue(deleteMap.isEmpty());
        Assert.assertEquals(5,proKeyMap.size());

        int num = 0;
        for(Map.Entry<String,String> entry:proKeyMap.entrySet()){
            if(entry.getKey().equals(BreakerParam.ENABLE_BREAKER_TYPE)){
                Assert.assertEquals(bs1.getEnableBreaker().toString(),entry.getValue());
                num++;
            }

            if(entry.getKey().equals(BreakerParam.SLEEP_TYPE)){
                Assert.assertEquals(bs1.getSleep().toString(),entry.getValue());

                num++;
            }

            if(entry.getKey().equals(BreakerParam.TIMEOUT_TYPE)){
                Assert.assertEquals(bs1.getTimeout().toString(),entry.getValue());
                num++;
            }

            if(entry.getKey().equals(BreakerParam.REQUEST_VOLUME_TYPE)){
                Assert.assertEquals(bs1.getRequestVolume().toString(),entry.getValue());
                num++;
            }

            if(entry.getKey().equals(BreakerParam.FAIL_RATE_TYPE)){
                Assert.assertEquals(bs1.getFailRate().toString(),entry.getValue());
                num++;
            }
        }
        Assert.assertEquals(5,num);
    }

    @Test
    public void testModifyStrategy() throws Exception{
        BreakerStrategy bs1 = new BreakerStrategy();
        bs1.setId(1);
        bs1.setIsDefault(false);
        bs1.setStrategyName("TEST2");
        bs1.setEnableBreaker(true);
        bs1.setSleep(10000);
        bs1.setTimeout(1000);

        BreakerStrategy old = new BreakerStrategy();
        old.setId(1);
        old.setIsDefault(false);
        old.setStrategyName("TEST");

        when(breakerStrategyRepository.findOne(bs1.getId())).thenReturn(old);

        Map<String,Object> result = breakerService.manageBreakStrategy(bs1);
        Assert.assertNotNull(result);
        Map<String,String> proKeyMap = (Map<String,String>)result.get("update");
        Set<String> deleteSet = (Set<String>)result.get("delete");

        Assert.assertFalse(proKeyMap.isEmpty());
        Assert.assertFalse(deleteSet.isEmpty());
        Assert.assertEquals(3,proKeyMap.size());
        Assert.assertEquals(2,deleteSet.size());

        int num = 0;
        for(Map.Entry<String,String> entry:proKeyMap.entrySet()){
            if(entry.getKey().equals(BreakerParam.ENABLE_BREAKER_TYPE)){
                Assert.assertEquals(bs1.getEnableBreaker().toString(),entry.getValue());
                num++;
            }

            if(entry.getKey().equals(BreakerParam.SLEEP_TYPE)){
                Assert.assertEquals(bs1.getSleep().toString(),entry.getValue());

                num++;
            }

            if(entry.getKey().equals(BreakerParam.TIMEOUT_TYPE)){
                Assert.assertEquals(bs1.getTimeout().toString(),entry.getValue());
                num++;
            }

        }
        Assert.assertEquals(3,num);

        int num2 = 0;
        for(String key:deleteSet){
           if(key.equals(BreakerParam.FAIL_RATE_TYPE)){
               num2++;
           }
           if(key.equals(BreakerParam.REQUEST_VOLUME_TYPE)){
               num2++;
           }
        }

        Assert.assertEquals(2,num2);
    }
}
