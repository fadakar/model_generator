package com.fadakar.modelGenerator.ui;

import com.fadakar.modelGenerator.database.Database;
import com.fadakar.modelGenerator.database.DatabaseConnection;
import jwizardcomponent.JWizardComponents;
import jwizardcomponent.JWizardPanel;

import javax.swing.*;
import java.awt.*;

/**
 * @author Gholamreza Fadakar <fadakargholamreza@gmail.com>
 */
public class WizardStep2 extends JWizardPanel
{
    private Database database;
    private JPanel panel;
    private JTextField jTextFieldHostName;
    private JTextField jTextFieldPort;
    private JTextField jTextFieldUserName;
    private JTextField jTextFieldPassword;
    private JTextField jTextFieldDBName;
    private JTextField jTextFieldPrefix;
    private String[] labels = {
            "Host Name : ",
            "Port : ",
            "User Name : ",
            "Password : ",
            "DB Name : ",
            "Remove Prefix : "
    };



    public WizardStep2(JWizardComponents wizardComponents, Database database)
    {
        super(wizardComponents,"Enter database connection setting");
        this.database = database;
        init();
    }

    private void init()
    {
        jTextFieldHostName = new JTextField(20);
        jTextFieldPort = new JTextField(20);
        jTextFieldUserName = new JTextField(20);
        jTextFieldPassword = new JTextField(20);
        jTextFieldDBName = new JTextField(20);
        jTextFieldPrefix = new JTextField(20);



        jTextFieldHostName.setText("localhost");
        jTextFieldPort.setText("3306");
        jTextFieldUserName.setText("root");


        panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        for(int i = 0;i < labels.length;i++)
        {
            c.gridx = 0;
            c.gridy = i;
            JLabel label = new JLabel(labels[i]);
//            label.setHorizontalTextPosition(JLabel.RIGHT);
            panel.add(label, c);
        }

        c.gridx = 1;
        c.gridy = 0;
        panel.add(jTextFieldHostName,c);

        c.gridy = 1;
        panel.add(jTextFieldPort,c);

        c.gridy = 2;
        panel.add(jTextFieldUserName,c);

        c.gridy = 3;
        panel.add(jTextFieldPassword,c);

        c.gridy = 4;
        panel.add(jTextFieldDBName,c);

        c.gridy = 5;
        panel.add(jTextFieldPrefix,c);

        add(panel);
    }


    @Override
    public void update()
    {
        setNextButtonEnabled(true);
        setFinishButtonEnabled(false);
    }

    @Override
    public void next()
    {
        String h = jTextFieldHostName.getText().trim();
        int p = Integer.parseInt(jTextFieldPort.getText().trim());
        String u = jTextFieldUserName.getText().trim();
        String pw = jTextFieldPassword.getText().trim();
        String d = jTextFieldDBName.getText().trim();
        String prefix = jTextFieldPrefix.getText().trim();

        try
        {
            database = new Database(h, p, u, pw, d,prefix);
            database.open();
            switchPanel(GeneratorWizard.PANEL_TABLE_SELECT);
            ((WizardStep3) getWizardComponents().getCurrentPanel()).fillData(database);
        }
        catch (Exception ex)
        {
            StringBuffer message = new StringBuffer();
            message.append("Cannot connect to database please check your connection setting");
            if(ex.getMessage() != null)
                message.append("\n"  + ex.getMessage());
            JOptionPane.showMessageDialog(this,message,"Error!",JOptionPane.ERROR_MESSAGE);
        }
    }
}
