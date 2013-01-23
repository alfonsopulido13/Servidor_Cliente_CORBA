package Forms_Alfa;

import Extras.CORBA_Conn;
import Extras.Data;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author Isay
 */
public class NewT extends CORBA_Conn {
    private boolean guardar = true;
    private Data t;
    private FAdmin fa;
    private FNormal fn;
    private Hilo h;

    /** Creates new form NewT */
    public NewT() {
        initComponents();
        centrarVentana();
        pgbProgress.setMaximum(100);
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        cboProgress.setVisible(false);
        jLabel6.setVisible(false);
        Datos();
    }
    
    public void setFAdmin(FAdmin fa){this.fa = fa;}
    public void setFNormal(FNormal fn){this.fn = fn;}    
    
    void Datos(){
        DefaultListModel modelList = new DefaultListModel();;
        cboProy.removeAllItems();
        lstUsersDB.setModel(modelList);
        String[][] registros = exRef.Obtener("Normal", (byte)1, (byte)3);
        int i = 0;
        while(true){
            try{
                modelList.addElement(new Data(Integer.parseInt(registros[0][i]), registros[4][i]));
                i++;
            }catch(Exception e){ 
                registros = null;
                break; 
            }
        }        
        registros = exRef.Obtener("", (byte)1, (byte)1);
        i = 0;
        while(true){
            try{
                cboProy.addItem(new Data(Integer.parseInt(registros[0][i]), registros[2][i]));
                i++;
            }catch(Exception e){ break; }
        }
    }        
    
    void Limpiar(){
        txtnomT.setText("");
        cboProy.setSelectedIndex(0);
        DefaultListModel modelo = new DefaultListModel();        
        lstUsersTask.setModel(modelo);        
        txtDesc.setText("");
        cboPrioridad.setSelectedIndex(0);
        txtfi.setText("");
        txtff.setText("");
        cboProgress.setSelectedIndex(0);
        txtnomT.requestFocus();
    }
    
    private int getIndex(javax.swing.JComboBox caja, String cadena){
        int i = 0;
        for (int j = 0; j < caja.getItemCount(); j++) 
            if(caja.getItemAt(j).toString().equals(cadena)){
                i = j;
                break;
            }
        return i;
    }
        
    public void setDatos(Data t, String proyecto, String desc, String fi, String ff, String prioridad, String progreso, boolean admin){
        this.t = t;
        txtnomT.setText(this.t.Name());                
        
        cboProy.setSelectedIndex(getIndex(cboProy, proyecto));
        DefaultListModel modelo = new DefaultListModel();
        String[][] datos = exRef.ObtenerUsuarioPorTarea(t.Id());        
        int i = 0;
        while(true){
            try{
                modelo.addElement(new Data(Integer.parseInt(datos[0][i]), datos[1][i]));
                i++;
            }catch(Exception e){ break; }
        }
        lstUsersTask.setModel(modelo);
        txtDesc.setText(desc);
        cboPrioridad.setSelectedItem(prioridad);
        txtfi.setText(fi);
        txtff.setText(ff);
        cboProgress.setSelectedItem(progreso);
        cboProgress.setVisible(true);
        jLabel6.setVisible(true);
        if(!admin){
            txtnomT.setEnabled(admin);
            cboProy.setEnabled(admin);
            txtDesc.setEnabled(admin);
            cboPrioridad.setEnabled(admin);
            txtfi.setEnabled(admin);
            txtff.setEnabled(admin);
            jPanel1.setEnabled(admin);
            lstUsersTask.setEnabled(admin);
            lstUsersDB.setEnabled(admin);
            btnaddAll.setEnabled(admin);
            btnaddSelected.setEnabled(admin);
            btnremoveSelected.setEnabled(admin);
            btnremoveAll.setEnabled(admin);
        }
        guardar = false;
    }
    
    public void setLider(String lider){ cboProy.setSelectedIndex(getIndex(cboProy, lider)); }
    
    private String getUsuariosID_List(javax.swing.JList lista){
        String cadena = "";
        for(int i = 0; i < lista.getModel().getSize(); i++)
            cadena += ((Data) lista.getModel().getElementAt(i)).Id() + ",";
                
        return cadena;
    }
    
    void Salir(){        
        if(fa != null) fa.nt = null;
        else if(fn != null) fn.nt = null;
        this.dispose();
        if (h != null)h.stop();
    }
    
    class Hilo extends Thread{
        public void run(){
            try{
                if(!txtnomT.getText().equals("") && !txtDesc.getText().equals("") && !txtfi.getText().equals("") && 
                       !txtff.getText().equals("") && lstUsersTask.getModel().getSize() > 0){ 
                    Data p = (Data) cboProy.getItemAt(cboProy.getSelectedIndex());
                    pgbProgress.setValue(10);

                    String[] datos = {String.valueOf(p.Id()), txtnomT.getText(), txtDesc.getText(), txtfi.getText(),
                    txtff.getText(), cboPrioridad.getSelectedItem().toString(), cboProgress.getSelectedItem().toString() , getUsuariosID_List(lstUsersTask)};

                    if(guardar){
                        if(exRef.Crear(datos, (byte)2)){
                            pgbProgress.setValue(80);
                            fa.Refresh();
                            pgbProgress.setValue(90);
                            if(fa.ftA != null) fa.ftA.Refresh();
                            pgbProgress.setValue(0);
                            if(JOptionPane.showOptionDialog(null, "La tarea se ha creado satisfactoriamente. ¿Deseas crear otra tarea?", "Crear Tarea", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, rootPane) == 0){
                                Limpiar();
                            }else Salir();
                        }else JOptionPane.showMessageDialog(null, "Ha ocurrido un error al tratar de crear la tarea.", "Crear Tarea", JOptionPane.ERROR_MESSAGE);
                    }else{
                        if(exRef.Modificar(t.Id(), datos, (byte)2)){
                            pgbProgress.setValue(80);
                            if(fa != null){                                
                                fa.Refresh();
                                pgbProgress.setValue(90);
                                if(fa.ftA != null) fa.ftA.Refresh();
                                pgbProgress.setValue(0);
                            }else if(fn != null){
                                fn.Refresh();
                                pgbProgress.setValue(90);
                                if(fn.ftN != null) fn.ftN.Refresh();
                                pgbProgress.setValue(0);
                            }
                            JOptionPane.showMessageDialog(null, "La tarea se ha modificado satisfactoriamente.", "Modificar Tarea", JOptionPane.PLAIN_MESSAGE);
                            Salir();
                        }else{
                            JOptionPane.showMessageDialog(null, "Ha ocurrido un error al tratar de modificar la tarea.", "Modificar Tarea", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    pgbProgress.setValue(0);
               }else{
                   JOptionPane.showMessageDialog(null, "Se deben de llenar todos los campos.", "Crear Tarea", JOptionPane.ERROR_MESSAGE);
               }
            }catch(Exception e){}
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

        jLabel1 = new javax.swing.JLabel();
        txtnomT = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtfi = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtff = new javax.swing.JTextField();
        btnSalir = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        cboProy = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        cboPrioridad = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstUsersTask = new javax.swing.JList();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstUsersDB = new javax.swing.JList();
        btnremoveAll = new javax.swing.JButton();
        btnremoveSelected = new javax.swing.JButton();
        btnaddSelected = new javax.swing.JButton();
        btnaddAll = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtDesc = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        cboProgress = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        pgbProgress = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Captura de Datos de Tarea");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText("Nombre de la Tarea");

        jLabel5.setText("Proyecto");

        jLabel7.setText("Descripción");

        jLabel3.setText("Fecha de Inicio");

        jLabel4.setText("Fecha de Termino");

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        jLabel8.setText("Prioridad");

        cboPrioridad.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Baja", "Media", "Alta" }));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Agrega los usuarios que serán asignados a la tarea", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14))); // NOI18N

        jLabel2.setText("Usuarios asignados en la tarea");

        jLabel9.setText("Usuarios de la Base de Datos");

        jScrollPane2.setViewportView(lstUsersTask);

        lstUsersDB.setOpaque(false);
        jScrollPane1.setViewportView(lstUsersDB);

        btnremoveAll.setText(">>");
        btnremoveAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnremoveAllActionPerformed(evt);
            }
        });

        btnremoveSelected.setText(">");
        btnremoveSelected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnremoveSelectedActionPerformed(evt);
            }
        });

        btnaddSelected.setText("<");
        btnaddSelected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaddSelectedActionPerformed(evt);
            }
        });

        btnaddAll.setText("<<");
        btnaddAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaddAllActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnaddAll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnremoveAll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnaddSelected, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnremoveSelected, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnaddAll)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnaddSelected)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnremoveSelected)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnremoveAll)
                        .addGap(43, 43, 43))))
        );

        txtDesc.setColumns(20);
        txtDesc.setRows(5);
        jScrollPane3.setViewportView(txtDesc);

        jLabel6.setText("Estatus de la tarea");

        cboProgress.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Realizando", "Detenida", "Terminada" }));

        jLabel10.setFont(new java.awt.Font("Tahoma", 3, 10));
        jLabel10.setText("Formato: YYYY-MM-DD (2000-12-31)");

        jLabel11.setFont(new java.awt.Font("Tahoma", 3, 10));
        jLabel11.setText("Formato: YYYY-MM-DD (2000-12-31)");

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel12.setText("Guardando...");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pgbProgress, javax.swing.GroupLayout.DEFAULT_SIZE, 668, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel12)
            .addComponent(pgbProgress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboProgress, 0, 221, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE))
                            .addComponent(txtff, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel1)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtfi, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                            .addComponent(jLabel10)
                            .addComponent(cboPrioridad, javax.swing.GroupLayout.Alignment.TRAILING, 0, 224, Short.MAX_VALUE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                            .addComponent(cboProy, 0, 224, Short.MAX_VALUE)
                            .addComponent(txtnomT, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnGuardar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 255, Short.MAX_VALUE)
                        .addComponent(btnSalir))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtnomT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(cboProy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboPrioridad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtfi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnSalir)
                    .addComponent(jLabel6)
                    .addComponent(cboProgress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        h = new Hilo();
        h.start();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnremoveSelectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnremoveSelectedActionPerformed
        DefaultListModel modelo = (DefaultListModel)lstUsersTask.getModel();
        if(lstUsersTask.getSelectedIndices().length > 0){            
            int j = lstUsersTask.getSelectedIndices().length;            
            int[] indices;            
            for(int i = (j - 1); i >= 0; i--){                
                indices = lstUsersTask.getSelectedIndices();                
                modelo.remove(indices[i]);                
            }
        }
        lstUsersTask.setModel(modelo);
    }//GEN-LAST:event_btnremoveSelectedActionPerformed

    private boolean isInsert(javax.swing.JList lista, String value){
        for(int i = 0; i < lista.getModel().getSize(); i++)
            if(lista.getModel().getElementAt(i).toString().equals(value)) return true;        
        return false;
    }
    
    private void btnaddSelectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaddSelectedActionPerformed
        DefaultListModel modelo;
        try{
            modelo = (DefaultListModel) lstUsersTask.getModel();
        }catch(Exception e){
            modelo = new DefaultListModel();
        }

        if(lstUsersDB.getSelectedIndices().length > 0){
            int[] indices = lstUsersDB.getSelectedIndices();
            for(int i = 0; i < indices.length; i++)
                if(!isInsert(lstUsersTask, lstUsersDB.getModel().getElementAt(indices[i]).toString()))
                    modelo.addElement((Data)lstUsersDB.getModel().getElementAt(indices[i]));
        }
        lstUsersTask.setModel(modelo);
    }//GEN-LAST:event_btnaddSelectedActionPerformed

    private void btnaddAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaddAllActionPerformed
        DefaultListModel modelo = new DefaultListModel();
        for(int i = 0; i < lstUsersDB.getModel().getSize(); i++){
            modelo.addElement((Data) lstUsersDB.getModel().getElementAt(i));
        }
        lstUsersTask.setModel(modelo);
    }//GEN-LAST:event_btnaddAllActionPerformed

    private void btnremoveAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnremoveAllActionPerformed
        DefaultListModel modelo = new DefaultListModel();        
        lstUsersTask.setModel(modelo);
    }//GEN-LAST:event_btnremoveAllActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Salir();
    }//GEN-LAST:event_formWindowClosing

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        Salir();
    }//GEN-LAST:event_btnSalirActionPerformed

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
            java.util.logging.Logger.getLogger(NewT.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewT.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewT.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewT.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new NewT().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton btnaddAll;
    private javax.swing.JButton btnaddSelected;
    private javax.swing.JButton btnremoveAll;
    private javax.swing.JButton btnremoveSelected;
    private javax.swing.JComboBox cboPrioridad;
    private javax.swing.JComboBox cboProgress;
    private javax.swing.JComboBox cboProy;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList lstUsersDB;
    private javax.swing.JList lstUsersTask;
    private javax.swing.JProgressBar pgbProgress;
    private javax.swing.JTextArea txtDesc;
    private javax.swing.JTextField txtff;
    private javax.swing.JTextField txtfi;
    private javax.swing.JTextField txtnomT;
    // End of variables declaration//GEN-END:variables
}
