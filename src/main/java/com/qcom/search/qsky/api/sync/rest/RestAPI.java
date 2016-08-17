package com.qcom.search.qsky.api.sync.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mlalapet on 8/17/16.
 */
@RestController
@RequestMapping("/sync/rest")
public class RestAPI {

    MessageProducer producer;

    @Autowired
    public RestAPI(MessageProducer producer){
        this.producer = producer;
    }

    @RequestMapping(
            value = "/create",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Result create(@RequestParam("project") String project,
                       @RequestParam("type") String type,
                       @RequestParam("ts") String ts,
                       @RequestParam("payload") String payload)
            throws Exception {

        System.out.println("Create + Project : "+project+" Type : "+type+" TS "+ts+" Payload "+payload);

        String messageKey = project+":"+type;
        boolean success = producer.sendMessage(messageKey, payload, MessageProducer.Operation.CREATE);

        if(success)
            return new Result("id", Result.Status.SUCCESS);
        else
            return new Result("id", Result.Status.FAIL);
    }

    @RequestMapping(
            value = "/update",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Result update(@RequestParam("id") String id, @RequestParam("payload") String payload)
            throws Exception {

        System.out.println("Update : "+payload);

        return new Result(id, Result.Status.SUCCESS);

    }

    @RequestMapping(
            value = "/done",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Result done(@RequestParam("id") String id, @RequestParam("payload") String payload)
            throws Exception {

        System.out.println("Done : "+payload);

        return new Result(id, Result.Status.SUCCESS);

    }

}
