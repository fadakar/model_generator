package com.fadakar.modelGenerator.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Gholamreza Fadakar <fadakargholamreza@gmail.com>
 */
public class DatabaseConnection
{
    private String hostName;
    private int port;
    private String userName;
    private String password;
    private String databaseName;
//    private DriverManager driverManager;
    private Connection connection;

    public DatabaseConnection(String hostName, int port,String userName,String password, String databaseName)
    {
       setHostName(hostName);
       setPort(port);
       setUserName(userName);
       setPassword(password);
       setDatabaseName(databaseName);
    }

    public void openConnection() throws Exception
    {
        String connectionString = "jdbc:mysql://"+ hostName +":"+ port +"/"+ databaseName;
        connection = DriverManager.getConnection(connectionString,userName,password);
    }

    public String getHostName()
    {
        return hostName;
    }

    public void setHostName(String hostName)
    {
        this.hostName = hostName;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getDatabaseName()
    {
        return databaseName;
    }

    public void setDatabaseName(String databaseName)
    {
        this.databaseName = databaseName;
    }

    public Connection getConnection()
    {
        return connection;
    }
}
