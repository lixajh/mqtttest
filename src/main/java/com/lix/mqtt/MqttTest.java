package com.lix.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttTest {

    private  int qos = 2; //只有一次
    private  String broker = "tcp://118.24.155.154:1883";
    private  String userName = "test";
    private  String passWord = "test";
    private  String clientId = "";

    MqttTest(String clientId){
        this.clientId = clientId;
    }

    private  MqttClient connect() throws MqttException {
        MemoryPersistence persistence = new MemoryPersistence();
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(false);
        connOpts.setUserName(userName);
        connOpts.setPassword(passWord.toCharArray());
        connOpts.setConnectionTimeout(10);
        connOpts.setKeepAliveInterval(20);
//		String[] uris = {"tcp://10.100.124.206:1883","tcp://10.100.124.207:1883"};
//		connOpts.setServerURIs(uris);  //起到负载均衡和高可用的作用
//        MqttClient mqttClient = new MqttClient(broker, clientId, persistence);
        MqttClient mqttClient = new MqttClient(broker, clientId);
//        mqttClient.setCallback(new PushCallback("test"));
        mqttClient.setCallback(new MqttCallback() {
            public void connectionLost(Throwable throwable) {
                System.out.println(clientId + "connectionLost" );
            }

            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                System.out.println(clientId + "messageArrived" );
            }

            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                System.out.println(clientId + "deliveryComplete" );
            }
        });
        mqttClient.connect(connOpts);
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mqttClient;
    }

    public void doConnect(){
        new Thread(new Runnable() {
            public void run() {
                try {
                    connect();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private  void pub(MqttClient sampleClient, String msg,String topic)
            throws MqttPersistenceException, MqttException {
        MqttMessage message = new MqttMessage("ertwersdfas".getBytes());
        message.setQos(qos);
        message.setRetained(false);
        sampleClient.publish(topic, message);
    }

    private  void publish(String str,String clientId,String topic) throws MqttException{
        MqttClient mqttClient = connect();

        if (mqttClient != null) {
            pub(mqttClient, str, topic);
            System.out.println("pub-->" + str);
        }
        try {
            Thread.sleep(600000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (mqttClient != null) {
            mqttClient.disconnect();
        }
    }

    public static void main(String[] args) throws MqttException {
//        publish("message content","1234","$share/edge/server/public/a");
//        MqttTest mqttTest = new MqttTest("1234");
//        mqttTest.doConnect();
        MqttTest mqttTest1 = new MqttTest("1235");
        mqttTest1.doConnect();
//        MqttTest mqttTest2 = new MqttTest("1236");
//        mqttTest2.doConnect();
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class PushCallback implements MqttCallback {
    private String threadId;
    public PushCallback(String threadId){
        this.threadId = threadId;
    }

    public void connectionLost(Throwable cause) {
        System.out.println(threadId + "connectionLost" );

    }

    public void deliveryComplete(IMqttDeliveryToken token) {
//       System.out.println("deliveryComplete---------" + token.isComplete());
    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String msg = new String(message.getPayload());
        System.out.println(threadId + " " + msg);
    }


}
