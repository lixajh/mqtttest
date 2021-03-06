package com.lix.mqtt;


import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Client {

    public static final String HOST = "tcp://118.24.155.154:1883";
    public static final String TOPIC = "tokudu/yzq124";
    public static final String TOPIC_CLIENTID = "tokudu/clientid/";
    private String clientid = "1234";
    private MqttClient client;
    private MqttConnectOptions options;
    private String userName = "test";
    private String passWord = "test";

    private ScheduledExecutorService scheduler;

    Client(String clientId) {
        this.clientid = clientId;
    }

    //重新链接
    public void startReconnect() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                if (!client.isConnected()) {
                    try {
                        System.out.println("重新连接：" + client.getClientId());
                        client.connect(options);

                        int[] Qos = {0,0};
                        String[] topic1 = {TOPIC, TOPIC_CLIENTID + clientid};
                        client.subscribe(topic1, Qos);

                    } catch (MqttSecurityException e) {
                        e.printStackTrace();
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 0 * 1000, 30 * 1000, TimeUnit.MILLISECONDS);
    }

    private void start() {
        try {
            // host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
            client = new MqttClient(HOST, clientid, new MemoryPersistence());
            // MQTT的连接设置
            options = new MqttConnectOptions();
            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            // 设置连接的用户名
            options.setUserName(userName);
            // 设置连接的密码
            options.setPassword(passWord.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(20);
//            options.set
            // 设置回调
//            client.setCallback(new PushCallback("test"));
            client.setCallback(new MqttCallback() {
                public void connectionLost(Throwable throwable) {
                    System.out.println("connectionLost:" + throwable.getMessage());
                    startReconnect();
                }

                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    System.out.println("aaaaaa" + clientid + ":" + mqttMessage.toString());
                }

                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    System.out.println("bbbbb");
                }
            });
            MqttTopic topic = client.getTopic("aaaa");
            //setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息
            options.setWill(topic, "close".getBytes(), 0, true);

            client.connect(options);
            //订阅消息
            int[] Qos = {0,0};
            String[] topic1 = {TOPIC, TOPIC_CLIENTID + clientid};
//            String[] topic1 = {TOPIC};
            client.subscribe(topic1, Qos);
        } catch (Exception e) {
            System.out.println("94");
            e.printStackTrace();
            startReconnect();
        }
    }

    public void reconnect() {

    }

    public void disconnect() {
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws MqttException, SQLException, InterruptedException {
//        Client client = new Client("1234");
//        client.dostart();
        PersonDao personDao = new PersonDaoImpl();
        List<Device> all = personDao.findAll();
        int i=0;
        for (Device d : all) {
            if ("test".equals(d.getUsername())) {
                Client client1 = new Client(d.getClientid());
                client1.dostart();
                i++;
                if (i>1000){
                    Thread.sleep(1000);
                    i = 0;
                }
            }
        }


//        Client client1 = new Client("1235");
//        client1.dostart();
//        Client client2 = new Client("1236");
//        client2.dostart();
    }

    public void dostart() {
        new Thread(new Runnable() {
            public void run() {
                start();
            }
        }).start();
    }
}

