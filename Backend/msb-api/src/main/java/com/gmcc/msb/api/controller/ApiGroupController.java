package com.gmcc.msb.api.controller;

import com.gmcc.msb.api.entity.API;
import com.gmcc.msb.api.entity.ApiGroup;
import com.gmcc.msb.api.service.ApiGroupService;
import com.gmcc.msb.api.service.ApiService;
import com.gmcc.msb.api.vo.request.ApiGroupRequest;
import com.gmcc.msb.api.vo.response.ApiGroupResponse;
import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.common.vo.Response;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ApiGroupController {

    @Autowired
    private ApiGroupService apiGroupService;


    @Autowired
    private ApiService apiService;


    private ApiGroupResponse buildResponse(ApiGroup apiGroup) {
        List<API> items = apiService.findAllApisInGroup(apiGroup.getId());
        ApiGroupResponse apiGroupResponse = new ApiGroupResponse();
        BeanUtils.copyProperties(apiGroup, apiGroupResponse);
        apiGroupResponse.setApis(items);
        return apiGroupResponse;
    }

    private ApiGroupResponse buildResponse(Integer groupId) {
        ApiGroup find = apiGroupService.findOne(groupId);
        if (find == null) {
            throw new MsbException("0001-10130");
        }
        return buildResponse(find);
    }


    @PostMapping("/api_group")
    @ApiOperation(value = "新增API分组")
    public Response<ApiGroupResponse> add(@RequestBody @Valid @ApiParam(value = "api分组请求", required = true) ApiGroupRequest vo) {
        ApiGroup saved = apiGroupService.add(vo);
        return Response.ok(buildResponse(saved));
    }

    @PutMapping("/api_group/{groupId}")
    @ApiOperation(value = "修改API分组")
    public Response<ApiGroupResponse> update(@RequestBody @Valid @ApiParam(value = "api分组请求", required = true) ApiGroupRequest vo, @PathVariable @ApiParam(value = "组ID", required = true) Integer groupId) {
        apiGroupService.update(groupId, vo);
        return Response.ok(buildResponse(groupId));
    }

    @PutMapping("/api_group/{groupId}/name")
    @ApiOperation(value = "修改API分组名称")
    public Response<ApiGroupResponse> update(@RequestParam @ApiParam(value = "api分组名称", required = true) String name, @PathVariable @ApiParam(value = "api分组ID", required = true) Integer groupId) {
        Assert.hasLength(name, "0001-00019");
        apiGroupService.updateName(groupId, name);
        return Response.ok(buildResponse(groupId));
    }


    @DeleteMapping("/api_group/{groupId}")
    @ApiOperation(value = "删除API分组")
    public Response delete(@PathVariable @ApiParam(value = "api分组ID", required = true) Integer groupId) {
        apiGroupService.delete(groupId);
        return Response.ok();
    }

    @GetMapping("/api_group/{groupId}")
    @ApiOperation(value = "获取API分组信息")
    public Response<ApiGroupResponse> get(@PathVariable @ApiParam(value = "api分组ID", required = true) Integer groupId) {
        return Response.ok(buildResponse(groupId));
    }

    @GetMapping("/api_group/{groupId}/apis")
    @ApiOperation(value = "获取API分组包含的API", response = Response.class)
    public Response<List<API>> getGroupApis(@PathVariable @ApiParam(value = "api分组ID", required = true) Integer groupId) {
        ApiGroup find = apiGroupService.findOne(groupId);
        if (find == null) {
            throw new MsbException("0001-10131");
        }
        List<API> items = apiService.findAllApisInGroup(groupId);
        return Response.ok(items);
    }


    @GetMapping("/api_groups")
    @ApiOperation(value = "获取所有api分组")
    public Response<Page<ApiGroupResponse>> getAll(Pageable pageable) {

        Page<ApiGroup> page = apiGroupService.find(pageable);

        List<ApiGroupResponse> list = new ArrayList<>();

        if (page.getContent() != null) {
            for (ApiGroup apiGroup : page.getContent()) {
                list.add(buildResponse(apiGroup));
            }
        }

        Page<ApiGroupResponse> returnPage = new PageImpl<>(list, pageable, page.getTotalElements());

        return Response.ok(returnPage);
    }

    @GetMapping("/api_groups/all")
    @ApiOperation(value = "获取所有api分组,不加组过滤")
    public Response<Page<ApiGroupResponse>> getAllGroups(Pageable pageable) {

        Page<ApiGroup> page = apiGroupService.findAll(pageable);

        List<ApiGroupResponse> list = new ArrayList<>();

        if (page.getContent() != null) {
            for (ApiGroup apiGroup : page.getContent()) {
                list.add(buildResponse(apiGroup));
            }
        }

        Page<ApiGroupResponse> returnPage = new PageImpl<>(list, pageable, page.getTotalElements());

        return Response.ok(returnPage);
    }


}
