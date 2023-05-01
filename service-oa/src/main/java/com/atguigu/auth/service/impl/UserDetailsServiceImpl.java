package com.atguigu.auth.service.impl;

import com.atguigu.auth.service.SysMenuService;
import com.atguigu.auth.service.SysUserService;
import com.atguigu.model.system.SysUser;
import com.atguigu.security.custom.CustomUser;
import com.atguigu.security.custom.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author bestHandsomePeople
 * @creat 2023-04-12-22:13
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名进行查询
        SysUser sysUser=sysUserService.getUserByUsername(username);
        if(null == sysUser) {
            throw new UsernameNotFoundException("用户名不存在！");
        }

        if(sysUser.getStatus().intValue() == 0) {
            throw new RuntimeException("账号已停用");
        }

        //根据用户id查询用户权限
        List<String> userPerms = sysMenuService.findUserPermsByUserId(sysUser.getId());

        //创建list集合，封装最终权限数据
        List<SimpleGrantedAuthority> list = new ArrayList<>();

        //查询出的集合进行遍历，再封装进新创建的集合
        for (String Perm : userPerms) {
            list.add(new SimpleGrantedAuthority(Perm.trim()));
        }
        return new CustomUser(sysUser, list);

    }
}
