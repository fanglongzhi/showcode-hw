package com.gmcc.msb.zuul.route;

import com.gmcc.msb.common.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yuan Chunhai
 */
@RestController
public class DynamicRouteController {

    @Autowired
    RefreshRouteService refreshRouteService;

    @Autowired
    CustomRouteLocator customRouteLocator;

    @PostMapping("/refresh_route")
    public Response<String> refresh(){
        refreshRouteService.refreshRoute();

        customRouteLocator.refresh();

        return Response.ok("ok");
    }
}
