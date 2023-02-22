import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Crypto {
	static final int MOD = 1_000_000_007;
	int factorWildCard = 1;
	int nrLitereWildCard = 0; // Numarul de litere cu care poate fi inlocuit ?

	// Functia primeste subsirul si intoarce un ArrayList cu toate literele diferite din acesta.
	ArrayList<Character> litereDiferiteSubsir(String subsir) {
		ArrayList<Character> litereSubsir = new ArrayList<>();

		for (int i = 0; i < subsir.length(); i++) {
			char litera = subsir.charAt(i);
			boolean exists = false;
			for (int j = 0; j < litereSubsir.size() && exists == false; j++) {
				if (litereSubsir.get(j) == litera) {
					exists = true;
				}
			}

			if (exists == false) {
				litereSubsir.add(litera);
			}
		}

		return litereSubsir;
	}

	/*
	 * Daca litera curenta nu este ultima din subsir, inseamna ca subsirul curent apare la
	 * fel de multe ori ca inainte. Daca in schimb ultima litera este ultima litera a subsirului,
	 * atunci mai apar in plus atatea elemente cate subsiruri cu o litera in minus au aparut
	 * pana la pasul anterior. Daca subsirul curent este o litera, atunci trebuie sa luam in calcul
	 * si numarul de moduri in care se poate inlocui caracterul de wild card '?'.
	 */
	int calculateCellFromMatrix(char litera, String subsir, int[][] valuesMatrix, int i, int j) {
		int value = 0;
		if (litera != subsir.charAt(i)) {
			value = valuesMatrix[i][j - 1];
		} else {
			if (i == 0) {
				value = ((valuesMatrix[i][j - 1] % MOD)
						+ (factorWildCard % MOD)) % MOD;
			} else {
				value = ((valuesMatrix[i][j - 1] % MOD)
						+ (valuesMatrix[i - 1][j - 1] % MOD)) % MOD;
			}
		}

		return value;
	}

	public int solveCrypto(int N, int L, String cheie, String subsir) {
		ArrayList<Character> litereSubsir = litereDiferiteSubsir(subsir);
		int[][] valuesMatrix = new int[L][N];
		/*
		 * Elementul (i, j) din matrice va reprezenta de cate ori a aparut subsirul format
		 * din primele (i + 1) litere din subsir in primele (j + 1) litere ale cheii. Pentru
		 * ambele, am zis primele (i + 1) litere, intrucat indexarea incepe de la 0, nu de la
		 * 1 asa cum este natural.
		 */

		nrLitereWildCard = litereSubsir.size();

		/*
		 * Verificam daca prima litera din cheie este sau nu prima litera din subsir.
		 * Acesta este pasul de initializare pentru rezolvarea noastra cu programare dinamica.
		 */
		if (cheie.charAt(0) == subsir.charAt(0)) {
			valuesMatrix[0][0] = 1;
		} else if (cheie.charAt(0) == '?') {
			for (Character litera : litereSubsir) {
				if (subsir.charAt(0) == litera) {
					valuesMatrix[0][0] = 1;
					break;
				}
			}

			factorWildCard = (int) ((1L * (factorWildCard % MOD)
					* (nrLitereWildCard % MOD)) % MOD);
		}

		for (int j = 1; j < N; j++) {
			for (int i = 0; i < L; i++) {
				if (cheie.charAt(j) != '?') {
					valuesMatrix[i][j] =
							calculateCellFromMatrix(cheie.charAt(j), subsir, valuesMatrix, i, j);
				} else {
					// Pentru fiecare litera cu care se poate inlocui '?', calculam numarul de
					// subsiruri.
					for (int l = 0; l < nrLitereWildCard; l++) {
						valuesMatrix[i][j] = ((valuesMatrix[i][j] % MOD)
								+ (calculateCellFromMatrix(litereSubsir.get(l),
										subsir, valuesMatrix, i, j) % MOD)) % MOD;
					}
				}
			}

			/*
			 * Calculam numarul de aranjamente de litere cu care se pot inlocui toate literele
			 * de tipul '?' din subsirul curent. Avem grija sa nu faca overflow si sa
			 * intoarcem rezultatul modulo 10^9+7.
			 */
			if (cheie.charAt(j) == '?') {
				factorWildCard = (int) ((1L * (factorWildCard % MOD)
						* (nrLitereWildCard % MOD)) % MOD);
			}
		}

		// Ultimul element din matrice reprezinta de cate ori a aparut subsirul in cheie.
		return valuesMatrix[L - 1][N - 1];
	}

	public static void main(String[] args) {
		Crypto crypto = new Crypto();
		int N, L;
		String cheie, subsir;

		try (BufferedReader br = new BufferedReader(new FileReader("crypto.in"));
				FileWriter fileWriter = new FileWriter("crypto.out")) {
			String line = br.readLine();
			String[] numbers = line.split("\\s");

			N = Integer.parseInt(numbers[0]);
			L = Integer.parseInt(numbers[1]);
			cheie = br.readLine();
			subsir = br.readLine();

			fileWriter.write(crypto.solveCrypto(N, L, cheie, subsir) + "\n");

		} catch (IOException e) {
			System.out.println("Nu s-a putut deschide fisierul cautat.");
			e.printStackTrace();
		}
	}
}
