package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.DefaultEditorKit;
import project.AutoComplete;

public class userInterface extends JFrame implements ActionListener {

    private final JTextArea textArea;
    private final JMenuItem browse;
    private final JToolBar bar;
    JButton browseButton, compileButton;
    JButton CommentBtn = new JButton("Comment");
    JButton UnCommentBtn = new JButton("UnComment");

    public userInterface() {

        // Set the initial size of the window
        setSize(800, 1000);

        // Set the title of the window
        setTitle("Code Scanner");

        // Set the default close operation (exit when it gets closed)
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // center the frame on the monitor
        setLocationRelativeTo(null);

        // Set a default font for the TextArea
        textArea = new JTextArea("", 0, 0);
        //textArea.setFont(new Font("Century Gothic", Font.PLAIN, 12));
        textArea.setTabSize(2);
        //textArea.setFont(new Font("Century Gothic", Font.PLAIN, 12));
        textArea.setTabSize(2);

        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setWrapStyleWord(true);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        textArea.getDocument().putProperty(DefaultEditorKit.EndOfLineStringProperty, "\n");
        AutoComplete ac = new AutoComplete(textArea);

        getContentPane().setLayout(new BorderLayout()); // the BorderLayout bit makes it fill it automatically
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane);
        getContentPane().add(panel);

        browse = new JMenuItem("Open");
        CommentBtn = new JButton("Comment");
        UnCommentBtn = new JButton("UnComment");

        // Open File
        browse.addActionListener(this);

        bar = new JToolBar();
        this.add(bar, BorderLayout.NORTH);
        // used to create space between button groups

        bar.addSeparator();
        bar.addSeparator();

        bar.add(CommentBtn);
        bar.addSeparator();
        bar.addSeparator();
        bar.addSeparator();
        bar.addSeparator();
        bar.addSeparator();
        bar.addSeparator();
        bar.addSeparator();
        bar.addSeparator();
        bar.addSeparator();
        bar.addSeparator();

        bar.add(UnCommentBtn);
        bar.addSeparator();
        bar.addSeparator();
        bar.addSeparator();
        bar.addSeparator();
        bar.addSeparator();
        bar.addSeparator();

        bar.addSeparator();
        bar.addSeparator();
        bar.addSeparator();

        browseButton = new JButton("Browse");
        browseButton.addActionListener(this);
        bar.add(browseButton);
        bar.addSeparator();
        bar.addSeparator();
        bar.addSeparator();
        bar.addSeparator();
        bar.addSeparator();
        bar.addSeparator();
        bar.addSeparator();
        bar.addSeparator();
        bar.addSeparator();
        bar.addSeparator();
        bar.addSeparator();
        bar.addSeparator();
        bar.addSeparator();
        bar.addSeparator();
        bar.addSeparator();
        CommentBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                String t1 = textArea.getSelectedText();
                if (!t1.isBlank()&&!t1.isEmpty()) {
                    
                    String[] arrt = t1.lines().toArray(String[]::new);
                    if (arrt.length > 1) {
                        arrt[0] = "/@" + arrt[0];
                        arrt[arrt.length - 1] = arrt[arrt.length - 1] + "@/";
                    } else if (arrt.length == 1) {
                        arrt[0] = "/^" + arrt[0];
                    }
                    t1 = "";
                    for (int i = 0; i < arrt.length-1; i++) {
                        t1 += arrt[i] + "\n";
                    }
                    t1+=arrt[arrt.length-1];
                }
                textArea.replaceSelection(t1);
            }
        });
        UnCommentBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                String t1 = textArea.getSelectedText();
                if (t1.startsWith("/^")) {
                    t1 = t1.substring(2);
                } else if (t1.startsWith("/@") && t1.endsWith("@/")) {
                    t1 = t1.substring(2, t1.length() - 2);
                }
                textArea.replaceSelection(t1);
            }
        });

        compileButton = new JButton("Compile");
        compileButton.addActionListener(this);
        bar.add(compileButton);
        bar.addSeparator();
        bar.addSeparator();

    }

    // Make the TextArea available to the autocomplete handler
    protected JTextArea getEditor() {
        return textArea;
    }

    public void actionPerformed(ActionEvent e) {

        // If the source was the "analyze" file option
        if (e.getSource() == compileButton) {

            String[] s = textArea.getText().split("\\r?\\n");
            ArrayList<String> arrList = new ArrayList<>(Arrays.asList(s));

            StringBuilder source = new StringBuilder();
            for (String value : arrList) {
                source.append(value).append("\n");
            }

            lexicalAnalyzer l = new lexicalAnalyzer(source.toString());
            ArrayList<lexicalAnalyzer.Token> res = l.printTokens();
            output.show(res);

        } // If the source was the "open" option
        else if (e.getSource() == browse || e.getSource() == browseButton) {

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(".")); //sets current directory
            int response = fileChooser.showOpenDialog(this); //select file to open
            if (response == JFileChooser.APPROVE_OPTION) {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                Scanner scanner = null;
                try {
                    scanner = new Scanner(file);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
                }
                StringBuilder source = new StringBuilder();
                while (true) {
                    assert scanner != null;
                    if (!scanner.hasNext()) {
                        break;
                    }
                    source.append(scanner.nextLine()).append("\n");
                }
                System.out.println(source);
                lexicalAnalyzer l = new lexicalAnalyzer(source.toString());
                ArrayList<lexicalAnalyzer.Token> res = l.printTokens();
                output.show(res);
            }
        }
    }
}
