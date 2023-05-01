package com.atguigu.auth;

import com.atguigu.auth.mapper.SysRoleMapper;
import com.atguigu.model.system.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author bestHandsomePeople
 * @creat 2023-04-01-10:39
 */
@SpringBootTest
public class TestMpDemo1 {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    //查询所有记录
    @Test
    public void getAll() {
        List<SysRole> list = sysRoleMapper.selectList(null);
        list.forEach(i->System.out.println(i));
    }

    @Test
    public void add() {
        SysRole role = new SysRole();
        role.setRoleName("翟梦锦");
        role.setRoleCode("asdsad");
        role.setDescription("高冷女神");

        int i = sysRoleMapper.insert(role);
        System.out.println(i);
        System.out.println(role);
    }

    @Test
    public void update() {
        SysRole role = sysRoleMapper.selectById(9);
        role.setDescription("零食毁灭神");
        int i = sysRoleMapper.updateById(role);
        System.out.println(i);
    }

    @Test
    public void deleteById() {
        int i = sysRoleMapper.deleteById(8);
        System.out.println(i);
    }
}
