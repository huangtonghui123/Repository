package com.hisoft;

import cn.smbms.dao.Singleton;
import cn.smbms.pojo.Role;
import cn.smbms.service.role.RoleService;
import cn.smbms.service.role.RoleServiceImpl;
import org.junit.Test;

import java.util.List;

public class test {

    @Test
    public void testGetSingleton() {
        Singleton singleton1 = Singleton.getSingleton();
        Singleton singleton2 = Singleton.getSingleton();
        System.out.println((singleton1 == singleton2)); //true
    }

    @Test
    public void test() {
        RoleService roleService = new RoleServiceImpl();
        Role role = new Role();
        role.setRoleCode("222");
        role.setRoleName("222");
        int addRole = roleService.addRole(role);
        System.out.println(addRole);
    }
}
