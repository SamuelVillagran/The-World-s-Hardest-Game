package domain;

public class GreenCoin extends SkinCoin {

	public GreenCoin(Coin coin) {
		super(coin);
	}

	@Override
	public void onContactWithEnemy(Enemy enemy, Level level) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onContactWithPlayer(Player player, Level level) {
		if (player.getPlayerType() != PlayerType.GREEN) {
			player.setState(new Green(player));
		}
	}

	@Override
	public String getNameSkin() {
		return "green";
	}

}
