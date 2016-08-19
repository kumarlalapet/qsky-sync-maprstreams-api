# QSky REST API to write data to MapR Streams

Create API

1) Create API supports only synchronous way of sending messages to MapR Streams
2) Supports only POST and needs 4 parameters a) project b) user c) simid and d) payload
3) Below is a sample curl command to call create rest api

curl -k -i  -X POST -d "payload={\"name\":\"value\"}&project=sample&simid=s1&user=m1" http://vdpsbdprd06.dps.aws.qualcomm.com:8080/sync/rest/create

Update Sync API

1) Accepts two parameters a) id and b) payload
2) Below is a sample curl command

curl -k -i  -X POST -d "payload={\"name\":\"value\"}&id=iid1" http://vdpsbdprd06.dps.aws.qualcomm.com:8080/sync/rest/update-sync

Update ASync API

1) Accepts two parameters a) id and b) payload
2) Below is a sample curl command

curl -k -i  -X POST -d "payload={\"name\":\"value\"}&id=iid2" http://vdpsbdprd06.dps.aws.qualcomm.com:8080/sync/rest/update-async

Done API

1) Done API supports only Synchronous send to MapR Streams
2) Below is a sample curl command

curl -k -i  -X POST -d "payload={\"name\":\"value\"}&id=iid3" http://vdpsbdprd06.dps.aws.qualcomm.com:8080/sync/rest/done

Running the REST Application

1) Copy the Uber jar file to the client nodes
2) Execute the following command

java -cp /tmp/uber-qsky-sync-maprstreams-api-0.1.jar:`mapr classpath` com.qcom.search.qsky.api.sync.Application

NOTE - add the `mapr classpath` to the end of the classpath as shown above.