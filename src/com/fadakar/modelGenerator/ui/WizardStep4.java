package com.fadakar.modelGenerator.ui;

import com.fadakar.modelGenerator.database.Table;
import com.fadakar.modelGenerator.generator.TemplateEngine;
import jwizardcomponent.JWizardComponents;
import jwizardcomponent.JWizardPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * @author Gholamreza Fadakar <fadakargholamreza@gmail.com>
 */
public class WizardStep4 extends JWizardPanel
{
    private ArrayList<Table> selectedTables;
    private JPanel panel;
    private JTextArea jTextAreaPhpClassList;

    public WizardStep4(JWizardComponents wizardComponents)
    {
        super(wizardComponents,"Created php files list");
    }


   public void fillData(ArrayList<Table> selectedTables)
   {
        this.selectedTables = selectedTables;
        init();
   }

    private void init()
    {
        jTextAreaPhpClassList = new JTextArea(10,40);
        jTextAreaPhpClassList.append("Generated files :\r\n");

        JScrollPane jScrollPane = new JScrollPane(
                jTextAreaPhpClassList,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        panel = new JPanel(new BorderLayout(10,10));
        panel.add(jScrollPane, BorderLayout.CENTER);

        add(panel);

        setNextButtonEnabled(false);
        setBackButtonEnabled(false);
        setFinishButtonEnabled(true);

        generateClasses();
    }

    private void generateClasses()
    {
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.generate(selectedTables);
        for(String file : templateEngine.getGeneratedFiles())
            jTextAreaPhpClassList.append(file + "\r\n");
    }


}
