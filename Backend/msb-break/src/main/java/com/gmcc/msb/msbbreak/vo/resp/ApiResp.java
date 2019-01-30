package com.gmcc.msb.msbbreak.vo.resp;

import com.gmcc.msb.msbbreak.entity.ServiceApi;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @program: msb-break
 * @description: 策略关联API查询返回结果集
 * @author: zhifanglong
 * @create: 2018-11-07 16:13
 */
@ApiModel
public class ApiResp {
    @ApiModelProperty("已绑定本策略的API列表")
    List<ApiVo> apiListInStrategy = Lists.newArrayList();
    @ApiModelProperty("已绑定其他策略的API列表")
    List<ApiVo> apiListOtherStrategy = Lists.newArrayList();
    @ApiModelProperty("未绑定熔断策略的API列表")
    List<ApiVo> apiListNoStrategy = Lists.newArrayList();

    public List<ApiVo> getApiListInStrategy() {
        return apiListInStrategy;
    }

    public void setApiListInStrategy(List<ApiVo> apiListInStrategy) {
        this.apiListInStrategy = apiListInStrategy;
    }

    public List<ApiVo> getApiListOtherStrategy() {
        return apiListOtherStrategy;
    }

    public void setApiListOtherStrategy(List<ApiVo> apiListOtherStrategy) {
        this.apiListOtherStrategy = apiListOtherStrategy;
    }

    public List<ApiVo> getApiListNoStrategy() {
        return apiListNoStrategy;
    }

    public void setApiListNoStrategy(List<ApiVo> apiListNoStrategy) {
        this.apiListNoStrategy = apiListNoStrategy;
    }
}
