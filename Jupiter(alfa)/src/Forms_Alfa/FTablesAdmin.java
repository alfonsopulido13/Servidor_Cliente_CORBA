package Forms_Alfa;

import Extras.CORBA_Conn;
import Extras.Data;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Isay
 */
public class FTablesAdmin extends CORBA_Conn {
    private DefaultTableModel modeloP, modeloT, modeloU;    
    private Data me;
    private FAdmin fa;

    /** Creates new form FTables2 */
    public FTablesAdmin() {
        initComponents();
        lstProyectos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstProyectos.addMouseListener(mouseP);
        lstTareas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstTareas.addMouseListener(mouseT);
        lstUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstUsuarios.addMouseListener(mouseU);        
        setTitulosTablas();
        tblProy.addMouseListener(mouselstP);
        tblTareas.addMouseListener(mouselstT);
        tblUsuarios.addMouseListener(mouselstU);
        lstTareas.setSize(lstTareas.getWidth(), 128);
        pgbProgressP.setMaximum(100);
        pgbProgressT.setMaximum(100);
        pgbProgressU.setMaximum(100);        
    }
    
    MouseListener mouseP = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1){
                int posicion = lstProyectos.locationToIndex(e.getPoint());
                pgbProgressP.setValue(40);
                if(posicion == 0) fa.loadTablaProy(exRef.ObtenerMisProyectos(me.Id(), true), tblProy, modeloP, null, lstTareas);
                else fa.loadTablaProy(exRef.Obtener("", (byte)1, (byte)1), tblProy, modeloP, null, lstTareas);
                pgbProgressP.setValue(100);
                setEnabledP(false);
                lstTareas.setSelectedIndex(0);
                txtBuscarP.setText("");
                pgbProgressP.setValue(0);
             }
        }
    };
    
    MouseListener mouseT = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1){
                int posicion = lstTareas.locationToIndex(e.getPoint()); 
                pgbProgressT.setValue(40);
                if(posicion == lstTareas.getModel().getSize() - 1) fa.loadTablaTarea(exRef.Obtener("", (byte)1, (byte)2), tblTareas, modeloT);
                else{
                    Data p = (Data) lstTareas.getModel().getElementAt(posicion);
                    fa.loadTablaTarea(exRef.ObtenerMisTareas(p.Id(), true), tblTareas, modeloT);
                }
                pgbProgressT.setValue(100);
                setEnabledP(false);
                txtBuscarT.setText("");
                pgbProgressT.setValue(0);
             }
        }
    };
    
    MouseListener mouseU = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1){
                int posicion = lstUsuarios.locationToIndex(e.getPoint());
                pgbProgressU.setValue(40);
                if(posicion == 0) fa.loadTablaUser("Administrador", tblUsuarios, modeloU);
                else if(posicion == 1) fa.loadTablaUser("Normal", tblUsuarios, modeloU);
                else fa.loadTablaUser("", tblUsuarios, modeloU);
                pgbProgressU.setValue(100);
                setEnabledP(false);
                txtBuscarU.setText("");
                pgbProgressU.setValue(0);
             }
        }
    };
    
    MouseListener mouselstP = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1)setEnabledP(true);
        }
    };
    
    MouseListener mouselstT = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1)setEnabledT(true);
        }
    };
    
    MouseListener mouselstU = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1)setEnabledU(true);
        }
    };
    
    private void setEnabledP(boolean value){
        btnAsignar.setEnabled(value);
        btnModificarP.setEnabled(value);
        btnPapeleraP.setEnabled(value);
        btnEliminarP.setEnabled(value);
    }
    
    private void setEnabledT(boolean value){
        btnModificarT.setEnabled(value);
        btnPapeleraT.setEnabled(value);
        btnEliminarT.setEnabled(value);
    }
    
    private void setEnabledU(boolean value){
        btnModificarU.setEnabled(value);
        btnPapeleraU.setEnabled(value);
        btnEliminarU.setEnabled(value);
    }
    
    public void setData(Data me, FAdmin fa){
        this.me = me;
        this.fa = fa;
        lstProyectos.setSelectedIndex(0);
        lstUsuarios.setSelectedIndex(0);
        Refresh();
    }
    
    private void setTitulosTablas(){
        String[] titulosP = {"ID", "Nombre del Proyecto", "Lider del Proyecto", "Descripción", "Fecha de Inicio", "Fecha de Termino"};
        modeloP = new DefaultTableModel(null, titulosP);
        String[] titulosT = {"ID", "Nombre de la Tarea", "Proyecto", "Descripción", "Fecha de Inicio", 
            "Fecha de Termino", "Prioridad", "Proceso"};
        modeloT = new DefaultTableModel(null, titulosT);
        String[] titulosU = {"ID", "Usuario", "Privilegio", "Nombre", "Email", "Género", "Fecha de Nacimiento"};
        modeloU = new DefaultTableModel(null, titulosU);        
    }
    
    public void Refresh(){
        if(lstProyectos.getSelectedIndex() == 0) fa.loadTablaProy(exRef.ObtenerMisProyectos(me.Id(), true), tblProy, modeloP, null, lstTareas);
        else fa.loadTablaProy(exRef.Obtener("", (byte)1, (byte)1), tblProy, modeloP, null, lstTareas);
        lstTareas.setSelectedIndex(0);
        
        if(lstTareas.getSelectedIndex() == lstTareas.getModel().getSize() - 1) fa.loadTablaTarea(exRef.Obtener("", (byte)1, (byte)2), tblTareas, modeloT);
        else{
            Data t = (Data) lstTareas.getModel().getElementAt(lstTareas.getSelectedIndex());
            fa.loadTablaTarea(exRef.ObtenerMisTareas(t.Id(), true), tblTareas, modeloT);
        }
        
        if(lstUsuarios.getSelectedIndex() == 2) fa.loadTablaUser("", tblUsuarios, modeloU);
        else if(lstUsuarios.getSelectedIndex() == 0) fa.loadTablaUser("Administrador", tblUsuarios, modeloU);
        else if(lstUsuarios.getSelectedIndex() == 1) fa.loadTablaUser("Normal", tblUsuarios, modeloU);
        
        setEnabledP(false);
        setEnabledT(false);
        setEnabledU(false);
    }
    
    public void setIndex(int index){jpnContenedor.setSelectedIndex(index);}
    
    void setVisibleTrash(){
        if(fa.tr == null) fa.tr = new Trash();
        fa.tr.setFAdmin(fa);
        fa.tr.setVisible(true);
        fa.ftA = null;
        this.dispose();
    }
    
    void trashdeleteProyectTask(javax.swing.JTable tabla, DefaultTableModel modelo, boolean permanente, byte tablaBD){
        javax.swing.JProgressBar p;
        if(tablaBD == 1) p = pgbProgressP;
        else p = pgbProgressT;
        p.setMaximum(tabla.getRowCount());
        while(true){
            try{                
                modelo = (DefaultTableModel)tabla.getModel();
                exRef.Eliminar(Integer.parseInt(tabla.getValueAt(0, 0).toString()), permanente, tablaBD);
                modelo.removeRow(0);
                tabla.setModel(modelo);
                p.setValue(p.getValue() + 1);
            }catch(Exception e){
                p.setMaximum(100);
                fa.Refresh();
                p.setValue(50);
                Refresh();
                p.setValue(0);
                break;
            }
        }
    }
    
    void trashdeleteUser(javax.swing.JTable tabla, DefaultTableModel modelo, boolean permanente){
        int i = 0;        
        pgbProgressU.setMaximum(tabla.getRowCount());
        while(true){
            try{
                modelo = (DefaultTableModel)tabla.getModel();
                if(Integer.parseInt(tabla.getValueAt(i, 0).toString()) != me.Id()){
                    exRef.Eliminar(Integer.parseInt(tabla.getValueAt(i, 0).toString()), permanente, (byte)3);
                    modelo.removeRow(i);                    
                }else i++;
                pgbProgressU.setValue(pgbProgressU.getValue() + 1);                                
                tabla.setModel(modelo);
            }catch(Exception e){
                pgbProgressU.setMaximum(100);                
                fa.Refresh();
                pgbProgressU.setValue(50);
                Refresh();
                pgbProgressU.setValue(0);
                break;
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnSalir = new javax.swing.JButton();
        jpnContenedor = new javax.swing.JTabbedPane();
        jpnProyectos = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProy = new javax.swing.JTable();
        txtBuscarP = new javax.swing.JTextField();
        btnBuscarP = new javax.swing.JButton();
        btnNuevoP = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstProyectos = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnAsignar = new javax.swing.JButton();
        btnModificarP = new javax.swing.JButton();
        btnPapeleraP = new javax.swing.JButton();
        btnPapeleraTodosP = new javax.swing.JButton();
        btnEliminarP = new javax.swing.JButton();
        btnEliminarTodosP = new javax.swing.JButton();
        pgbProgressP = new javax.swing.JProgressBar();
        jpnTareas = new javax.swing.JPanel();
        btnNuevoT = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        lstTareas = new javax.swing.JList();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblTareas = new javax.swing.JTable();
        txtBuscarT = new javax.swing.JTextField();
        btnBuscarT = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        btnModificarT = new javax.swing.JButton();
        btnPapeleraT = new javax.swing.JButton();
        btnPapeleraTodosT = new javax.swing.JButton();
        btnEliminarT = new javax.swing.JButton();
        btnEliminarTodosT = new javax.swing.JButton();
        pgbProgressT = new javax.swing.JProgressBar();
        jpnUsuarios = new javax.swing.JPanel();
        btnNuevoU = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        lstUsuarios = new javax.swing.JList();
        txtBuscarU = new javax.swing.JTextField();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblUsuarios = new javax.swing.JTable();
        btnBuscarU = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        btnModificarU = new javax.swing.JButton();
        btnPapeleraU = new javax.swing.JButton();
        btnPapeleraTodosU = new javax.swing.JButton();
        btnEliminarU = new javax.swing.JButton();
        btnEliminarTodosU = new javax.swing.JButton();
        pgbProgressU = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Tablas uProject [Usuario Administrador]");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        btnSalir.setFont(new java.awt.Font("Tahoma", 0, 14));
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        jpnContenedor.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpnContenedor.setFont(new java.awt.Font("Tahoma", 1, 18));

        tblProy.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblProy.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblProy);

        btnBuscarP.setText("Buscar");
        btnBuscarP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarPActionPerformed(evt);
            }
        });

        btnNuevoP.setFont(new java.awt.Font("Tahoma", 0, 18));
        btnNuevoP.setText("Nuevo");
        btnNuevoP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoPActionPerformed(evt);
            }
        });

        lstProyectos.setFont(new java.awt.Font("Tahoma", 0, 18));
        lstProyectos.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Mis Proyectos", "Todos" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(lstProyectos);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabel1.setText("Categorías de Proyectos");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabel3.setForeground(new java.awt.Color(0, 0, 255));
        jLabel3.setText("Ir a la Papelera");
        jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        btnAsignar.setText("Asignar una Tarea...");
        btnAsignar.setEnabled(false);
        btnAsignar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAsignarActionPerformed(evt);
            }
        });

        btnModificarP.setText("Modificar...");
        btnModificarP.setEnabled(false);
        btnModificarP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarPActionPerformed(evt);
            }
        });

        btnPapeleraP.setText("Enviar a la Papelera");
        btnPapeleraP.setEnabled(false);
        btnPapeleraP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPapeleraPActionPerformed(evt);
            }
        });

        btnPapeleraTodosP.setText("Enviar todos a la Papelera");
        btnPapeleraTodosP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPapeleraTodosPActionPerformed(evt);
            }
        });

        btnEliminarP.setText("Eliminar");
        btnEliminarP.setEnabled(false);
        btnEliminarP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarPActionPerformed(evt);
            }
        });

        btnEliminarTodosP.setText("Eliminar Todos");
        btnEliminarTodosP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarTodosPActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpnProyectosLayout = new javax.swing.GroupLayout(jpnProyectos);
        jpnProyectos.setLayout(jpnProyectosLayout);
        jpnProyectosLayout.setHorizontalGroup(
            jpnProyectosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnProyectosLayout.createSequentialGroup()
                .addGroup(jpnProyectosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jpnProyectosLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pgbProgressP, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpnProyectosLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jpnProyectosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jpnProyectosLayout.createSequentialGroup()
                                .addGroup(jpnProyectosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jpnProyectosLayout.createSequentialGroup()
                                        .addGap(42, 42, 42)
                                        .addComponent(btnNuevoP))
                                    .addComponent(jLabel1)
                                    .addGroup(jpnProyectosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(btnAsignar)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnModificarP)
                                        .addComponent(btnPapeleraP)
                                        .addComponent(btnPapeleraTodosP)
                                        .addComponent(btnEliminarP)))
                                .addGap(18, 18, 18))
                            .addGroup(jpnProyectosLayout.createSequentialGroup()
                                .addComponent(btnEliminarTodosP)
                                .addGap(18, 18, 18)))
                        .addGroup(jpnProyectosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpnProyectosLayout.createSequentialGroup()
                                .addComponent(txtBuscarP, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBuscarP)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 341, Short.MAX_VALUE)
                                .addComponent(jLabel3))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 717, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jpnProyectosLayout.setVerticalGroup(
            jpnProyectosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnProyectosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnProyectosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscarP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarP)
                    .addComponent(btnNuevoP)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpnProyectosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnProyectosLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(btnAsignar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnModificarP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPapeleraP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPapeleraTodosP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminarP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminarTodosP))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pgbProgressP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jpnContenedor.addTab("Proyectos", jpnProyectos);

        btnNuevoT.setFont(new java.awt.Font("Tahoma", 0, 18));
        btnNuevoT.setText("Nuevo");
        btnNuevoT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoTActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabel2.setText("Categorías de Tareas");

        lstTareas.setFont(new java.awt.Font("Tahoma", 0, 18));
        jScrollPane3.setViewportView(lstTareas);

        tblTareas.setFont(new java.awt.Font("Tahoma", 0, 12));
        tblTareas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(tblTareas);

        btnBuscarT.setText("Buscar");
        btnBuscarT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarTActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 255));
        jLabel4.setText("Ir a la Papelera");
        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });

        btnModificarT.setText("Modificar...");
        btnModificarT.setEnabled(false);
        btnModificarT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarTActionPerformed(evt);
            }
        });

        btnPapeleraT.setText("Enviar a la Papelera");
        btnPapeleraT.setEnabled(false);
        btnPapeleraT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPapeleraTActionPerformed(evt);
            }
        });

        btnPapeleraTodosT.setText("Enviar todos a la Papelera");
        btnPapeleraTodosT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPapeleraTodosTActionPerformed(evt);
            }
        });

        btnEliminarT.setText("Eliminar");
        btnEliminarT.setEnabled(false);
        btnEliminarT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarTActionPerformed(evt);
            }
        });

        btnEliminarTodosT.setText("Eliminar Todos");
        btnEliminarTodosT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarTodosTActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpnTareasLayout = new javax.swing.GroupLayout(jpnTareas);
        jpnTareas.setLayout(jpnTareasLayout);
        jpnTareasLayout.setHorizontalGroup(
            jpnTareasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnTareasLayout.createSequentialGroup()
                .addGroup(jpnTareasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jpnTareasLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pgbProgressT, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpnTareasLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jpnTareasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpnTareasLayout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addComponent(btnNuevoT))
                            .addComponent(jLabel2)
                            .addGroup(jpnTareasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btnModificarT)
                                .addComponent(btnPapeleraT)
                                .addComponent(btnPapeleraTodosT)
                                .addComponent(btnEliminarT)
                                .addComponent(btnEliminarTodosT)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jpnTareasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpnTareasLayout.createSequentialGroup()
                                .addComponent(txtBuscarT, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBuscarT)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 341, Short.MAX_VALUE)
                                .addComponent(jLabel4))
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 717, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jpnTareasLayout.setVerticalGroup(
            jpnTareasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnTareasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnTareasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscarT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarT)
                    .addComponent(btnNuevoT)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpnTareasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnTareasLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnModificarT)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPapeleraT)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPapeleraTodosT)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminarT)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminarTodosT))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pgbProgressT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpnContenedor.addTab("Tareas", jpnTareas);

        btnNuevoU.setFont(new java.awt.Font("Tahoma", 0, 18));
        btnNuevoU.setText("Nuevo");
        btnNuevoU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoUActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabel5.setText("Categorías de Usuarios");

        lstUsuarios.setFont(new java.awt.Font("Tahoma", 0, 18));
        lstUsuarios.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Administradores", "Normales", "Todos" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane5.setViewportView(lstUsuarios);

        tblUsuarios.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane6.setViewportView(tblUsuarios);

        btnBuscarU.setText("Buscar");
        btnBuscarU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarUActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabel6.setForeground(new java.awt.Color(0, 0, 255));
        jLabel6.setText("Ir a la Papelera");
        jLabel6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });

        btnModificarU.setText("Modificar...");
        btnModificarU.setEnabled(false);
        btnModificarU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarUActionPerformed(evt);
            }
        });

        btnPapeleraU.setText("Enviar a la Papelera");
        btnPapeleraU.setEnabled(false);
        btnPapeleraU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPapeleraUActionPerformed(evt);
            }
        });

        btnPapeleraTodosU.setText("Enviar todos a la Papelera");
        btnPapeleraTodosU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPapeleraTodosUActionPerformed(evt);
            }
        });

        btnEliminarU.setText("Eliminar");
        btnEliminarU.setEnabled(false);
        btnEliminarU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarUActionPerformed(evt);
            }
        });

        btnEliminarTodosU.setText("Eliminar Todos");
        btnEliminarTodosU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarTodosUActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpnUsuariosLayout = new javax.swing.GroupLayout(jpnUsuarios);
        jpnUsuarios.setLayout(jpnUsuariosLayout);
        jpnUsuariosLayout.setHorizontalGroup(
            jpnUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnUsuariosLayout.createSequentialGroup()
                .addGroup(jpnUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnUsuariosLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jpnUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpnUsuariosLayout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addComponent(btnNuevoU))
                            .addComponent(jLabel5)
                            .addGroup(jpnUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btnModificarU)
                                .addComponent(btnPapeleraU)
                                .addComponent(btnPapeleraTodosU)
                                .addComponent(btnEliminarU)
                                .addComponent(btnEliminarTodosU)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jpnUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpnUsuariosLayout.createSequentialGroup()
                                .addComponent(txtBuscarU, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBuscarU)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 341, Short.MAX_VALUE)
                                .addComponent(jLabel6))
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 717, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnUsuariosLayout.createSequentialGroup()
                        .addContainerGap(711, Short.MAX_VALUE)
                        .addComponent(pgbProgressU, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jpnUsuariosLayout.setVerticalGroup(
            jpnUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnUsuariosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscarU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarU)
                    .addComponent(btnNuevoU)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpnUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnUsuariosLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnModificarU)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPapeleraU)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPapeleraTodosU)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminarU)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminarTodosU))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pgbProgressU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpnContenedor.addTab("Usuarios", jpnUsuarios);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(857, Short.MAX_VALUE)
                .addComponent(btnSalir)
                .addContainerGap())
            .addComponent(jpnContenedor, javax.swing.GroupLayout.DEFAULT_SIZE, 924, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jpnContenedor, javax.swing.GroupLayout.PREFERRED_SIZE, 445, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSalir)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        fa.ftA = null;
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        fa.ftA= null;
    }//GEN-LAST:event_formWindowClosing

    private void btnNuevoPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoPActionPerformed
        fa.newP();
    }//GEN-LAST:event_btnNuevoPActionPerformed

    private void btnNuevoTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoTActionPerformed
        fa.newT();
    }//GEN-LAST:event_btnNuevoTActionPerformed

    private void btnNuevoUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoUActionPerformed
        fa.newU();
    }//GEN-LAST:event_btnNuevoUActionPerformed

    private void btnBuscarPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarPActionPerformed
        fa.loadTablaProy(exRef.Obtener(txtBuscarP.getText(), (byte)1, (byte)1), tblProy, modeloP, null, lstTareas);
        setEnabledP(false);
    }//GEN-LAST:event_btnBuscarPActionPerformed

    private void btnBuscarTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarTActionPerformed
        fa.loadTablaTarea(exRef.Obtener(txtBuscarT.getText(), (byte)1, (byte)2), tblTareas, modeloT);
        setEnabledP(false);
        System.out.println("Height: " + lstTareas.getHeight());
        System.out.println("Width: " + lstTareas.getWidth());
    }//GEN-LAST:event_btnBuscarTActionPerformed

    private void btnBuscarUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarUActionPerformed
        fa.loadTablaUser(txtBuscarU.getText(), tblUsuarios, modeloU);
        setEnabledP(false);
    }//GEN-LAST:event_btnBuscarUActionPerformed

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        setVisibleTrash();
    }//GEN-LAST:event_jLabel6MouseClicked

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        setVisibleTrash();
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        setVisibleTrash();
    }//GEN-LAST:event_jLabel4MouseClicked

    private void btnAsignarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAsignarActionPerformed
        fa.asignTasktoProject(tblProy);
    }//GEN-LAST:event_btnAsignarActionPerformed

    private void btnModificarPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarPActionPerformed
        fa.modifyProject(tblProy);
    }//GEN-LAST:event_btnModificarPActionPerformed

    private void btnPapeleraPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPapeleraPActionPerformed
        fa.trashProject(tblProy);
    }//GEN-LAST:event_btnPapeleraPActionPerformed

    private void btnPapeleraTodosPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPapeleraTodosPActionPerformed
        trashdeleteProyectTask(tblProy, modeloP, false, (byte)1);
    }//GEN-LAST:event_btnPapeleraTodosPActionPerformed

    private void btnEliminarPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarPActionPerformed
        fa.deleteProject(tblProy);
    }//GEN-LAST:event_btnEliminarPActionPerformed

    private void btnEliminarTodosPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarTodosPActionPerformed
        trashdeleteProyectTask(tblProy, modeloP, true, (byte)1);
    }//GEN-LAST:event_btnEliminarTodosPActionPerformed

    private void btnModificarTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarTActionPerformed
        fa.modifyTask(tblTareas);
    }//GEN-LAST:event_btnModificarTActionPerformed

    private void btnPapeleraTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPapeleraTActionPerformed
        fa.trashTask(tblTareas);
    }//GEN-LAST:event_btnPapeleraTActionPerformed

    private void btnPapeleraTodosTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPapeleraTodosTActionPerformed
        trashdeleteProyectTask(tblTareas, modeloT, false, (byte)2);
    }//GEN-LAST:event_btnPapeleraTodosTActionPerformed

    private void btnEliminarTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarTActionPerformed
        fa.deleteTask(tblTareas);
    }//GEN-LAST:event_btnEliminarTActionPerformed

    private void btnEliminarTodosTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarTodosTActionPerformed
        trashdeleteProyectTask(tblTareas, modeloT, true, (byte)2);
    }//GEN-LAST:event_btnEliminarTodosTActionPerformed

    private void btnModificarUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarUActionPerformed
        fa.modifyUser(tblUsuarios);
    }//GEN-LAST:event_btnModificarUActionPerformed

    private void btnPapeleraUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPapeleraUActionPerformed
        fa.trashUser(tblUsuarios);
    }//GEN-LAST:event_btnPapeleraUActionPerformed

    private void btnPapeleraTodosUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPapeleraTodosUActionPerformed
        trashdeleteUser(tblUsuarios, modeloU, false);
    }//GEN-LAST:event_btnPapeleraTodosUActionPerformed

    private void btnEliminarUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarUActionPerformed
        fa.deleteUser(tblUsuarios);
    }//GEN-LAST:event_btnEliminarUActionPerformed

    private void btnEliminarTodosUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarTodosUActionPerformed
        trashdeleteUser(tblUsuarios, modeloU, true);
    }//GEN-LAST:event_btnEliminarTodosUActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FTablesAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FTablesAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FTablesAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FTablesAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new FTablesAdmin().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAsignar;
    private javax.swing.JButton btnBuscarP;
    private javax.swing.JButton btnBuscarT;
    private javax.swing.JButton btnBuscarU;
    private javax.swing.JButton btnEliminarP;
    private javax.swing.JButton btnEliminarT;
    private javax.swing.JButton btnEliminarTodosP;
    private javax.swing.JButton btnEliminarTodosT;
    private javax.swing.JButton btnEliminarTodosU;
    private javax.swing.JButton btnEliminarU;
    private javax.swing.JButton btnModificarP;
    private javax.swing.JButton btnModificarT;
    private javax.swing.JButton btnModificarU;
    private javax.swing.JButton btnNuevoP;
    private javax.swing.JButton btnNuevoT;
    private javax.swing.JButton btnNuevoU;
    private javax.swing.JButton btnPapeleraP;
    private javax.swing.JButton btnPapeleraT;
    private javax.swing.JButton btnPapeleraTodosP;
    private javax.swing.JButton btnPapeleraTodosT;
    private javax.swing.JButton btnPapeleraTodosU;
    private javax.swing.JButton btnPapeleraU;
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jpnContenedor;
    private javax.swing.JPanel jpnProyectos;
    private javax.swing.JPanel jpnTareas;
    private javax.swing.JPanel jpnUsuarios;
    private javax.swing.JList lstProyectos;
    private javax.swing.JList lstTareas;
    private javax.swing.JList lstUsuarios;
    private javax.swing.JProgressBar pgbProgressP;
    private javax.swing.JProgressBar pgbProgressT;
    private javax.swing.JProgressBar pgbProgressU;
    private javax.swing.JTable tblProy;
    private javax.swing.JTable tblTareas;
    private javax.swing.JTable tblUsuarios;
    private javax.swing.JTextField txtBuscarP;
    private javax.swing.JTextField txtBuscarT;
    private javax.swing.JTextField txtBuscarU;
    // End of variables declaration//GEN-END:variables
}
