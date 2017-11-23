
package cranshashtable2017;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/******************************************************* 
 * ‘***  Gui
    ‘***  Nick Crans
    ‘******************************************************
    ‘*** Creates the gui and parses the text from the document
    ‘*** 
    ‘*** 
    ‘******************************************************
    ‘*** 10/31/2017
    ‘******************************************************/
public class Gui extends JFrame implements ActionListener
{
    private final GridBagLayout layout;
    private final GridBagConstraints constraints;
    private final JTextArea display;
    private final JTextField enter;
    private final JButton find;
    private final JButton openFile;
    private final JScrollPane scroll;
    private Hashtable hMap;

    /******************************************************
    ‘***  GUI
    ‘***  Nick Crans
    ‘******************************************************
    ‘*** Constructor
    ‘*** Initializes all the components for the window
    ‘******************************************************
    ‘*** 09/29/2017
    ‘******************************************************/
    public Gui()
    {
       super("Hash Table");
       
       layout = new GridBagLayout();
       setLayout(layout);
       constraints = new GridBagConstraints();
       
       /***************************************************************
       Help for this code was found on 
       http://docs.oracle.com/javase/tutorial/uiswing/components/scrollpane.html
       */
       display = new JTextArea("Display", 10, 20);
       display.setEditable(false);
       scroll = new JScrollPane(display);
       /**********************************************************/
       openFile = new JButton("Open File");
       
       openFile.addActionListener(this);
       enter = new JTextField ();
       find = new JButton("Find Word");
       find.addActionListener(this);

       constraints.weightx = 1;
       constraints.weighty = 1;
       constraints.fill = GridBagConstraints.BOTH;
       constraints.gridwidth = GridBagConstraints.REMAINDER;
       addComponents(scroll);

       constraints.gridwidth = 1;
       addComponents(openFile);
       
       addComponents(find);
       addComponents(enter);
    }
    
    /******************************************************
    ‘***  addComponents
    ‘***  Nick Crans
    ‘******************************************************
    ‘*** Adds the components to the window. Takes the component
    ‘*** to be added as an parameter
    ‘******************************************************
    ‘*** 09/29/2017
    ‘******************************************************/
    private void addComponents(Component com)
    {
        layout.setConstraints(com, constraints);
        add(com);
    } 
    
    /******************************************************
    ‘***  actionPerformed
    ‘***  Nick Crans
    ‘******************************************************
    ‘*** Adds functionality to the text area and button.
    ‘*** It also reads in the lines from a text file and parses
    ‘*** the lines into single words with no punctuation.
    *   Also tests if word is in table
    ‘******************************************************
    ‘*** 09/29/2017
    ‘******************************************************/
    @Override
    public void actionPerformed(ActionEvent event)
    {
        if(event.getSource() == openFile)
        {
            JFileChooser choice = new JFileChooser();
            int returnVal = choice.showOpenDialog(this);
            hMap = new Hashtable();
            
            if(returnVal == JFileChooser.APPROVE_OPTION)
            {
                File file = choice.getSelectedFile();
                ArrayList<String> lines = new ArrayList<String>();
                String catchLines = "";
                String temp = "";
                String[] words;
                
                try
                {
                    FileReader read = new FileReader(file.getAbsolutePath());
                    BufferedReader buffedRead = new BufferedReader(read);
         
                    while((catchLines = buffedRead.readLine()) != null)
                        lines.add(catchLines);

                    for(int i = 0; i < lines.size(); i++)
                    {
                        temp += lines.get(i) + " ";
                    }
                    /***********************************************************
                     * Help for this code was found on
                     * https://docs.oracle.com/javase/tutorial/essential/regex/pre_char_classes.html
                     * and https://www.tutorialspoint.com/java/java_string_split.htm
                     **********************************************************/
                    words = temp.split("-|\\s+");
                    
                    for(int j = 0; j < words.length; j++)
                    {
                        String sent = words[j].replaceAll("[^a-zA-Z_0-9]", "").toLowerCase();
                        
                        if(sent.isEmpty())
                        {
                            j++;
                            sent = words[j].replaceAll("[^a-zA-Z_0-9]", "").toLowerCase();
                        }
                        MapEntry entry = new MapEntry(sent, j);
                        if(hMap.contains(entry))
                        {
                            
                        }
                        else
                            hMap.put(j, entry);
                    }
                    //**********************************************************    
                    buffedRead.close();
                    
                    //display.setText("Unique Word Count: "  + "\n");
                    display.append("Unique Words:\n");
                    /*
                    Help for this code was found on https://docs.oracle.com/javase/7/docs/api/java/util/Hashtable.html#elements()
                    
                    */
                    Enumeration items = hMap.elements();
                    
                    while(items.hasMoreElements())
                    {
                        display.append(items.nextElement().toString());
                    }
                }
                catch(IOException e)
                {
                    
                }  
            }
        }
        if(event.getSource() == find)
        {
            String value = enter.getText();
            Enumeration keys = hMap.keys();
            int key;
            if(value.compareTo("") == 0)
            {
                display.append("Please enter a word to find.\n");
            }
            else
            {
                String test;
                while(keys.hasMoreElements())
                {
                    key = (int) keys.nextElement();
                    test = hMap.get(key).toString();
                    test = test.replace("\n", "");
                    if(test.compareToIgnoreCase(value) == 0)
                    {
                        display.append(value + " is in the hash table.\n");
                        break;
                    }
                    else if(!keys.hasMoreElements())
                        display.append(value + " is not in the hash table.\n");
                }
            }
        }
    }
}
