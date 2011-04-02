/*
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * The Original Code is jvi - vi editor clone.
 *
 * The Initial Developer of the Original Code is Ernie Rael.
 * Portions created by Ernie Rael are
 * Copyright (C) 2000-2010 Ernie Rael.  All Rights Reserved.
 *
 * Contributor(s): Ernie Rael <err@raelity.com>
 */

/*
 * MapCommands.java
 * <p/>
 * NEEDSWORK: getPrefs was package scope.
 * <br/> Should be using a bean here.
 * <br/> With a bean, could more easily make this
 *       a generic text area independent of mappings.
 */
package com.raelity.jvi.swing.ui.options;

import com.raelity.jvi.core.Options;
import com.raelity.jvi.options.OptUtil;
import com.raelity.jvi.options.Option;
import com.raelity.jvi.options.OptionsBean;
import com.raelity.text.XMLUtil;
import java.beans.PropertyVetoException;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * NEEDSWORK:
 * This should use the property descriptors to read/write Options.mapCommands.
 *
 * @author Ernie Rael <err at raelity.com>
 */
public class MapCommands extends javax.swing.JPanel
implements Options.EditControl {
    private final OptionsPanel optionsPanel;
    private final Option opt;
    private String previousText;
    private OptionsBean.General bean;

    /** Creates new form MapCommands */
    public MapCommands(String optName, OptionsPanel optionsPanel)
    {
        assert optName.equals(Options.mapCommands);
        initComponents();
        this.optionsPanel = optionsPanel;
        opt = OptUtil.getOption(optName);
        bean = new OptionsBean.General();
    }

    @Override
    public void start()
    {
        // read property values from backing store
        // and prepare for a new property edit op

        previousText = opt.getValue();
        mappings.setText(previousText);

        XMLUtil xmlFix = new XMLUtil(OptionSheet.IN_RANGE_INVALID_CR,
                                     OptionSheet.IN_RANGE_VALID_CR);

        description.setText("<html>"
            + "<b>"
            + opt.getDisplayName()
            + "</b><br>"
            + xmlFix.utf2xml(opt.getDesc()));
        description.setCaretPosition(0);
    }

    @Override
    public void ok()
    {
        String newText = mappings.getText();
        if(previousText.equals(newText))
            return;

        boolean change = false;
        try {
            bean.setViMapCommands(newText);
            change = true;
        } catch(PropertyVetoException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    ex.getCause() != null
                        ? ex.getCause().getMessage()
                        : ex.getMessage(),
                    "jVi Option Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        if(change && optionsPanel.changeNotify != null) {
            optionsPanel.changeNotify.change();
        }
    }

    @Override
    public void cancel()
    {
        // nothing to undo
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                jSplitPane1 = new javax.swing.JSplitPane();
                jScrollPane1 = new javax.swing.JScrollPane();
                mappings = new javax.swing.JTextArea();
                jScrollPane2 = new javax.swing.JScrollPane();
                description = new javax.swing.JEditorPane();

                jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
                jSplitPane1.setResizeWeight(0.65);

                mappings.setColumns(20);
                mappings.setRows(15);
                jScrollPane1.setViewportView(mappings);

                jSplitPane1.setTopComponent(jScrollPane1);

                description.setBackground(UIManager.getColor("Panel.background"));
                description.setContentType("text/html"); // NOI18N
                description.setEditable(false);
                jScrollPane2.setViewportView(description);

                jSplitPane1.setRightComponent(jScrollPane2);

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
                this.setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                                .addContainerGap())
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                                .addContainerGap())
                );
        }// </editor-fold>//GEN-END:initComponents
        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JEditorPane description;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JScrollPane jScrollPane2;
        private javax.swing.JSplitPane jSplitPane1;
        private javax.swing.JTextArea mappings;
        // End of variables declaration//GEN-END:variables
}
