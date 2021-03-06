package Forms_Alfa;

import Extras.CORBA_Conn;
import Extras.Data;
import javax.swing.JOptionPane;

/**
 *
 * @author Isay
 */
public class NewP extends CORBA_Conn {
    private boolean guardar = true;
    private Data p;
    private FAdmin fa;
    private Hilo h;

    /** Creates new form NewP */
    public NewP() {
        initComponents();
        pgbProgress.setMaximum(100);
        centrarVentana();
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        Datos();        
    }
    
    public void setFAdmin(FAdmin fa){this.fa = fa;}    
    
    void Datos(){
        cboLider.removeAllItems();
        String[][] registros = exRef.Obtener("Administrador", (byte)1, (byte)3);
        int i = 0;
        while(true){
            try{                
                cboLider.addItem(new Data(Integer.parseInt(registros[0][i]), registros[4][i]));
                i++;
            }catch(Exception e){ break; }
        }        
    }
    
    void Limpiar(){
        txtnomP.setText("");        
        cboLider.setSelectedIndex(0);
        txtDesc.setText("");
        txtfi.setText("");
        txtff.setText("");
        txtnomP.requestFocus();
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
    
    public void setDatos(Data u, String lider, String desc, String fi, String ff){
        p = u;
        txtnomP.setText(p.Name());                
        
        cboLider.setSelectedIndex(getIndex(cboLider, lider));
        txtDesc.setText(desc);
        txtfi.setText(fi);
        txtff.setText(ff);
        guardar = false;
    }
    
    void Salir(){        
        fa.np = null;                
        this.dispose();
        if (h != null)h.stop();
    }
    
    class Hilo extends Thread{
        public void run(){            
            try{
                if(!txtnomP.getText().equals("") && !txtDesc.getText().equals("") && !txtfi.getText().equals("") && 
                        !txtff.getText().equals("")){
                    Data u = (Data) cboLider.getItemAt(cboLider.getSelectedIndex());
                    pgbProgress.setValue(10);

                    String[] datos = {String.valueOf(u.Id()), txtnomP.getText(), txtDesc.getText(), txtfi.getText(),
                        txtff.getText()};

                    if(guardar){
                        if(exRef.Crear(datos, (byte)1)){
                            pgbProgress.setValue(80);
                            fa.Refresh();
                            pgbProgress.setValue(90);
                            if(fa.ftA != null) fa.ftA.Refresh();
                            pgbProgress.setValue(0);
                            if(JOptionPane.showOptionDialog(null, "El proyecto se ha creado satisfactoriamente. ¿Deseas crear otro proyecto?", "Crear Proyecto", 
                                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, rootPane) == 0){
                                Limpiar();
                            }else Salir();
                        }else JOptionPane.showMessageDialog(null, "Ha ocurrido un error al tratar de crear el proyecto.", "Crear Proyecto", JOptionPane.ERROR_MESSAGE);
                    }else{
                        if(exRef.Modificar(p.Id(), datos, (byte)1)){
                            pgbProgress.setValue(80);
                            fa.Refresh();
                            pgbProgress.setValue(90);
                            if(fa.ftA != null) fa.ftA.Refresh();
                            pgbProgress.setValue(0);
                            JOptionPane.showMessageDialog(null, "El proyecto se ha modificado satisfactoriamente.", "Modificar Proyecto", JOptionPane.PLAIN_MESSAGE);                            
                            Salir();                            
                        }else JOptionPane.showMessageDialog(null, "Ha ocurrido un error al tratar de modificar el proyecto.", "Modificar Proyecto", JOptionPane.ERROR_MESSAGE);
                    }
                    pgbProgress.setValue(0);
                }else{
                    JOptionPane.showMessageDialog(null, "Se deben de llenar todos los campos.", "Crear Proyecto", JOptionPane.ERROR_MESSAGE);
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
        txtnomP = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        cboLider = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtfi = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        txtff = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDesc = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btnSalir = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        pgbProgress = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Captura de Datos de Proyecto");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText("Nombre del Proyecto");

        jLabel2.setText("Lider del Proyecto");

        jLabel3.setText("Descripción");

        jLabel4.setText("Fecha de Inicio");

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        txtff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtffActionPerformed(evt);
            }
        });

        jLabel5.setText("Fecha de Termino");

        txtDesc.setColumns(20);
        txtDesc.setRows(5);
        jScrollPane1.setViewportView(txtDesc);

        jLabel6.setFont(new java.awt.Font("Tahoma", 3, 10));
        jLabel6.setText("Formato: YYYY-MM-DD (2000-12-31)");

        jLabel7.setFont(new java.awt.Font("Tahoma", 3, 10));
        jLabel7.setText("Formato: YYYY-MM-DD (2000-12-31)");

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel8.setText("Guardando...");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pgbProgress, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel8)
            .addComponent(pgbProgress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cboLider, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtnomP, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(7, 7, 7))
                            .addComponent(txtfi, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtff, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(btnGuardar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnSalir))
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING))
                        .addContainerGap())))
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtnomP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cboLider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtfi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnSalir))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        h = new Hilo();        
        h.start();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void txtffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtffActionPerformed
        btnGuardarActionPerformed(evt);
    }//GEN-LAST:event_txtffActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        Salir();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Salir();       
    }//GEN-LAST:event_formWindowClosing

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
            java.util.logging.Logger.getLogger(NewP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new NewP().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox cboLider;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JProgressBar pgbProgress;
    private javax.swing.JTextArea txtDesc;
    private javax.swing.JTextField txtff;
    private javax.swing.JTextField txtfi;
    private javax.swing.JTextField txtnomP;
    // End of variables declaration//GEN-END:variables
}
