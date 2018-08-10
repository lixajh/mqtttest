package com.lix.mqtt;


import java.sql.SQLException;
import java.util.List;


public interface PersonDao {
    // 添加方法
    public void add(Device p) throws SQLException;

    // 更新方法
    public void update(Device p) throws SQLException;

    // 删除方法
    public void delete(int id) throws SQLException;

    // 查找方法
    public Device findById(int id) throws SQLException;

    // 查找所有
    public List<Device> findAll() throws SQLException;

    // 查询有几条记录
    public long personCount() throws SQLException;

}
