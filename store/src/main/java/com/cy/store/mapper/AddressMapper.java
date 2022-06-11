package com.cy.store.mapper;

import com.cy.store.entity.Address;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**处理收货地址数据的持久层接口*/
public interface AddressMapper {

    /**
     * 插入收货地址数据
     * @param address 收货地址数据
     * @return 受影响的行数
     */
   Integer insert(Address address);

    /**
     * 统计某用户收货地址数据量
     * @param uid 用户的id
     * @return 该用户收货地址数据的数量
     */
    Integer countByUid(Integer uid);

    /**
     * 根据用户的id查询用户的收货地址数据
     * @param uid 用户的id
     * @return 收货地址数据
     */
    List<Address> findByUid(Integer uid);

    /**
     * 根据aid查询收货地址
     * @param aid 收货地址id
     * @return 查询收货地址
     */
    Address findByAid(Integer aid);

    /**
     * 根据用户的id值修改用户的收货地址设置为得默认
     * @param uid 用户的id值
     * @return 受影响的行数
     */
    Integer updateNonDefaultByUid(Integer uid);

 /**
  *将指定的收货地址设置为默认地址
  * @param aid
  * @param modifiedUser
  * @param modifiedTime
  * @return
  */
 Integer updateDefaultByAid(
         @Param("aid") Integer aid,
         @Param("modifiedUser") String modifiedUser,
         @Param("modifiedTime") Date modifiedTime);


 /**
  * 根据收货地址id删除收货地址数据
  * @param aid 收货地址id
  * @return 受影响的行数
  */
 Integer deleteByAid(Integer aid);


 /**
  * 根据用户id查询挡前用户的最后一次被修改的收货地址数据
  * @param uid 用户id
  * @return 收货地址数据
  */
 Address findLastModified(Integer uid);

}


