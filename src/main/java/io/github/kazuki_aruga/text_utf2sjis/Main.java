package io.github.kazuki_aruga.text_utf2sjis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 * UTF-8のテキストをMS932に変換する。<br>
 * ただし、MS932に変換できない文字が含まれる「行」は無視する。
 */
public class Main {

	/**
	 * 処理開始。
	 * 
	 * @param args
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {

		if (args.length < 1) {

			System.out.println("ファイル名を指定してください。");
			System.exit(1);
		}

		final File infile = new File(args[0]);
		if (!infile.exists() || !infile.isFile()) {

			System.out.println("指定されたファイルが見つかりません。" + args[0]);
			System.exit(1);
		}

		final File outfile = new File(createOutFileName(args[0]));

		final CharsetEncoder ce = Charset.forName("MS932").newEncoder();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(infile), "UTF-8"));
				PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outfile), "MS932"))) {

			for (String line = null; (line = reader.readLine()) != null;) {

				line = line.replaceAll("−", "-").replaceAll("〜", "～");

				if (ce.canEncode(line)) {

					writer.println(line);

				} else {

					System.out.println("変換できない文字が含まれています : " + line);
				}
			}
		}
	}

	/**
	 * 入力ファイル名から出力ファイル名を生成する。
	 * 
	 * @param infilename
	 *            入力ファイル名。
	 * @return 出力ファイル名。
	 */
	private static String createOutFileName(String infilename) {

		int extindex = infilename.lastIndexOf('.');
		if (extindex < 0) {

			extindex = infilename.length() - 1;
		}

		final String name = infilename.substring(0, extindex);
		final String ext = infilename.substring(extindex);

		return name + ".sjis" + ext;
	}

}
