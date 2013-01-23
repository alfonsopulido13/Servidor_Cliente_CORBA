package BaseDatos;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Isay
 */
public class Usuarios extends BDMySQL{
    public Usuarios(){ super(); }
    public Usuarios(String db, String ip_loc, String user, String pwd){ super(db, ip_loc, user, pwd); }
    
    /**
     * Función que a partir de una cadena, la regresa encriptada.
     * @param s Cadena a ser encriptada.
     * @return Cadena encriptada.
     */
    private String md5(String s) {
        String r = null;
        try {
            if (s != null) {
                MessageDigest algorithm =MessageDigest.getInstance("MD5");
                algorithm.reset();
                algorithm.update(s.getBytes());
                byte bytes[] = algorithm.digest();
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < bytes.length; i++) {
                    String hex = Integer.toHexString(0xff & bytes[i]);
                    if (hex.length() == 1)
                    sb.append('0');
                    sb.append(hex);
                }
                r = sb.toString();
            }
        } catch (NoSuchAlgorithmException e) {
        }
        return r;
    }
    
    private String find(int id, String pwd){
        String dato;
        String sSQL = "SELECT * FROM pms_users WHERE Password = '" + md5(pwd) + "' AND ID_User = " + id +
                " AND State = 1";
        try{            
            Statement st = getCon().createStatement();                        
            ResultSet rs = st.executeQuery(sSQL);
            
            rs.first();
            dato = rs.getString("ID_User");
        }catch(Exception e){
            dato = "";            
        }        
        CerrarConexion();        
        return dato;
    }
    
    private String findUser(String user, byte estado){
        String dato;
        String sSQL = "SELECT Username FROM pms_users WHERE Username = '" + user + "' AND State = " + estado;
        
        try{            
            Statement st = getCon().createStatement();                        
            ResultSet rs = st.executeQuery(sSQL);
            
            rs.first();
            dato = rs.getString("Username");
        }catch(Exception e){
            dato = "";            
        }        
        CerrarConexion();        
        return dato;
    }
    
    public String[] login(String user, String pwd){
        String[] registro = null;
        pwd = md5(pwd);        
        String sSQL = "SELECT ID_User, Username, Password, Privilege, Name FROM pms_users WHERE Username = '" 
                + user + "' AND Password = '" + pwd + "' AND State = 1";
        try{
            Statement st = getCon().createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            
            rs.first();
            registro = new String[5];
            registro[0] = rs.getString("ID_User");            
            registro[1] = rs.getString("Username");
            registro[2] = rs.getString("Password");
            registro[3] = rs.getString("Privilege");
            registro[4] = rs.getString("Name");            
        }catch(Exception e){}
        CerrarConexion();
        return registro;
    }
    
    public boolean save(String[] datos){
        if(findUser(datos[0], (byte)1).equals(datos[0])){
            CerrarConexion();
            return false;
        }else AbrirConexion();
                    
        String sSQL = "INSERT INTO pms_users(Username, Password, Privilege, Name, Email, Gender, Birthday, "
                + "State) VALUES(?, ?, ?, ?, ?, ?, ?, 1)";
                        
        try {            
            PreparedStatement ps = getCon().prepareStatement(sSQL);
            ps.setString(1, datos[0]);            
            ps.setString(2, md5(datos[1]));
            ps.setString(3, datos[2]);
            ps.setString(4, datos[3]);
            ps.setString(5, datos[4]);
            ps.setString(6, datos[5]);            
            ps.setString(7, datos[6]);
            ps.execute();
            CerrarConexion();
            return true;            
        } catch (Exception ex) {            
            CerrarConexion();
            return false;
        }
    }
    
    public String[] getUserByID(int id, byte estado){
        String[] registro = new String [8];
        String sSQL = "SELECT * FROM pms_users WHERE ID_User = " + id + " AND State = " + estado;
        try{
            PreparedStatement ps = getCon().prepareStatement(sSQL);
            ResultSet rs = ps.executeQuery();
            
            rs.first();
            registro[0] = rs.getString("ID_User");
            registro[1] = rs.getString("Username");
            registro[2] = rs.getString("Password");
            registro[3] = rs.getString("Privilege");
            registro[4] = rs.getString("Name");                
            registro[5] = rs.getString("Email");
            registro[6] = rs.getString("Gender");
            registro[7] = rs.getString("Birthday");            
        }catch(Exception e){}
        CerrarConexion();
        return registro;
    }
    
    public String[][] getUserByIDTask(int id){
        String[][] datos = null;
        String sSQL = "SELECT pms_users.`ID_User`, pms_users.`Name` FROM pms_userstasks "
                +"INNER JOIN pms_users ON pms_userstasks.`ID_User` = pms_users.`ID_User`"
                +"INNER JOIN pms_tasks ON pms_userstasks.`ID_Task` = pms_tasks.`ID_Task`"
                +"WHERE pms_tasks.`ID_Task` = " + id;
        try{
            PreparedStatement ps = getCon().prepareStatement(sSQL);
            ResultSet rs = ps.executeQuery();
            
            int contador = 0;
            while(rs.next()){ contador++; }
            datos = new String[2][contador];
            
            contador = 0;
            rs.first();
            do{
                datos[0][contador] = rs.getString("ID_User");
                datos[1][contador] = rs.getString("Name");
                contador++;
            }while(rs.next());                        
        }catch(Exception e){}
        CerrarConexion();
        return datos;
    }    

    public String[][] get(String busqueda, byte estado){
        String[][] registros = null;
        String sSQL = "SELECT * FROM pms_users WHERE State = " + estado + " AND CONCAT(Name, ' ', Privilege) LIKE '%" 
                + busqueda + "%'";
        try{            
            Statement st = getCon().createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            
            int contador = 0;
            while(rs.next()){ contador++; }
            registros = new String[8][contador];
            
            rs.first();            
            contador = 0;            
            do{
                registros[0][contador] = rs.getString("ID_User");
                registros[1][contador] = rs.getString("Username");
                registros[2][contador] = rs.getString("Password");
                registros[3][contador] = rs.getString("Privilege");
                registros[4][contador] = rs.getString("Name");                
                registros[5][contador] = rs.getString("Email");
                registros[6][contador] = rs.getString("Gender");
                registros[7][contador] = rs.getString("Birthday");
                contador++;
            }while(rs.next());
        }catch(Exception e){
        }
        
        CerrarConexion();
        return registros;
    }
            
    public boolean password(int id, String oldPwd, String newPwd){
        if(find(id, oldPwd).equals("")){
            CerrarConexion();
            return false;
        }else AbrirConexion();

        String sSQL = "UPDATE pms_users SET Password = ? WHERE ID_USer = " + id;
        try{
            PreparedStatement ps = getCon().prepareStatement(sSQL);
            ps.setString(1, md5(newPwd));
            ps.execute();
            CerrarConexion();
            return true;
        }catch (Exception ex) {
            CerrarConexion();
            return false;
        }
    }
    
    public boolean edit(int id, String[] datos){        
        String sSQL = "UPDATE pms_users SET Username = ?, Privilege = ?, Name = ?, "
                + "Email = ?, Gender = ?, Birthday = ? WHERE ID_User = " + id;

        try {            
            PreparedStatement ps = getCon().prepareStatement(sSQL);
            ps.setString(1, datos[0]);
            ps.setString(2, datos[1]);
            ps.setString(3, datos[2]);
            ps.setString(4, datos[3]);
            ps.setString(5, datos[4]);
            ps.setString(6, datos[5]);                            
            ps.execute();
            CerrarConexion();
            return true;
        } catch (Exception ex) {
            CerrarConexion();
            return false;
        }                    
    }
        
    public boolean restore(int id){
        String sSQL = "UPDATE pms_users SET State = 1 WHERE ID_User = " + id;
        try{
            PreparedStatement ps = getCon().prepareStatement(sSQL);
            ps.execute();
            CerrarConexion();
            return true;
        }catch(Exception e){
            CerrarConexion();
            return false;
        }
    }
    
    public boolean trash(int id){
        String sSQL = "UPDATE pms_users SET State = 0 WHERE ID_User = " + id;
        try{
            PreparedStatement ps = getCon().prepareStatement(sSQL);
            ps.execute();
            
            //Buscar ID del Proyecto
            sSQL = "SELECT ID_Project FROM pms_projects WHERE ID_User = " + id;
            ps = getCon().prepareStatement(sSQL);
            ResultSet rs = ps.executeQuery(sSQL);
            
            while(rs.next()){
                int id_p = rs.getInt("ID_Project");
                new Proyectos().trash(id_p);
            }
            
            CerrarConexion();
            return true;
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e, "error en usuarios", JOptionPane.ERROR_MESSAGE);
            CerrarConexion();
            return false;
        }
    }
    
    public boolean delete(int id){
        String sSQL = "DELETE FROM pms_users WHERE ID_User = " + id;
        try{
            PreparedStatement ps = getCon().prepareStatement(sSQL);
            ps.execute();
            
            //Buscar ID del Proyecto
            sSQL = "SELECT ID_Project FROM pms_projects WHERE ID_User = " + id;
            ps = getCon().prepareStatement(sSQL);
            ResultSet rs = ps.executeQuery(sSQL);
            
            while(rs.next()){
                int id_p = rs.getInt("ID_Project");
                new Proyectos().delete(id_p);
            }
                        
            CerrarConexion();
            return true;
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e, "error en usuarios", JOptionPane.ERROR_MESSAGE);
            CerrarConexion();
            return false;
        }
    }
}
