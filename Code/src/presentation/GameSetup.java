package presentation;

import javax.swing.JOptionPane;

import domain.GameMode;
import domain.HardestGameException;
import domain.ModePlayer;
import domain.PlayerType;
import domain.PlayerVsPlayerMode;
import domain.PlayerVsMachineMode;

/**
 * This class store information about the different configuration panels
 * the panels are PlayerConfig, and MenuPanel.
 * @return GameSetup instance.
 */

public class GameSetup {
	private ModeType modeType;
    private PlayerType typeP1;
    private String nameP1;
    private PlayerType typeP2;
    private String nameP2;
    
    @SuppressWarnings("PMD.ConstructorCallsOverridableMethod")
    public GameSetup setMode(ModeType mode){
    	this.modeType = mode;
    	return this;
    }

    public GameSetup setPlayer(PlayerType type, String name) {
    	this.typeP1 = type;
    	this.nameP1 = name;
    	return this;
    }

    public GameSetup setPlayer2(PlayerType type, String name) {
        this.typeP2 = type;
        this.nameP2 = name;
        return this;
    }

    /**
     * Build the game mode with the specific type players and their names.
     * @return GameMode instance.
     */
    public GameMode build() throws HardestGameException{
        switch (modeType) {
            case SINGLE_PLAYER:
                return new ModePlayer(typeP1, nameP1);
            case PVP:
                return new PlayerVsPlayerMode(typeP1, nameP1, typeP2, nameP2);
            case PVM:
				return new PlayerVsMachineMode(typeP1, nameP1, typeP2);
            default:
                throw new HardestGameException("Modo no configurado");
        }
    }

	public ModeType getMode() {
		return modeType;
	}
    
}
    
