package Extras;
import ExApp.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;
/**
 *
 * @author Isay
 */
public abstract class CORBA_Conn extends javax.swing.JFrame{
    public ORB orb;
    public org.omg.CORBA.Object objRef;
    public NamingContext ncRef;
    public NameComponent nc;
    public Ex exRef;
    
    public CORBA_Conn(){
        String[] args = {"-ORBInitialPort","1050","-ORBInitialHost","192.168.1.68"};
        try{			            
            orb = ORB.init(args, null);
            
            objRef = orb.resolve_initial_references("NameService");
            ncRef = NamingContextHelper.narrow(objRef);
                                  
            nc = new NameComponent("Ex", "");   
            NameComponent path[] = {nc};
            exRef = ExHelper.narrow(ncRef.resolve(path));
            
        } catch(Exception e) {            
            System.out.println("ERROR 1: " + e);            
        }
    }
    
    protected void centrarVentana() {
            // Se obtienen las dimensiones en pixels de la pantalla.
            Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
            // Se obtienen las dimensiones en pixels de la ventana.
            Dimension ventana = getSize();
            // Una cuenta para situar la ventana en el centro de la pantalla.
            setLocation((pantalla.width - ventana.width) / 2,
                            (pantalla.height - ventana.height) / 2);
    }
}
