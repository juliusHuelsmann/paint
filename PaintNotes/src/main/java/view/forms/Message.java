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


import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import control.forms.CMessage;
import view.util.RoundedBorder;
import view.util.mega.MButton;
import view.util.mega.MPanel;
import model.settings.ViewSettings;


/**
 * View class for informing the user about errors, warnings, additional info
 * or help messages.
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public final class Message extends MPanel {

    
    /**
     * The only instance of this class. Is never given to other class but only
     * used inside.
     */
    private static Message instance;
    
    /*
     * graphical user interface stuff
     */

    /**
     * JTextArea containing the message.
     */
    private JTextArea jta_text;
    
    
    /**
     * MButton for hiding the message.
     */
    private MButton jbtn_hide;
    
    
    /**
     * The border distance of each component.
     */
    private final int borderDistance = 15;
    
    /*
     * settings values
     */
    
    /**
     * The time of visibility after message is printed.
     */
    private final int sleepTime = 7000;
    
    
    /**
     * The thread which waits for hiding Message.
     */
    private Thread t_show;
    
    
    /**
     * Empty utility class constructor.
     */
    public Message() { 
    	initialize();
    }
    
    
    /**
     * Initialize graphical user interface.
     */
    private void initialize() {

        super.setSize(ViewSettings.getSizeMessage());
        super.setLocation(ViewSettings.getMessagelocation());
        super.setOpaque(true);
        super.setLayout(null);
        super.setBackground(Color.darkGray);
        super.setBorder(new RoundedBorder());
        super.setVisible(false);
    
        jbtn_hide = new MButton("OK");
        jbtn_hide.setContentAreaFilled(false);
        jbtn_hide.setOpaque(false);
        jbtn_hide.addActionListener(CMessage.getInstance());
        jbtn_hide.setBorder(BorderFactory.createLineBorder(Color.gray));
        jbtn_hide.setSize(ViewSettings.getSizeMessage().height,
                ViewSettings.getSizeMessage().height - 2 * 2);
        jbtn_hide.setLocation(getWidth() - ViewSettings.getSizeMessage().height 
                - borderDistance, 2);
        jbtn_hide.setFont(ViewSettings.GENERAL_FONT_HEADLINE_2);
        jbtn_hide.setFocusable(false);
        jbtn_hide.setForeground(Color.lightGray);
        super.add(jbtn_hide);
        
        jta_text = new JTextArea();
        jta_text.setVisible(true);
        jta_text.setOpaque(false);
        jta_text.setBorder(null);
        jta_text.setEditable(false);
        jta_text.setFocusable(false);
        jta_text.setForeground(Color.white);
        jta_text.setSize(getWidth() - jbtn_hide.getWidth() - borderDistance * 2,
                getHeight());
        jta_text.setLocation(borderDistance, 0);
        jta_text.setFont(ViewSettings.GENERAL_FONT_ITEM_PLAIN);
        super.add(jta_text);
    
    }
    
    /**
     * Message types that are to be used for printing message.
     */
    public static final int MESSAGE_ID_ERROR = 0, MESSAGE_ID_WARNING = 1, 
    MESSAGE_ID_INFO = 2, MESSAGE_ID_HELP = 3;
    
    
    
    /**
     * Print message of specified messageType to graphical user interface.
     * 
     * @param _messageType the specified type.
     * @param _errorMessage the error message which is printed.
     */
    public static void showMessage(final int _messageType,
            final String _errorMessage) {
        if (instance == null) { 
            instance = new Message();
            instance.initialize();
        }

        instance.jta_text.setText(_errorMessage);
        instance.setVisible(true);
        
        if (instance.t_show != null) {
            
            instance.t_show.interrupt();
        }
        instance.t_show = new Thread() {
            @Override public void run() {
                
                try {
                    for (int i = 0; i < instance.sleepTime; i++) {
                        Thread.sleep(1);
                        instance.repaint();
                    }
                    instance.setVisible(false);
                } catch (InterruptedException e) { 
                    e = null;
                }
            }
        };
        
        instance.t_show.start();
    }
    
    
    
    
    /**
     * Set invisible.
     */
    public static void fadeOut() {

        if (instance.t_show != null) {
            instance.t_show.interrupt();
        }
        instance.setVisible(false);
    }
}
