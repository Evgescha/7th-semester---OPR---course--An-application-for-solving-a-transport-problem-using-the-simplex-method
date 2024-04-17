package solvv;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Show {

    private JFrame frame;
    private JTextField textField;
    private JTextField textField_1;
    private JTable table;
    private DefaultTableModel model = new DefaultTableModel();
    private JTextField textField_2;
    private String ans="";

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Show window = new Show();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public Show() {
        initialize();
    }


    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 436, 436);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        
        JLabel label = new JLabel("Количество строк");
        label.setFont(new Font("Tahoma", Font.PLAIN, 14));
        label.setBounds(10, 11, 174, 17);
        frame.getContentPane().add(label);
        
        textField = new JTextField();
        textField.setBounds(171, 11, 86, 20);
        frame.getContentPane().add(textField);
        textField.setColumns(10);
        
        JButton button = new JButton("Создать таблицу");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                int row = Integer.parseInt(textField.getText());
                createTable(row);
                }catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Неверное число строк.");
                }
            }
        });
        button.setBounds(265, 7, 137, 23);
        frame.getContentPane().add(button);
        
        table = new JTable();
        table.setFont(new Font("Tahoma", Font.PLAIN, 15));
        table.setBounds(10, 133, 392, 160);
        frame.getContentPane().add(table);
        
        JLabel label_1 = new JLabel("Функция");
        label_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
        label_1.setBounds(10, 295, 86, 23);
        frame.getContentPane().add(label_1);
        
        JLabel label_2 = new JLabel("Примем: 1 15 max");
        label_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
        label_2.setBounds(79, 294, 193, 23);
        frame.getContentPane().add(label_2);
        
        textField_1 = new JTextField();
        textField_1.setBounds(171, 39, 86, 20);
        frame.getContentPane().add(textField_1);
        textField_1.setColumns(10);
        
        JButton button_1 = new JButton("Решить");
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                solve();
            }
        });
        button_1.setBounds(265, 328, 137, 23);
        frame.getContentPane().add(button_1);
        
        JLabel label_3 = new JLabel("Матрица");
        label_3.setFont(new Font("Tahoma", Font.PLAIN, 14));
        label_3.setBounds(10, 81, 86, 20);
        frame.getContentPane().add(label_3);
        
        JLabel label_4 = new JLabel("Пример: 5 15 <= 280");
        label_4.setFont(new Font("Tahoma", Font.PLAIN, 18));
        label_4.setBounds(107, 79, 180, 23);
        frame.getContentPane().add(label_4);
        
//        JButton btnHelp = new JButton("help");
//        btnHelp.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                help();
//            }
//        });
//        btnHelp.setBounds(121, 65, 137, 23);
//        frame.getContentPane().add(btnHelp);
        
        JLabel label_5 = new JLabel("Количество столбцов");
        label_5.setFont(new Font("Tahoma", Font.PLAIN, 14));
        label_5.setBounds(10, 42, 174, 17);
        frame.getContentPane().add(label_5);
        
        textField_2 = new JTextField();
        textField_2.setColumns(10);
        textField_2.setBounds(10, 329, 247, 20);
        frame.getContentPane().add(textField_2);
        
        JButton button_2 = new JButton("Открыть файл");
        button_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });
        button_2.setBounds(265, 41, 137, 23);
        frame.getContentPane().add(button_2);
        
        JButton button_3 = new JButton("Сохранить");
        button_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
        button_3.setBounds(265, 363, 137, 23);
        frame.getContentPane().add(button_3);
    }
    
    void createTable(int row){
        try {
            
            table.removeAll();
            table.setModel(model);
            model.setRowCount(row);
            model.setColumnCount(1);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            table.setRowHeight(160 / row);
        }catch(Exception ex) {
            JOptionPane.showMessageDialog(null, "Ошибка при создании таблицы. \nПроверьте правильность ввода");
        }
    }
    void help() {
        String helpStr = "Каждая строка таблицы - это ограничение.\n"
                + "Знаки '<=','=','>=' заменяются на буквенные, так\n"
                + "'<=' заменяется на 'le'\n"
                + "'=' заменяется на 'eq'\n"
                + "'>='заменяется на 'ge'\n";
        
        JOptionPane.showMessageDialog(null, helpStr);
    }
    void solve() {
        ans="";
        try {
        String func= textField_2.getText()+" . ";
        int col = Integer.parseInt(textField_1.getText());
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                func+=" \n " + table.getValueAt(i, j).toString();
            }
        }
        func=func.replace("<=", "le").replace("=", "eq").replace(">=", "ge");
        ans = Solv.Solve(func, col);
        JOptionPane.showMessageDialog(null, ans);
        }catch(Exception ex) {
            JOptionPane.showMessageDialog(null, "Некоректные данные. Проверьте введёные вами условия.");
            }
        

    }
    void openFile() {
        JFileChooser fileopen = new JFileChooser();
        int ret = fileopen.showDialog(null, "Открыть файл");                
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fileopen.getSelectedFile();
             Scanner sc = null;
            try {
                sc = new Scanner(file);
                //число ограничений, число столбцов
                //функция
                // матрица
                int row = sc.nextInt();
                int col=sc.nextInt();
                sc.nextLine();
                createTable(row);
                textField.setText(row+"");
                textField_1.setText(col+"");
                textField_2.setText(sc.nextLine());
                for(int i=0; i<row;i++)table.setValueAt(sc.nextLine(), i, 0);
                
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Файл не найден или расширенеи не поддерживается.");
                e.printStackTrace();
            }
        }
    }
    void save() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
          File file = fileChooser.getSelectedFile();
          try {
            file.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
          FileWriter fr = null;
//          BufferedWriter br = null;
          
          try{
              fr = new FileWriter(file);
              fr.write(Solv.saveTable(Solv.phase_one));
              fr.write("\n");
              fr.write(Solv.saveTable(Solv.phase_two));
              fr.write("\n");
              fr.write(ans);
              fr.flush();
          } catch (IOException e) {
              e.printStackTrace();
          }
        }
    }
}
