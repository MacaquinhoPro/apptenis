import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

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
        JPanel formPanel = new JPanel(new GridLayout(7, 2)); // Ahora tenemos una fila extra para el ID
        JLabel lblID = new JLabel("ID:");
        JTextField txtID = new JTextField();
        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField();
        JLabel lblEdad = new JLabel("Edad:");
        JTextField txtEdad = new JTextField();
        JLabel lblTelefono = new JLabel("Teléfono:");
        JTextField txtTelefono = new JTextField();
        JButton btnRegistrar = new JButton("Registrar");

        formPanel.add(lblID);
        formPanel.add(txtID);
        formPanel.add(lblNombre);
        formPanel.add(txtNombre);
        formPanel.add(lblEdad);
        formPanel.add(txtEdad);
        formPanel.add(lblTelefono);
        formPanel.add(txtTelefono);
        formPanel.add(new JLabel());
        formPanel.add(btnRegistrar);

        // Tabla de personas
        String[] columnNames = {"ID", "Nombre", "Edad", "Teléfono", "Estado de Pago"};
        DefaultTableModel tableModel = new DefaultTableModel(new Object[][]{}, columnNames) {
            // Sobrescribimos el método isCellEditable para hacerlo no editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Todas las celdas son de solo lectura
            }
        };
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
            String idStr = txtID.getText().trim();
            String nombre = txtNombre.getText().trim();
            String edadStr = txtEdad.getText().trim();
            String telefono = txtTelefono.getText().trim();
        
            // Verificación de formato de ID (debe ser un número entero positivo)
            if (!idStr.matches("\\d+")) {
                JOptionPane.showMessageDialog(frame, "El ID debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        
            try {
                int id = Integer.parseInt(idStr);
        
                // Comprobación de ID único
                boolean idExistente = personas.stream().anyMatch(p -> p.getId() == id);
                if (idExistente) {
                    JOptionPane.showMessageDialog(frame, "El ID ya existe. Por favor, ingrese uno diferente.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
        
                int edad = Integer.parseInt(edadStr);
                if (edad < 10) {
                    JOptionPane.showMessageDialog(frame, "La edad debe ser mayor a 10 años.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
        
                // Validación del número de teléfono (solo dígitos y longitud entre 7 y 10)
                if (!telefono.matches("\\d{7,10}")) {
                    JOptionPane.showMessageDialog(frame, "El teléfono debe contener entre 7 y 10 dígitos numéricos.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
        
                // Registro de la persona
                Persona persona = new Persona(id, nombre, edad, telefono);
                personas.add(persona);
                tableModel.addRow(new Object[]{id, nombre, edad, telefono, "Al día"});
        
                JOptionPane.showMessageDialog(frame, "Persona registrada con éxito.");
                txtID.setText("");
                txtNombre.setText("");
                txtEdad.setText("");
                txtTelefono.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Por favor, ingrese una edad válida.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        

        // Acción del botón Editar
        btnEditar.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String nuevoNombre = JOptionPane.showInputDialog("Nuevo nombre:", tableModel.getValueAt(selectedRow, 1));
                String nuevaEdad = JOptionPane.showInputDialog("Nueva edad:", tableModel.getValueAt(selectedRow, 2));
                String nuevoTelefono = JOptionPane.showInputDialog("Nuevo teléfono:", tableModel.getValueAt(selectedRow, 3));

                if (nuevoNombre != null && !nuevoNombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+")) {
                    JOptionPane.showMessageDialog(frame, "El nombre solo puede contener letras.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    int edad = Integer.parseInt(nuevaEdad);

                    if (!nuevoTelefono.matches("\\d{7,10}")) {
                        JOptionPane.showMessageDialog(frame, "El teléfono debe contener entre 7 y 10 dígitos.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    tableModel.setValueAt(nuevoNombre, selectedRow, 1);
                    tableModel.setValueAt(edad, selectedRow, 2);
                    tableModel.setValueAt(nuevoTelefono, selectedRow, 3);

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
                    tableModel.setValueAt(nuevoEstado, selectedRow, 4);
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
            String idStr = JOptionPane.showInputDialog("ID de la persona:");
            boolean found = false;

            try {
                int id = Integer.parseInt(idStr);

                for (Persona persona : personas) {
                    if (persona.getId() == id) {
                        persona.incrementarAsistencia();
                        JOptionPane.showMessageDialog(frame, "Asistencia registrada.");
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    JOptionPane.showMessageDialog(frame, "Persona no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnTorneo.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog("ID de la persona:");
            String posicionStr = JOptionPane.showInputDialog("Posición obtenida:");
            boolean found = false;

            try {
                int id = Integer.parseInt(idStr);
                int posicion = Integer.parseInt(posicionStr);

                for (Persona persona : personas) {
                    if (persona.getId() == id) {
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
                JOptionPane.showMessageDialog(frame, "Posición o ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
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
    private int id;
    private String nombre;
    private int edad;
    private String telefono;
    private int asistencias;
    private int torneosParticipados;
    private int mejorPosicion;
    private double sumaPosiciones;
    private String estadoPago;

    public Persona(int id, String nombre, int edad, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.telefono = telefono;
        this.asistencias = 0;
        this.torneosParticipados = 0;
        this.mejorPosicion = Integer.MAX_VALUE; // Inicializar con un valor alto.
        this.sumaPosiciones = 0;
        this.estadoPago = "Al día";
    }

    public int getId() {
        return id;
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