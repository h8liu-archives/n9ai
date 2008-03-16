import java.io.BufferedInputStream;

import tuai.Court;
import tuai.Game;
import tuai.sample.GameSample;
import tuai.sample.PlayerSample;

public class test {
	static public void main(String args[]) {
		Game game = new GameSample();
		Court court = new Court(game);
		court.prepare();
		for (int i = 0; i < court.getPlayerCount(); i++) {
			court.setPlayer(i, new PlayerSample("Player "+i));
		}
		BufferedInputStream input = new BufferedInputStream(game.getLog());
		court.run();
		try {
			int len;
			byte[] buf = new byte[1024];
			while (input.available() > 0) {
				len = input.read(buf);
				System.out.write(buf, 0, len );
			}
			System.out.println("(end)");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
