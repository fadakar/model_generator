package com.fadakar.modelGenerator.generator;

import com.fadakar.modelGenerator.database.Table;

import java.io.*;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Gholamreza Fadakar <fadakargholamreza@gmail.com>
 */
public class TemplateEngine
{
    private String templateFolderName = "template";
    private String templateFileName = "template";
    private String outFileExt = ".php";
    private ArrayList<String> generatedFiles;
    private ArrayList<String> loopTemplates;
    private Map<String,String> extraVariables;

    public TemplateEngine()
    {
        generatedFiles = new ArrayList<String>();
        extraVariables = new HashMap<String, String>();

        String currentDate = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss").format(new Date()).toString();
        extraVariables.put("date",currentDate);

        readConfig();
    }

    private void readConfig()
    {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(getExecPath() + "config.conf"));
            String line = "";
            while ((line = reader.readLine()) != null)
            {
                if(line.trim().startsWith("//"))
                    continue;
                if(line.startsWith("templateFolderName"))
                {
                    String[] v= line.split("=");
                    if(v[1].trim().length() > 0)
                        templateFolderName = v[1].trim();
                }
                else if(line.startsWith("templateFileName"))
                {
                    String[] v = line.split("=");
                    if(v[1].trim().length() > 0)
                        templateFileName = v[1].trim();
                }
                else if(line.startsWith("outFileExtension"))
                {
                    String[] v = line.split("=");
                    if(v[1].trim().length() > 0)
                        outFileExt = "." + v[1].trim();
                }
            }
            reader.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }


    private String readFile(String path)
    {
        StringBuilder templateFileBuilder = new StringBuilder();
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line = "";
            while ((line = reader.readLine()) != null)
            {
                templateFileBuilder.append(line + "\r\n");
            }
            reader.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return templateFileBuilder.toString();
    }

    private void createDir(String dir)
    {
        if(!dir.endsWith(File.separator))
            dir += File.separator;
        new File(dir).mkdirs(); // create directory
    }

    private String writeFile(String filename, String content, String path)
    {
        try
        {
            path = path.replace(getExecPath() + templateFolderName + File.separator,"");
            path = getExecPath() + "out" + File.separator + path;
            createDir(path);
            path += filename + outFileExt;
            PrintWriter writer = new PrintWriter(path, "UTF-8");
            writer.append(content);
            writer.close();
            generatedFiles.add(path);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return path;
    }


    public String getExecPath()
    {
        String path = null;
        try
        {
            path = TemplateEngine.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            path = URLDecoder.decode(path, "UTF-8");
            path = new File(path).getParentFile().getPath();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return path + File.separator;
    }


    private void getTree(File file, Collection<File> all)
    {
        File[] children = file.listFiles();
        if (children != null)
        {
            for (File child : children)
            {
                all.add(child);
                getTree(child, all);
            }
        }
    }

    private void fetchLoopTemplates(String template)
    {
        loopTemplates = new ArrayList<String>();
        Pattern pattern = Pattern.compile(".*\\[\\[(.*?)\\]\\].*");
        Matcher matcher = pattern.matcher(template);
        while (matcher.find())
            loopTemplates.add(matcher.group(1));
    }

    private ArrayList<String> getLoopTemplates(String variableKey)
    {
        ArrayList<String> templates = new ArrayList<String>();
        for(String row : loopTemplates)
        {
            Pattern pattern = Pattern.compile("\\{\\{" + variableKey + "(.*?)\\}\\}");
            Matcher matcher = pattern.matcher(row);
            if(matcher.find())
                 templates.add(row);
        }
        if(templates.size() > 0)
            return templates;
        else
            return null;
    }

    private String changeVariable(String templateFile,String variableName , String value)
    {
        String renderValue = templateFile.replaceAll("\\{\\{" + variableName + "\\}\\}", value);
        renderValue = renderValue.replaceAll("\\{\\{" + variableName + "\\|uppercase\\}\\}",value.toUpperCase());
        renderValue = renderValue.replaceAll("\\{\\{" + variableName + "\\|lowercase\\}\\}",value.toLowerCase());

        return renderValue;
    }

    private String changeLoopTemplate(String templateFile,String loopTemplate,String value)
    {
        return templateFile.replace(loopTemplate, value);
    }

    private String generateLoop(ArrayList<String> variables,
                                String fileTemplate,
                                String variableKey)
    {
        ArrayList<String> loopTemplates = getLoopTemplates(variableKey);
        String out = fileTemplate;


        if(loopTemplates != null)
        {
            for (String loopTemplate : loopTemplates)
            {
                StringBuilder loopBuilder = new StringBuilder();
                loopBuilder.append("");
                if(variables.size() > 0)
                {
                    for (String variable : variables)
                    {
                        String codeRow = changeVariable(loopTemplate, variableKey, variable);
                        if (loopBuilder.toString().length() != 0)
                            loopBuilder.append("\t");
                        loopBuilder.append(codeRow + "\r\n");
                    }
                }
                out = changeLoopTemplate(out, "[[" + loopTemplate + "]]", loopBuilder.toString());
            }
        }

        return out;
    }


    private String generateFile(Table table,
                                String template,
                                String path,
                                String filenamePrefix,
                                String fileNameSuffix)
    {
        fetchLoopTemplates(template);
        String fileContent = template;

        // render all primary keys
        fileContent = generateLoop(table.getPrimaryKeys(),fileContent,"primaryKey");

        // render all foreign keys
        fileContent = generateLoop(table.getForeignKeys(),fileContent,"foreignKey");

        // render all field names
        fileContent = generateLoop(table.getFields(),fileContent,"fieldName");

        // render {tableName} in file
        fileContent = changeVariable(fileContent,"tableName",table.getName());

        // change all extra vriable value with keys in template
        for(Map.Entry<String,String> entry : extraVariables.entrySet())
                fileContent = changeVariable(fileContent,entry.getKey(),entry.getValue());

        // write to file
        String fileName = table.getName();
        if(filenamePrefix != null)
            fileName = filenamePrefix + fileName;
        if(fileNameSuffix != null)
            fileName += fileNameSuffix;
        String filePath = writeFile(fileName, fileContent, path);

        return filePath;
    }

    public void generate(ArrayList<Table> tables)
    {
        Collection<File> files = new ArrayList<File>();
        getTree(new File(getExecPath() + templateFolderName), files);

        for(Table table : tables)
        {
            for (File file : files)
            {
                if(new File(file.toString()).isDirectory())
                    continue;
                if(!file.toString().endsWith(".tpl"))
                    continue;

                String[] name = file.getName().split("#");
                String prefix = null;
                String suffix = null;
                if(name.length <= 3 && name.length >= 2)
                {
                    for (String n : name)
                    {
                        if (n.endsWith(".tpl"))
                        {
                            if(!n.contains(templateFileName))
                            {
                                suffix = n;
                                suffix = suffix.substring(0, suffix.length() - 4);
                            }
                        }
                        else if (!n.equals(templateFileName))
                        {
                            prefix = n;
                        }
                    }
                }

                String template = readFile(file.getPath());
                generateFile(table,template,file.getParent() + File.separator,prefix,suffix);
            }
        }
    }

    public ArrayList<String> getGeneratedFiles()
    {
        return generatedFiles;
    }
}
