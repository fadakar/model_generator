package com.fadakar.modelGenerator.ui;

import jwizardcomponent.JWizardComponents;
import jwizardcomponent.JWizardPanel;

import javax.swing.*;
import java.awt.*;

/**
 * @author Gholamreza Fadakar <fadakargholamreza@gmail.com>
 */
public class WizardStep1 extends JWizardPanel
{
    public WizardStep1(JWizardComponents wizardComponents)
    {
        super(wizardComponents,"Model Generator");
        init();
    }

    private void init()
    {
        String message = "<html><p>Welcome to model generator wizard<br/>" +
                         "Written by <b>Gholamreza Fadakar</b> in 05/09/2014<br/>Email :<b>fadakargholamreza@gmail.com</b></p>" +
                "<br/><br/><p>First edit your template file(in /template/ folder)<br/> to your own template for generate files</p></html>";
        JPanel panel = new JPanel(new BorderLayout(10,10));
        JLabel label = new JLabel(message,SwingConstants.LEFT);
        panel.add(label,BorderLayout.NORTH);

        add(panel);
    }
}
