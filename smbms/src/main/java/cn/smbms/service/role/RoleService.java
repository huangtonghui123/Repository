package cn.smbms.service.role;

import java.util.List;

import cn.smbms.pojo.Role;

public interface RoleService {

	public List<Role> getRoleList();

	public List<Role> queryRoleList(String roleCode,String roleName);

	public int addRole(Role role);

	public Role getRoleById(String id);

	public int modify(Role role);

	public int deleteRole(String id);
	
}
