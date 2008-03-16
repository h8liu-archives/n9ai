package tuai.sample;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import tuai.Box;
import tuai.Game;

public class GameSample implements Game {

	/**
	 * Construct a sample game.
	 */
	public GameSample() {
		try {
			_log = new PipedOutputStream();
			_input = new PipedInputStream(_log);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Box generateInit(String startingMode) {
		return null;
	}

	@Override
	public InputStream getLog() {
		return _input;
	}

	@Override
	public Box getShare() {
		return null;
	}

	@Override
	public Box getSpecial(int playerId) {
		return new Box(new String("hi").getBytes());
	}

	@Override
	public int getTimeLimit(int playerId) {
		return 100;
	}

	@Override
	public int getWinner() {
		return -1;
	}

	@Override
	public boolean progress(int round) {
		if (round >= 10)
			return false;
		try {
			if (_lastReaction != null) {
				_log.write((new Integer(round).toString() + ":").getBytes());
				_log.write(_lastReaction.getContent());
				_log.write(new String("\n").getBytes());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean setInit(Box box) {
		return true;
	}

	@Override
	public void setReaction(int playerId, Box box, int timeUsed) {
		_lastReaction = box;
	}

	private PipedOutputStream _log;
	private PipedInputStream _input;

	private Box _lastReaction;
}
