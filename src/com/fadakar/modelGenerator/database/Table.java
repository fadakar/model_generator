package com.fadakar.modelGenerator.database;

import java.util.ArrayList;

/**
 * @author Gholamreza Fadakar <fadakargholamreza@gmail.com>
 */
public class Table
{
    private String name;
    private ArrayList<String> fields;
    private ArrayList<String> primaryKeys;
    private ArrayList<String> foreignKeys;

    public Table()
    {
        fields = new ArrayList<String>();
        primaryKeys = new ArrayList<String>();
        foreignKeys = new ArrayList<String>();
    }

    public Table(String name)
    {
        this();
        this.name = name;
    }

    public Table(String name, ArrayList<String> fields)
    {
        this();
        this.name = name;
        this.fields = fields;
    }


    public boolean hasPrimaryKey(String fieldName)
    {
       return primaryKeys.contains(fieldName);
    }

    public boolean hasForeignKey(String fieldName)
    {
        return foreignKeys.contains(fieldName);
    }


    public int getFieldCount()
    {
        return fields.size();
    }

    public int getPrimaryKeyCount()
    {
        return primaryKeys.size();
    }

    public int getForeignKeyCount()
    {
        return foreignKeys.size();
    }

    public void addField(String fieldName)
    {
        if(!fields.contains(fieldName))
            fields.add(fieldName);
    }

    public void removeField(String fieldName)
    {
        if(fields.contains(fieldName))
            fields.remove(fieldName);
    }

    public void addPrimaryKey(String primaryKey)
    {
        if(!primaryKeys.contains(primaryKey))
            primaryKeys.add(primaryKey);
    }

    public void removePrimaryKey(String primaryKey)
    {
        if(primaryKeys.contains(primaryKey))
            primaryKeys.remove(primaryKey);
    }

    public void addForeignKey(String foreignKey)
    {
        if(!foreignKeys.contains(foreignKey))
            foreignKeys.add(foreignKey);
    }

    public void removeForeignKey(String foreignKey)
    {
        if(foreignKeys.contains(foreignKey))
            foreignKeys.remove(foreignKey);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public ArrayList<String> getFields()
    {
        return fields;
    }

    public void setFields(ArrayList<String> fields)
    {
        this.fields = fields;
    }

    public ArrayList<String> getPrimaryKeys()
    {
        return primaryKeys;
    }

    public void setPrimaryKeys(ArrayList<String> primaryKeys)
    {
        this.primaryKeys = primaryKeys;
    }

    public ArrayList<String> getForeignKeys()
    {
        return foreignKeys;
    }

    public void setForeignKeys(ArrayList<String> foreignKeys)
    {
        this.foreignKeys = foreignKeys;
    }

    @Override
    public String toString()
    {
        return "Table{" +
                "name='" + name + '\'' +
                ", fields=" + fields +
                ", primaryKeys=" + primaryKeys +
                ", foreignKeys=" + foreignKeys +
                '}';
    }
}
