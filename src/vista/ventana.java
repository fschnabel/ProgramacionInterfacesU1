package vista;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import controlador.logica_ventana;

public class ventana extends JFrame {

    public JPanel contentPane;
    public JTextField txt_nombres;
    public JTextField txt_telefono;
    public JTextField txt_email;
    public JTextField txt_buscar;
    public JCheckBox chb_favorito;
    public JComboBox<String> cmb_categoria;
    public JButton btn_add;
    public JButton btn_modificar;
    public JButton btn_eliminar;
    public JList<String> lst_contactos;
    public JScrollPane scrLista;
    public JPopupMenu menuVentana;

    public JTable tabla_estadisticas;
    public DefaultTableModel modeloEstadisticas;
    public JProgressBar progressBar;
    public JButton btn_exportar_csv;
    public JPanel panelEstadisticas;
    public JTabbedPane tabbedPane;
    
    public JMenuItem itemAgregar;
    public JMenuItem itemModificar;
    public JMenuItem itemEliminar;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ventana frame = new ventana();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ventana() {
        setTitle("GESTIÓN DE CONTACTOS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setBounds(100, 100, 1026, 748);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        setContentPane(tabbedPane);

        // === PANEL CONTACTOS ===
        contentPane = new JPanel();
        contentPane.setLayout(null);
        tabbedPane.addTab("Contactos", contentPane);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_C);

        // === PANEL ESTADÍSTICAS ===
        panelEstadisticas = new JPanel();
        panelEstadisticas.setLayout(null);
        tabbedPane.addTab("Estadísticas", panelEstadisticas);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_E);

        JLabel lblNombre = new JLabel("NOMBRES:");
        lblNombre.setBounds(25, 30, 100, 20);
        lblNombre.setFont(new Font("Tahoma", Font.BOLD, 15));
        contentPane.add(lblNombre);

        JLabel lblTelefono = new JLabel("TELÉFONO:");
        lblTelefono.setBounds(25, 70, 100, 20);
        lblTelefono.setFont(new Font("Tahoma", Font.BOLD, 15));
        contentPane.add(lblTelefono);

        JLabel lblEmail = new JLabel("EMAIL:");
        lblEmail.setBounds(25, 110, 100, 20);
        lblEmail.setFont(new Font("Tahoma", Font.BOLD, 15));
        contentPane.add(lblEmail);

        JLabel lblBuscar = new JLabel("BUSCAR POR NOMBRE:");
        lblBuscar.setBounds(25, 661, 192, 13);
        lblBuscar.setFont(new Font("Tahoma", Font.BOLD, 15));
        contentPane.add(lblBuscar);

        txt_nombres = new JTextField();
        txt_nombres.setBounds(130, 30, 400, 25);
        contentPane.add(txt_nombres);

        txt_telefono = new JTextField();
        txt_telefono.setBounds(130, 70, 400, 25);
        contentPane.add(txt_telefono);

        txt_email = new JTextField();
        txt_email.setBounds(130, 110, 400, 25);
        contentPane.add(txt_email);

        txt_buscar = new JTextField();
        txt_buscar.setBounds(220, 650, 770, 30);
        contentPane.add(txt_buscar);

        chb_favorito = new JCheckBox("CONTACTO FAVORITO");
        chb_favorito.setBounds(25, 150, 200, 25);
        chb_favorito.setFont(new Font("Tahoma", Font.PLAIN, 15));
        contentPane.add(chb_favorito);

        cmb_categoria = new JComboBox<>();
        cmb_categoria.setBounds(260, 150, 270, 25);
        contentPane.add(cmb_categoria);

        String[] categorias = { "Elija una Categoria", "Familia", "Amigos", "Trabajo" };
        for (String categoria : categorias) {
            cmb_categoria.addItem(categoria);
        }

        btn_add = new JButton("AGREGAR");
        btn_add.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btn_add.setMnemonic(KeyEvent.VK_A);
        btn_add.setBounds(601, 30, 125, 40);
        contentPane.add(btn_add);

        btn_modificar = new JButton("MODIFICAR");
        btn_modificar.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btn_modificar.setMnemonic(KeyEvent.VK_M);
        btn_modificar.setBounds(736, 30, 125, 40);
        contentPane.add(btn_modificar);

        btn_eliminar = new JButton("ELIMINAR");
        btn_eliminar.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btn_eliminar.setMnemonic(KeyEvent.VK_L);
        btn_eliminar.setBounds(871, 30, 125, 40);
        contentPane.add(btn_eliminar);

        lst_contactos = new JList<>();
        lst_contactos.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lst_contactos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scrLista = new JScrollPane(lst_contactos);
        scrLista.setBounds(25, 200, 971, 430);
        contentPane.add(scrLista);

        menuVentana = new JPopupMenu();
        itemAgregar = new JMenuItem("Agregar contacto");
        itemModificar = new JMenuItem("Modificar contacto");
        itemEliminar = new JMenuItem("Eliminar contacto");

        menuVentana.add(itemAgregar);
        menuVentana.add(itemModificar);
        menuVentana.add(itemEliminar);

        

        // === COMPONENTES PANEL ESTADÍSTICAS ===
        modeloEstadisticas = new DefaultTableModel(new Object[]{"Nombre", "Teléfono", "Email", "Categoría", "Favorito"}, 0);
        tabla_estadisticas = new JTable(modeloEstadisticas);
        JScrollPane scrollEstadisticas = new JScrollPane(tabla_estadisticas);
        scrollEstadisticas.setBounds(25, 50, 950, 500);
        panelEstadisticas.add(scrollEstadisticas);

        btn_exportar_csv = new JButton("Exportar CSV");
        btn_exportar_csv.setBounds(25, 10, 150, 30);
        panelEstadisticas.add(btn_exportar_csv);

        progressBar = new JProgressBar();
        progressBar.setBounds(200, 10, 300, 30);
        progressBar.setStringPainted(true);
        panelEstadisticas.add(progressBar);

        // === CONTROLADOR ===
        logica_ventana lv = new logica_ventana(this);
    }
}
