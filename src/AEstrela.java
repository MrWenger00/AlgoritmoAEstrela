import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AEstrela {

	/**
	 * @param args
	 *            the command line arguments
	 */
	static No[][] matriz;
	static int linhas;
	static int colunas;

	public static void main(String[] args) {

		String linhaArquivo;
		String caminhoArquivoEntrada = "entrada.txt";
		File arquivoEntrada = new File(caminhoArquivoEntrada);

		try {

			BufferedReader br = new BufferedReader(new FileReader(arquivoEntrada));
			linhaArquivo = br.readLine();
			linhaArquivo = linhaArquivo.trim();

			String[] d = linhaArquivo.split("x");
			matriz = new No[Integer.parseInt(d[0])][Integer.parseInt(d[1])];
			while (br.ready()) {

				linhaArquivo = br.readLine();
				linhaArquivo = linhaArquivo.trim();
				String[] aux = linhaArquivo.split(";");

				No no = new No();
				no.lin = Integer.parseInt(aux[0]);
				no.col = Integer.parseInt(aux[1]);
				no.valor = Integer.parseInt(aux[2]);

				if (aux.length == 4) {
					if (aux[aux.length - 1].equals("o")) {
						no.origem = true;
						no.destino = false;
					} else if (aux[aux.length - 1].equals("d")) {
						no.origem = false;
						no.destino = true;
					} else {
						no.origem = false;
						no.destino = false;
					}
				} else {
					no.origem = false;
					no.destino = false;
				}

				matriz[Integer.parseInt(aux[0])][Integer.parseInt(aux[1])] = no;
			}
			
			linhas = Integer.parseInt(d[0]);
			colunas = Integer.parseInt(d[1]);
			String coordenadas = encontrarOrigemDestino(matriz, linhas, colunas);
			String[] od = coordenadas.split(";");
			String[] origem = od[0].split(",");
			String[] destino = od[1].split(",");
			int oi = Integer.parseInt(origem[0]);
			int oj = Integer.parseInt(origem[1]);
			int di = Integer.parseInt(destino[0]);
			int dj = Integer.parseInt(destino[1]);			
			
			
			System.out.println("Matriz Original:");
			
			for (int i = 0; i < linhas; i++) {
				for (int j = 0; j < colunas; j++) {
					System.out.print(matriz[i][j].valor+" ");
				}
				System.out.println();
			}
			
			System.out.println();
			System.out.println("Matriz Percorrida");
			
			for (int i = 0; i < linhas; i++) {
				for (int j = 0; j < colunas; j++) {
					System.out.print(matriz[i][j].valor+" ");
				}
				System.out.println();
			}
			
			System.out.println();
			System.out.println("Caminho");
			System.out.println(aEstrela(matriz, oi, oj, di, dj));

			br.close();
		} catch (FileNotFoundException ex) {
			Logger.getLogger(AEstrela.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(AEstrela.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	public static String aEstrela(No[][] m, int oi, int oj, int di, int dj) {

		boolean caminhoEncontrado = false;
		definirDistancia(di, dj);
		No[] vizinhos = verificarVizinhos(oi, oj);
		vizinhos = ordenar(vizinhos);
		String lista = "";
		matriz[oi][oj].valor = 4;
		matriz[vizinhos[0].lin][vizinhos[0].col].valor = 4;

		while (!caminhoEncontrado) {
			lista += vizinhos[0].lin + "-" + vizinhos[0].col + ";";
			vizinhos = verificarVizinhos(vizinhos[0].lin, vizinhos[0].col);
			vizinhos = ordenar(vizinhos);
			matriz[vizinhos[0].lin][vizinhos[0].col].valor = 4; 

			if ((vizinhos[0].lin == di) && (vizinhos[0].col == dj)) {
				caminhoEncontrado = true;
				lista += vizinhos[0].lin + "-" + vizinhos[0].col + ";";
			}

		}

		return lista;

	}

	public static No[] verificarVizinhos(int oi, int oj) {

		No[] v = new No[8];
		No[] vizinhos;
		int cont = 0;

		for (int i = 0; i < linhas; i++) {
			for (int j = 0; j < colunas; j++) {
				if ((i == oi - 1) || (i == oi + 1)) {
					if (((j == oj - 1) || (j == oj + 1)) && (matriz[i][j].valor == 0)) {
						v[cont] = matriz[i][j];
						cont++;
					} else if ((j == oj) && (matriz[i][j].valor == 0)) {
						v[cont] = matriz[i][j];
						cont++;
					}
				} else if (((i == oi) && ((j == oj - 1) || (j == oj + 1))) && (matriz[i][j].valor == 0)) {
					v[cont] = matriz[i][j];
					cont++;
				}
			}

		}

		vizinhos = new No[cont];
		for (int i = 0; i < vizinhos.length; i++) {
			vizinhos[i] = v[i];
		}

		return vizinhos;

	}

	public static void definirDistancia(int di, int dj) {
		for (int i = 0; i < linhas; i++) {
			for (int j = 0; j < colunas; j++) {
				matriz[i][j].distancia = calcularDistancia(i, j, di, dj);

			}
		}
	}

	public static int calcularDistancia(int oi, int oj, int di, int dj) {
		int d = 0;
		int i = oi;
		int j = oj;
		boolean chegou = false;

		while (!chegou) {
			if (i == di && j != dj) {
				if (j < dj) {
					j++;
					d++;
				} else if (j > dj) {
					j--;
					d++;
				}
			} else if (j == dj && i != di) {
				if (i < di) {
					i++;
					d++;
				} else if (i > di) {
					i--;
					d++;
				}
			} else {
				if (i < di) {
					d++;
					i++;
					if (j < dj) {
						d++;
						j++;
					} else if (j > dj) {
						d++;
						j--;
					}
				} else if (i > di) {
					d++;
					i--;
					if (j > dj) {
						d++;
						j--;
					} else if (j < dj) {
						d++;
						j++;
					}
				}
			}
			
			if ((j == dj) && (i == di)) {
				
				chegou = true;
			}
		}
		return d;

	}

	public static String encontrarOrigemDestino(No[][] m, int l, int c) {
		String origem = "";
		String destino = "";
		for (int i = 0; i < l; i++) {
			for (int j = 0; j < c; j++) {
				No n = m[i][j];
				if (n.origem) {
					origem += i + "," + j;
				} else if (n.destino) {
					destino += i + "," + j;
				}
			}
		}

		return origem + ";" + destino;
	}

	public static No[] ordenar(No v[]) {
		int i, j, min;
		No aux;
		for (i = 0; i < (v.length - 1); i++) {
			min = i;
			for (j = (i + 1); j < v.length; j++) {
				if (v[j].distancia < v[min].distancia) {
					min = j;
				}
			}
			if (i != min) {
				aux = v[i];
				v[i] = v[min];
				v[min] = aux;
			}

		}
		return v;
	}
}
