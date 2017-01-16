package cns_main;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.Action;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import cns_controller.ModuleMonitor;
import cns_controller.NetworkMonitor;


public class CnsGui<MyLoadFileComboBox> extends JFrame{
	/*
	 * Only GUI! should not include any functionality
	 * Config is the Model that should be visualized
	 * Calls the controllers ModuleMonitor and NetworkMonitor
	 * 
	 * 
	 */

	private static final long serialVersionUID = 1L;

	private CnsConfig config;
	private ModuleMonitor module_monitor;
	private NetworkMonitor network_monitor;
	private CnsSettings setting;
	
	private JTable computer_table, module_table;

	public CnsGui(CnsConfig config, ModuleMonitor module_monitor, NetworkMonitor network_monitor, CnsSettings setting){
		super("CNS_Monitor");
		this.config = config;
		this.module_monitor = module_monitor;
		this.network_monitor = network_monitor;
		this.setting = setting;


		JFrame total = new JFrame ("crazy CNS monitor");
		BorderLayout bl = new BorderLayout();		
		total.setLayout(bl);


		// Build Jpanel buttons
		JPanel buttons = new JPanel();
		
		// Load Config Combo Box
		MyLoadFileComboBoxModel loadConfigComboModel = new MyLoadFileComboBoxModel();
		JComboBox<String> loadConfigCombo = new JComboBox<String>(loadConfigComboModel);             
		//loadConfigCombo.setPrototypeDisplayValue(loadConfigComboModel.getHeader().toString());
		
		
		JButton check_network = new JButton ("Check Network");
		JButton start_modules = new JButton ("Start Modules");
		
		check_network.setEnabled(false);
		start_modules.setEnabled(false);

		buttons.add(loadConfigCombo);
		buttons.add(check_network);
		buttons.add(start_modules);

		// Build computers table
		class MyComputerTableModel extends AbstractTableModel {
			private static final long serialVersionUID = 1L;
			String[] columnNames = { "Name", "Mac", "IP", "User", "Reachable" };

			public int getColumnCount() {
				return columnNames.length;
			}

			public int getRowCount() {
				return config.getAll_computers().size();
			}

			public String getColumnName(int col) {
				return columnNames[col];
			}
			
			public Object getValueAt(int row, int col) {
				if (col == 0){
					String name = config.getAll_computers().get(row).getName();
//					String info = "";
//					if (config.getAll_computers().get(row).isThisPC()) {
//						info = " (This computer)";
//					}
					return (Object) name;
				}else if (col == 1)
					return (Object) config.getAll_computers().get(row).getMacInfoText();
				else if (col == 2)
					return (Object) config.getAll_computers().get(row).getIpInfoText();
				else if (col == 3)
					return (Object) config.getAll_computers().get(row).getUser();
				else
					return (Object) config.getAll_computers().get(row).isReachableInfoText();
			}
		}
		
		computer_table = new JTable(new MyComputerTableModel()){
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component component = super.prepareRenderer(renderer, row, column);
				int rendererWidth = component.getPreferredSize().width;
				TableColumn tableColumn = getColumnModel().getColumn(column);
				tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
		        return component;
	        }
		};

		computer_table.setPreferredScrollableViewportSize(new Dimension(500, 70));

		//Build Module Table
		class MyModuleTableModel extends AbstractTableModel {
			private static final long serialVersionUID = 1L;
			String[] columnNames = { "Name", "Computer", "Port", "", "" };

			public int getColumnCount() {
				return columnNames.length;
			}

			public int getRowCount() {
				return config.getAll_modules().size();
			}

			public String getColumnName(int col) {
				return columnNames[col];
			}

			public boolean isCellEditable(int row, int col)
		      {
				if ( col==3 || col==4)
					return true;
				else
					return false;
				}


			public Object getValueAt(int row, int col) {
				if (col == 0)
					return (Object) config.getAll_modules().get(row).getName();
				else if (col == 1)
					return (Object) config.getAll_modules().get(row).getComputer().getName();
				else if (col == 2)
					return (Object) config.getAll_modules().get(row).getListeningPort();
				else if (col == 3)
					return (Object) "Start";
				else
					return (Object) "Show output";
			}
						
		}
		module_table = new JTable(new MyModuleTableModel()){
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component component = super.prepareRenderer(renderer, row, column);
				int rendererWidth = component.getPreferredSize().width;
				TableColumn tableColumn = getColumnModel().getColumn(column);
				tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
		        return component;
	        }
		};
		Action start_module = new AbstractAction()
		{

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e)
		    {
		        int modelRow = Integer.valueOf( e.getActionCommand() );
		        System.out.println("Starting!! pressed row: "+modelRow);
		    }
		};
		Action show_module_output = new AbstractAction()
		{
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e)
		    {
		        int modelRow = Integer.valueOf( e.getActionCommand() );
		        System.out.println("Show output!! pressed row: "+modelRow);
		    }
		};
		
		//see: tips4java.wordpress.com/2009/07/12/table-button-column/
		ButtonColumn bc3 = new ButtonColumn (module_table, start_module, 3 );
		ButtonColumn bc4 = new ButtonColumn (module_table, show_module_output, 4 );
		bc3.setMnemonic(KeyEvent.VK_D);
		bc4.setMnemonic(KeyEvent.VK_D);
		
		module_table.setPreferredScrollableViewportSize(new Dimension(500, 250));

		JPanel tables = new JPanel(new GridLayout(2,1));
		tables.add(new JScrollPane(computer_table));
		tables.add(new JScrollPane(module_table));


		total.add(buttons, BorderLayout.NORTH);
		total.add(tables, BorderLayout.CENTER);
		total.setSize(1000,500);
		total.setVisible(true);
		
		
		loadConfigCombo.addItemListener( new ItemListener() {
			public void itemStateChanged( ItemEvent e ) {
				if (e.getStateChange() == ItemEvent.SELECTED && e.getItem().equals(loadConfigComboModel.getBrowse())){
					JFileChooser fC = new JFileChooser();
					fC.showOpenDialog(total);
					if (true) { //TODO: check cancel button
						if(config.load(fC.getSelectedFile())){
							computer_table.updateUI();
							module_table.updateUI();
							check_network.setEnabled(true);
							start_modules.setEnabled(true);
							setting.addRecentOpenConfig(fC.getSelectedFile().getPath());
							loadConfigCombo.updateUI();
						}
					}
					loadConfigCombo.setSelectedItem(loadConfigComboModel.getHeader());
				}else if(e.getStateChange() == ItemEvent.SELECTED && !e.getItem().equals(loadConfigComboModel.getHeader())){
					//System.out.println("path selected");
					if(config.load(e.getItem().toString())){
						computer_table.updateUI();
						module_table.updateUI();
						check_network.setEnabled(true);
						start_modules.setEnabled(true);
						setting.addRecentOpenConfig(e.getItem().toString());
						loadConfigCombo.updateUI();
					}
					loadConfigCombo.setSelectedItem(loadConfigComboModel.getHeader());
				}	
			}
		});

		check_network.addActionListener ( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				network_monitor.startCompleteNetworkCheck();
				computer_table.updateUI();
			}
		});
		
		start_modules.addActionListener ( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				module_monitor.start_all_modules();
			}
		});


	}
	
	public JTable getComputerTable(){
		return computer_table;
	}
	
	public NetworkMonitor getNetworkMonitor(){
		return network_monitor;
	}

	class MyLoadFileComboBoxModel extends AbstractListModel implements ComboBoxModel {
    	String loadConfigComboHeader = "Load Config";
    	String loadConfigComboBrowse = "Open Other Config...";

    	String selection = loadConfigComboHeader;
    	
		@Override
		public int getSize() {
			if (setting != null) {
    			return setting.getRecentOpenConfig().size() + 2;
			}else{
				return 2;
			}
		}

		@Override
		public Object getElementAt(int index) {
			if (index == 0) {
				return loadConfigComboHeader;
    		}else if (index == getSize() - 1){
    			return loadConfigComboBrowse;
			} else {
				if (setting != null) {
					return setting.getRecentOpenConfig().get(index-1);
				}else{
					return "";
				}
			}
		}

		@Override
		public void setSelectedItem(Object anItem) {
			selection = (String) anItem;
		}

		@Override
		public Object getSelectedItem() {
			return selection;
		}
		
		public Object getHeader(){
			return loadConfigComboHeader;
		}
    	
		public Object getBrowse(){
			return loadConfigComboBrowse;
		}
		
    }

}
