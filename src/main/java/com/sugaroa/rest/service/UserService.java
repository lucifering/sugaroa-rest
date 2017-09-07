package com.sugaroa.rest.service;

import com.sugaroa.rest.domain.User;
import com.sugaroa.rest.domain.User;
import com.sugaroa.rest.domain.UserRepository;
import com.sugaroa.rest.exception.AppException;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final PrivilegeService servicePrivilege;

    @Autowired
    public UserService(UserRepository repository, PrivilegeService servicePrivilege) {
        this.repository = repository;
        this.servicePrivilege = servicePrivilege;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByAccount(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with account '%s'.", username));
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        User jwtUser = new User(user.getId(), user.getAccount(), user.getPassword(), authorities);
        return jwtUser;
    }


    /**
     * 创建
     */
    public User save(Map<String, String[]> params) {
        User user = new User();
        return this.save(user, params);
    }

    /**
     * 根据Id更新
     *
     * @param id
     * @param params
     */
    public User save(Integer id, Map<String, String[]> params) {

        //先查找对应记录
        User user = repository.findOne(id);
        return this.save(user, params);

    }

    public User save(User user, Map<String, String[]> params) {

        //初始化BeanWrapper
        BeanWrapper bw = new BeanWrapperImpl(user);

        //根据params对需要更新的值做处理，即动态调用对应的setter方法
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            //只要有传参数进来，就认为修改该属性
            bw.setPropertyValue(entry.getKey(), entry.getValue());
        }
        //把实体类的值填充了，才能再做下一步处理。

        //同pid下重名判断及path处理
        if (params.containsKey("password")) {
            // TODO 生成salt并生成password
        }
        //有设置关联权限
        if (params.containsKey("privileges")) {
            Map<String, Integer> object = new HashMap<String, Integer>();
            Set<Integer> list = new HashSet<Integer>();
            servicePrivilege.parse(user.getPrivileges(), object, list);

            //不能判断不为空才处理，可能就是要赋为空
            user.setPrivilegeArray(list);
            user.setPrivilegeObject(object);
        }
        return repository.save(user);
    }

    public User get(Integer id)  {
        return repository.findOne(id);
    }
}
