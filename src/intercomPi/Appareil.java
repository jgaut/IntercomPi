package intercomPi;

public class Appareil {

	private String compte;

	private String server;

	private int port;

	private int portSsh;

	public int getPortSsh() {
		return portSsh;
	}

	public void setPortSsh(int portSsh) {
		this.portSsh = portSsh;
	}

	public Appareil(String compte, String server, int port, int portSsh) {
		this.compte = compte;
		this.server = server;
		this.port = port;
		this.portSsh = portSsh;
	}

	public String getCompte() {
		return compte;
	}

	public void setCompte(String compte) {
		this.compte = compte;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}