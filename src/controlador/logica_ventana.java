package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import vista.ventana;
import modelo.*;

public class logica_ventana implements ActionListener, ListSelectionListener, ItemListener {

	private ventana delegado;
	private String nombres, email, telefono, categoria = "";
	private persona persona;
	private List<persona> contactos;
	private boolean favorito = false;

	public logica_ventana(ventana delegado) {
		this.delegado = delegado;
		cargarContactosRegistrados();
		this.delegado.btn_add.addActionListener(this);
		this.delegado.btn_eliminar.addActionListener(this);
		this.delegado.btn_modificar.addActionListener(this);
		this.delegado.lst_contactos.addListSelectionListener(this);
		this.delegado.cmb_categoria.addItemListener(this);
		this.delegado.chb_favorito.addItemListener(this);
		
		this.delegado.itemAgregar.addActionListener(e -> this.delegado.btn_add.doClick());
		this.delegado.itemModificar.addActionListener(e -> this.delegado.btn_modificar.doClick());
		this.delegado.itemEliminar.addActionListener(e -> this.delegado.btn_eliminar.doClick());

		this.delegado.contentPane.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					delegado.menuVentana.show(delegado.contentPane, e.getX(), e.getY());
				}
			}
		});

		// Inicializar tabla de estadísticas
		cargarTablaEstadisticas();

		// Agregar filtro al campo de búsqueda (si se usa uno en estadísticas)
		if (delegado.txt_buscar != null && delegado.tabla_estadisticas != null) {
			TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(delegado.modeloEstadisticas);
			delegado.tabla_estadisticas.setRowSorter(sorter);
			delegado.txt_buscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
				public void insertUpdate(javax.swing.event.DocumentEvent e) {
					filtrar();
				}

				public void removeUpdate(javax.swing.event.DocumentEvent e) {
					filtrar();
				}

				public void changedUpdate(javax.swing.event.DocumentEvent e) {
					filtrar();
				}

				private void filtrar() {
					sorter.setRowFilter(RowFilter.regexFilter(delegado.txt_buscar.getText()));
				}
			});
		}

		// Exportar CSV desde botón si existe
		if (delegado.btn_exportar_csv != null) {
			delegado.btn_exportar_csv.addActionListener(e -> exportarCSV());
		}
	}

	private void incializacionCampos() {
		nombres = delegado.txt_nombres.getText();
		email = delegado.txt_email.getText();
		telefono = delegado.txt_telefono.getText();
	}

	private void cargarContactosRegistrados() {
		try {
			contactos = new personaDAO(new persona()).leerArchivo();
			DefaultListModel<String> modelo = new DefaultListModel<>();
			for (persona contacto : contactos) {
				modelo.addElement(contacto.formatoLista());
			}
			delegado.lst_contactos.setModel(modelo);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(delegado, "Existen problemas al cargar todos los contactos");
		}
	}

	private void limpiarCampos() {
		delegado.txt_nombres.setText("");
		delegado.txt_telefono.setText("");
		delegado.txt_email.setText("");
		categoria = "";
		favorito = false;
		delegado.chb_favorito.setSelected(favorito);
		delegado.cmb_categoria.setSelectedIndex(0);
		incializacionCampos();
		cargarContactosRegistrados();
		cargarTablaEstadisticas();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		incializacionCampos();

		if (e.getSource() == delegado.btn_add) {
			if ((!nombres.equals("")) && (!telefono.equals("")) && (!email.equals(""))) {
				if ((!categoria.equals("Elija una Categoria")) && (!categoria.equals(""))) {
					persona = new persona(nombres, telefono, email, categoria, favorito);
					new personaDAO(persona).escribirArchivo();
					limpiarCampos();
					JOptionPane.showMessageDialog(delegado, "Contacto Registrado!!!");
				} else {
					JOptionPane.showMessageDialog(delegado, "Elija una Categoria!!!");
				}
			} else {
				JOptionPane.showMessageDialog(delegado, "Todos los campos deben ser llenados!!!");
			}
		} else if (e.getSource() == delegado.btn_eliminar) {
			// Implementar eliminación de contacto aquí
		} else if (e.getSource() == delegado.btn_modificar) {
			// Implementar modificación de contacto aquí
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		int index = delegado.lst_contactos.getSelectedIndex();
		if (index != -1) {
			if (index >= 0) {
				cargarContacto(index);
			}
		}
	}

	private void cargarContacto(int index) {
		delegado.txt_nombres.setText(contactos.get(index).getNombre());
		delegado.txt_telefono.setText(contactos.get(index).getTelefono());
		delegado.txt_email.setText(contactos.get(index).getEmail());
		delegado.chb_favorito.setSelected(contactos.get(index).isFavorito());
		delegado.cmb_categoria.setSelectedItem(contactos.get(index).getCategoria());
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == delegado.cmb_categoria) {
			categoria = delegado.cmb_categoria.getSelectedItem().toString();
		} else if (e.getSource() == delegado.chb_favorito) {
			favorito = delegado.chb_favorito.isSelected();
		}
	}

	private void cargarTablaEstadisticas() {
		if (delegado.tabla_estadisticas == null || delegado.modeloEstadisticas == null || delegado.progressBar == null)
			return;
		delegado.modeloEstadisticas.setRowCount(0);
		for (persona p : contactos) {
			delegado.modeloEstadisticas.addRow(new Object[] { p.getNombre(), p.getTelefono(), p.getEmail(),
					p.getCategoria(), p.isFavorito() ? "Sí" : "No" });
		}

	}

	private void exportarCSV() {
	    delegado.progressBar.setValue(0);
	    FileWriter writer;
	    try {
	        writer = new FileWriter("contactos_exportados.csv");
	        writer.write("Nombre,Teléfono,Email,Categoría,Favorito\n");

	        final int[] i = {0};
	        Timer timer = new Timer(30, null);
	        timer.addActionListener(evt -> {
	            try {
	                if (i[0] < contactos.size()) {
	                    persona p = contactos.get(i[0]);
	                    writer.write(String.format("%s,%s,%s,%s,%s\n",
	                            p.getNombre(), p.getTelefono(), p.getEmail(),
	                            p.getCategoria(), p.isFavorito() ? "Sí" : "No"));
	                    i[0]++;
	                    delegado.progressBar.setValue((int) (((double) i[0] / contactos.size()) * 100));
	                } else {
	                    timer.stop();
	                    writer.close(); // cerrar al final
	                    JOptionPane.showMessageDialog(delegado,
	                            "Contactos exportados correctamente a contactos_exportados.csv");
	                }
	            } catch (IOException ex) {
	                JOptionPane.showMessageDialog(delegado, "Error al escribir el archivo: " + ex.getMessage());
	                timer.stop();
	                try {
	                    writer.close();
	                } catch (IOException ignored) {}
	            }
	        });

	        timer.start();
	    } catch (IOException ex) {
	        JOptionPane.showMessageDialog(delegado, "Error al crear el archivo: " + ex.getMessage());
	    }
	}

}
