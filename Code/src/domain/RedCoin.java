package domain;

public class RedCoin extends SkinCoin {

	public RedCoin(Coin coin) {
		super(coin);
	}

	@Override
	public void onContactWithEnemy(Enemy enemy, Level level) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onContactWithPlayer(Player player, Level level) {
		if (player.getPlayerType() != PlayerType.RED) {
			player.setState(new Red(player));
		}
	}

	@Override
	public String getNameSkin() {
		return "red";
	}

}
