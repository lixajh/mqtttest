package com.lix.mqtt;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;


public class PersonDaoImpl implements PersonDao {
    private QueryRunner runner = null;//查询运行器
    public PersonDaoImpl(){
//        runner = new QueryRunner();
        runner = DBUtils.getQueryRuner();
    }

    //方法：向数据库中添加一条记录
    @Override
    public void add(Device p) throws SQLException {
        String sql = "insert into mqtt_acl(username,clientid,access)values(?,?,?)";
        Connection connection = DBUtils.getConnection();

        runner.update(connection, sql, p.getUsername(), p.getClientid(),1);
        DBUtils.close(null,null,connection);
    }

    //方法：根据id向数据库中修改某条记录
    @Override
    public void update(Device p) throws SQLException {
        Connection connection = DBUtils.getConnection();
        String sql = "update mqtt_acl set username=?,clientid=? where id=?";
        runner.update(connection, sql, p.getUsername(),p.getClientid(),p.getId());
        DBUtils.close(null,null,connection);
    }

    //方法：根据id删除数据库中的某条记录
    @Override
    public void delete(int id) throws SQLException {
        Connection connection = DBUtils.getConnection();
        String sql = "delete from mqtt_acl where id=?";
        runner.update(connection, sql, id);
        DBUtils.close(null,null,connection);
    }


    //方法：使用BeanHandler查询一个对象
    @Override
    public Device findById(int id) throws SQLException {
        Connection connection = DBUtils.getConnection();
        String sql = "select username,id,clientid from mqtt_acl where id=?";
        Device p = runner.query(connection, sql, new BeanHandler<Device>(Device.class),id);
        DBUtils.close(null,null,connection);
        return p;
    }

    //方法：使用BeanListHandler查询所有对象
    @Override
    public List<Device> findAll() throws SQLException {
        Connection connection = DBUtils.getConnection();
        String sql = "select username,id,clientid from mqtt_acl order by id limit 10007 ";
        List<Device> devices = runner.query(connection, sql, new BeanListHandler<Device>(Device.class));
        DBUtils.close(null,null,connection);
        return devices;
    }

    //方法：使用ScalarHandler查询一共有几条记录
    @Override
    public long personCount()throws SQLException{
        Connection connection = DBUtils.getConnection();
        String sql = "select count(id) from mqtt_acl";
        long a = runner.query(connection,sql, new ScalarHandler<Long>());
        DBUtils.close(null,null,connection);
        return a;
    }

}
