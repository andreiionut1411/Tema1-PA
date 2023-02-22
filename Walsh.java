import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Walsh {
	int solveWalsh(int N, int x, int y) {
		if (N == 1) {
			return 0;
		}

		/*
		 * Stim ca N este o putere a lui 2, asa ca daca impartim N la 2, vom obtine
		 * o alta putere, care este, deci, un apel corect al functiei solve_Walsh.
		 * Verificam in ce cadran se afla numarul cautat, translatam apoi x si y, iar
		 * apoi apelam recursiv.
		 */
		if (x <= N / 2 && y <= N / 2) {
			return solveWalsh(N / 2, x, y);
		} else if (x <= N / 2 && y > N / 2) {
			// Translatam din coltul din dreapta sus, in coltul din stanga sus.
			y -= N / 2;
			return solveWalsh(N / 2, x, y);
		} else if (x > N / 2 && y <= N / 2) {
			// Translatam din coltul din stanga jos, in coltul din stanga sus.
			x -= N / 2;
			return solveWalsh(N / 2, x, y);
		} else {
			// Translatam din coltul dreapta jos, in coltul dreapta sus.
			x -= N / 2;
			y -= N / 2;
			int rez_recursiv = solveWalsh(N / 2, x, y);

			// Tinem cont de faptul ca in coltul din dreapta jos se pune negatia lui W n/2.
			if (rez_recursiv == 0) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	public static void main(String[] args) {
		int N, k, x, y;
		Walsh walsh = new Walsh();

		try (BufferedReader bw = new BufferedReader(new FileReader("walsh.in"));
			FileWriter fileWriter = new FileWriter("walsh.out")) {
			String line = bw.readLine();
			String[] tokens = line.split("\\s");
			N = Integer.parseInt(tokens[0]);
			k = Integer.parseInt(tokens[1]);

			for (int i = 0; i < k; i++) {
				line = bw.readLine();
				tokens = line.split("\\s");
				x = Integer.parseInt(tokens[0]);
				y = Integer.parseInt(tokens[1]);
				fileWriter.write(walsh.solveWalsh(N, x, y) + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Nu exista fisierul cautat.");
		}
	}
}
