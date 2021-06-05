/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.awt.Point;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.DefaultListModel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import javax.swing.text.BadLocationException;

public class AutoComplete extends JPopupMenu {

    SuggestString suggestString = new SuggestString();
    JTextArea textpane;
    DefaultListModel model = new DefaultListModel();
    JList list = new JList(model);
    StringBuffer word = new StringBuffer();
    boolean isShow = false;

    AutoComplete(JTextArea textpane) {
        super();
        this.textpane = textpane;
        removeAll();
        setOpaque(false);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFocusable(false);
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    final int position = textpane.getCaretPosition();
                    try {
                        textpane.getDocument().insertString(position, getSelectedString().substring(word.length()), null);
                    } catch (BadLocationException e2) {
                        e2.printStackTrace();
                    }
                    word.delete(0, word.length());
                    model.clear();
                    setVisible(false);
                    isShow = false;
                }
            }
        });
        textpane.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                int position = textpane.getCaretPosition();
                if (Character.isWhitespace(e.getKeyChar())) {
                    if (e.getKeyChar() == '\n' && isShow) {
                        try {
                            textpane.getDocument().insertString(position, getSelectedString().substring(word.length()), null);
                        } catch (BadLocationException e2) {
                            e2.printStackTrace();
                        }
                        e.consume();
                    }
                    textpane.setEditable(true);
                    word.delete(0, word.length());
                    model.clear();
                    setVisible(false);
                    isShow = false;
                    return;
                } else if (Character.isLetterOrDigit(e.getKeyChar())) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            if (word.length() == 0) {
                                word.append(getWord(position - 1));
                            }
                            word.append(e.getKeyChar());
                            if (word.length() >= 1) {
                                model.clear();
                                if (!updateWordList()) {
                                    setVisible(false);
                                    isShow = false;
                                    return;
                                }
                                isShow = true;
                                showPanel();
                            }
                        }
                    });
                } else {
                    word.delete(0, word.length());
                    model.clear();
                    setVisible(false);
                    isShow = false;
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN && isShow) {                    
                    
                    list.setSelectedIndex(Math.min(list.getModel().getSize() - 1, list.getSelectedIndex() + 1));
                    e.consume();
                } else if (e.getKeyCode() == KeyEvent.VK_UP && isShow) {
                    textpane.setEditable(false);
                    list.setSelectedIndex(Math.max(list.getSelectedIndex() - 1, 0));
                    e.consume();
                }
            }
        });
    }

    private boolean updateWordList() {
        final String[] words = suggestString.serche(word.toString());
        if (words.length < 1) {
            return false;
        }
        for (String str : words) {
            model.addElement(str);
        }
        return true;
    }

    private void showPanel() {
        final int position = textpane.getCaretPosition();
        try {
            
            Point location = textpane.modelToView(position).getLocation();
            setVisible(true);
            setOpaque(true);
            add(list);
            show(textpane, location.x, textpane.getBaseline(0, 0) + location.y + 15);
            list.setSelectedIndex(0);
            textpane.setCaretPosition(position);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                textpane.requestFocusInWindow();
            }
        });
    }

    private String getWord(int index) {
        if (index < 0) {
            return "";
        }
        String text = textpane.getText();
        char word;
        StringBuffer br = new StringBuffer();
        while (!Character.isWhitespace(word = text.charAt(index))) {
            index--;
            br.append(word);
            if (index < 0) {
                br.reverse();
                return br.toString();
            }
        }
        br.reverse();
        return br.toString();
    }

    private String getSelectedString() {
        if (list.getSelectedValue() != null) {
            return (String) list.getSelectedValue();
        }
        return "";
    }
}
