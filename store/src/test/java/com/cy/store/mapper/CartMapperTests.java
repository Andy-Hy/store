package com.cy.store.mapper;

import com.cy.store.entity.Address;
import com.cy.store.entity.Cart;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.*;
import java.util.Date;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CartMapperTests {

    @Autowired
    private CartMapper cartMapper;

    @Test
    public void insert(){
        Cart cart = new Cart();
        cart.setUid(9);
        cart.setPid(100011);
        cart.setNum(2);
        cart.setPrice(10000L);
        cartMapper.insert(cart);
    }

    @Test
    public void findVOByUid() {
        System.out.println(cartMapper.findVOByUid(9));
    }

    @Test
    public void findByCid() {
        System.out.println(cartMapper.findByCid(1));
    }

    @Test
    public void findVOByCid() {
        Integer[] cids = {1};
        System.out.println(cartMapper.findVOByCids(cids))  ;
    }
}
