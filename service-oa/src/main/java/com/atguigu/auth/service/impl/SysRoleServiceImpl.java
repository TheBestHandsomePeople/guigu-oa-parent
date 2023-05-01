package com.atguigu.auth.service.impl;

import com.atguigu.auth.mapper.SysRoleMapper;
import com.atguigu.auth.service.SysRoleService;
import com.atguigu.auth.service.SysUserRoleService;
import com.atguigu.model.system.SysRole;
import com.atguigu.model.system.SysUserRole;
import com.atguigu.vo.system.AssginRoleVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author bestHandsomePeople
 * @creat 2023-04-01-20:15
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService  {

    @Autowired
    private SysUserRoleService sysUserRoleService;

    // 1 查询所有角色 和 当前用户所属角色.
    @Override
    public Map<String, Object> findRoleByAdminId(Long userId) {
        //查询所有角色，返回list集合
        List<SysRole> allRoleList = baseMapper.selectList(null);
        //根据userid查询 角色用户关系表，查询userid对应所有角色的id
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        List<SysUserRole> existUserRoleList = sysUserRoleService.list(wrapper);


        //从查询出来的用户id对应角色list集合，获取所有的角色id
        ArrayList<Long> list = new ArrayList<>();
        for (SysUserRole sysUserRole : existUserRoleList) {
            Long roleId = sysUserRole.getRoleId();
            list.add(roleId);
        }
//        List<Long> existUserRoleIdList =
//                existUserRoleList.stream().map(i -> i.getRoleId()).collect(Collectors.toList());

        // 3 根据查询所有角色id，找到对应角色信息
        //根据角色id的所有的角色的list集合进行比较
        List<SysRole> assginRoleList = new ArrayList<>();
        for (SysRole sysRole : allRoleList) {
            if (list.contains(sysRole.getId())) {
                assginRoleList.add(sysRole);
            }
        }

        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("assginRoleList", assginRoleList);
        roleMap.put("allRolesList", allRoleList);
        return roleMap;




    }

    @Override
    public void doAssign(AssginRoleVo assginRoleVo) {
        //把用户之前分配的角色数据删除，用户角色关系表里面，根据userid删除
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, assginRoleVo.getUserId());
        sysUserRoleService.remove(wrapper);
        //重新进行分配
        List<Long> roleIdList = assginRoleVo.getRoleIdList();
        for (Long roleId : roleIdList) {
            if (StringUtils.isEmpty(roleId)) {
                continue;
            }
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(assginRoleVo.getUserId());
            sysUserRole.setRoleId(roleId);
            sysUserRoleService.save(sysUserRole);
        }
    }
}
