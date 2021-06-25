import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ChoiceHandler implements ActionListener {
    

    public  void actionPerformed(ActionEvent event){
        
        int state = 0; 
        String choice = event.getActionCommand();

        if(choice.equals("c1")){
            state = 1;
            MainMenu a =new MainMenu(state);

        }
        else if(choice == "c2"){
            state = 2;
            MainMenu b =new MainMenu(state);
        }
    }

}