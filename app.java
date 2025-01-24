import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class app {

    private JFrame frame;
    private JTabbedPane tabbedPane;
    private JPanel panelInscripcion, panelSeguimiento, panelInformes;

    // Datos
    private ArrayList<Persona> personas = new ArrayList<>();

    public app() {
        frame = new JFrame("Club de Tenis");
        tabbedPane = new JTabbedPane();

        // Crear paneles
        panelInscripcion = crearPanelInscripcion();
        panelSeguimiento = crearPanelSeguimiento();
        panelInformes = crearPanelInformes();

        // Agregar paneles a tabbedPane
        tabbedPane.addTab("Inscripción", panelInscripcion);
        tabbedPane.addTab("Seguimiento", panelSeguimiento);
        tabbedPane.addTab("Informes", panelInformes);

        // Configuración de la ventana principal
        frame.add(tabbedPane);
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private JPanel crearPanelInscripcion() {
        JPanel panel = new JPanel(new BorderLayout());

        // Formulario de inscripción
        JPanel formPanel = new JPanel(new GridLayout(6, 2));
        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField();
        JLabel lblEdad = new JLabel("Edad:");
        JTextField txtEdad = new JTextField();
        JLabel lblTelefono = new JLabel("Teléfono:");
        JTextField txtTelefono = new JTextField();
        JButton btnRegistrar = new JButton("Registrar");

        formPanel.add(lblNombre);
        formPanel.add(txtNombre);
        formPanel.add(lblEdad);
        formPanel.add(txtEdad);
        formPanel.add(lblTelefono);
        formPanel.add(txtTelefono);
        formPanel.add(new JLabel());
        formPanel.add(btnRegistrar);

        // Tabla de personas
        String[] columnNames = {"Nombre", "Edad", "Teléfono", "Estado de Pago"};
        DefaultTableModel tableModel = new DefaultTableModel(new Object[][]{}, columnNames);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Botones de acción
        JPanel actionPanel = new JPanel(new FlowLayout());
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnModificarPago = new JButton("Modificar Pago");

        actionPanel.add(btnEditar);
        actionPanel.add(btnEliminar);
        actionPanel.add(btnModificarPago);

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.SOUTH);

        // Acción del botón Registrar
        btnRegistrar.addActionListener(e -> {
            try {
                String nombre = txtNombre.getText();
                int edad = Integer.parseInt(txtEdad.getText());
                String telefono = txtTelefono.getText();

                personas.add(new Persona(nombre, edad, telefono));
                tableModel.addRow(new Object[]{nombre, edad, telefono, "Al día"});

                JOptionPane.showMessageDialog(frame, "Persona registrada con éxito.");
                txtNombre.setText("");
                txtEdad.setText("");
                txtTelefono.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Por favor, ingrese datos válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Acción del botón Editar
        btnEditar.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String nuevoNombre = JOptionPane.showInputDialog("Nuevo nombre:", tableModel.getValueAt(selectedRow, 0));
                String nuevaEdad = JOptionPane.showInputDialog("Nueva edad:", tableModel.getValueAt(selectedRow, 1));
                String nuevoTelefono = JOptionPane.showInputDialog("Nuevo teléfono:", tableModel.getValueAt(selectedRow, 2));

                try {
                    int edad = Integer.parseInt(nuevaEdad);

                    tableModel.setValueAt(nuevoNombre, selectedRow, 0);
                    tableModel.setValueAt(edad, selectedRow, 1);
                    tableModel.setValueAt(nuevoTelefono, selectedRow, 2);

                    Persona persona = personas.get(selectedRow);
                    persona.setNombre(nuevoNombre);
                    persona.setEdad(edad);
                    persona.setTelefono(nuevoTelefono);

                    JOptionPane.showMessageDialog(frame, "Registro actualizado con éxito.");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Edad inválida.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Seleccione una fila para editar.");
            }
        });

        // Acción del botón Eliminar
        btnEliminar.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                tableModel.removeRow(selectedRow);
                personas.remove(selectedRow);
                JOptionPane.showMessageDialog(frame, "Registro eliminado con éxito.");
            } else {
                JOptionPane.showMessageDialog(frame, "Seleccione una fila para eliminar.");
            }
        });

        // Acción del botón Modificar Pago
        btnModificarPago.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Persona persona = personas.get(selectedRow);
                String nuevoEstado = (String) JOptionPane.showInputDialog(
                        frame,
                        "Seleccione el nuevo estado de pago:",
                        "Modificar Pago",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new String[]{"Al día", "En deuda"},
                        persona.getEstadoPago()
                );

                if (nuevoEstado != null) {
                    persona.setEstadoPago(nuevoEstado);
                    tableModel.setValueAt(nuevoEstado, selectedRow, 3);
                    JOptionPane.showMessageDialog(frame, "Estado de pago actualizado.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Seleccione una fila para modificar el estado de pago.");
            }
        });

        return panel;
    }

    private JPanel crearPanelSeguimiento() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel label = new JLabel("Registro de asistencia y torneos", JLabel.CENTER);
        JButton btnAsistencia = new JButton("Registrar Asistencia");
        JButton btnTorneo = new JButton("Registrar Participación en Torneo");

        // Panel de botones
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.add(btnAsistencia);
        btnPanel.add(btnTorneo);

        panel.add(label, BorderLayout.NORTH);
        panel.add(btnPanel, BorderLayout.CENTER);

        btnAsistencia.addActionListener(e -> {
            String nombre = JOptionPane.showInputDialog("Nombre de la persona:");
            boolean found = false;

            for (Persona persona : personas) {
                if (persona.getNombre().equalsIgnoreCase(nombre)) {
                    persona.incrementarAsistencia();
                    JOptionPane.showMessageDialog(frame, "Asistencia registrada para " + nombre);
                    found = true;
                    break;
                }
            }

            if (!found) {
                JOptionPane.showMessageDialog(frame, "Persona no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnTorneo.addActionListener(e -> {
            String nombre = JOptionPane.showInputDialog("Nombre de la persona:");
            String posicionStr = JOptionPane.showInputDialog("Posición obtenida:");
            boolean found = false;

            try {
                int posicion = Integer.parseInt(posicionStr);

                for (Persona persona : personas) {
                    if (persona.getNombre().equalsIgnoreCase(nombre)) {
                        persona.registrarTorneo(posicion);
                        JOptionPane.showMessageDialog(frame, "Participación en torneo registrada.");
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    JOptionPane.showMessageDialog(frame, "Persona no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Posición inválida.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel crearPanelInformes() {
        JPanel panel = new JPanel(new GridLayout(4, 1));
        JButton btnInformeAsistencia = new JButton("Informe de Asistencia");
        JButton btnInformeTorneos = new JButton("Informe de Torneos");
        JButton btnInformePagos = new JButton("Informe de Pagos");

        panel.add(btnInformeAsistencia);
        panel.add(btnInformeTorneos);
        panel.add(btnInformePagos);

        btnInformeAsistencia.addActionListener(e -> {
            StringBuilder informe = new StringBuilder("Informe de Asistencia:\n");
            for (Persona persona : personas) {
                informe.append(persona.getNombre()).append(": ").append(persona.getAsistencias()).append(" asistencias\n");
            }
            JOptionPane.showMessageDialog(frame, informe.toString());
        });

        btnInformeTorneos.addActionListener(e -> {
            StringBuilder informe = new StringBuilder("Informe de Torneos:\n");
            for (Persona persona : personas) {
                informe.append(persona.getNombre()).append(": Participaciones: ").append(persona.getTorneosParticipados())
                        .append(", Mejor posición: ").append(persona.getMejorPosicion())
                        .append(", Promedio de posiciones: ").append(persona.getPromedioPosiciones()).append("\n");
            }
            JOptionPane.showMessageDialog(frame, informe.toString());
        });

        btnInformePagos.addActionListener(e -> {
            StringBuilder informe = new StringBuilder("Informe de Pagos Mensuales:\n");
            for (Persona persona : personas) {
                informe.append(persona.getNombre()).append(": ").append(persona.getEstadoPago()).append("\n");
            }
            JOptionPane.showMessageDialog(frame, informe.toString());
        });

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(app::new);
    }
}

class Persona {
    private String nombre;
    private int edad;
    private String telefono;
    private int asistencias;
    private int torneosParticipados;
    private int mejorPosicion;
    private double sumaPosiciones;
    private String estadoPago;

    public Persona(String nombre, int edad, String telefono) {
        this.nombre = nombre;
        this.edad = edad;
        this.telefono = telefono;
        this.asistencias = 0;
        this.torneosParticipados = 0;
        this.mejorPosicion = Integer.MAX_VALUE; // Inicializar con un valor alto.
        this.sumaPosiciones = 0;
        this.estadoPago = "Al día";
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getAsistencias() {
        return asistencias;
    }

    public void incrementarAsistencia() {
        this.asistencias++;
    }

    public void registrarTorneo(int posicion) {
        this.torneosParticipados++;
        this.sumaPosiciones += posicion;
        this.mejorPosicion = Math.min(this.mejorPosicion, posicion);
    }

    public int getTorneosParticipados() {
        return torneosParticipados;
    }

    public int getMejorPosicion() {
        return mejorPosicion == Integer.MAX_VALUE ? 0 : mejorPosicion;
    }

    public double getPromedioPosiciones() {
        return torneosParticipados > 0 ? sumaPosiciones / torneosParticipados : 0;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }
}