/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
import Interface.Principal;
import Principal.Admin;
import Principal.User;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 *
 * @author ANGEL
 */
public class PrincipalCtr implements ActionListener{
    String[] args = {}; 
    
    Principal principal= new Principal();
    
    public PrincipalCtr (){
        principal.user.setActionCommand("User");
        principal.user.addActionListener(this);
        
        principal.admin.setActionCommand("Admin");
        principal.admin.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("User")){
            User.main(args);
        }else{
            Admin.main(args);
        }
        principal.dispose();
    }
    
}
