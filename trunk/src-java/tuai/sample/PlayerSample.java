package tuai.sample;

import java.io.InputStream;

import tuai.Box;
import tuai.Player;

public class PlayerSample implements Player {
	public PlayerSample(String name){
		_name = name;
	}
	@Override
	public boolean launch() {
		return true;
	}

	@Override
	public void terminate() {
	}

	@Override
	public void setRound(int round) {
	}

	@Override
	public void setShare(Box box) {

	}

	@Override
	public void setSpecial(Box box) {

	}

	@Override
	public Box getReaction(int timeLimit) {
		return new Box(new String("hello").getBytes());
	}

	@Override
	public int getLastTimeUsed() {
		return 0;
	}
	
	@Override
	public InputStream getLog() {
		return null;
	}
	
	@Override
	public String getName() {
		return _name;
	}
	
	String _name;
}
