import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class FileExport extends JFrame{
    private JPanel panel1;
    private JTextField textField1;
    private JButton okButton;
    private JButton cancelButton;
    private Boolean Ok = false;
    private Boolean Cancel = false;


    public FileExport(Simulador s){
        add(panel1);
        setTitle("Simulador de Eventos");
        setSize(200, 200);
        this.setLocation((int)((Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.getWidth())/ 2), (int)((Toolkit.getDefaultToolkit().getScreenSize().getHeight() - this.getHeight()) / 2));

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                s.ExportVehicleList(true);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                s.ExportVehicleList(false);
            }
        });
    }

    public void setVisible(Boolean Visible) {
        if (Visible)
            setVisible(true);
        else
            setVisible(false);
    }

    public String getTextField1() {
        return textField1.getText();
    }

}
