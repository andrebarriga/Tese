import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.List;
import java.util.ArrayList;
import arduino.Arduino;
import java.lang.*;

public class Simulador extends JFrame {
    private JPanel panel1;
    private JTextField Velocity;
    private JTextField VehicleName;
    private JTextField Time_to_previous_car;
    private JComboBox Layout;
    private JScrollPane SimulatorSequence;
    private JScrollPane TemplateScrollPane;
    private JButton loadSequenceButton;
    private JButton runSequenceButton;
    private JButton stopSequenceButton;
    private JComboBox Number_runs;
    private JTextField Distance_Front_1axis;
    private JTextField Distance_1axis_2axis;
    private JTextField Distance_2axis_3axis;
    private JTextField Distance_3axis_4axis;
    private JTextField Distance_4axis_5axis;
    private JButton Add;
    private JTextField Distance_lastaxis_back;
    private JTextField Height_1axis;
    private JList TemplateList;
    private JList List;
    private JTextField Distance_5axis_6axis;
    private JTextField Distance_6axis_7axis;
    private JTextField Number_runs_value;
    private JButton LoadList;
    private JButton Save_List;
    private JButton Remove;
    private JButton Edit;
    private JButton Add_Template;
    private JButton Remove_Template;
    private JButton Save_TemplateList;
    private JButton Edit_Template;
    private JButton configLayoutsButton;

    List<Vehicle> VehicleList = new ArrayList<Vehicle>();
    List<Vehicle> TemplateVehicleList = new ArrayList<Vehicle>();
    Simulador s = this;
    DefaultListModel ListModel = new DefaultListModel();
    DefaultListModel TemplateListModel = new DefaultListModel();
    FileExport ExportFileframe = new FileExport(s);
    ReadWrite_File file = new ReadWrite_File();
    Arduino ArduinoPort = new Arduino("/dev/cu.usbmodem14101", 9600);
    String path = getPath();

    public Simulador() throws IOException {
        add(panel1);
        setTitle("Simulador de Eventos");
        setVisible(true);
        setSize(1400, 500);
        this.setLocation((int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.getWidth()) / 2), (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight() - this.getHeight()) / 2));

        String[] TemplateVehicle;
        String[] TemplateVehicles = file.ReadFile("Vehicles.txt");

        for (int i = 0; i < TemplateVehicles.length; i++) {
            System.out.print("\n" + TemplateVehicles[i]);
            TemplateVehicle = TemplateVehicles[i].split(" ");
            TemplateListModel.add(i, TemplateVehicle[0]);
            Vehicle vehicle = new Vehicle(TemplateVehicle[0], Float.parseFloat(TemplateVehicle[1]), Float.parseFloat(TemplateVehicle[2]),
                    Float.parseFloat(TemplateVehicle[3]), Float.parseFloat(TemplateVehicle[4]), Float.parseFloat(TemplateVehicle[5]),
                    Float.parseFloat(TemplateVehicle[6]), Float.parseFloat(TemplateVehicle[7]), Float.parseFloat(TemplateVehicle[8]),
                    Float.parseFloat(TemplateVehicle[9]), Float.parseFloat(TemplateVehicle[10]), Float.parseFloat(TemplateVehicle[11]));
            TemplateVehicleList.add(vehicle);
        }

        TemplateList.setModel(TemplateListModel);
        List.setModel(ListModel);

        if (ArduinoPort.openConnection()) {
            System.out.println("Port open");
        } else {
            System.out.println("Port didnt open");
        }

        try {
            File VehicleListFolder = new File(path+"/VehicleLists");
            if (!VehicleListFolder.exists())
                VehicleListFolder.mkdir();

            Thread.sleep(4000);

        } catch (Exception ex) {
            System.out.println(ex);
        }

        Add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (check_nulls()){
                    Vehicle vehicle = Get_Vehicle_from_Gui();
                    VehicleList.add(vehicle);
                    ListModel.add(ListModel.getSize(), vehicle.getName());
                }
            }
        });

        Add_Template.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (check_nulls()){
                    Vehicle vehicle = Get_Vehicle_from_Gui();
                    TemplateVehicleList.add(vehicle);
                    TemplateListModel.add(TemplateListModel.getSize(), vehicle.getName());
                }
            }
        });

        Remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int Index = List.getSelectedIndex();

                if ( Index >= 0) {
                    VehicleList.remove(Index);
                    ListModel.remove(Index);
                }

                if (Index <= List.getLastVisibleIndex())
                    List.setSelectedIndex(Index);
                else
                    List.setSelectedIndex(Index-1);
            }
        });

        Remove_Template.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int Index = TemplateList.getSelectedIndex();

                if (Index >= 0) {
                    TemplateVehicleList.remove(Index);
                    TemplateListModel.remove(Index);
                }

                if (Index <= TemplateList.getLastVisibleIndex())
                    TemplateList.setSelectedIndex(Index);
                else
                    TemplateList.setSelectedIndex(Index-1);
            }
        });

        Edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int Index = List.getSelectedIndex();

                if ( Index >= 0) {
                    if (check_nulls()) {
                        Vehicle vehicle = Get_Vehicle_from_Gui();
                        VehicleList.set(Index, vehicle);
                        ListModel.set(Index, vehicle.getName());
                    }
                }
            }
        });

        Edit_Template.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int Index = TemplateList.getSelectedIndex();

                if ( Index >= 0) {
                    if (check_nulls()) {
                        Vehicle vehicle = Get_Vehicle_from_Gui();
                        TemplateVehicleList.set(Index, vehicle);
                        TemplateListModel.set(Index, vehicle.getName());
                    }
                }
            }
        });

        Save_List.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ExportFileframe.setVisible(true);
            }
        });

        Save_TemplateList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] Vehicles = new String[TemplateListModel.getSize()];

                for (int i = 0; i < TemplateListModel.getSize(); i++) {
                    Vehicles[i] = Get_VehicleString_from_List(TemplateVehicleList, i);
                }

                try {
                    file.WriteFile("/Vehicles.txt", Vehicles);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        LoadList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame();
                frame.setVisible(true);
                frame.setLocation((int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth() - frame.getWidth()) / 2), (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight() - frame.getHeight()) / 2));
                JFileChooser jf = new JFileChooser();
                jf.setCurrentDirectory(new File(path+"/VehicleLists"));
                jf.setFileSelectionMode(JFileChooser.FILES_ONLY);
                FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
                jf.setFileFilter(filter);

                int result = jf.showOpenDialog(frame);

                switch (result) {
                    case JFileChooser.APPROVE_OPTION:
                        String filename = jf.getSelectedFile().getName();

                        try {
                            String[] fileLines = file.ReadFile("VehicleLists" + File.separator + filename);
                            ListModel.clear();

                            for (int i = 0; i < fileLines.length; i++) {
                                String[] Line = fileLines[i].split(" ");
                                ListModel.add(i, Line[0]);
                                Vehicle vehicle = new Vehicle(Line[0], Float.parseFloat(Line[1]), Float.parseFloat(Line[2]),
                                        Float.parseFloat(Line[3]), Float.parseFloat(Line[4]), Float.parseFloat(Line[5]),
                                        Float.parseFloat(Line[6]), Float.parseFloat(Line[7]), Float.parseFloat(Line[8]),
                                        Float.parseFloat(Line[9]), Float.parseFloat(Line[10]), Float.parseFloat(Line[11]));
                                VehicleList.clear();
                                VehicleList.add(vehicle);
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                        System.out.println("Approve (Open or Save) was clicked");
                        break;
                    case JFileChooser.CANCEL_OPTION:
                        System.out.println("Cancel or the close-dialog icon was clicked");
                        break;
                    case JFileChooser.ERROR_OPTION:
                        System.out.println("Error");
                        break;
                }
                frame.setVisible(false);
            }
        });

        Number_runs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Number_runs.getSelectedIndex() == 0) {
                    Number_runs_value.setEditable(true);
                }
                else if (Number_runs.getSelectedIndex() == 1) {
                    Number_runs_value.setEditable(false);
                    Number_runs_value.setText("");
                }
            }
        });

        loadSequenceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] LayoutFile = new String[0];

                try {
                    LayoutFile = file.ReadFile("Layouts.txt");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                int size = 0;
                int totalsize= 0;
                int WheelSize = 12; //Vehicle Wheel Size equal to 12 cm

                if (VehicleList.size() > 0) {
                    int LayoutType = Layout.getSelectedIndex();
                    String[] ToArduino = new String[ListModel.getSize()];
                    String[] Sensor_File_Line = LayoutFile[LayoutType].split(" ");
                    Sensor[] Sensores = new Sensor[Sensor_File_Line.length / 3];

                    for (int i = 0; i < Sensores.length; i++) {
                        Sensores[i] = new Sensor(Sensor_File_Line[i * 3], Float.parseFloat(Sensor_File_Line[i * 3 + 1]), Float.parseFloat(Sensor_File_Line[i * 3 + 2]));
                    }

                    for (int i = 0; i < ListModel.getSize(); i++) {
                        int axis = VehicleList.get(i).getAxis();
                        float[] distances_between_axis = {VehicleList.get(i).getD_1axis_2axis(),VehicleList.get(i).getD_2axis_3axis(),
                                VehicleList.get(i).getD_3axis_4axis(), VehicleList.get(i).getD_4axis_5axis(), VehicleList.get(i).getD_5axis_6axis(), VehicleList.get(i).getD_6axis_7axis()};
                        double[][] Transicao_Sensores = new double[Sensores.length][axis * 2];
                        for (int b = 0; b < Sensores.length; b++) {
                            for (int h = 0; h < axis * 2; h++) {
                                Transicao_Sensores[b][h] = -5;
                            }
                        }
                        System.out.println("Axis: " + axis);
                        float VehicleSize = VehicleList.get(i).getD_front_1axis() + VehicleList.get(i).getD_1axis_2axis() + VehicleList.get(i).getD_2axis_3axis() +
                                VehicleList.get(i).getD_3axis_4axis() + VehicleList.get(i).getD_4axis_5axis() + VehicleList.get(i).getD_5axis_6axis() + VehicleList.get(i).getD_6axis_7axis() +
                                VehicleList.get(i).getD_lastaxis_back();

                        System.out.print(VehicleSize);

                        for (int k = 0; k < Sensores.length; k++) {
                            if (Sensores[k].getType().equals("L1") || Sensores[k].getType().equals("L2") || Sensores[k].getType().equals("L3")) {
                                Transicao_Sensores[k][0] = (Sensores[k].getX_inicial() / 1000000) * 3600 / (VehicleList.get(i).getVelocity()) + VehicleList.get(i).getTime_to_previous_vehicle();
                                Transicao_Sensores[k][1] = (Sensores[k].getX_final() / 1000000 + VehicleSize / 1000) * 3600 / VehicleList.get(i).getVelocity() + VehicleList.get(i).getTime_to_previous_vehicle();
                                size = size + 2;
                                System.out.println(Sensores[k].getType() + ": Open: " + Transicao_Sensores[k][0] + " Close: " + Transicao_Sensores[k][1]);
                            }
                            else if (Sensores[k].getType().equals("P1") || Sensores[k].getType().equals("P2")) {
                                double soma_eixos = 0;

                                for (int j = 0; j < axis; j++) {
                                    Transicao_Sensores[k][j * 2] = ((Sensores[k].getX_inicial() - 120)/ 1000000 + (VehicleList.get(i).getD_front_1axis() + soma_eixos) / 1000) * 3600 / (VehicleList.get(i).getVelocity()) + VehicleList.get(i).getTime_to_previous_vehicle();
                                    Transicao_Sensores[k][j * 2 + 1] = ((Sensores[k].getX_final() + 120)/ 1000000 + (VehicleList.get(i).getD_front_1axis() + soma_eixos) / 1000) * 3600 / (VehicleList.get(i).getVelocity()) + VehicleList.get(i).getTime_to_previous_vehicle();
                                    size = size + 2;

                                    System.out.println(Sensores[k].getType() + ": Open: " + Transicao_Sensores[k][j*2] + " Close: " + Transicao_Sensores[k][j*2+1]);

                                    if (j < (axis - 1)) {
                                        soma_eixos = soma_eixos + distances_between_axis[j];
                                    }
                                }
                            }

                            else if (Sensores[k].getType().equals("DA")) {
                                if (VehicleList.get(i).getHeight() < 1.1) {
                                    Transicao_Sensores[k][0] = (Sensores[k].getX_inicial() / 1000000 + (VehicleList.get(i).getD_front_1axis() + 0.5) / 1000) * 3600 / (VehicleList.get(i).getVelocity()) + VehicleList.get(i).getTime_to_previous_vehicle();
                                    Transicao_Sensores[k][1] = (Sensores[k].getX_final() / 1000000 + (VehicleSize - 0.1) / 1000) * 3600 / (VehicleList.get(i).getVelocity()) + VehicleList.get(i).getTime_to_previous_vehicle();
                                }
                                else if (VehicleList.get(i).getHeight() >= 1.1) {
                                    Transicao_Sensores[k][0] = (Sensores[k].getX_inicial() / 1000000 + (VehicleList.get(i).getD_front_1axis() - VehicleList.get(i).getD_front_1axis()) / 1000) * 3600 / (VehicleList.get(i).getVelocity()) + VehicleList.get(i).getTime_to_previous_vehicle();
                                    Transicao_Sensores[k][1] = (Sensores[k].getX_final() / 1000000 + (VehicleSize - 0.1) / 1000) * 3600 / (VehicleList.get(i).getVelocity()) + VehicleList.get(i).getTime_to_previous_vehicle();
                                }

                                size = size+2;

                                System.out.println(Sensores[k].getType() + ": Open: " + Transicao_Sensores[k][0] + " Close: " + Transicao_Sensores[k][1]);
                            }
                        }

                        ToArduino[i] = "";
                        double anterior = -1;
                        float timer = 0;

                        for (int z = 0; z < size; z++) {

                            double min = 1000;
                            int Sensor = 0;

                            for (int j = 0; j < Sensores.length; j++) {
                                for (int k = 0; k < axis * 2; k++) {
                                    if (Transicao_Sensores[j][k] < min && Transicao_Sensores[j][k] > anterior) {
                                        min = Transicao_Sensores[j][k];
                                        Sensor = j;
                                    }
                                }
                            }

                            if (z > 0)
                                timer = (float) (min - anterior);
                            else
                                timer = (float) min;

                            ToArduino[i] = ToArduino[i] + timer + " " + Sensores[Sensor].getType() + " ";
                            anterior = min;


                        }
                        System.out.println(ToArduino[i]);

                        totalsize = totalsize + size;
                        size = 0;
                    }

                    ArduinoPort.serialWrite("Load" + totalsize + " ");
                    System.out.print("Load" + totalsize + " ");

                    for (int i = 0; i < ListModel.getSize(); i++) {
                        String ToArduino_Split[] = ToArduino[i].split(" ");

                        for (int k = 0; k < ToArduino_Split.length; k++) {
                            ArduinoPort.serialWrite(ToArduino_Split[k] + " ");
                        }
                        System.out.print(ToArduino[i]);
                    }

                    System.out.print("*");
                    ArduinoPort.serialWrite("*");
                }
            }

        });

        runSequenceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String runs = Number_runs_value.getText();

                if (Number_runs.getSelectedIndex() == 1)
                    runs = String.valueOf(1000);

                ArduinoPort.serialWrite("Go" + runs + " ");
            }
        });

        stopSequenceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArduinoPort.serialWrite("Stop");
            }
        });

        List.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int Index = List.getSelectedIndex();

                if (Index >= 0) {
                    TemplateList.clearSelection();
                    Load_vehicle(VehicleList.get(Index));
                }
            }
        });

        TemplateList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int Index = TemplateList.getSelectedIndex();

                if (Index >= 0) {
                    List.clearSelection();
                    Load_vehicle(TemplateVehicleList.get(Index));
                }
            }
        });

        panel1.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
                ArduinoPort.closeConnection();
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {
            }
        });
    }

    public void ExportVehicleList(boolean ok) {

        if (ok) {
            String filename = ExportFileframe.getTextField1();
            String[] Vehicles = new String[ListModel.getSize()];

            for (int i = 0; i < ListModel.getSize(); i++) {

                Vehicles[i] = Get_VehicleString_from_List(VehicleList, i);
            }

            try {
                file.WriteFile("/VehicleLists" + File.separator + filename + ".txt", Vehicles);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        ExportFileframe.setVisible(false);
    }

    private void Load_vehicle(Vehicle vehicle) {

        VehicleName.setText(vehicle.getName());
        Distance_Front_1axis.setText(String.valueOf(vehicle.getD_front_1axis()));
        Distance_1axis_2axis.setText(String.valueOf(vehicle.getD_1axis_2axis()));
        Distance_2axis_3axis.setText(String.valueOf(vehicle.getD_2axis_3axis()));
        Distance_3axis_4axis.setText(String.valueOf(vehicle.getD_3axis_4axis()));
        Distance_4axis_5axis.setText(String.valueOf(vehicle.getD_4axis_5axis()));
        Distance_5axis_6axis.setText(String.valueOf(vehicle.getD_5axis_6axis()));
        Distance_6axis_7axis.setText(String.valueOf(vehicle.getD_6axis_7axis()));
        Distance_lastaxis_back.setText(String.valueOf(vehicle.getD_lastaxis_back()));
        Height_1axis.setText(String.valueOf(vehicle.getHeight()));
        Velocity.setText(String.valueOf(vehicle.getVelocity()));
        Time_to_previous_car.setText(String.valueOf(vehicle.getTime_to_previous_vehicle()));
    }

    private Vehicle Get_Vehicle_from_Gui(){
        Vehicle vehicle = new Vehicle(VehicleName.getText(), Float.parseFloat(Distance_Front_1axis.getText()),
                Float.parseFloat(Distance_1axis_2axis.getText()), Float.parseFloat(Distance_2axis_3axis.getText()),
                Float.parseFloat(Distance_3axis_4axis.getText()), Float.parseFloat(Distance_4axis_5axis.getText()),
                Float.parseFloat(Distance_5axis_6axis.getText()), Float.parseFloat(Distance_6axis_7axis.getText()),
                Float.parseFloat(Distance_lastaxis_back.getText()), Float.parseFloat(Height_1axis.getText()),
                Float.parseFloat(Velocity.getText()), Float.parseFloat(Time_to_previous_car.getText()));

        return vehicle;
    }

    private String Get_VehicleString_from_List(List<Vehicle> vehicle_list, int i){
        String vehicle = vehicle_list.get(i).getName() + " " + vehicle_list.get(i).getD_front_1axis() + " " +
                vehicle_list.get(i).getD_1axis_2axis() + " " + vehicle_list.get(i).getD_2axis_3axis() + " " + vehicle_list.get(i).getD_3axis_4axis() + " " +
                vehicle_list.get(i).getD_4axis_5axis() + " " + vehicle_list.get(i).getD_5axis_6axis() + " " + vehicle_list.get(i).getD_6axis_7axis() + " " +
                vehicle_list.get(i).getD_lastaxis_back() + " " + vehicle_list.get(i).getHeight() + " " + vehicle_list.get(i).getVelocity() + " " +
                vehicle_list.get(i).getTime_to_previous_vehicle();

        return vehicle;
    }

    private boolean check_nulls(){
        //Besides empty check if the values are negative.
        if (!VehicleName.getText().isEmpty() && !Distance_Front_1axis.getText().isEmpty() && !Distance_1axis_2axis.getText().isEmpty() && !Distance_lastaxis_back.getText().isEmpty() &&
                !Height_1axis.getText().isEmpty() && !Velocity.getText().isEmpty() && !Time_to_previous_car.getText().isEmpty()) {
            return true;
        }
        else
            return false;
    }

    private String getPath(){
        String path = "";
        try {
            path = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return path;
    }
}
