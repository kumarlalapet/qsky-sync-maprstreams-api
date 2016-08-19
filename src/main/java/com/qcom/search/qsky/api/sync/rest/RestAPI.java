package com.qcom.search.qsky.api.sync.rest;

import com.qcom.search.qsky.api.sync.streams.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Created by mlalapet on 8/17/16.
 */
@RestController
@RequestMapping("/sync/rest")
public class RestAPI {

    private MessageProducer producer;
    private enum CRUD_OPERATION {
        CREATE, UPDATE, DONE;
    }

    @Autowired
    public RestAPI(MessageProducer producer){
        this.producer = producer;
    }

    @RequestMapping(
            value = "/create",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Result create(@RequestParam("project") String project,
                       @RequestParam("simid") String simId,
                       @RequestParam("user") String user,
                       @RequestParam("payload") String payload)
            throws Exception {

        System.out.println("Create + Project : "+project+" Simulation : "+simId+" User "+user+" Payload "+payload);

        String docId = UUID.randomUUID().toString();
        String messageKey = user+"_"+project+"_"+simId+"_"+docId;
        payload = addRootToPayload(payload, CRUD_OPERATION.CREATE);
        return sendMessage(messageKey, payload, MessageProducer.OPERATION.SYNC);
    }

    @RequestMapping(
            value = "/update-sync",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Result updateSync(@RequestParam("id") String id, @RequestParam("payload") String payload)
            throws Exception {
        payload = addRootToPayload(payload, CRUD_OPERATION.UPDATE);
        return sendMessage(id,payload, MessageProducer.OPERATION.SYNC);
    }

    @RequestMapping(
            value = "/update-async",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Result updateAsync(@RequestParam("id") String id, @RequestParam("payload") String payload)
            throws Exception {
        payload = addRootToPayload(payload, CRUD_OPERATION.UPDATE);
        return sendMessage(id,payload, MessageProducer.OPERATION.ASYNC);
    }

    @RequestMapping(
            value = "/done",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Result done(@RequestParam("id") String id, @RequestParam("payload") String payload)
            throws Exception {
        payload = addRootToPayload(payload, CRUD_OPERATION.DONE);
        return sendMessage(id, payload, MessageProducer.OPERATION.SYNC);
    }

    private Result sendMessage(String id, String payload, MessageProducer.OPERATION operation){
        System.out.println("Update : "+payload);

        boolean success = producer.sendMessage(id, payload, operation);
        if(success)
            return new Result(id, Result.Status.SUCCESS);
        else
            return new Result(null, Result.Status.FAIL);
    }

    private String addRootToPayload(String payload, CRUD_OPERATION crudOperation) {
        String finalJson = "{\"root\":{\"operation\":\""+crudOperation+"\",\"rawmsg\":"+payload+"}}";
        return finalJson;
    }

}
