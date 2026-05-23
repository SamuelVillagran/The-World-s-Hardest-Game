package domain;

public abstract class SkinCoin implements Interactable {

	protected Coin coin;
	
	public SkinCoin(Coin coin) {
		this.coin = coin;
	}

	public abstract String getNameSkin();
	
}
