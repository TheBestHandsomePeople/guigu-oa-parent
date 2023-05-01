package com.atguigu.auth.controller;

/**
 * @author bestHandsomePeople
 * @creat 2023-04-04-15:33
 */

import com.atguigu.auth.service.SysMenuService;
import com.atguigu.auth.service.SysRoleService;
import com.atguigu.auth.service.SysUserService;
import com.atguigu.common.config.exception.JinCaiException;
import com.atguigu.common.jwt.JwtHelper;
import com.atguigu.common.result.Result;
import com.atguigu.common.utils.MD5;
import com.atguigu.model.system.SysRole;
import com.atguigu.model.system.SysUser;
import com.atguigu.vo.system.LoginVo;
import com.atguigu.vo.system.RouterVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台登录登出
 * </p>
 */
@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    //login
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("token", "admin-token");
//        return Result.ok(map);

        // 1 获取输入用户名和密码
        // 2 根据用户名查询数据库
        String username = loginVo.getUsername();
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserService.getOne(wrapper);


        //  3 用户信息是否存在
        if (sysUser == null) {
            throw new JinCaiException(201, "用户不存在");
        }

        // 4 判断密码
        //输入的密码
        String password_input = loginVo.getPassword();
        String encrypt = MD5.encrypt(password_input);
        //数据库的密码
        String password_db = sysUser.getPassword();
        if (!password_db.equals(encrypt)) {
            throw new JinCaiException(201, "密码错误");
        }
        // 5 判断用户是否被禁用
        if (sysUser.getStatus().longValue() == 0) {
            throw new JinCaiException(201, "用户已被禁用");
        }
        // 6 使用jwt根据用户id和用户名生成token字符串
        String token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());
        // 7 返回
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        return Result.ok(map);
    }

    //info
    @GetMapping("info")
    public Result info(HttpServletRequest request) {

        //从请求头获取用户信息（获取请求头token字符串）
        String token = request.getHeader("token");

        //从token字符串获取用户id 或者 用户名称
        Long userId = JwtHelper.getUserId(token);

        //根据用户id查询数据库，把用户信息获取出来
        SysUser sysUser = sysUserService.getById(userId);

        //根据用户id获取用户可以操作的菜单列表
        //查询数据库动态构建路由结构，进行显示
        List<RouterVo> routerList = sysMenuService.findUserMenuListByUserId(userId);

        //根据用户id获取用户可以操作的按钮列表
        List<String> permsList = sysMenuService.findUserPermsByUserId(userId);

        //返回相应数据
        Map<String, Object> map = new HashMap<>();
        map.put("roles", "[admin]");
        map.put("name", sysUser.getName());
        map.put("avatar", "https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
        map.put("routers", routerList);
        map.put("buttons", permsList);
        return Result.ok(map);

    }

    /* 退出
     * @return
     */
    @PostMapping("logout")
    public Result logout() {
        return Result.ok();
    }


}
