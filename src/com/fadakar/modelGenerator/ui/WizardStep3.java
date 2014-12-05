package com.fadakar.modelGenerator.ui;

import com.fadakar.modelGenerator.database.Database;
import com.fadakar.modelGenerator.database.Table;
import com.fadakar.modelGenerator.generator.TemplateEngine;
import jwizardcomponent.JWizardComponents;
import jwizardcomponent.JWizardPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 * @author Gholamreza Fadakar <fadakargholamreza@gmail.com>
 */
public class WizardStep3 extends JWizardPanel
{
    private Database database;
    private static final String[] COLUMNS = {"Table Name", "Create File"};
    private DataModel dataModel;
    private JTable table;
    private DefaultListSelectionModel selectionModel;
    private static final int CHECK_COL = 1;
    private static Object[][] DATA;

    public WizardStep3(JWizardComponents wizardComponents, Database database)
    {
        super(wizardComponents,"Select tables");
        this.database = database;
    }

    private void init()
    {
        if(database != null)
        {
            ArrayList<Table> tables = database.getTables();
            DATA = new Object[tables.size()][2];

            for (int i =0;i < tables.size();i++)
            {
                DATA[i][0] = tables.get(i).getName();
                DATA[i][1] = Boolean.FALSE;
            }

            dataModel = new DataModel(DATA, COLUMNS);
            table = new JTable(dataModel);
        }

        setLayout(new BorderLayout());
        this.add(new JScrollPane(table));
        this.add(new ControlPanel(), BorderLayout.SOUTH);
        table.setPreferredScrollableViewportSize(new Dimension(250, 175));
        selectionModel = (DefaultListSelectionModel) table.getSelectionModel();

        setFinishButtonEnabled(false);
        setNextButtonEnabled(true);
    }

    public void fillData(Database database)
    {
        this.database = database;
        init();
    }

    private ArrayList<Table> getSelectedTables()
    {
        ArrayList<Table> selectedTables = new ArrayList<Table>();
        for(int i =0;i < dataModel.getRowCount();i++)
        {
            if(((Boolean)dataModel.getValueAt(i, CHECK_COL)) == Boolean.TRUE)
            {
                for (Table table : database.getTables())
                {
                    if (table.getName().equals(dataModel.getValueAt(i, 0)))
                    {
                        selectedTables.add(table);
                    }
                }
            }
        }
        return selectedTables;
    }


    @Override
    public void next()
    {
        if(getSelectedTables().size() > 0)
        {
            switchPanel(GeneratorWizard.PANEL_FILES_LIST);
            try
            {
                ((WizardStep4) getWizardComponents().getCurrentPanel()).fillData(getSelectedTables());
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
//                JOptionPane.showMessageDialog(this,ex.getMessage(),"Select table error",JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class DataModel extends DefaultTableModel
    {

        public DataModel(Object[][] data, Object[] columnNames) {
            super(data, columnNames);
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == CHECK_COL) {
                return getValueAt(0, CHECK_COL).getClass();
            }
            return super.getColumnClass(columnIndex);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column == CHECK_COL;
        }
    }

    private class ControlPanel extends JPanel
    {

        public ControlPanel()
        {
            this.add(new JButton(new SelectionAction("Deselect", false)));
            this.add(new JButton(new SelectionAction("Select", true)));
            this.add(new JButton(new SelectionAction("Select All", true)));
            this.add(new JButton(new SelectionAction("Deselect All", false)));
        }
    }

    private class SelectionAction extends AbstractAction {

        boolean value;

        public SelectionAction(String name, boolean value) {
            super(name);
            this.value = value;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            for (int i = 0; i < dataModel.getRowCount(); i++)
            {
                if(e.getActionCommand().toLowerCase().equals("select all") ||
                     e.getActionCommand().toLowerCase().equals("deselect all"))
                {
                    dataModel.setValueAt(value, i, CHECK_COL);
                }
                else
                {
                    if (selectionModel.isSelectedIndex(i))
                    {
                        dataModel.setValueAt(value, i, CHECK_COL);
                    }
                }
            }

        }
    }

}
