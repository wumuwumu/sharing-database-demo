package cn.sciento.wumu.sharingdatabase.mapper;

import cn.sciento.wumu.sharingdatabase.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    int insert(User user);
//    int updateById(User user);
//    User getById(Long id);
}
