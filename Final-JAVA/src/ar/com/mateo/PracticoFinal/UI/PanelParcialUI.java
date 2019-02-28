package ar.com.mateo.PracticoFinal.UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.button.StandardButtonShaper;

import ar.com.mateo.PracticoFinal.BO.ControlesBO;
import ar.com.mateo.PracticoFinal.BO.ParcialBO;
import ar.com.mateo.PracticoFinal.Model.Parcial;
import ar.com.mateo.PracticoFinal.DAO.ParcialDAO;
import ar.com.mateo.PracticoFinal.Exceptions.ValidarSeleccionException;

public class PanelParcialUI extends JPanel {

	/**
	 * Create the panel
	 */
	private static final long serialVersionUID = 1L;
	private List<Parcial> listaParcial = new ArrayList<Parcial>();
	private ParcialBO pBO = new ParcialBO();
	private TableRowSorter<TableModel> trsfiltro;
	private JTextField textFieldBuscar;

	public PanelParcialUI() {

		setLayout(null);
		setBounds(2, 0, 960, 626);
		setBackground(new Color(102, 102, 102));

		JLabel lblPersonas = new JLabel("PARCIALES");
		lblPersonas.setForeground(Color.WHITE);
		lblPersonas.setFont(new Font("Copperplate Gothic Light", Font.BOLD, 22));
		lblPersonas.setBounds(31, 42, 268, 32);
		add(lblPersonas);

		/**
		 * Defino mis botones
		 */
		JButton btnAgregar = new JButton("Agregar Nota Parcial");
		btnAgregar.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 15));
		btnAgregar.setBounds(108, 529, 222, 48);
		btnAgregar.putClientProperty(SubstanceLookAndFeel.BUTTON_SHAPER_PROPERTY, new StandardButtonShaper());
		add(btnAgregar);

		JButton btnEliminar = new JButton("Eliminar Nota Parcial");
		btnEliminar.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 15));
		btnEliminar.setBounds(634, 529, 222, 48);
		btnEliminar.putClientProperty(SubstanceLookAndFeel.BUTTON_SHAPER_PROPERTY, new StandardButtonShaper());
		add(btnEliminar);

		JButton btnModificar = new JButton("Modificar Nota Parcial");
		btnModificar.putClientProperty(SubstanceLookAndFeel.BUTTON_SHAPER_PROPERTY, new StandardButtonShaper());
		btnModificar.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 15));
		btnModificar.setBounds(375, 529, 213, 48);
		add(btnModificar);

		/**
		 * Defino mi tabla donde se listaran los datos
		 */
		String[] columnas = { "Alumno", "Materia", "Nota Parcial 1", "Nota Parcial 2", "Estado Regularidad" };
		JTable tabla = new JTable() {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int rowIndex, int colIndex) {
				return false;
			}
		};
		tabla.setFont(new Font("Times New Roman", Font.PLAIN, 14));

		DefaultTableModel modelo = new DefaultTableModel();
		modelo.setColumnIdentifiers(columnas);
		tabla.setModel(modelo);

		listaParcial = pBO.list();
		for (Parcial p : listaParcial) {
			modelo.addRow(new Object[] { p.getAlumno(), p.getMateria(), p.getParcial1(), p.getParcial2(),
					p.getEstadoRegularidad() });
		}

		JScrollPane scrollPane = new JScrollPane(tabla, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setFont(new Font("Copperplate Gothic Light", Font.BOLD, 13));
		scrollPane.setBounds(20, 85, 919, 423);
		add(scrollPane);
		scrollPane.setViewportView(tabla);

		/**
		 * Filtro
		 */
		textFieldBuscar = new JTextField((String) null);
		textFieldBuscar.setBackground(Color.WHITE);
		textFieldBuscar.setCaretColor(Color.WHITE);
		textFieldBuscar.setHorizontalAlignment(SwingConstants.LEFT);
		textFieldBuscar.setColumns(10);
		textFieldBuscar.setBorder(UIManager.getBorder("Menu.border"));
		textFieldBuscar.setBounds(564, 38, 304, 20);
		add(textFieldBuscar);
		textFieldBuscar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final KeyEvent e) {
				String cadena = (textFieldBuscar.getText());
				textFieldBuscar.setText(cadena);
				repaint();
				filtrar();
			}
		});

		JLabel label = new JLabel("");
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Copperplate Gothic Light", Font.BOLD, 14));
		label.setBounds(533, 28, 38, 46);
		add(label);
		trsfiltro = new TableRowSorter<TableModel>(tabla.getModel());
		tabla.setRowSorter(trsfiltro);

		/**
		 * Funcionalidades
		 */

		btnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AgregraNotaParcial frame = new AgregraNotaParcial();
				VentanaPrincipal.panelGeneral.removeAll();
				VentanaPrincipal.panelGeneral.updateUI();
				frame.setVisible(true);
				VentanaPrincipal.panelGeneral.add(frame);
			}
		});

		btnModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				int row;
				try {
					row = tabla.getSelectedRow();
					ControlesBO.validarSeleccionLista(row);
					int modelRow = tabla.convertRowIndexToModel(row);
					Parcial parcial = new Parcial();

					Integer parcialModificar = (Integer) modelo.getValueAt(modelRow, 2);
					for (Parcial p : listaParcial) {
						if (p.getParcial1() == (parcialModificar)) {
							parcial = p;
						}
					}
					System.out.println(parcial.getId());
					VentanaPrincipal.panelGeneral.removeAll();
					VentanaPrincipal.panelGeneral.updateUI();

					AgregraNotaParcial frame = new AgregraNotaParcial(parcial);
					frame.setVisible(true);
					VentanaPrincipal.panelGeneral.add(frame);

				} catch (ValidarSeleccionException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage() + " el parcial a modificar", "",
							JOptionPane.WARNING_MESSAGE);
				}

			}
		});

		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int row;
				try {
					row = tabla.getSelectedRow();
					ControlesBO.validarSeleccionLista(row);
					int modelRow = tabla.convertRowIndexToModel(row);
					Parcial parcial = new Parcial();

					Integer ParcialEliminar = (Integer) modelo.getValueAt(modelRow, 2);

					for (Parcial p : listaParcial) {
						if (p.getParcial1()== (ParcialEliminar)) {
							parcial = p;
						}
					}

					int opcion = JOptionPane.showConfirmDialog(null,
							"¿Está seguro que deseas eliminar el parcial " + parcial + "?", "",
							JOptionPane.YES_NO_OPTION);
					if (opcion == JOptionPane.OK_OPTION) {
						pBO.eliminar(parcial);
						modelo.removeRow(tabla.getSelectedRow());
					}

				} catch (ValidarSeleccionException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage() + "la persona que desea eliminar.", "",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});

	}

	public void filtrar() {
		trsfiltro.setRowFilter(RowFilter.regexFilter("(?i)" + textFieldBuscar.getText(), 0));
	}

}