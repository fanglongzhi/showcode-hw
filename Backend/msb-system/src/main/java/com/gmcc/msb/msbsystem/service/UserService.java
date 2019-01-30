package com.gmcc.msb.msbsystem.service;

import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.msbsystem.common.CommonConstant;
import com.gmcc.msb.msbsystem.common.UserStatus;
import com.gmcc.msb.msbsystem.common.resp.Result;
import com.gmcc.msb.msbsystem.config.MyProperties;
import com.gmcc.msb.msbsystem.entity.user.User;
import com.gmcc.msb.msbsystem.repository.user.UserCustomerSpec;
import com.gmcc.msb.msbsystem.repository.user.UserRepository;
import com.gmcc.msb.msbsystem.vo.req.user.QueryUserParam;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MyProperties myProperties;

    private Gson gson = new Gson();

    private Map<String,UserStatus> userStatusMap = Maps.newHashMap();

    {
        userStatusMap.put("0",UserStatus.VALID);
        userStatusMap.put("1",UserStatus.INVALID);
    }

    @Transactional
    public Result<User> saveUser(User user) throws MsbException {
        if (userRepository.countUser(user.getOperatorId(), user.getUserId(), user.getLoginId(),
                user.getPersonCardNo(), user.getEmail(), user.getMobile()) > 0) {
            throw new MsbException(CommonConstant.ERROR_CODE, CommonConstant.ERROR_MSG);
        }

        Result<User> result = Result.success();
        Date createDate = new Date();
        user.setCreateTime(createDate);
        user.setUpdateTime(createDate);
        userRepository.save(user);

        return result;
    }

    public Result<User> findOneUser(Long id) {
        User retUser = userRepository.findOne(id);
        Result<User> result = Result.success();
        result.setContent(retUser);
        return result;
    }

    public Result<List<User>> findUserList(QueryUserParam param) {
        List<User> userList = userRepository.findAll(UserCustomerSpec.queryUserListSpec(param), new Sort(Sort.Direction.DESC, "updateTime"));

        Result<List<User>> result = Result.success();
        result.setContent(userList);

        return result;
    }

    public Result<List<User>> findUserListWithDataOrgInfo(QueryUserParam param) {
        List<Object> userList = userRepository.findUserListWithDataOrgInfo(
                param.getOperatorId(), param.getName(), param.getStatus(), param.getIsLock());
        List<User> users = Lists.newArrayList();
        try {
            for (Object m : userList) {
                Object[] ml = (Object[])m;
                User user = new User(
                        ml[0]==null?null:Long.valueOf((Integer)ml[0]),
                        ml[1]==null?null:Long.valueOf((Integer)ml[1]),
                        ml[2]==null?"":ml[2].toString(),
                        ml[3]==null?"":ml[3].toString(),
                        ml[4]==null?"":ml[4].toString(),
                        ml[5]==null?"":ml[5].toString(),
                        ml[6]==null?"":ml[6].toString(),
                        ml[7]==null?null:Long.valueOf((Integer)ml[7]),
                        ml[8] == null?"":ml[8].toString(),
                        ml[9]==null?"":ml[9].toString(),
                        ml[10]==null?null:userStatusMap.get(ml[10]),
                        ml[11]==null?false:Boolean.valueOf(ml[11].toString()),
                        ml[12] == null ? null : Integer.valueOf(ml[12].toString()),
                        ml[13] == null ? "" : ml[13].toString(),
                        ml[14] == null ? null : (Date) ml[14],
                        ml[15] == null ? null : (Date)ml[15]
                );
                users.add(user);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        Result<List<User>> result = Result.success();
        result.setContent(users);

        return result;
    }


    public List<User> findAll() {
        List<User> userList = (List<User>) userRepository.findAll();
        return userList;
    }

    public Result<Page<User>> findUserListPage(QueryUserParam param) {
        Pageable pageable = new PageRequest(param.getPageInfo().getCurrentPage(),
                param.getPageInfo().getPageSize(), Sort.Direction.ASC, "createTime");
        Page<User> userList = userRepository.findAll(UserCustomerSpec.queryUserListSpec(param), pageable);

        Result<Page<User>> result = Result.success();
        result.setContent(userList);

        return result;
    }

    @Transactional
    public Result modifyUserStatus(QueryUserParam.ModifyUserParam param) {
        userRepository.updateUserStatus(param.getStatus(), param.getId());
        Result result = Result.success();
        return result;
    }

    @Transactional
    public Result modifyUserLock(QueryUserParam.ModifyUserParam param) {
        userRepository.updateUserLock(param.getIsLock(), param.getId());
        Result result = Result.success();
        return result;
    }

    public Result checkSSOToken(String token) {
        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .connectTimeout(myProperties.getRequestRefreshConnectionTime(), TimeUnit.MILLISECONDS)
                .readTimeout(myProperties.getRequestRefreshReadTime(), TimeUnit.MILLISECONDS)
                .build();

        String url = myProperties.getSsoCheckTokenPath() + "?token=" + token;

        Request request = new Request.Builder()
                .url(url)
                .get().header("Accept", "application/json")
                .build();
        Response response = null;
        try {
            log.debug("请求 {} 验证SSO TOKEN", url);
            response = client.newCall(request).execute();
            int code = response.code();
            String respStr = response.body().string();
            if (code != 200) {
                log.error("验证失败，{}, {}", code, respStr);
                throw new MsbException("0007-10018", "错误码" + code);
            }
            log.debug("请求 {} 验证SSO TOKEN,结果：{}", url, respStr);

            return Result.success(gson.fromJson(respStr, Map.class));
        } catch (IOException e) {
            log.error("验证TOKEN失败，{}", ExceptionUtils.getMessage(e));
            throw new MsbException("0007-10019");
        }

    }
}
