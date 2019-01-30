package com.gmcc.msb.zuul.filter;

import com.gmcc.msb.zuul.Constant;
import com.gmcc.msb.zuul.properties.MsbZuulProperties;
import com.gmcc.msb.zuul.service.FilterHelperService;
import com.gmcc.msb.zuul.service.RedisService;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.monitoring.CounterFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.actuate.metrics.repository.InMemoryMetricRepository;
import org.springframework.boot.actuate.metrics.writer.DefaultCounterService;
import org.springframework.cloud.netflix.zuul.metrics.DefaultCounterFactory;
import org.springframework.cloud.netflix.zuul.util.ZuulRuntimeException;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockHttpServletRequest;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Yuan Chunhai
 * @Date 9/30/2018-11:21 AM
 */
public class CheckTokenPreFilterTest {

    @InjectMocks
    CheckTokenPreFilter checkTokenPreFilter;

    @Mock
    private MsbZuulProperties msbZuulProperties;

    @Mock
    private RedisService redisService;

    @Mock
    private FilterHelperService filterHelperService;


    @Rule
    public ExpectedException expected = ExpectedException.none();


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        CounterFactory.initialize(new DefaultCounterFactory(new DefaultCounterService(
                new InMemoryMetricRepository())));
    }

    @Test
    public void test_empty_token() {

        expected.expect(ZuulRuntimeException.class);
        expected.expectMessage("0008-10003");

        RequestContext requestContext = RequestContext.getCurrentContext();

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("GET", "/uri");
//        mockRequest.addHeader("api-id", "msb_gmcc_net");
        requestContext.setRequest(mockRequest);

        checkTokenPreFilter.run();

    }


    @Test
    public void test_token_from_url_param() {

        expected.expect(ZuulRuntimeException.class);
        expected.expectMessage("0008-10005");

        RequestContext requestContext = RequestContext.getCurrentContext();

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("GET", "/uri");
        mockRequest.setParameter("token", "msb_gmcc_net");
        requestContext.setRequest(mockRequest);

        checkTokenPreFilter.run();

    }

    @Test
    public void test_token_from_header() {

        expected.expect(ZuulRuntimeException.class);
        expected.expectMessage("0008-10005");

        RequestContext requestContext = RequestContext.getCurrentContext();

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("GET", "/uri");
        mockRequest.addHeader("Authorization", "Bearer msb_gmcc_net");
        requestContext.setRequest(mockRequest);

        checkTokenPreFilter.run();

    }


    @Test
    public void test_invalid_sign_token() {

        expected.expect(ZuulRuntimeException.class);
        expected.expectMessage("0008-10005");

        when(msbZuulProperties.getSsoTokenSignKey())
                .thenReturn("123");
        when(msbZuulProperties.getRasPublikKey())
                .thenReturn("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCUiaxToKuP+IU4UZ4sg3J5QS0IadGYo/vo3FmWMfIlu6mw4XRX4dPm6Sl/u9rDm4uJYeXwq7OuMd1porRmER4b32RQ/NK3TFx3l+aRNXqoFHrAnJjbjkffvAdTQLBDAE2HXM+IVioxuH7rl/KFf1lbnBhJLyGNBTDOUlihngwwkQIDAQAB");


        RequestContext requestContext = RequestContext.getCurrentContext();
        MockHttpServletRequest mockRequest = new MockHttpServletRequest("GET", "/uri");
        mockRequest.addHeader("Authorization",
                "Bearer " +
                        "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NDQ3NTQ1OTAsInVzZXJfbmFtZSI6ImR3aHV6aGl3ZW4yIiwianRpIjoiOWIzODE0ZTgtMGQwOC00NTk5LWFhNTktOTJmOWUzMmM0NTJlIiwiY2xpZW50X2lkIjoibXNiX2dtY2NfbmV0Iiwic2NvcGUiOlsiVXNlcl9OYW1lIl0sIm9yZ2FuaXphdGlvbiI6ImR3aHV6aGl3ZW4yNzQ3N2EzZmEtYTJkNC00Zjk5LTkzNmQtNjMyNTRhNTc1NjMyIn0.aHJZnBoAIt59BweoDjG4yaC49QwJ0rREpouGPZmxA0lDjfRPpkbQyXulvhS1LP4wWqLmNHzA1IFe5c0a7jjV_jsj9SpvllAfvzvHXPc4FNKHzSaQ3xW4irv6IqjX79R_i_5mZURJTjgSVJmJdy2XW-cIiyjo9yZyHkUVMla7b81232"
        );
        requestContext.setRequest(mockRequest);

        try {
            checkTokenPreFilter.ssoPublicKey = getRsaPublicKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        checkTokenPreFilter.run();

    }

    @Test
    public void test_invalid_token_payload() {

        expected.expect(ZuulRuntimeException.class);
        expected.expectMessage("0008-10005");

        when(msbZuulProperties.getSsoTokenSignKey())
                .thenReturn("123");

        RequestContext requestContext = RequestContext.getCurrentContext();
        MockHttpServletRequest mockRequest = new MockHttpServletRequest("GET", "/uri");
        mockRequest.addHeader("Authorization",
                "Bearer " +
                        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.ImNsaWVudF9pZCI6Y2xpZW50X2lk.-b2eD_axIOMlZUw7Rph8ESh8NOrzwz3BHEFxtDemTvg"
        );
        requestContext.setRequest(mockRequest);

        checkTokenPreFilter.run();

    }


    @Test
    public void test_run() {

        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NDQ3NTQ1OTAsInVzZXJfbmFtZSI6ImR3aHV6aGl3ZW4yIiwianRpIjoiOWIzODE0ZTgtMGQwOC00NTk5LWFhNTktOTJmOWUzMmM0NTJlIiwiY2xpZW50X2lkIjoibXNiX2dtY2NfbmV0Iiwic2NvcGUiOlsiVXNlcl9OYW1lIl0sIm9yZ2FuaXphdGlvbiI6ImR3aHV6aGl3ZW4yNzQ3N2EzZmEtYTJkNC00Zjk5LTkzNmQtNjMyNTRhNTc1NjMyIn0.aHJZnBoAIt59BweoDjG4yaC49QwJ0rREpouGPZmxA0lDjfRPpkbQyXulvhS1LP4wWqLmNHzA1IFe5c0a7jjV_jsj9SpvllAfvzvHXPc4FNKHzSaQ3xW4irv6IqjX79R_i_5mZURJTjgSVJmJdy2XW-cIiyjo9yZyHkUVMla7b8I";

        when(redisService.appTokenValid("01563694-baef-4622-8cb8-bfa65be0547d", token))
                .thenReturn(true);

        when(filterHelperService.skipTokenCheck())
                .thenReturn(true);

        when(msbZuulProperties.getRasPublikKey())
                .thenReturn("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCUiaxToKuP+IU4UZ4sg3J5QS0IadGYo/vo3FmWMfIlu6mw4XRX4dPm6Sl/u9rDm4uJYeXwq7OuMd1porRmER4b32RQ/NK3TFx3l+aRNXqoFHrAnJjbjkffvAdTQLBDAE2HXM+IVioxuH7rl/KFf1lbnBhJLyGNBTDOUlihngwwkQIDAQAB");

//
//        try {
//            rsaPublicKey = getRsaPublicKey();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (InvalidKeySpecException e) {
//            e.printStackTrace();
//        }


        RequestContext requestContext = RequestContext.getCurrentContext();
        MockHttpServletRequest mockRequest = new MockHttpServletRequest("GET", "/uri");
        mockRequest.addHeader("Authorization",
                "Bearer " +
                        token
        );
        requestContext.setRequest(mockRequest);

        try {
            checkTokenPreFilter.ssoPublicKey = getRsaPublicKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        checkTokenPreFilter.run();

        assertEquals("dwhuzhiwen2", requestContext.get(Constant.USER_ID));
    }


    @Bean
    private RSAPublicKey getRsaPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(java.util.Base64.getDecoder().decode(
                msbZuulProperties.getRasPublikKey().getBytes()));
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

}
