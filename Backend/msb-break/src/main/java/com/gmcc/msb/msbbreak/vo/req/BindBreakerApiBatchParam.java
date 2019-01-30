package com.gmcc.msb.msbbreak.vo.req;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @program: msb-break
 * @description: 批量绑定或解绑熔断策略到API参数对象
 * @author: zhifanglong
 * @create: 2018-11-07 18:14
 */
@ApiModel
public class BindBreakerApiBatchParam {
    @ApiModelProperty("api主键列表")
    private List<Integer> apiIdList;

    public List<Integer> getApiIdList() {
        if(apiIdList==null){
            this.apiIdList = Lists.newArrayList();
        }
        return apiIdList;
    }

    public void setApiIdList(List<Integer> apiIdList) {
        this.apiIdList = apiIdList;
    }
}
