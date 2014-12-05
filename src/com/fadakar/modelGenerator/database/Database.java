package com.fadakar.modelGenerator.database;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * @author Gholamreza Fadakar <fadakargholamreza@gmail.com>
 */
public class Database
{
    private DatabaseConnection databaseConnection;
    private ArrayList<Table> tables;
    private String tablePrifix;

    public Database(DatabaseConnection databaseConnection)
    {
        this.databaseConnection = databaseConnection;
    }


    public Database(String hostname,int port,String username,String password,String databaseName,String tablePrifix)
    {
        tables = new ArrayList<Table>();
        databaseConnection = new DatabaseConnection(
                hostname,
                port,
                username,
                password,
                databaseName
        );
        this.tablePrifix = tablePrifix;
    }

    public void open() throws Exception
    {
        // connect to database
        databaseConnection.openConnection();
        fetchTables();
    }


    private void fetchTables()
    {
        try
        {
            // fetch database table names
            DatabaseMetaData metaData = databaseConnection.getConnection().getMetaData();

            String[] types = { "TABLE" };

            ResultSet resultSet = metaData.getTables(databaseConnection.getDatabaseName(), null, "%", types);
            String tableName = "";
            String tableNameWithOutPrefix;

            while (resultSet.next())
            {
                tableName = resultSet.getString(3);
                tableNameWithOutPrefix = tableName;

                if(tablePrifix.length() > 0)
                {
                    if (tableName.startsWith(tablePrifix))
                    {
                        int startLen = tablePrifix.length();
                        tableNameWithOutPrefix = tableName.substring(startLen, tableName.length());
                    }
                }

                Table table = new Table(tableNameWithOutPrefix);

               // fetch table columns name , primary keys , foreign keys
                ResultSet resCols = metaData.getColumns(databaseConnection.getDatabaseName(), null, tableName, "%");
                ResultSet resPrimaryKeys = metaData.getPrimaryKeys(databaseConnection.getDatabaseName(),null,tableName);
                ResultSet resForeignKeys = metaData.getImportedKeys(databaseConnection.getDatabaseName(), null, tableName);

                // set columns names
                while (resCols.next())
                    table.addField(resCols.getString(4));
                // set primary keys
                while (resPrimaryKeys.next())
                    table.addPrimaryKey(resPrimaryKeys.getString(4));
                // set foreign keys
                while (resForeignKeys.next())
                    table.addForeignKey(resForeignKeys.getString(4));

                tables.add(table);

            }
            resultSet.close();

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public ArrayList<Table> getTables()
    {
        return tables;
    }
}
