package cn.smbms.service.role;

import java.sql.Connection;
import java.util.List;

import cn.smbms.dao.BaseDao;
import cn.smbms.dao.role.RoleDao;
import cn.smbms.dao.role.RoleDaoImpl;
import cn.smbms.dao.role.RoleMapper;
import cn.smbms.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("roleService")
@Transactional
public class RoleServiceImpl implements RoleService {

	/*@Autowired
	private RoleDao roleDao;*/

    @Autowired
    private RoleMapper roleMapper;

	/*public RoleServiceImpl(){
		roleDao = new RoleDaoImpl();
	}*/

    @Override
	@Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public List<Role> getRoleList() {
        return roleMapper.getRoleList();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public List<Role> queryRoleList(String roleCode, String roleName) {
        return roleMapper.queryRoleList(roleCode,roleName);
    }

    @Override
    public int addRole(Role role) {
        return roleMapper.addRole(role);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public Role getRoleById(String id) {
        return roleMapper.getRoleById(id);
    }

    @Override
    public int modify(Role role) {
        return roleMapper.modify(role);
    }

    @Override
    public int deleteRole(String id) {
        return roleMapper.delRole(id);
    }

}
