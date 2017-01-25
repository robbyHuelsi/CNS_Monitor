package cns_main;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
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
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
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
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import cns_controller.ModuleMonitor;
import cns_controller.NetworkMonitor;
import config_utilities.Computer;


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
	
	private JFrame total;
	private JTable computer_table, module_table;
	
	private ModuleOutputGui[] moduleGuis;

	public CnsGui(CnsConfig config, ModuleMonitor module_monitor, NetworkMonitor network_monitor, CnsSettings setting){
		super("CNS_Monitor");
		this.config = config;
		this.module_monitor = module_monitor;
		this.network_monitor = network_monitor;
		this.setting = setting;
		
		this.moduleGuis= new ModuleOutputGui[config.getAll_modules().size()];


		total = new JFrame ("crazy CNS monitor");
		total.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		BorderLayout bl = new BorderLayout();		
		total.setLayout(bl);
		
		//Create the menu bar.
		JMenuBar menuBar = new JMenuBar();
		JMenu menuFile = new JMenu("File");
		JMenu menuNetwork = new JMenu("Network");
		JMenu menuScene = new JMenu("Scene");
		JMenu menuModule = new JMenu("Module");
		JMenu menuComputer = new JMenu("Computer");
		
		JMenuItem menuItemOpen = new JMenuItem("Open...");
		JMenuItem menuItemReload = new JMenuItem("Reload");
		JMenuItem menuItemExit = new JMenuItem("Exit");
		JMenuItem menuItemCheckNetwork = new JMenuItem("Check Network");
		JMenuItem menuItemStartModules = new JMenuItem("Start Modules");
		JMenuItem menuItemKillModules = new JMenuItem("Kill Modules");
		JMenuItem menuItemCloseModuleGuis = new JMenuItem("Close Module GUIs");
		
		//Create RecentOpen Class
		class JMenuRecentOpen extends JMenu {
			
			public JMenuRecentOpen(){
				this.setText("Open Recent");
				setItems();
			}
			
			private void setItems(){
				if (setting.getRecentOpenConfig().isEmpty()) {
					this.setVisible(false);
				}else{
					this.setVisible(true);
					this.removeAll();
					
					for (int i = 0; i < setting.getRecentOpenConfig().size(); i++) {
						String path = setting.getRecentOpenConfig().get(i);
						JMenuItem menuItemPath = new JMenuItem(path);
						this.add(menuItemPath);
						menuItemPath.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								menuItemPathActionPerformed(path);
							}
						});
						if (i == 0) {
							if (config.getFile() == null) {
								menuItemPath.setAccelerator(KeyStroke.getKeyStroke("alt O"));
							}else{
								if (config.getFile().getPath().equals(path)) {
									menuItemPath.setEnabled(false);
								}else{
									menuItemPath.setAccelerator(KeyStroke.getKeyStroke("alt O"));
								}
							}
						}else if(i == 1){
							if (config.getFile() != null) {
								menuItemPath.setAccelerator(KeyStroke.getKeyStroke("alt O"));
							}
						}
					}
					
					this.addSeparator();
					
					JMenuItem  menuItemRemove = new JMenuItem("Remove All");
					this.add(menuItemRemove);
					menuItemRemove.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							menuItemRemoveActionPerformed();
						}
					});
				}
			}
			
			public void menuItemPathActionPerformed(String path) {
				System.out.println(path);
				if(config.load(path)){
					computer_table.updateUI();
					module_table.updateUI();
					module_monitor.kill_all_modules();
					module_monitor.reset();
					menuItemCheckNetwork.setEnabled(true);
					menuItemStartModules.setEnabled(true);
					menuItemKillModules.setEnabled(true);
					menuItemCloseModuleGuis.setEnabled(true);
					setting.addRecentOpenConfig(path);
					this.setItems();
					moduleGuis= new ModuleOutputGui[config.getAll_modules().size()];
				}
			}
			
			public void menuItemRemoveActionPerformed() {
				setting.removeAllRecentOpenConfig();
				this.setItems();
			}
			
		}
		
		//Create Passwords Class
		class JMenuPasswords extends JMenu {
			
			public JMenuPasswords(){
				this.setText("Passwords");
				setItems();
			}
			
			private void setItems(){
				if (setting.getPasswords().isEmpty()) {
					this.removeAll();
					JMenuItem menuItemPass = new JMenuItem("No Passwords");
					menuItemPass.setEnabled(false);
					this.add(menuItemPass);
				}else{
					this.removeAll();
					
					for (int i = 0; i < setting.getPasswords().size(); i++) {
						CnsPassword pass = setting.getPasswords().get(i);
						String text = null;
						
						for (Computer computer : config.getAll_computers()) {
							if (pass.getMac().equals(computer.getMacLan())) {
								text = "Remove Password for " + computer.getName() + " via LAN";
								break;
							}else if(pass.getMac().equals(computer.getMacWlan())){
								text = "Remove Password for " + computer.getName() + " via WLAN";
								break;
							}
						}
						
						if (text == null) {
							text = "Remove Password for " + pass.getMac();
						}
						
						JMenuItem menuItemPass = new JMenuItem(text);
						this.add(menuItemPass);
						menuItemPass.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								menuItemPasswordActionPerformed(pass);
							}
						});
						
					}
					
					this.addSeparator();
					
					JMenuItem menuItemRemovePasswords = new JMenuItem("Remove All Passwords");
					this.add(menuItemRemovePasswords);
					menuItemRemovePasswords.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							menuItemRemoveActionPerformed();
						}
					});
				}
			}
			
			public void menuItemPasswordActionPerformed(CnsPassword password) {
				setting.getPasswords().remove(password);
				setting.save();
				this.setItems();
			}
			
			public void menuItemRemoveActionPerformed() {
				setting.removeAllPasswords();
				this.setItems();
			}
			
		}
		
		JMenuRecentOpen menuOpenRecent = new JMenuRecentOpen();
		JMenuPasswords menuPasswords = new JMenuPasswords();
		
		//Set up the menu bar
		menuBar.add(menuFile);
		
		menuItemOpen.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
		menuFile.add(menuItemOpen);
		menuFile.add(menuOpenRecent);
		menuItemReload.setAccelerator(KeyStroke.getKeyStroke('R', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
		menuFile.add(menuItemReload);
		menuFile.addSeparator();
		menuItemExit.setAccelerator(KeyStroke.getKeyStroke('Q', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
		menuFile.add(menuItemExit);
		
		menuBar.add(menuNetwork);
		menuItemCheckNetwork.setEnabled(false);
		menuNetwork.add(menuItemCheckNetwork);
		
		menuBar.add(menuScene);
		
		menuBar.add(menuModule);
		menuItemStartModules.setEnabled(false);
		menuModule.add(menuItemStartModules);
		menuItemKillModules.setEnabled(false);
		menuModule.add(menuItemKillModules);
		menuItemCloseModuleGuis.setEnabled(false);
		menuModule.add(menuItemCloseModuleGuis);
		
		menuBar.add(menuComputer);
		menuComputer.add(menuPasswords);
		

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
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

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
			String[] columnNames = { "Name", "Computer", "Port", "", "", "" };

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
				if ( col==3 || col==4 || col==5)
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
				else if (col == 4)
					return (Object) "Show output";
				else
					return (Object) "Kill";
			}
						
		}
		module_table = new JTable(new MyModuleTableModel()){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

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
		        module_monitor.start_module(modelRow);
		        //System.out.println("Starting!! pressed row: "+modelRow);
		    }
		};
		Action show_module_output = new AbstractAction()
		{
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e)
		    {
		        int modelRow = Integer.valueOf( e.getActionCommand() );
		        if (moduleGuis[modelRow]==null)
		        	moduleGuis[modelRow] = new ModuleOutputGui(config.getModule(modelRow));
		        else
		        	moduleGuis[modelRow].show();
		    }
		};
		Action kill_module = new AbstractAction()
		{
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e)
		    {
		        int modelRow = Integer.valueOf( e.getActionCommand() );
		        module_monitor.kill_module(modelRow);
		        //System.out.println("Starting!! pressed row: "+modelRow);
		    }
		};

		
		//see: tips4java.wordpress.com/2009/07/12/table-button-column/
		ButtonColumn bc3 = new ButtonColumn (module_table, start_module, 3 );
		ButtonColumn bc4 = new ButtonColumn (module_table, show_module_output, 4 );
		ButtonColumn bc5 = new ButtonColumn (module_table, kill_module, 5 );
		bc3.setMnemonic(KeyEvent.VK_D);
		bc4.setMnemonic(KeyEvent.VK_D);
		bc5.setMnemonic(KeyEvent.VK_D);
		
		module_table.setPreferredScrollableViewportSize(new Dimension(500, 250));

		JPanel tables = new JPanel(new GridLayout(2,1));
		tables.add(new JScrollPane(computer_table));
		tables.add(new JScrollPane(module_table));


		total.add(menuBar, BorderLayout.NORTH);
		total.add(tables, BorderLayout.CENTER);
		total.setSize(1000,500);
		total.setVisible(true);

		menuItemOpen.addActionListener ( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fC = new JFileChooser();
				fC.showOpenDialog(total);
				if (true) { //TODO: check cancel button
					if(config.load(fC.getSelectedFile())){
						module_monitor.kill_all_modules();
						module_monitor.reset();
						computer_table.updateUI();
						module_table.updateUI();
						menuItemCheckNetwork.setEnabled(true);
						menuItemStartModules.setEnabled(true);
						menuItemKillModules.setEnabled(true);
						menuItemCloseModuleGuis.setEnabled(true);
						setting.addRecentOpenConfig(fC.getSelectedFile().getPath());
						moduleGuis= new ModuleOutputGui[config.getAll_modules().size()];
					}
				}
			}
		});
		
		menuItemReload.addActionListener ( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(config.load(config.getFile())){
					module_monitor.kill_all_modules();
					module_monitor.reset();
					computer_table.updateUI();
					module_table.updateUI();
					menuItemCheckNetwork.setEnabled(true);
					menuItemStartModules.setEnabled(true);
					menuItemKillModules.setEnabled(true);
					menuItemCloseModuleGuis.setEnabled(true);
					moduleGuis= new ModuleOutputGui[config.getAll_modules().size()];
				}
			}
		});
		
		menuItemExit.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ev) {
		            System.exit(0);
		    }
		});
		
		menuItemCheckNetwork.addActionListener ( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				network_monitor.startCompleteNetworkCheck();
				computer_table.updateUI();
			}
		});
		
		menuItemStartModules.addActionListener ( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				module_monitor.start_all_modules();
			}
		});
		
		menuItemKillModules.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				module_monitor.kill_all_modules();
			}
		});
		
		menuItemCloseModuleGuis.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAllModuleWindows();
			}
		});
		
		menuOpenRecent.addMenuListener(new MenuListener() {
			public void menuSelected(MenuEvent e) {menuOpenRecent.setItems();}
			public void menuDeselected(MenuEvent e) {}
			public void menuCanceled(MenuEvent e) {}
		});
		
		menuPasswords.addMenuListener(new MenuListener() {
			public void menuSelected(MenuEvent e) {menuPasswords.setItems();}
			public void menuDeselected(MenuEvent e) {}
			public void menuCanceled(MenuEvent e) {}
		});
		
	}
	
	public JTable getComputerTable(){
		return computer_table;
	}
	
	public NetworkMonitor getNetworkMonitor(){
		return network_monitor;
	}
	
	public void closeAllModuleWindows(){
		for (int i=0; i<config.getAll_modules().size();++i){
			if (moduleGuis[i] != null)
				moduleGuis[i].hide();
		}
		
	}

	public void setTotalTitle(String title){
		total.setTitle(title);
	}
}
