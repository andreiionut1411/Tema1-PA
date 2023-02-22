import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Prinel {

	/*
	 * Calculam numarul minim de operatii de care avem nevoie pentru a ajunge la target.
	 * Vom calcula tot folsoind programare dinamica. Vom avea un vector in care
	 * elementul i reprezinta numarul minim de operatii necesare pentru a se ajunge la i.
	 */
	int[] numberOfOperations(int target_maxim) {
		int minim;
		int[] numberOfOp = new int[target_maxim + 1];
		numberOfOp[0] = -1;

		for (int i = 2; i <= target_maxim; i++) {
			minim = Integer.MAX_VALUE;
			double sqrt = Math.sqrt(i);

			/*
			 * Ne plimbam prin divizorii lui pentru ca daca avem un numar k la care adunam
			 * unul din divizorii sai, atunci vom obtine un numar care este divizibil cu
			 * divizorul respectiv.
			 */
			for (int j = 1; j <= sqrt; j++) {
				if (i % j == 0) {
					if (minim > 1 + numberOfOp[i - j]) {
						minim = 1 + numberOfOp[i - j];
					}

					/*
					 * Evitam cazul j == 1 pentru ca daca am tine cont de el, i / j == i, deci
					 * ar insemna ca la numarul precedent adaugam 0 pentru a ajunge la i. Dar
					 * cum de fiecare data adaugam cel putin 1, acest caz nu are sens.
					 */
					if (minim > 1 + numberOfOp[i - i / j] && j != 1) {
						minim = 1 + numberOfOp[i - i / j];
					}
				}
			}

			numberOfOp[i] = minim;
		}

		return numberOfOp;
	}

	/*
	 * Odata ce stim numarul de operatii necesare unei transformari, problema
	 * devine acum problema rucsacului discret. O vom rezolva folosind programare
	 * dinamica.
	 */
	int solvePrinel(int N, int k, int[] target, int[] p, int target_maxim) {
		int[] weights = new int[N];
		int[] nrOfOperations = new int[target_maxim + 1];
		int[] prev_row = new int[k + 1];
		int[] cur_row = new int[k + 1];
		// Vom rezolva acum problema rucsacului discret folosind doar 2 vectori nu matrice.

		nrOfOperations = numberOfOperations(target_maxim);

		// Pentru fiecare target calculam numarul de operatii necesare.
		for (int i = 0; i < N; i++) {
			weights[i] = nrOfOperations[target[i]];
		}

		// Luam in calcul si cazul in care k = 0 astfel.
		for (int i = 0; i <= k; i++) {
			if (weights[0] <= i) {
				prev_row[i] = p[0];
			}
		}

		for (int i = 1; i < N; i++) {
			for (int j = 0; j <= k; j++) {
				if (weights[i] <= j) {
					cur_row[j] = Math.max(prev_row[j],
							prev_row[j - weights[i]] + p[i]);
				} else {
					cur_row[j] = prev_row[j];
				}
			}

			for (int j = 0; j <= k; j++) {
				prev_row[j] = cur_row[j];
			}
		}

		return cur_row[k];
	}

	public static void main(String[] args) {
		int N, k;
		Prinel prinel = new Prinel();

		try (BufferedReader br = new BufferedReader(new FileReader("prinel.in"));
				FileWriter fileWriter = new FileWriter("prinel.out")) {
			String line = br.readLine();
			String[] numbers = line.split("\\s");

			N = Integer.parseInt(numbers[0]);
			k = Integer.parseInt(numbers[1]);
			int[] target = new int[N];
			int[] p = new int[N];
			int target_maxim = 0;
			line = br.readLine();
			numbers = line.split("\\s");

			for (int i = 0; i < N; i++) {
				target[i] = Integer.parseInt(numbers[i]);
				if (target[i] > target_maxim) {
					target_maxim = target[i];
				}
			}

			line = br.readLine();
			numbers = line.split("\\s");

			for (int i = 0; i < N; i++) {
				p[i] = Integer.parseInt(numbers[i]);
			}

			fileWriter.write(prinel.solvePrinel(N, k, target, p, target_maxim) + "\n");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Nu exista fisierul cautat.");
		}
	}
}
