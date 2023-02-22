import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Regele {

	/*
	 * Primim vectorul de coordonate si cream orasele, tinand cont ca orasele din capete
	 * au o singura ruta.
	 */
	Oras[] creareOrase(int[] coord, int N) {
		Oras[] orase = new Oras[N + 1];

		// Vrem ca indexarea sa inceapa de la 1 pentru simplitate, asa ca orase[0] e inutil.
		orase[0] = new Oras(0, 0);

		for (int i = 1; i <= N; i++) {
			int ruta_st = coord[i] - coord[i - 1];
			if (i == N) {
				orase[i] = new Oras(ruta_st, 0);
			} else {
				int ruta_dr = coord[i + 1] - coord[i];
				if (i == 1) {
					orase[i] = new Oras(0, ruta_dr);
				} else {
					orase[i] = new Oras(ruta_st, ruta_dr);
				}
			}
		}

		return orase;
	}

	/*
	 * Vom avea o matrice care pe linia i si coloana j tine elementul care ne indica
	 * faptul ca daca vrem sa activam i orase, dintre care primul este orasul j.
	 * Vrem sa calculam din start numerele maxime de negustori necesari pentru
	 * activarea oricarui numar de orase.
	 */
	int[] calculateBiggestActivation(Oras[] orase, int N, int[] coord) {
		// Pastram indexarea de la 1 pentru simplitate.
		int[] biggestActivation = new int[N + 1];
		int maxim = 0;
		int[] previousMax = new int[N + 1];
		int numberOfRowsNecessary = (N + 1) / 2;
		int[][] valuesMatrix = new int[numberOfRowsNecessary + 1][N + 1];
		/*
		 * Matricea de valori pe care o tinem minte are nevoie de (N + 1) / 2 valori
		 * si nu mai mult, intrucat daca se activeaza orasele din 2 in 2, atunci
		 * si orasele intermediare se vor activa si ele, deci practic, de la acest
		 * numar in colo, numarul maxim de negustori pentru a activa oricare orase
		 * va ramane constant.
		 */

		for (int i = N; i >= 1; i--) {
			if (maxim > orase[i].getActivare()) {
				valuesMatrix[1][i] = maxim;
			} else {
				maxim = orase[i].getActivare();
				valuesMatrix[1][i] = maxim;
			}

			previousMax[i] = valuesMatrix[1][i];
		}

		biggestActivation[1] = maxim;

		for (int i = 2; i <= numberOfRowsNecessary; i++) {
			int maximLinie = 0;
			for (int j = 1; j <= N - i; j++) {
				valuesMatrix[i][j] = orase[j].getActivare() + previousMax[j + 2];

				if (valuesMatrix[i][j] > maximLinie) {
					maximLinie = valuesMatrix[i][j];
				}
			}

			valuesMatrix[i][N - i + 1] = coord[N] - coord[N - i + 1]
					+ orase[N - i + 1].getRuta_st();

			if (valuesMatrix[i][N - i + 1] > maximLinie) {
				maximLinie = valuesMatrix[i][N - i + 1];
			}

			/*
			 * Daca ajungem la N - i + 1, inseamna ca trebuie activate toate orasele de
			 * dupa cel curent, inclusiv. Asa ca practic avem coord[N-i+1] - coord[N].
			 */
			previousMax[N - i + 1] = valuesMatrix[i][N - i + 1];

			for (int j = N - i; j >= 1; j--) {
				if (valuesMatrix[i][j] > previousMax[j + 1]) {
					previousMax[j] = valuesMatrix[i][j];
				} else {
					previousMax[j] = previousMax[j + 1];
				}
			}

			biggestActivation[i] = maximLinie;
		}

		for (int i = numberOfRowsNecessary + 1; i <= N; i++) {
			biggestActivation[i] = coord[N] - coord[1];
		}

		return biggestActivation;
	}

	/*
	 * Odata ce avem numarul de negustori necesari pentru a activa un numar anumit de
	 * orase, primim o intrebare si facem o cautare binara a numarului din intrebare
	 * in vectorul de activari. Nu este o cautare binara clasica, intrucat nu verificam
	 * daca elementul cautat este mijlocul, ci daca se afla in unul din cele 2 intervale
	 * alaturate sale, in [middle - 1, middle] sau in [middle, middle + 1].
	 */
	int answerQuestion(int question, int[] biggestActivation, int[] coord, int N) {
		int answer = 0;
		int first = 1, last = N, middle = (first + last) / 2;

		// Daca avem mai multi negustori decat avem nevoie, atunci toate orasele vor fi active.
		if (question >= coord[N] - coord[1]) {
			return N;
		}

		while (first < last) {
			if (question >= biggestActivation[middle]) {
				if (question < biggestActivation[middle + 1]) {
					return middle;
				} else {
					first = middle + 1;
					middle = (first + last) / 2;
				}
			} else {
				if (question >= biggestActivation[middle - 1]) {
					return middle - 1;
				} else {
					last = middle - 1;
					middle = (first + last) / 2;
				}
			}
		}

		return answer;
	}

	public static void main(String[] args) {
		Regele regele = new Regele();

		try (BufferedReader br = new BufferedReader(new FileReader("regele.in"));
				FileWriter fileWriter = new FileWriter("regele.out")) {
			int N;
			int question;
			String line = br.readLine();
			String[] tokens;
			N = Integer.parseInt(line);

			// Incepem indexarea de la 1.
			int[] coord = new int[N + 1];
			line = br.readLine();
			tokens = line.split("\\s");

			for (int i = 1; i <= N; i++) {
				coord[i] = Integer.parseInt(tokens[i - 1]);
			}

			Oras[] orase = regele.creareOrase(coord, N);
			int[] biggestActivation = regele.calculateBiggestActivation(orase, N, coord);
			line = br.readLine();
			int Q = Integer.parseInt(line);

			for (int i = 0; i < Q; i++) {
				line = br.readLine();
				question = Integer.parseInt(line);

				fileWriter.write(regele.answerQuestion(question, biggestActivation, coord, N)
						+ "\n");
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Nu s-a putut deschide fisierul");
		}
	}
}
