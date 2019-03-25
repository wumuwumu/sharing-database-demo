package cn.sciento.wumu.sharingdatabase.service;

import cn.sciento.wumu.sharingdatabase.entity.User;
import cn.sciento.wumu.sharingdatabase.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;


    public int add(User user){
        return userMapper.insert(user);
    }

//    public int updateById(User user){
//        return userMapper.updateById(user);
//    }
//
//    public User getById(Long id){
//        return userMapper.getById(id);
//    }
}
