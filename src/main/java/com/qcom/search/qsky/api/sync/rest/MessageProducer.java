package com.qcom.search.qsky.api.sync.rest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Created by mlalapet on 8/17/16.
 */
@Component
@Scope("singleton")
public class MessageProducer {

    private int value = 0;

    public MessageProducer(){
        this.value = (new Random()).nextInt(10 - 0 + 1) + 0;
    }

    public int getValue() {
        return value;
    }

}
