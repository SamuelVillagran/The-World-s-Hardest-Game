package domain;

public final class CoinFactory {

	private CoinFactory() {
	}
	
	public static SkinCoin createSkin(String type, Coin coin) {
		return switch (type) {
			case ("red") -> new RedCoin(coin);
			case ("blue") -> new BlueCoin(coin);
			case ("green") -> new GreenCoin(coin);
			default -> new RedCoin(coin);
		};
	}
}
