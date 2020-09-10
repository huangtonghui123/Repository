package cn.smbms.dao.role;

import cn.smbms.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper {

    /**
     * 查询角色列表
     *
     * @return
     */
    List<Role> getRoleList();

    /**
     * 条件查询角色列表
     *
     * @return
     */
    List<Role> queryRoleList(@Param("roleCode") String roleCode,
                             @Param("roleName") String roleName);

    /**
     * 添加角色
     *
     * @param role
     * @return
     */
    int addRole(Role role);

    /**
     * 根据ID获取角色信息
     *
     * @param id
     * @return
     */
    Role getRoleById(String id);

    /**
     * 修改角色
     *
     * @param role
     * @return
     */
    int modify(Role role);

    /**
     * 删除角色
     *
     * @param delId
     * @return
     */
    int delRole(String delId);
}
