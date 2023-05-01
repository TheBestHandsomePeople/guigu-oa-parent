package com.atguigu.auth.controller;

import com.atguigu.auth.service.impl.SysRoleServiceImpl;
import com.atguigu.common.config.exception.JinCaiException;
import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysRole;
import com.atguigu.vo.system.AssginRoleVo;
import com.atguigu.vo.system.SysRoleQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Wrapper;
import java.util.List;
import java.util.Map;

/**
 * @author bestHandsomePeople
 * @creat 2023-04-01-20:27
 */
@Api(tags = "角色管理接口")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleServiceImpl service;

  /*  //统一返回数据结果
    @ApiOperation("查询所有角色")
    @GetMapping("/findAll")
    public Result getAll() {
        List<SysRole> list = service.list();

        try {
            int i = 10 / 0;
        } catch (Exception e) {
            throw new JinCaiException(10000, "闪闪牛逼的金财错误出现。。。");
        }
        return Result.ok(list);

    }
*/


    @ApiOperation(value = "根据用户获取角色数据")
    @GetMapping("/toAssign/{userId}")
    public Result toAssign(@PathVariable Long userId) {
        Map<String, Object> roleMap = service.findRoleByAdminId(userId);
        return Result.ok(roleMap);
    }

    @ApiOperation(value = "根据用户分配角色")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssginRoleVo assginRoleVo) {
        service.doAssign(assginRoleVo);
        return Result.ok();
    }
    /**
     * 条件分页查询
     *
     * @param page           当前页
     * @param limit          每页显示记录数
     * @param sysRoleQueryVo 条件对象
     * @return
     */

    @ApiOperation("条件分页查询")
    @GetMapping("{page}/{limit}")
    public Result pageQueryRole(@PathVariable Long page
            , @PathVariable Long limit
            , SysRoleQueryVo sysRoleQueryVo) {

        //调用service的方法实现
        //1 创建page对象，传递分页相关参数
        Page<SysRole> pageParam = new Page<>(page, limit);
        //2 封装条件，判断条件是否为空，不为空进行封装
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<SysRole>();
        String roleName = sysRoleQueryVo.getRoleName();
        if (!StringUtils.isEmpty(roleName)) {
            wrapper.like(SysRole::getRoleName, roleName);
        }
        Page page1 = service.page(pageParam, wrapper);
        return Result.ok(page1);
    }

    /**
     * 新增角色
     *
     * @param sysRole
     * @return
     */
    @PreAuthorize("hasAuthority('bnt.sysRole.add')")
    @ApiOperation(value = "新增角色")
    @PostMapping("save")
    public Result save(@RequestBody SysRole sysRole) {
        //调用service方法
        boolean is_success = service.save(sysRole);
        if (is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }

    }


    //修改角色，根据id查询
    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation(value = "修改角色，根据id查询")
    @GetMapping("get/{id}")
    public Result getById(@PathVariable Long id) {
        SysRole role = service.getById(id);
        return Result.ok(role);
    }

    //修改角色-最终修改
    @PreAuthorize("hasAuthority('bnt.sysRole.update')")
    @ApiOperation(value = "修改角色-最终修改")
    @PutMapping("update")
    public Result update(@RequestBody SysRole sysRole) {
        //调用service方法
        boolean is_success = service.updateById(sysRole);
        if (is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }

    }

    //根据id删除
    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation("根据id删除")
    @DeleteMapping("remove/{id}")
    public Result removeById(@PathVariable Long id) {
        boolean is_success = service.removeById(id);
        if (is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //批量删除
    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation("批量删除")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> ids) {
        boolean is_success = service.removeByIds(ids);
        if (is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

}
