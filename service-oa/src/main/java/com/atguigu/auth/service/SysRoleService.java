package com.atguigu.auth.service;

import com.atguigu.model.system.SysRole;
import com.atguigu.vo.system.AssginRoleVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author bestHandsomePeople
 * @creat 2023-04-01-20:14
 */
public interface SysRoleService extends IService<SysRole> {
    Map<String, Object> findRoleByAdminId(Long userId);

    void doAssign(AssginRoleVo assginRoleVo);
}
