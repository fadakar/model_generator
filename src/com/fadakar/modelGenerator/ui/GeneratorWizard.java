package com.fadakar.modelGenerator.ui;

import com.fadakar.modelGenerator.database.Database;
import com.fadakar.modelGenerator.generator.TemplateEngine;
import jwizardcomponent.JWizardPanel;
import jwizardcomponent.Utilities;
import jwizardcomponent.frame.JWizardFrame;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


/**
 * @author Gholamreza Fadakar <fadakargholamreza@gmail.com>
 */
public class GeneratorWizard extends JWizardFrame
{
    public static final int PANEL_FIRST = 0;
    public static final int PANEL_DB_SETTING = 1;
    public static final int PANEL_TABLE_SELECT = 2;
    public static final int PANEL_FILES_LIST = 3;

    private Database database;


    public GeneratorWizard()
    {
        super();
        init();
        createConfigIfNotFound();
    }


    private void createConfigIfNotFound()
    {
        String path = TemplateEngine.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        try
        {
            path = URLDecoder.decode(path, "UTF-8");
            path = new File(path).getParentFile().getPath(); //Path of the jar
            path = path + File.separator;
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        path += "config.conf";
        if(!(new File(path).isFile()))
        {
            String defaultConfig = "// ORMapper settings file\n" +
                    "templateFolderName=template\n" +
                    "templateFileName=template\n" +
                    "outFileExtension=php\n";
            try
            {
                PrintWriter writer = new PrintWriter(path);
                writer.append(defaultConfig);
                writer.close();
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }

            JOptionPane.showMessageDialog(this,
                    "Config.conf not found beside the model generator executive file\nCreate config.conf by default values",
                    "ERROR: Not Found Config File",
                    JOptionPane.OK_OPTION);
        }

    }


    private void init()
    {
        this.setTitle("ORMapper File Generator Wizard");

        JWizardPanel panel = null;

        panel = new WizardStep1(getWizardComponents());
        getWizardComponents().addWizardPanel(PANEL_FIRST, panel);

        panel = new WizardStep2(getWizardComponents(),database);
        getWizardComponents().addWizardPanel(PANEL_DB_SETTING, panel);

        panel = new WizardStep3(getWizardComponents(),database);
        getWizardComponents().addWizardPanel(PANEL_TABLE_SELECT,panel);

        panel = new WizardStep4(getWizardComponents());
        getWizardComponents().addWizardPanel(PANEL_FILES_LIST,panel);


        setSize(500, 300);
        Utilities.centerComponentOnScreen(this);
        setResizable(false);
    }


    public static void main(String[] args)
    {
        GeneratorWizard wizard = new GeneratorWizard();
        wizard.setVisible(true);
    }


}
