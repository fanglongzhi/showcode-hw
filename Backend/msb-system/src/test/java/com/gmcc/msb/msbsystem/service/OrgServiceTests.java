package com.gmcc.msb.msbsystem.service;

import com.gmcc.msb.msbsystem.entity.org.Org;
import com.gmcc.msb.msbsystem.repository.org.OrgRepository;
import com.gmcc.msb.msbsystem.vo.resp.OrgVo;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class OrgServiceTests {

    @InjectMocks
    OrgService orgService;

    @Mock
    OrgRepository orgRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindOneWithChildrenEmpty() {
        when(orgRepository.findOne(1)).thenReturn(null);
        OrgVo result = orgService.findOneWithChildren(1);
        assertNull(result);
    }

    @Test
    public void testFindOneWithChildrenNoChildren() {
        Org org = new Org();
        org.setOrgid(1);
        org.setOrgname("name");


        when(orgRepository.findOne(1)).thenReturn(org);
        when(orgRepository.findAllByParentorgid(1)).thenReturn(null);

        OrgVo result = orgService.findOneWithChildren(1);
        assertEquals(result.getData().getOrgid(), org.getOrgid());
        assertNotNull(result.getChildren());
        assertEquals(0, result.getChildren().size());
        assertTrue(result.isLeaf());
    }

    @Test
    public void testFindOneWithChildrenOneChildren() {
        Org org = new Org();
        org.setOrgid(1);
        org.setOrgname("name");

        Org child = new Org();
        child.setOrgid(2);
        child.setParentorgid(1);
        child.setOrgname("child");


        when(orgRepository.findOne(1)).thenReturn(org);
        when(orgRepository.findAllByParentorgid(1)).thenReturn(Arrays.asList(child));
        when(orgRepository.countByParentorgid(2)).thenReturn(0);

        OrgVo result = orgService.findOneWithChildren(1);
        assertNotNull(result.getChildren());
        assertEquals(1, ((List) result.getChildren()).size());

        assertFalse(result.isLeaf());

        OrgVo chi = (OrgVo)((List) result.getChildren()).get(0);
        assertTrue(chi.isLeaf());

    }


    @Test
    public void testFindOne() throws Exception {

        Org org1 = new Org();
        org1.setOrgid(1);
        org1.setOrgname("org1");
        org1.setParentorgid(null);

        Org org2 = new Org();
        org2.setOrgid(2);
        org2.setOrgname("org2");
        org2.setParentorgid(1);

        Org org3 = new Org();
        org3.setOrgid(3);
        org3.setOrgname("org2");
        org3.setParentorgid(2);



        Pageable pageable = new PageRequest(1, 3);
        when(orgRepository.findAll(pageable)).thenReturn(new PageImpl<Org>(Arrays.asList(org1, org2, org3)));
        when(orgRepository.findAllByOrgidIn(Arrays.asList(1,2))).thenReturn(Arrays.asList(org1, org2));
        when(orgRepository.findAllByOrgidIn(Arrays.asList(1))).thenReturn(Arrays.asList(org1));

        List<OrgVo> results = orgService.find("", pageable);


        assertEquals(org1.getOrgid(), results.get(0).getData().getOrgid());
        assertEquals(org2.getOrgid(), results.get(0).getChildren().iterator().next().getData().getOrgid());
        assertEquals(org3.getOrgid(), results.get(0).getChildren().iterator().next()
                                              .getChildren().iterator().next()
                                              .getData().getOrgid());

    }


}
