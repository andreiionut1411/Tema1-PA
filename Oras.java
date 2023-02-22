public class Oras {
	private int ruta_st, ruta_dr, activare;

	public Oras(int ruta_st, int ruta_dr) {
		this.ruta_dr = ruta_dr;
		this.ruta_st = ruta_st;
		this.activare = ruta_dr + ruta_st;
	}

	public int getRuta_dr() {
		return ruta_dr;
	}

	public int getRuta_st() {
		return ruta_st;
	}

	public int getActivare() {
		return activare;
	}
}
