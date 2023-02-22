import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Statistics {

	int solveStatistics(int N, ArrayList<String> cuvinte) {
		int nrMaxDeCuv = 0, nrCuvPtLitera, litereInPlus, indexCurent;
		int[] litereUtilePerCuvant = new int[N];

		/* Verificam pentru fiecare litera cate cuvinte pot fi concatenate pentru ca aceasta
		 * sa apara de mai multe ori decat jumatate din lungimea sirului obtinut.
		 */
		for (char c = 'a'; c <= 'z'; c++) {
			nrCuvPtLitera = 0;
			for (int i = 0; i < N; i++) {
				String cuv = cuvinte.get(i);
				litereUtilePerCuvant[i] = 0;

				// Numaram de cate ori apare litera c in cuvantul curent.
				for (int j = 0; j < cuv.length(); j++) {
					if (cuv.charAt(j) == c) {
						litereUtilePerCuvant[i]++;
					}
				}

				/*
				 * Dupa ce stim numarul de aparitii al unei litere in cuvant, vrem sa
				 * vedem diferenta dintre numarul acesta si numarul de aparitii al celorlalte
				 * litere.
				 */
				litereUtilePerCuvant[i] -= cuv.length() - litereUtilePerCuvant[i];
			}

			// Sortam crescator dupa diferenta dintre numerele literelor.
			Arrays.sort(litereUtilePerCuvant);
			litereInPlus = 0;
			indexCurent = N - 1;

			/*
			 * Luam de la coada la cap sirul, adica pornind de la cea mai mare diferenta.
			 * Adunam aceste diferente atata timp cat numarul este mai mare ca 0. Daca ajunge
			 * sa fie 0 sau mai mic, inseamna ca sunt cel putin la fel de multe litere diferite
			 * de c in concatenare. Cum conditia era ca numarul de c-uri sa fie strict mai mare
			 * decat jumatate din lungime, atunci ne oprim.
			 */
			while (indexCurent >= 0) {
				if (litereInPlus + litereUtilePerCuvant[indexCurent] > 0) {
					litereInPlus += litereUtilePerCuvant[indexCurent];
					indexCurent--;
					nrCuvPtLitera++;
				} else {
					break;
				}
			}

			if (nrCuvPtLitera > nrMaxDeCuv) {
				nrMaxDeCuv = nrCuvPtLitera;
			}
		}

		// Daca nu se poate alege niciun cuvant intoarcem -1.
		if (nrMaxDeCuv == 0) {
			nrMaxDeCuv = -1;
		}

		return nrMaxDeCuv;
	}

	public static void main(String[] args) {
		int N;
		ArrayList<String> cuvinte = new ArrayList<>();
		Statistics statistics = new Statistics();

		try (BufferedReader br = new BufferedReader(new FileReader("statistics.in"));
				FileWriter fw = new FileWriter("statistics.out")) {
			String line = br.readLine();
			N = Integer.parseInt(line);

			for (int i = 0; i < N; i++) {
				line = br.readLine();
				cuvinte.add(line);
			}

			fw.write(statistics.solveStatistics(N, cuvinte) + "\n");
		} catch (IOException e) {
			System.out.println("Fisierul cautat nu exista.");
			e.printStackTrace();
		}
	}
}
