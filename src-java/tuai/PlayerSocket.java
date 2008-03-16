package tuai;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;

class PlayerSocket implements Player {
	/**
	 * Generate a Player on socket. A holder will be run to hold the AI command.
	 * 
	 * @param AIcmd
	 *            AI command. This program should connect 'localhost:9999' to
	 *            communicate with the game.
	 * @throws IOException
	 *             When starting server socket failed.
	 */
	public PlayerSocket(String AIcmd) throws IOException {
		_cmd = AIcmd;
		if (_holderHost == null)
			_holderHost = new ServerSocket(9990);
		if (_AIHost == null) {
			_AIHost = new ServerSocket(9999);
			_AIHost.setSoTimeout(100);
		}
	}

	@Override
	public boolean launch() {
		try {
			_holderPr = Runtime.getRuntime().exec(
					new String[] { "holder", _cmd });

			_holder = _holderHost.accept();
			_AI = _AIHost.accept();

			_AIOutput = _AI.getOutputStream();
			_holderInput = _holder.getInputStream();
			_holderOutput = _holder.getOutputStream();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void setRound(int round) {
		_round = round;
	}

	@Override
	public void setShare(Box box) {
		_share = box;
	}

	@Override
	public void setSpecial(Box box) {
		_special = box;
	}

	@Override
	public void terminate() {
		_terminate();
	}

	@Override
	public Box getReaction(int timeLimit) {
		if (timeLimit <= 0)
			return null;
		Box ret = null;
		_lastTimeUsed = -1;

		_resume();
		try {
			long start = System.currentTimeMillis();
			long due = start + timeLimit;
			if (_share == null)
				_share = new Box(new byte[0]);
			if (_special == null)
				_special = new Box(new byte[0]);
			
			/*
			 * Format of buffer:
			 * 
			 * INT round
			 * INT share piece length
			 * BYTE[] share piece content
			 * INT special piece length
			 * BYTE[] special piece content
			 * INT time limit for this round		
			 */
			ByteBuffer toSend = ByteBuffer.allocate(4 + 4
					+ _share.getContent().length + 4
					+ _special.getContent().length + 4);
			toSend.putInt(_round);
			toSend.putInt(_share.getContent().length);
			toSend.put(_share.getContent());
			toSend.putInt(_special.getContent().length);
			toSend.put(_special.getContent());
			toSend.putInt(timeLimit);
			
			_AIOutput.write(toSend.array());

			try {
				/*
				 * Try to read length of the reaction.
				 */
				byte[] buffer = new byte[4];
				if (_fill(_AI, buffer, due) != 4)
					throw new IOException();
				ByteBuffer bufLen = ByteBuffer.wrap(buffer);
				int len = bufLen.getInt();
				
				/*
				 * Try to fill the reaction buffer.
				 */
				buffer = new byte[len];
				if (_fill(_AI, buffer, due) != len)
					throw new IOException();
				
				/*
				 * Return the reaction and save the last time used.
				 */
				ret = new Box(buffer);
				
				_lastTimeUsed = (int) (System.currentTimeMillis() - start);
				if (_lastTimeUsed > timeLimit)
					_lastTimeUsed = timeLimit;
			} catch (SocketTimeoutException e) {
			}
		} catch (IOException e) {
		}
		_suspend();
		return ret;
	}

	@Override
	public int getLastTimeUsed() {
		return _lastTimeUsed;
	}

	@Override
	public InputStream getLog() {
		return _holderPr.getInputStream();
	}

	@Override
	public String getName() {
		return _cmd;
	}

	private void _suspend() {
		try {
			_holderOutput.write(1);
			_holderInput.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void _resume() {
		try {
			_holderOutput.write(2);
			_holderInput.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void _terminate() {
		try {
			_holderOutput.write(0);
			_holderInput.read();
			_holderPr.waitFor();
		} catch (IOException e) {
			System.out.println(e);
		} catch (InterruptedException e) {
			System.out.println(e);
		}
	}

	private static int _fill(Socket sock, byte[] bytes, long due)
			throws IOException {
		if (sock == null)
			return 0;
		else if (bytes == null)
			return 0;
		InputStream input = sock.getInputStream();
		int read = 0;
		int len;
		while (read < bytes.length) {
			if (due != 0) {
				long left = due - System.currentTimeMillis();
				if (left <= 0)
					break;
				sock.setSoTimeout((int) (left));
			} else
				sock.setSoTimeout(0);

			len = input.read(bytes, read, bytes.length - read);

			if (len <= 0)
				break;
			read += len;
		}
		return read;
	}

	private int _lastTimeUsed = -1;
	private Box _share;
	private Box _special;
	private int _round;
	private Socket _holder;
	private Socket _AI;
	private String _cmd;
	private Process _holderPr;

	private InputStream _holderInput;
	private OutputStream _holderOutput;

	private OutputStream _AIOutput;

	private static ServerSocket _holderHost;
	private static ServerSocket _AIHost;
}
