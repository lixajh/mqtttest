package com.lix.mqtt;


import java.sql.SQLException;
import java.util.UUID;

public class Device {
    private int id;
    private String username;
    private String clientid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", clientid='" + clientid + '\'' +
                '}';
    }

    public static void main(String[] args) {
        PersonDao personDao = new PersonDaoImpl();
        for (int i=0; i<1000; i++){
            Device device = new Device();
            device.setUsername("test");
            device.setClientid(UUID22.getUUID22());
            try {
                personDao.add(device);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}