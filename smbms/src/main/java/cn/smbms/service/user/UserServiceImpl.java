package cn.smbms.service.user;

import cn.smbms.dao.BaseDao;
import cn.smbms.dao.user.UserDao;
import cn.smbms.dao.user.UserMapper;
import cn.smbms.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * service层捕获异常，进行事务处理
 * 事务处理：调用不同dao的多个方法，必须使用同一个connection（connection作为参数传递）
 * 事务完成之后，需要在service层进行connection的关闭，在dao层关闭（PreparedStatement和ResultSet对象）
 *
 * @author Administrator
 */
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

	/*@Autowired
	private UserDao userDao;*/

    @Autowired
    private UserMapper userMapper;

	/*public UserServiceImpl(){
		userDao = new UserDaoImpl();
	}*/

    @Override
    public boolean add(User user) {
        boolean flag = false;
        int updateRows = userMapper.add(user);
        if (updateRows > 0) {
            flag = true;
            System.out.println("add success!");
        } else {
            System.out.println("add failed!");
        }
        return flag;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public User login(String userCode) {
        User user = userMapper.getLoginUser(userCode);
        //匹配密码
		/*if(null != user){
			if(!user.getUserPassword().equals(userPassword))
				user = null;
		}*/

        return user;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) {
        List<User> userList = null;
        userList = userMapper.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);
        return userList;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public User selectUserCodeExist(String userCode) {
        User user = userMapper.getLoginUser(userCode);
        return user;
    }

    @Override
    public boolean deleteUserById(Integer delId) {
        boolean flag = false;
        if (userMapper.deleteUserById(delId) > 0) {
            flag = true;
        }
        return flag;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public User getUserById(String id) {
        User user = userMapper.getUserById(id);
        return user;
    }

    @Override
    public boolean modify(User user) {
        boolean flag = false;

        if (userMapper.modify(user) > 0) {
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean updatePwd(int id, String pwd) {
        boolean flag = false;
        if (userMapper.updatePwd(id, pwd) > 0) {
            flag = true;
        }
        return flag;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public int getUserCount(String queryUserName, int queryUserRole) {
        int count = 0;
        System.out.println("queryUserName ---- > " + queryUserName);
        System.out.println("queryUserRole ---- > " + queryUserRole);
        count = userMapper.getUserCount(queryUserName, queryUserRole);
        return count;
    }

}
