package view.forms;


/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


//import declarations
import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import view.util.mega.MScrollPane;
import model.settings.State;
import model.settings.ViewSettings;

/**
 * 
 * @author Julius Huelsmann
 * @version "I", "U"
 */
@SuppressWarnings("serial")
public final class VHistory extends JFrame implements Observer {

    
    /**
     * JTextArea for information.
     */
    private JTextArea jta_info;
    
    
    /**
     * constructor of ViewHistory. initializes the components.
     */
    public VHistory() {
        super();
        super.setSize(ViewSettings.SIZE_OF_HISTORY);
        super.setAlwaysOnTop(true);
        super.setLayout(new BorderLayout());
        
        jta_info = new JTextArea("");
        jta_info.setEditable(false);
        jta_info.setOpaque(false);
        jta_info.setFocusable(false);
        jta_info.setBorder(null);
        jta_info.setLineWrap(true);
        jta_info.setFont(ViewSettings.GENERAL_FONT_ITEM_SMALL);

        MScrollPane scrollPane = new MScrollPane(jta_info);
        scrollPane.setAutoscrolls(true);
        scrollPane.setVerticalScrollBarPolicy(
                MScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        super.add(scrollPane, BorderLayout.CENTER);
        
        super.setVisible(true);
        super.setResizable(false);
        jta_info.setText(jta_info.getText() + "\nasdf");
        
        
    }

    
    /**
     * Update The history.
     * @param _arg0 the observable
     * @param _arg1 the argument passed by observable.
     */
    public void update(final Observable _arg0, final Object _arg1) {
        
        String c = (String) _arg1;
        if (c.startsWith("add ")) {
            jta_info.setText(jta_info.getText() + "\n" + c);
        } else if (c.startsWith("remove")) {
            String newText = jta_info.getText().replace(
                    "\n" + c.replaceFirst("remove ", "add "), "");
            
            State.getLogger().info("remove: " + newText);
        }
    }
    
}
