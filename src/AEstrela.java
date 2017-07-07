
/** @author Guilherme Wenger
 *  @author Guilherme Maeda
 *  @author Flavio Prado
 *  @author Joao Paulo
 *  @author Tharlyson 
 * */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AEstrela {

	static No[][] matriz;
	static int linhas;
	static int colunas;
	static int livres = 0;
	static int ocupados = 0;

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
			System.out.println();
			escreverMatriz(matriz);

			System.out.println();
			System.out.println("Matriz Percorrida");

			System.out.println();
			System.out.println("Caminho");
			System.out.println();
			System.out.println(aEstrela(oi, oj, di, dj));

			br.close();
		} catch (FileNotFoundException ex) {
			Logger.getLogger(AEstrela.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(AEstrela.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	public static String aEstrela(int oi, int oj, int di, int dj) {

		boolean caminhoEncontrado = false;
		boolean destinoBloqueado = false;
		int cont = 0;
		definirDistancia(di, dj);
		No[] vizinhos = verificarVizinhos(oi, oj);
		No[] vizinhosDestino = verificarVizinhos(di, dj);
		String[] visitados = new String[(colunas * linhas) - ocupados];
		String lista = "";

		if (vizinhos.length > 0) {
			vizinhos = ordenar(vizinhos);
			matriz[oi][oj].valor = 4;
			lista += oi + "-" + oj + ";";
			matriz[vizinhos[0].lin][vizinhos[0].col].valor = 4;

		} else {
			destinoBloqueado = true;
			lista = "Não é possível chegar ao destino, caminho bloqueado";
		}

		while (!caminhoEncontrado && !destinoBloqueado) {

			vizinhos = verificarVizinhos(vizinhos[0].lin, vizinhos[0].col);
			vizinhos = ordenar(vizinhos);

			if ((vizinhosDestino.length == 0)) {
				destinoBloqueado = true;
				lista = "Não é possível chegar ao destino, caminho bloqueado";
			} else {
				if ((vizinhos.length == 0)) {

					boolean encerrado = false;
					int i = cont - 1;

					while ((i >= 0)) {

						String[] p = visitados[i].split(";");
						vizinhos = verificarVizinhos(Integer.parseInt(p[0]), Integer.parseInt(p[1]));
						vizinhos = ordenar(vizinhos);

						if (vizinhos.length > 0) {
							i = -1;
						}

						if (i == 0 && vizinhos.length == 0) {
							encerrado = true;
						}
						i--;
					}
					if (encerrado) {

						destinoBloqueado = true;
						lista = "Não é possível chegar ao destino, caminho bloqueado";
					}
				}

				if ((vizinhos[0].lin == di) && (vizinhos[0].col == dj)) {

					caminhoEncontrado = true;
					lista += vizinhos[0].lin + "-" + vizinhos[0].col + ";";
					matriz[vizinhos[0].lin][vizinhos[0].col].valor = 4;

				} else {

					lista += vizinhos[0].lin + "-" + vizinhos[0].col + ";";
					matriz[vizinhos[0].lin][vizinhos[0].col].valor = 4;
					visitados[cont] = vizinhos[0].lin + ";" + vizinhos[0].col;
					cont++;

				}
			}
		}

		escreverMatriz(matriz);
		System.out.println();

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
				} else if ((((j == oj) && ((i == oi - 1) || (i == oi + 1))) && (matriz[i][j].valor == 0))) {
					v[cont] = matriz[i][j];
					cont++;
				}
			}

		}
		vizinhos = new No[cont];
		if (cont > 0) {

			for (int i = 0; i < vizinhos.length; i++) {
				vizinhos[i] = v[i];
			}
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

				if (n.valor == 0) {
					livres++;
				}

				if (n.valor == 1) {
					ocupados++;
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
				if ((v[j].distancia < v[min].distancia)) {
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

	public static void escreverMatriz(No[][] m) {
		for (int i = 0; i < linhas; i++) {
			for (int j = 0; j < colunas; j++) {
				System.out.print(m[i][j].valor + " ");
			}
			System.out.println();
		}
	}
}
