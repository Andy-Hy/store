package com.cy.store.mapper;

import com.cy.store.entity.Address;
import com.cy.store.entity.User;
import com.cy.store.service.IAddressService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AddressMapperTests {

    @Autowired
    private AddressMapper addressMapper;

    @Test
    public void insert() {
        Address address = new Address();
        address.setUid(12);
        address.setPhone("12345666666");
        address.setName("女朋友");
        addressMapper.insert(address);
    }


    @Test
    public void countByUid() {
        Integer count = addressMapper.countByUid(12);
        System.out.println(count);
    }

    @Test
    public void findByUid() {
        List<Address> list = addressMapper.findByUid(8);
        System.out.println(list);
    }

    @Test
    public void findByAid(){
        System.out.println(addressMapper.findByAid(9));
    }

    @Test
    public void updateNonDefaultByUid() {
        addressMapper.updateNonDefaultByUid(10);
    }

    @Test
    public void updateDefaultByAid() {
        addressMapper.updateDefaultByAid(10,"天天",new Date());
    }

    @Test
    public void deleteByAid() {
        addressMapper.deleteByAid(2);
    }

    @Test
    public void findLastModified() {
        System.out.println(addressMapper.findLastModified(12));
    }


}
