package Forms_Alfa;

import Extras.CORBA_Conn;
import Extras.Data;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Isay
 */
public class FAdmin extends CORBA_Conn {
    private DefaultTableModel modeloP, modeloT, modeloU;
    private Data me;        
    public FTablesAdmin ftA;
    public NewP np;
    public NewPwd npwd;
    public NewT nt;
    public NewU nu;
    public Profile pf;
    public Trash tr;

    /** Creates new form FAdmin */
    public FAdmin() {
        initComponents();
        pgbProgress.setVisible(false);
        pgbProgress.setMaximum(100);
        setTitulosTablas();
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
    
    private void removeRows(DefaultTableModel modelo){
        int count = modelo.getRowCount() - 1;
        for(int i = count; i >= 0; i--)
                modelo.removeRow(i);
    }
    
    public void loadTablaProy(String[][] registros, javax.swing.JTable tabla, DefaultTableModel modelo, javax.swing.JComboBox caja, javax.swing.JList lista){        
        removeRows(modelo);
        String[] registro = new String[6];
        int contador = 0;
        DefaultListModel modeloL = new DefaultListModel();
        if(caja != null)caja.removeAllItems();
        else if(lista != null) lista.setModel(modeloL);
        while(true){
            try{
                registro[0] = registros[0][contador];                
                registro[1] = registros[2][contador];
                registro[2] = registros[1][contador];
                registro[3] = registros[3][contador];
                registro[4] = registros[4][contador];
                registro[5] = registros[5][contador];                                
                if(caja != null)caja.addItem(new Data(Integer.parseInt(registro[0]), registro[1]));
                else if(lista != null){                    
                    modeloL.addElement(new Data(Integer.parseInt(registro[0]), registro[1]));
                    lista.setModel(modeloL);
                }
                contador++;
                modelo.addRow(registro);
            }catch(Exception e){ break; }            
        }
        if(caja != null)caja.addItem("Todas");
        else if(lista != null){
            modeloL.addElement("Todas");
            lista.setModel(modeloL);
        }
        tabla.setModel(modelo);
    }
      
    public void loadTablaTarea(String[][] registros, javax.swing.JTable tabla, DefaultTableModel modelo){
        removeRows(modelo);
        String[] registro = new String[8];
        int contador = 0;
        while(true){
            try{
                registro[0] = registros[0][contador];
                registro[1] = registros[2][contador];
                registro[2] = registros[1][contador];
                registro[3] = registros[3][contador];
                registro[4] = registros[4][contador];
                registro[5] = registros[5][contador];
                registro[6] = registros[6][contador];
                registro[7] = registros[7][contador];
                contador++;
                modelo.addRow(registro);
            }catch(Exception e){ break; }            
        }        
        tabla.setModel(modelo);
    }        
    
    public void loadTablaUser(String busqueda, javax.swing.JTable tabla, DefaultTableModel modelo){
        removeRows(modelo);
        String[][] registros = exRef.Obtener(busqueda, (byte)1, (byte)3);
        String[] registro = new String[7];
        int contador = 0;
        while(true){
            try{
                registro[0] = registros[0][contador];
                registro[1] = registros[1][contador];
                registro[2] = registros[3][contador];
                registro[3] = registros[4][contador];
                registro[4] = registros[5][contador];
                registro[5] = registros[6][contador];
                registro[6] = registros[7][contador];
                contador++;
                modelo.addRow(registro);
            }catch(Exception e){ break; }            
        }
        tabla.setModel(modelo);
    }
    
    public void Refresh(){
        boolean value = false;
        if(pgbProgress.isVisible()) value = true;        
        else pgbProgress.setVisible(true);
        
        pgbProgress.setValue(30);
        if(cboOrganizarP.getSelectedItem().toString().equals("Mis Proyectos")) loadTablaProy(exRef.ObtenerMisProyectos(me.Id(), true), tblProy, modeloP, cboOrganizarT, null);
        else loadTablaProy(exRef.Obtener("", (byte)1, (byte)1), tblProy, modeloP, cboOrganizarT, null);
        
        pgbProgress.setValue(60);
        if(cboOrganizarT.getSelectedItem().toString().equals("Todas")) loadTablaTarea(exRef.Obtener("", (byte)1, (byte)2), tblTareas, modeloT);
        else{
            Data t = (Data) cboOrganizarT.getItemAt(cboOrganizarT.getSelectedIndex());
            loadTablaTarea(exRef.ObtenerMisTareas(t.Id(), true), tblTareas, modeloT);
        }
        
        pgbProgress.setValue(90);
        if(cboOrganizarU.getSelectedItem().toString().equals("Todos")) loadTablaUser("", tblUser, modeloU);
        else if(cboOrganizarU.getSelectedItem().toString().equals("Administradores")) loadTablaUser("Administrador", tblUser, modeloU);
        else if(cboOrganizarU.getSelectedItem().toString().equals("Normales")) loadTablaUser("Normal", tblUser, modeloU);
        
        pgbProgress.setValue(100);
        pgbProgress.setValue(0);
        if(!value) pgbProgress.setVisible(false);
    }
    
    void closeSession(){
        new Login().setVisible(true);
        this.dispose();
    }
    
    public void newP(){
        if(np == null){
            np = new NewP();
            np.setFAdmin(this);
        }        
        np.setVisible(true);
    }
    
    public void newT(){
        if(nt == null){
            nt = new NewT();
            nt.setFAdmin(this);
        }        
        nt.setVisible(true);
    }
    
    public void newU(){
        if(nu == null){
            nu = new NewU();
            nu.setFAdmin(this);
        }        
        nu.setVisible(true);
    }
    
    void showProjects(){
        if(ftA == null) ftA = new FTablesAdmin();
        ftA.setData(me, this);
        ftA.setIndex(0);
        ftA.setVisible(true);
    }
    
    void showTasks(){
        if(ftA == null) ftA = new FTablesAdmin();
        ftA.setData(me, this);
        ftA.setIndex(1);
        ftA.setVisible(true);
    }
    
    void showUsers(){
        if(ftA == null) ftA = new FTablesAdmin();
        ftA.setData(me, this);
        ftA.setIndex(2);
        ftA.setVisible(true);
    }
    
    public void asignTasktoProject(javax.swing.JTable tabla){
        try{
            int fila = tabla.getSelectedRow();
            if(fila == -1) 
                JOptionPane.showMessageDialog(null, "No se ha seleccionado un proyecto", "Error", JOptionPane.ERROR_MESSAGE);
            else{
                if(nt == null){
                    nt = new NewT();
                    nt.setFAdmin(this);
                    nt.setLider(tabla.getValueAt(fila, 1).toString());
                }else{
                    nt.dispose();
                    nt = null;
                    asignTasktoProject(tabla);
                }
                nt.setVisible(true);
            }
        }catch(Exception e){            
            JOptionPane.showMessageDialog(null, e, "Error al seleccionar", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void trashProject(javax.swing.JTable tabla){
        try{
            int fila = tabla.getSelectedRow();
            if(fila == -1) 
                JOptionPane.showMessageDialog(null, "No se ha seleccionado un proyecto", "Error", JOptionPane.ERROR_MESSAGE);
            else
                if(JOptionPane.showOptionDialog(null, "Al enviar el proyecto '" + tabla.getValueAt(fila, 1) + "' a la papelera, "
                        + "también se enviaran sus tareas a la papelera. ¿Deseas continuar?", "Enviar Proyecto a la Papelera", 
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, rootPane) == 0){
                    pgbProgress.setVisible(true);
                    pgbProgress.setValue(20);
                    if(exRef.Eliminar(Integer.parseInt(tabla.getValueAt(fila, 0).toString()), false, (byte)1)){
                        pgbProgress.setValue(80);
                        Refresh();
                        pgbProgress.setValue(90);
                        if(ftA != null) ftA.Refresh();
                        pgbProgress.setValue(0);
                        pgbProgress.setVisible(false);
                        JOptionPane.showMessageDialog(null, "El proyecto seleccionado se ha enviado a la papelera satisfactoriamente.", "Enviar Proyecto a la papelera", JOptionPane.PLAIN_MESSAGE);
                    }else
                        JOptionPane.showMessageDialog(null, "Ocurrió un error al tratar de enviar el proyecto seleccionado a la papelera", "Enviar Proyecto a la papelera", JOptionPane.ERROR_MESSAGE);
                    pgbProgress.setValue(0);
                    pgbProgress.setVisible(false);
                }
        }catch(Exception e){            
            JOptionPane.showMessageDialog(null, e, "Error al seleccionar", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void trashTask(javax.swing.JTable tabla){
        try{
            int fila = tabla.getSelectedRow();
            if(fila == -1)
                JOptionPane.showMessageDialog(null, "No se ha seleccionado una tarea", "Error", JOptionPane.ERROR_MESSAGE);
            else
                if(JOptionPane.showOptionDialog(null, "¿Deseas enviar la tarea '" + tabla.getValueAt(fila, 1).toString()
                        + "' a la papelera?", "Enviar Tarea a la Papelera", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, rootPane) == 0){
                    pgbProgress.setVisible(true);
                    pgbProgress.setValue(20);
                    if(exRef.Eliminar(Integer.parseInt(tabla.getValueAt(fila, 0).toString()), false, (byte)2)){
                        pgbProgress.setValue(80);
                        Refresh();
                        pgbProgress.setValue(90);
                        if(ftA != null) ftA.Refresh();
                        pgbProgress.setValue(0);
                        pgbProgress.setVisible(false);
                        JOptionPane.showMessageDialog(null, "La tarea seleccionada se ha enviado a la papelera satisfactoriamente.", "Enviar Tarea a la papelera", JOptionPane.PLAIN_MESSAGE);
                    }else JOptionPane.showMessageDialog(null, "Ocurrió un error al tratar de enviar la tarea seleccionada a la papelera", "Enviar Tarea a la papelera", JOptionPane.ERROR_MESSAGE);
                    pgbProgress.setValue(0);
                    pgbProgress.setVisible(false);
                }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e, "Error al seleccionar", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void trashUser(javax.swing.JTable tabla){
        try{
            int fila = tabla.getSelectedRow();
            if(fila == -1)
                JOptionPane.showMessageDialog(null, "No se ha seleccionado un usuario", "Error", JOptionPane.ERROR_MESSAGE);
            else
                if(me.Id() != Integer.parseInt(tabla.getValueAt(fila, 0).toString())){
                    if(JOptionPane.showOptionDialog(null, "¿Deseas enviar a '" + tabla.getValueAt(fila, 3).toString() 
                            + "' a la papelera?", "Enviar Usuario a la Papelera", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, rootPane) == 0){
                        pgbProgress.setVisible(true);
                        pgbProgress.setValue(20);
                        if(exRef.Eliminar(Integer.parseInt(tabla.getValueAt(fila, 0).toString()), false, (byte)3)){
                            pgbProgress.setValue(80);
                            Refresh();
                            pgbProgress.setValue(90);
                            if(ftA != null) ftA.Refresh();
                            pgbProgress.setValue(0);
                            pgbProgress.setVisible(false);
                            JOptionPane.showMessageDialog(null, "El usuario seleccionado se ha enviado a la papelera satisfactoriamente.", "Enviar Usuario a la papelera", JOptionPane.PLAIN_MESSAGE);
                        }else JOptionPane.showMessageDialog(null, "Ocurrió un error al tratar de enviar el usuario seleccionado a la papelera", "Enviar Usuario a la papelera", JOptionPane.ERROR_MESSAGE);
                        pgbProgress.setValue(0);
                        pgbProgress.setVisible(false);
                    }
                }else
                    JOptionPane.showMessageDialog(null, "No puedes enviarte a la papelera.", "Enviar Usuario a la papelera", JOptionPane.ERROR_MESSAGE);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e, "Error al seleccionar", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void deleteProject(javax.swing.JTable tabla){
        try{
            int fila = tabla.getSelectedRow();
            if(fila == -1)
                JOptionPane.showMessageDialog(null, "No se ha seleccionado un proyecto", "Error", JOptionPane.ERROR_MESSAGE);
            else
                if(JOptionPane.showOptionDialog(null, "Al eliminar el proyecto '" + tabla.getValueAt(fila, 1) 
                        + "', también se eliminaran sus tareas. ¿Deseas continuar?", "Eliminar Proyecto", 
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, rootPane) == 0){
                    pgbProgress.setVisible(true);
                    pgbProgress.setValue(70);
                    if(exRef.Eliminar(Integer.parseInt(tabla.getValueAt(fila, 0).toString()), true, (byte)1)){                        
                        pgbProgress.setValue(100);
                        pgbProgress.setVisible(false);
                        JOptionPane.showMessageDialog(null, "El proyecto seleccionado se ha eliminado satisfactoriamente.", "Eliminar Proyecto", JOptionPane.PLAIN_MESSAGE);
                    }else{
                        pgbProgress.setVisible(false);
                        JOptionPane.showMessageDialog(null, "Ha ocurrido un error al tratar de eliminar el proyecto seleccionado", "Eliminar Proyecto", JOptionPane.ERROR_MESSAGE);                    
                    }
                    pgbProgress.setValue(0);                    
                }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e, "Error al seleccionar", JOptionPane.ERROR_MESSAGE);
        }
        Refresh();
        if(ftA != null) ftA.Refresh();
    }
    
    public void deleteTask(javax.swing.JTable tabla){
        try{
            int fila = tabla.getSelectedRow();
            if(fila == -1)
                JOptionPane.showMessageDialog(null, "No se ha seleccionado una tarea", "Error", JOptionPane.ERROR_MESSAGE);
            else
                if(JOptionPane.showOptionDialog(null, "¿Deseas eliminar la tarea '" + tabla.getValueAt(fila, 1).toString()
                        + "'?", "Eliminar Tarea", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, rootPane) == 0){
                    pgbProgress.setVisible(true);
                    pgbProgress.setValue(70);
                    if(exRef.Eliminar(Integer.parseInt(tabla.getValueAt(fila, 0).toString()), true, (byte)2)){
                        pgbProgress.setValue(100);
                        pgbProgress.setVisible(false);
                        JOptionPane.showMessageDialog(null, "La tarea seleccionada se ha eliminado satisfactoriamente", "Eliminar Tarea", JOptionPane.PLAIN_MESSAGE);
                    }else{
                        pgbProgress.setVisible(false);
                        JOptionPane.showMessageDialog(null, "Ha ocurrido un error al tratar de eliminar la tarea seleccionada", "Eliminar Tarea", JOptionPane.ERROR_MESSAGE);
                    }
                    pgbProgress.setValue(0);
                }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e, "Error al seleccionar", JOptionPane.ERROR_MESSAGE);
        }
        Refresh();
        if(ftA != null) ftA.Refresh();
    }
    
    public void deleteUser(javax.swing.JTable tabla){
        try{
            int fila = tabla.getSelectedRow();
            if(fila == -1)
                JOptionPane.showMessageDialog(null, "No se ha seleccionado un usuario", "Error", JOptionPane.ERROR_MESSAGE);
            else
                if(me.Id() != Integer.parseInt(tabla.getValueAt(fila, 0).toString())){
                    if(JOptionPane.showOptionDialog(null, "¿Deseas eliminar a '" + tabla.getValueAt(fila, 3).toString() 
                            + "'?", "Eliminar Usuario", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, rootPane) == 0){
                        pgbProgress.setVisible(true);
                        pgbProgress.setValue(70);
                        if(exRef.Eliminar(Integer.parseInt(tabla.getValueAt(fila, 0).toString()), true, (byte)3)){
                            pgbProgress.setValue(100);
                            pgbProgress.setVisible(false);
                            JOptionPane.showMessageDialog(null, "El usuario seleccionado se ha eliminado satisfactoriamente.", "Eliminar Usuario", JOptionPane.PLAIN_MESSAGE);
                        }else{
                            pgbProgress.setVisible(false);
                            JOptionPane.showMessageDialog(null, "Ha ocurrido un error al tratar de eliminar el usuario seleccionado", "Eliminar Usuario", JOptionPane.ERROR_MESSAGE);
                        }
                        pgbProgress.setValue(0);
                    }
                }else
                    JOptionPane.showMessageDialog(null, "No puedes eliminarte", "Eliminar Usuario", JOptionPane.ERROR_MESSAGE);                                        
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e, "Error al seleccionar", JOptionPane.ERROR_MESSAGE);
        }
        Refresh();
        if(ftA != null) ftA.Refresh();
    }
    
    public void modifyProject(javax.swing.JTable tabla){
        try{
            int fila = tabla.getSelectedRow();
            if(fila == -1) 
                JOptionPane.showMessageDialog(null, "No se ha seleccionado un proyecto", "Error", JOptionPane.ERROR_MESSAGE);
            else{
                if(np == null){
                    np = new NewP();
                    np.setDatos(new Data(Integer.parseInt(tabla.getValueAt(fila, 0).toString()), 
                            tabla.getValueAt(fila, 1).toString()), tabla.getValueAt(fila, 2).toString(), 
                            tabla.getValueAt(fila, 3).toString(), tabla.getValueAt(fila, 4).toString(), 
                            tabla.getValueAt(fila, 5).toString());
                    np.setFAdmin(this);
                    np.setVisible(true);                    
                }else
                    np.setVisible(true);
            }
        }catch(Exception e){            
            JOptionPane.showMessageDialog(null, e, "Error al seleccionar", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void modifyTask(javax.swing.JTable tabla){
        try{
            int fila = tabla.getSelectedRow();
            if(fila == -1) 
                JOptionPane.showMessageDialog(null, "No se ha seleccionado una tarea", "Error", JOptionPane.ERROR_MESSAGE);
            else{
                if(nt == null){
                    nt = new NewT();
                    nt.setDatos(new Data(Integer.parseInt(tabla.getValueAt(fila, 0).toString()), 
                            tabla.getValueAt(fila, 1).toString()), tabla.getValueAt(fila, 2).toString(),
                            tabla.getValueAt(fila, 3).toString(), 
                            tabla.getValueAt(fila, 4).toString(), tabla.getValueAt(fila, 5).toString(), 
                            tabla.getValueAt(fila, 6).toString(), tabla.getValueAt(fila, 7).toString(), 
                            true);
                    nt.setFAdmin(this);
                    nt.setVisible(true);
                }else
                    nt.setVisible(true);
            }
        }catch(Exception e){            
            JOptionPane.showMessageDialog(null, e, "Error al seleccionar", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void modifyUser(javax.swing.JTable tabla){
        boolean isMe = false;
        try{
            int fila = tabla.getSelectedRow();
            if(fila == -1) 
                JOptionPane.showMessageDialog(null, "No se ha seleccionado un usuario", "Error", JOptionPane.ERROR_MESSAGE);
            else{
                if(pf == null){
                    pf = new Profile();
                    if(Integer.parseInt(tabla.getValueAt(fila, 0).toString()) == me.Id()) isMe = true;
                    pf.setData(new Data(Integer.parseInt(tabla.getValueAt(fila, 0).toString()), tabla.getValueAt(fila, 3).toString()), isMe);
                    pf.setVisiblePriv(true);
                    pf.setFAdmin(this);
                    pf.setVisible(true);
                }else
                    pf.setVisible(true);
            }
        }catch(Exception e){            
            JOptionPane.showMessageDialog(null, e, "Error al seleccionar", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void setData(Data u){
        me = u;
        lblName.setText(me.Name());        
        Refresh();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mnProyectos = new javax.swing.JPopupMenu();
        mnAbrirP = new javax.swing.JMenuItem();
        mnAgregarP = new javax.swing.JMenuItem();
        mnAsignarP = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        mnEliminarMP = new javax.swing.JMenu();
        mnPapeleraP = new javax.swing.JMenuItem();
        mnEliminarP = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        mnModificarP = new javax.swing.JMenuItem();
        mnTareas = new javax.swing.JPopupMenu();
        mnAbrirT = new javax.swing.JMenuItem();
        mnAgregarT = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        mnEliminarMT = new javax.swing.JMenu();
        mnPapeleraT = new javax.swing.JMenuItem();
        mnEliminarT = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        mnModificarT = new javax.swing.JMenuItem();
        mnUsuarios = new javax.swing.JPopupMenu();
        mnAbrirU = new javax.swing.JMenuItem();
        mnAgregarU = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        mnEliminarMU = new javax.swing.JMenu();
        mnPapeleraU = new javax.swing.JMenuItem();
        mnEliminarU = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JPopupMenu.Separator();
        mnModificarU = new javax.swing.JMenuItem();
        lblName = new javax.swing.JLabel();
        btnPerfil = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblProy = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cboOrganizarP = new javax.swing.JComboBox();
        btnOrganizarP = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTareas = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        cboOrganizarT = new javax.swing.JComboBox();
        btnOrganizarT = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblUser = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        cboOrganizarU = new javax.swing.JComboBox();
        btnOrganizarU = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        lblClose = new javax.swing.JLabel();
        imgUser = new PictureBox.PictureBox();
        lblProy = new javax.swing.JLabel();
        lblTarea = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        lblProy1 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel7 = new javax.swing.JLabel();
        pgbProgress = new javax.swing.JProgressBar();
        mnBarra = new javax.swing.JMenuBar();
        mnuProject = new javax.swing.JMenu();
        mnNuevo = new javax.swing.JMenu();
        mnNewP = new javax.swing.JMenuItem();
        mnNewT = new javax.swing.JMenuItem();
        mnNewU = new javax.swing.JMenuItem();
        jSeparator12 = new javax.swing.JPopupMenu.Separator();
        mnCerrar = new javax.swing.JMenuItem();
        mnSalir = new javax.swing.JMenuItem();
        mnbProyecto = new javax.swing.JMenu();
        mnAsignT = new javax.swing.JMenuItem();
        mnAddP = new javax.swing.JMenuItem();
        jSeparator10 = new javax.swing.JPopupMenu.Separator();
        mnbDeleteP = new javax.swing.JMenu();
        mnTrashP = new javax.swing.JMenuItem();
        mnDeleteP = new javax.swing.JMenuItem();
        jSeparator11 = new javax.swing.JPopupMenu.Separator();
        mnModifyP = new javax.swing.JMenuItem();
        mnShowP = new javax.swing.JMenuItem();
        mnbTarea = new javax.swing.JMenu();
        mnAddT = new javax.swing.JMenuItem();
        jSeparator13 = new javax.swing.JPopupMenu.Separator();
        mnbDeleteT = new javax.swing.JMenu();
        mnTrashT = new javax.swing.JMenuItem();
        mnDeleteT = new javax.swing.JMenuItem();
        jSeparator14 = new javax.swing.JPopupMenu.Separator();
        mnModifyT = new javax.swing.JMenuItem();
        mnShowT = new javax.swing.JMenuItem();
        mnbUsuario = new javax.swing.JMenu();
        mnAddU = new javax.swing.JMenuItem();
        jSeparator15 = new javax.swing.JPopupMenu.Separator();
        mnbDeleteU = new javax.swing.JMenu();
        mnTrashU = new javax.swing.JMenuItem();
        mnDeleteU = new javax.swing.JMenuItem();
        jSeparator16 = new javax.swing.JPopupMenu.Separator();
        mnModifyU = new javax.swing.JMenuItem();
        mnShowU = new javax.swing.JMenuItem();
        mnbAyuda = new javax.swing.JMenu();
        mnAcerca = new javax.swing.JMenuItem();
        mnAyuda = new javax.swing.JMenuItem();

        mnAbrirP.setText("Abrir en otra ventana...");
        mnAbrirP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAbrirPActionPerformed(evt);
            }
        });
        mnProyectos.add(mnAbrirP);

        mnAgregarP.setText("Añadir Nuevo Proyecto...");
        mnAgregarP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAgregarPActionPerformed(evt);
            }
        });
        mnProyectos.add(mnAgregarP);

        mnAsignarP.setText("Asignar Tarea...");
        mnAsignarP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAsignarPActionPerformed(evt);
            }
        });
        mnProyectos.add(mnAsignarP);
        mnProyectos.add(jSeparator4);

        mnEliminarMP.setText("Eliminar");
        mnEliminarMP.setToolTipText("");

        mnPapeleraP.setText("Eliminar y enviar a la papelera");
        mnPapeleraP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnPapeleraPActionPerformed(evt);
            }
        });
        mnEliminarMP.add(mnPapeleraP);

        mnEliminarP.setText("Eliminar de manera permanente");
        mnEliminarP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnEliminarPActionPerformed(evt);
            }
        });
        mnEliminarMP.add(mnEliminarP);

        mnProyectos.add(mnEliminarMP);
        mnProyectos.add(jSeparator5);

        mnModificarP.setText("Modificar");
        mnModificarP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnModificarPActionPerformed(evt);
            }
        });
        mnProyectos.add(mnModificarP);

        mnAbrirT.setText("Abrir en otra ventana...");
        mnAbrirT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAbrirTActionPerformed(evt);
            }
        });
        mnTareas.add(mnAbrirT);

        mnAgregarT.setText("Añadir Nueva Tarea...");
        mnAgregarT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAgregarTActionPerformed(evt);
            }
        });
        mnTareas.add(mnAgregarT);
        mnTareas.add(jSeparator6);

        mnEliminarMT.setText("Eliminar");

        mnPapeleraT.setText("Eliminar y enviar a la papelera");
        mnPapeleraT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnPapeleraTActionPerformed(evt);
            }
        });
        mnEliminarMT.add(mnPapeleraT);

        mnEliminarT.setText("Eliminar de manera permanente");
        mnEliminarT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnEliminarTActionPerformed(evt);
            }
        });
        mnEliminarMT.add(mnEliminarT);

        mnTareas.add(mnEliminarMT);
        mnTareas.add(jSeparator7);

        mnModificarT.setText("Modificar");
        mnModificarT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnModificarTActionPerformed(evt);
            }
        });
        mnTareas.add(mnModificarT);

        mnAbrirU.setText("Abrir en otra ventana...");
        mnAbrirU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAbrirUActionPerformed(evt);
            }
        });
        mnUsuarios.add(mnAbrirU);

        mnAgregarU.setText("Añadir Nuevo Usuario...");
        mnAgregarU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAgregarUActionPerformed(evt);
            }
        });
        mnUsuarios.add(mnAgregarU);
        mnUsuarios.add(jSeparator8);

        mnEliminarMU.setText("Eliminar");

        mnPapeleraU.setText("Eliminar y enviar a la papelera");
        mnPapeleraU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnPapeleraUActionPerformed(evt);
            }
        });
        mnEliminarMU.add(mnPapeleraU);

        mnEliminarU.setText("Eliminar de manera permanente");
        mnEliminarU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnEliminarUActionPerformed(evt);
            }
        });
        mnEliminarMU.add(mnEliminarU);

        mnUsuarios.add(mnEliminarMU);
        mnUsuarios.add(jSeparator9);

        mnModificarU.setText("Modificar");
        mnModificarU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnModificarUActionPerformed(evt);
            }
        });
        mnUsuarios.add(mnModificarU);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        lblName.setFont(new java.awt.Font("Tahoma", 1, 14));
        lblName.setText("Nombre");

        btnPerfil.setText("Editar Perfil");
        btnPerfil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPerfilActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Resumen de Proyectos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14))); // NOI18N

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
        tblProy.setComponentPopupMenu(mnProyectos);
        jScrollPane2.setViewportView(tblProy);

        jLabel1.setForeground(new java.awt.Color(0, 0, 255));
        jLabel1.setText("Abrir en otra ventana");
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        jLabel2.setText("Organizar Por:");

        cboOrganizarP.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Mis Proyectos", "Todos" }));

        btnOrganizarP.setText("Organizar");
        btnOrganizarP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOrganizarPActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 10));
        jLabel8.setText("Selecciona un registro y haz clic derecho");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cboOrganizarP, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnOrganizarP))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 273, Short.MAX_VALUE)
                        .addComponent(jLabel1)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cboOrganizarP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOrganizarP))
                .addGap(8, 8, 8)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel8)))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Resumen de Tareas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14), new java.awt.Color(0, 0, 0))); // NOI18N

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
        tblTareas.setComponentPopupMenu(mnTareas);
        jScrollPane1.setViewportView(tblTareas);

        jLabel3.setText("Organizar Por:");

        btnOrganizarT.setText("Organizar");
        btnOrganizarT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOrganizarTActionPerformed(evt);
            }
        });

        jLabel4.setForeground(new java.awt.Color(0, 0, 255));
        jLabel4.setText("Abrir en otra ventana");
        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 10));
        jLabel9.setText("Selecciona un registro y haz clic derecho");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cboOrganizarT, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOrganizarT))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 273, Short.MAX_VALUE)
                        .addComponent(jLabel4)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cboOrganizarT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOrganizarT))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel9)))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Resumen de Usuarios", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14), new java.awt.Color(0, 0, 0))); // NOI18N

        tblUser.setModel(new javax.swing.table.DefaultTableModel(
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
        tblUser.setComponentPopupMenu(mnUsuarios);
        jScrollPane3.setViewportView(tblUser);

        jLabel5.setText("Organizar Por:");

        cboOrganizarU.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Administradores", "Normales", "Todos" }));

        btnOrganizarU.setText("Organizar");
        btnOrganizarU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOrganizarUActionPerformed(evt);
            }
        });

        jLabel6.setForeground(new java.awt.Color(0, 0, 255));
        jLabel6.setText("Abrir en otra ventana");
        jLabel6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 10));
        jLabel10.setText("Selecciona un registro y haz clic derecho");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cboOrganizarU, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnOrganizarU))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 273, Short.MAX_VALUE)
                        .addComponent(jLabel6)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cboOrganizarU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOrganizarU))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel10)))
        );

        lblClose.setForeground(new java.awt.Color(0, 0, 255));
        lblClose.setText("Cerrar Sesión");
        lblClose.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCloseMouseClicked(evt);
            }
        });

        imgUser.setArchivo("E:\\Isay\\Documentos\\Universidad de Colima\\5 semestre\\Programación Distribuida\\Programas\\Jupiter(alfa)\\src\\no user.jpg");
        imgUser.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        imgUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                imgUserMouseClicked(evt);
            }
        });

        lblProy.setForeground(new java.awt.Color(0, 0, 255));
        lblProy.setText("Crear Nuevo Proyecto...");
        lblProy.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblProy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblProyMouseClicked(evt);
            }
        });

        lblTarea.setForeground(new java.awt.Color(0, 0, 255));
        lblTarea.setText("Crear Nueva Tarea...");
        lblTarea.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblTarea.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTareaMouseClicked(evt);
            }
        });

        lblUser.setForeground(new java.awt.Color(0, 0, 255));
        lblUser.setText("Crear Nuevo Usuario...");
        lblUser.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblUserMouseClicked(evt);
            }
        });

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        lblProy1.setForeground(new java.awt.Color(0, 0, 255));
        lblProy1.setText("Cambiar Contraseña");
        lblProy1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblProy1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblProy1MouseClicked(evt);
            }
        });

        jLabel7.setForeground(new java.awt.Color(0, 0, 255));
        jLabel7.setText("Ir a la Papelera...");
        jLabel7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
        });

        mnuProject.setText("uProject");

        mnNuevo.setText("Nuevo");

        mnNewP.setText("Proyecto...");
        mnNewP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnNewPActionPerformed(evt);
            }
        });
        mnNuevo.add(mnNewP);

        mnNewT.setText("Tarea...");
        mnNewT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnNewTActionPerformed(evt);
            }
        });
        mnNuevo.add(mnNewT);

        mnNewU.setText("Usuario...");
        mnNewU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnNewUActionPerformed(evt);
            }
        });
        mnNuevo.add(mnNewU);

        mnuProject.add(mnNuevo);
        mnuProject.add(jSeparator12);

        mnCerrar.setText("Cerrar Sesión");
        mnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnCerrarActionPerformed(evt);
            }
        });
        mnuProject.add(mnCerrar);

        mnSalir.setText("Salir");
        mnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnSalirActionPerformed(evt);
            }
        });
        mnuProject.add(mnSalir);

        mnBarra.add(mnuProject);

        mnbProyecto.setText("Proyecto");

        mnAsignT.setText("Asignar Tarea...");
        mnAsignT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAsignTActionPerformed(evt);
            }
        });
        mnbProyecto.add(mnAsignT);

        mnAddP.setText("Crear Nuevo Proyecto...");
        mnAddP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAddPActionPerformed(evt);
            }
        });
        mnbProyecto.add(mnAddP);
        mnbProyecto.add(jSeparator10);

        mnbDeleteP.setText("Eliminar");

        mnTrashP.setText("Enviar a la Papelera");
        mnTrashP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnTrashPActionPerformed(evt);
            }
        });
        mnbDeleteP.add(mnTrashP);

        mnDeleteP.setText("Eliminar Permanentemente");
        mnDeleteP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnDeletePActionPerformed(evt);
            }
        });
        mnbDeleteP.add(mnDeleteP);

        mnbProyecto.add(mnbDeleteP);
        mnbProyecto.add(jSeparator11);

        mnModifyP.setText("Modificar Proyecto...");
        mnModifyP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnModifyPActionPerformed(evt);
            }
        });
        mnbProyecto.add(mnModifyP);

        mnShowP.setText("Mostrar Proyectos...");
        mnShowP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnShowPActionPerformed(evt);
            }
        });
        mnbProyecto.add(mnShowP);

        mnBarra.add(mnbProyecto);

        mnbTarea.setText("Tarea");

        mnAddT.setText("Crear Nueva Tarea...");
        mnAddT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAddTActionPerformed(evt);
            }
        });
        mnbTarea.add(mnAddT);
        mnbTarea.add(jSeparator13);

        mnbDeleteT.setText("Eliminar");

        mnTrashT.setText("Enviar a la Papelera");
        mnTrashT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnTrashTActionPerformed(evt);
            }
        });
        mnbDeleteT.add(mnTrashT);

        mnDeleteT.setText("Eliminar Permanentemente");
        mnDeleteT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnDeleteTActionPerformed(evt);
            }
        });
        mnbDeleteT.add(mnDeleteT);

        mnbTarea.add(mnbDeleteT);
        mnbTarea.add(jSeparator14);

        mnModifyT.setText("Modificar Tarea...");
        mnModifyT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnModifyTActionPerformed(evt);
            }
        });
        mnbTarea.add(mnModifyT);

        mnShowT.setText("Mostrar Tareas...");
        mnShowT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnShowTActionPerformed(evt);
            }
        });
        mnbTarea.add(mnShowT);

        mnBarra.add(mnbTarea);

        mnbUsuario.setText("Usuario");

        mnAddU.setText("Crear Nuevo Usuario...");
        mnAddU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAddUActionPerformed(evt);
            }
        });
        mnbUsuario.add(mnAddU);
        mnbUsuario.add(jSeparator15);

        mnbDeleteU.setText("Eliminar");

        mnTrashU.setText("Enviar a la Papelera");
        mnTrashU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnTrashUActionPerformed(evt);
            }
        });
        mnbDeleteU.add(mnTrashU);

        mnDeleteU.setText("Eliminar Permanentemente");
        mnDeleteU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnDeleteUActionPerformed(evt);
            }
        });
        mnbDeleteU.add(mnDeleteU);

        mnbUsuario.add(mnbDeleteU);
        mnbUsuario.add(jSeparator16);

        mnModifyU.setText("Modificar Usuario...");
        mnModifyU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnModifyUActionPerformed(evt);
            }
        });
        mnbUsuario.add(mnModifyU);

        mnShowU.setText("Mostrar Usuarios...");
        mnShowU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnShowUActionPerformed(evt);
            }
        });
        mnbUsuario.add(mnShowU);

        mnBarra.add(mnbUsuario);

        mnbAyuda.setText("Ayuda");

        mnAcerca.setText("Acerca de...");
        mnbAyuda.add(mnAcerca);

        mnAyuda.setText("Ayuda");
        mnbAyuda.add(mnAyuda);

        mnBarra.add(mnbAyuda);

        setJMenuBar(mnBarra);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(imgUser, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                        .addComponent(lblProy1)
                        .addComponent(lblClose))
                    .addComponent(lblProy)
                    .addComponent(lblTarea)
                    .addComponent(lblUser)
                    .addComponent(pgbProgress, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblName)
                        .addGap(18, 18, 18)
                        .addComponent(btnPerfil)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 372, Short.MAX_VALUE)
                        .addComponent(jLabel7))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblName)
                    .addComponent(btnPerfil)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(imgUser, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblProy1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblClose)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblProy)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTarea)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblUser)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 270, Short.MAX_VALUE)
                        .addComponent(pgbProgress, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCloseMouseClicked
        closeSession();
    }//GEN-LAST:event_lblCloseMouseClicked

    private void btnOrganizarPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOrganizarPActionPerformed
        if(cboOrganizarP.getSelectedItem().toString().equals("Mis Proyectos")) loadTablaProy(exRef.ObtenerMisProyectos(me.Id(), true), tblProy, modeloP, cboOrganizarT, null);
        else if(cboOrganizarP.getSelectedItem().toString().equals("Todos")) loadTablaProy(exRef.Obtener("", (byte)1, (byte)1), tblProy, modeloP, cboOrganizarT, null);
        
        Data p = (Data) cboOrganizarT.getItemAt(cboOrganizarT.getSelectedIndex());        
        loadTablaTarea(exRef.ObtenerMisTareas(p.Id(), true), tblTareas, modeloT);
    }//GEN-LAST:event_btnOrganizarPActionPerformed

    private void btnOrganizarTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOrganizarTActionPerformed
        if(cboOrganizarT.getSelectedItem().toString().equals("Todas")) loadTablaTarea(exRef.Obtener("", (byte)1, (byte)2), tblTareas, modeloT);
        else{
            Data p = (Data) cboOrganizarT.getItemAt(cboOrganizarT.getSelectedIndex());
            loadTablaTarea(exRef.ObtenerMisTareas(p.Id(), true), tblTareas, modeloT);
        }
    }//GEN-LAST:event_btnOrganizarTActionPerformed

    private void btnOrganizarUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOrganizarUActionPerformed
        if(cboOrganizarU.getSelectedItem().toString().equals("Administradores")) loadTablaUser("Administrador", tblUser, modeloU);
        else if(cboOrganizarU.getSelectedItem().toString().equals("Normales")) loadTablaUser("Normal", tblUser, modeloU);
        else loadTablaUser("", tblUser, modeloU);        
    }//GEN-LAST:event_btnOrganizarUActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        new Login().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_formWindowClosing

    private void lblProyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblProyMouseClicked
        newP();
    }//GEN-LAST:event_lblProyMouseClicked

    private void lblTareaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTareaMouseClicked
        newT();
    }//GEN-LAST:event_lblTareaMouseClicked

    private void lblUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblUserMouseClicked
        newU();
    }//GEN-LAST:event_lblUserMouseClicked

    private void btnPerfilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPerfilActionPerformed
        if(pf == null){
            pf = new Profile();
            pf.setData(me, true);
            pf.setVisiblePriv(true);
            pf.setFAdmin(this);
        }
        pf.setVisible(true);
    }//GEN-LAST:event_btnPerfilActionPerformed

    private void lblProy1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblProy1MouseClicked
        if(npwd == null){
            npwd = new NewPwd();
            npwd.setMe(me);
            npwd.setFAdmin(this);
            npwd.setVisible(true);            
        }else
            npwd.setVisible(true);
    }//GEN-LAST:event_lblProy1MouseClicked

    private void mnAgregarPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAgregarPActionPerformed
        newP();
    }//GEN-LAST:event_mnAgregarPActionPerformed

    private void mnAgregarTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAgregarTActionPerformed
        newT();
    }//GEN-LAST:event_mnAgregarTActionPerformed

    private void mnAgregarUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAgregarUActionPerformed
        newU();
    }//GEN-LAST:event_mnAgregarUActionPerformed

    private void mnPapeleraPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnPapeleraPActionPerformed
        trashProject(tblProy);
    }//GEN-LAST:event_mnPapeleraPActionPerformed

    private void mnEliminarPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnEliminarPActionPerformed
        deleteProject(tblProy);
    }//GEN-LAST:event_mnEliminarPActionPerformed

    private void mnPapeleraTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnPapeleraTActionPerformed
        trashTask(tblTareas);
    }//GEN-LAST:event_mnPapeleraTActionPerformed

    private void mnEliminarTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnEliminarTActionPerformed
        deleteTask(tblTareas);
    }//GEN-LAST:event_mnEliminarTActionPerformed

    private void mnPapeleraUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnPapeleraUActionPerformed
        trashUser(tblUser);
    }//GEN-LAST:event_mnPapeleraUActionPerformed

    private void mnEliminarUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnEliminarUActionPerformed
        deleteUser(tblUser);
    }//GEN-LAST:event_mnEliminarUActionPerformed

    private void mnModificarUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnModificarUActionPerformed
       modifyUser(tblUser);
    }//GEN-LAST:event_mnModificarUActionPerformed

    private void mnModificarPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnModificarPActionPerformed
        modifyProject(tblProy);
    }//GEN-LAST:event_mnModificarPActionPerformed

    private void mnModificarTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnModificarTActionPerformed
        modifyTask(tblTareas);
    }//GEN-LAST:event_mnModificarTActionPerformed

    private void mnAsignarPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAsignarPActionPerformed
        asignTasktoProject(tblProy);
    }//GEN-LAST:event_mnAsignarPActionPerformed

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        showProjects();
    }//GEN-LAST:event_jLabel1MouseClicked

    private void mnAbrirPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAbrirPActionPerformed
        showProjects();
    }//GEN-LAST:event_mnAbrirPActionPerformed

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        showUsers();
    }//GEN-LAST:event_jLabel6MouseClicked

    private void mnAbrirTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAbrirTActionPerformed
        showTasks();
    }//GEN-LAST:event_mnAbrirTActionPerformed

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        showTasks();
    }//GEN-LAST:event_jLabel4MouseClicked

    private void mnAbrirUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAbrirUActionPerformed
        showUsers();
    }//GEN-LAST:event_mnAbrirUActionPerformed

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
        if(tr == null) tr = new Trash();
        tr.setFAdmin(this);                    
        tr.setVisible(true);
    }//GEN-LAST:event_jLabel7MouseClicked

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        
    }//GEN-LAST:event_formWindowOpened

    private void mnNewPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnNewPActionPerformed
        newP();
    }//GEN-LAST:event_mnNewPActionPerformed

    private void mnNewTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnNewTActionPerformed
        newT();
    }//GEN-LAST:event_mnNewTActionPerformed

    private void mnNewUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnNewUActionPerformed
        newU();
    }//GEN-LAST:event_mnNewUActionPerformed

    private void mnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnCerrarActionPerformed
        closeSession();
    }//GEN-LAST:event_mnCerrarActionPerformed

    private void mnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_mnSalirActionPerformed

    private void mnAsignTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAsignTActionPerformed
        asignTasktoProject(tblProy);
    }//GEN-LAST:event_mnAsignTActionPerformed

    private void mnAddPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAddPActionPerformed
        newP();
    }//GEN-LAST:event_mnAddPActionPerformed

    private void mnTrashPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnTrashPActionPerformed
        trashProject(tblProy);
    }//GEN-LAST:event_mnTrashPActionPerformed

    private void mnDeletePActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnDeletePActionPerformed
        deleteProject(tblProy);
    }//GEN-LAST:event_mnDeletePActionPerformed

    private void mnModifyPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnModifyPActionPerformed
        modifyProject(tblProy);
    }//GEN-LAST:event_mnModifyPActionPerformed

    private void mnShowPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnShowPActionPerformed
        showProjects();
    }//GEN-LAST:event_mnShowPActionPerformed

    private void mnAddTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAddTActionPerformed
        newT();
    }//GEN-LAST:event_mnAddTActionPerformed

    private void mnTrashTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnTrashTActionPerformed
        trashTask(tblTareas);
    }//GEN-LAST:event_mnTrashTActionPerformed

    private void mnDeleteTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnDeleteTActionPerformed
        deleteTask(tblTareas);
    }//GEN-LAST:event_mnDeleteTActionPerformed

    private void mnModifyTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnModifyTActionPerformed
        modifyTask(tblTareas);
    }//GEN-LAST:event_mnModifyTActionPerformed

    private void mnShowTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnShowTActionPerformed
        showTasks();
    }//GEN-LAST:event_mnShowTActionPerformed

    private void mnAddUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAddUActionPerformed
        newU();
    }//GEN-LAST:event_mnAddUActionPerformed

    private void mnTrashUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnTrashUActionPerformed
        trashUser(tblUser);
    }//GEN-LAST:event_mnTrashUActionPerformed

    private void mnDeleteUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnDeleteUActionPerformed
        deleteUser(tblUser);
    }//GEN-LAST:event_mnDeleteUActionPerformed

    private void mnModifyUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnModifyUActionPerformed
        modifyUser(tblUser);
    }//GEN-LAST:event_mnModifyUActionPerformed

    private void mnShowUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnShowUActionPerformed
        showUsers();
    }//GEN-LAST:event_mnShowUActionPerformed

    private void imgUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imgUserMouseClicked
        JFileChooser fc = new JFileChooser();        
        if (fc.showDialog(null, "Selecciona el archivo...") == JFileChooser.APPROVE_OPTION){            
            imgUser.setArchivo(fc.getSelectedFile().toString());
        }
    }//GEN-LAST:event_imgUserMouseClicked

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
            java.util.logging.Logger.getLogger(FAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new FAdmin().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOrganizarP;
    private javax.swing.JButton btnOrganizarT;
    private javax.swing.JButton btnOrganizarU;
    private javax.swing.JButton btnPerfil;
    private javax.swing.JComboBox cboOrganizarP;
    private javax.swing.JComboBox cboOrganizarT;
    private javax.swing.JComboBox cboOrganizarU;
    private PictureBox.PictureBox imgUser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator10;
    private javax.swing.JPopupMenu.Separator jSeparator11;
    private javax.swing.JPopupMenu.Separator jSeparator12;
    private javax.swing.JPopupMenu.Separator jSeparator13;
    private javax.swing.JPopupMenu.Separator jSeparator14;
    private javax.swing.JPopupMenu.Separator jSeparator15;
    private javax.swing.JPopupMenu.Separator jSeparator16;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JPopupMenu.Separator jSeparator9;
    private javax.swing.JLabel lblClose;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblProy;
    private javax.swing.JLabel lblProy1;
    private javax.swing.JLabel lblTarea;
    private javax.swing.JLabel lblUser;
    private javax.swing.JMenuItem mnAbrirP;
    private javax.swing.JMenuItem mnAbrirT;
    private javax.swing.JMenuItem mnAbrirU;
    private javax.swing.JMenuItem mnAcerca;
    private javax.swing.JMenuItem mnAddP;
    private javax.swing.JMenuItem mnAddT;
    private javax.swing.JMenuItem mnAddU;
    private javax.swing.JMenuItem mnAgregarP;
    private javax.swing.JMenuItem mnAgregarT;
    private javax.swing.JMenuItem mnAgregarU;
    private javax.swing.JMenuItem mnAsignT;
    private javax.swing.JMenuItem mnAsignarP;
    private javax.swing.JMenuItem mnAyuda;
    private javax.swing.JMenuBar mnBarra;
    private javax.swing.JMenuItem mnCerrar;
    private javax.swing.JMenuItem mnDeleteP;
    private javax.swing.JMenuItem mnDeleteT;
    private javax.swing.JMenuItem mnDeleteU;
    private javax.swing.JMenu mnEliminarMP;
    private javax.swing.JMenu mnEliminarMT;
    private javax.swing.JMenu mnEliminarMU;
    private javax.swing.JMenuItem mnEliminarP;
    private javax.swing.JMenuItem mnEliminarT;
    private javax.swing.JMenuItem mnEliminarU;
    private javax.swing.JMenuItem mnModificarP;
    private javax.swing.JMenuItem mnModificarT;
    private javax.swing.JMenuItem mnModificarU;
    private javax.swing.JMenuItem mnModifyP;
    private javax.swing.JMenuItem mnModifyT;
    private javax.swing.JMenuItem mnModifyU;
    private javax.swing.JMenuItem mnNewP;
    private javax.swing.JMenuItem mnNewT;
    private javax.swing.JMenuItem mnNewU;
    private javax.swing.JMenu mnNuevo;
    private javax.swing.JMenuItem mnPapeleraP;
    private javax.swing.JMenuItem mnPapeleraT;
    private javax.swing.JMenuItem mnPapeleraU;
    private javax.swing.JPopupMenu mnProyectos;
    private javax.swing.JMenuItem mnSalir;
    private javax.swing.JMenuItem mnShowP;
    private javax.swing.JMenuItem mnShowT;
    private javax.swing.JMenuItem mnShowU;
    private javax.swing.JPopupMenu mnTareas;
    private javax.swing.JMenuItem mnTrashP;
    private javax.swing.JMenuItem mnTrashT;
    private javax.swing.JMenuItem mnTrashU;
    private javax.swing.JPopupMenu mnUsuarios;
    private javax.swing.JMenu mnbAyuda;
    private javax.swing.JMenu mnbDeleteP;
    private javax.swing.JMenu mnbDeleteT;
    private javax.swing.JMenu mnbDeleteU;
    private javax.swing.JMenu mnbProyecto;
    private javax.swing.JMenu mnbTarea;
    private javax.swing.JMenu mnbUsuario;
    private javax.swing.JMenu mnuProject;
    private javax.swing.JProgressBar pgbProgress;
    private javax.swing.JTable tblProy;
    private javax.swing.JTable tblTareas;
    private javax.swing.JTable tblUser;
    // End of variables declaration//GEN-END:variables
}
