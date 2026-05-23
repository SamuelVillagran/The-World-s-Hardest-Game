package domain;

public class BlueCoin extends SkinCoin  {

	public BlueCoin(Coin coin) {
		super(coin);
	}

	@Override
	public void onContactWithPlayer(Player player, Level level) {
		if (player.getPlayerType() != PlayerType.BLUE) {
			player.setState(new Blue(player));
		}
	}

	@Override
	public void onContactWithEnemy(Enemy enemy, Level level) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getNameSkin() {
		return "blue";
	}

}
