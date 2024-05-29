package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

public class Zoomer extends InteractiveComponent {
    //ATTRIBUTES
    private int zoomChoice;
    private InteractiveComponent interactiveComponent;





    //METHODS
    @Override
    public boolean handleInput(String input) {
        if(inputCounter == 0){
            
        }
        if(inputCounter == 1) {
            interactiveComponent.handleInput(input);
        }

        return false;
    }

    @Override
    public String getKeyword() {
        return "Zoom";
    }

    @Override
    public void print() {

    }
}
