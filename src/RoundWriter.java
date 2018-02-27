import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Created by nishu on 3/7/2017.
 */
public class RoundWriter {
    public RoundWriter() throws IOException {
        for (int i = 1; i < 18; i++) {
            writeRound(9, i);
        }
    }

    public static void writeRound(int set, int round) throws IOException {
        File file = new File("set" + set + "round" + round + ".pdf");
        PDDocument document = null;
        document = PDDocument.load(file);
        document.getClass();
        StringBuilder sb = new StringBuilder();
        if( !document.isEncrypted() ){
            try {
                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);
                PDFTextStripper Tstripper = new PDFTextStripper();
                String st = Tstripper.getText(document);
                sb.append(st);
            } finally {
                document.close();
            }
        }
        Scanner scan = new Scanner(sb.toString());
        File writeTo = new File("set "+set+"\\Set"+set+"Round"+round+".txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(writeTo));
        String newLine = System.getProperty("line.separator");
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            if (line.contains("___")) {
                line = " ";
            }
            if (!line.contains("Page ") && !line.contains("High School Round ") && !line.contains("ROUND ") && !line.contains("Solution:")) {
                line.trim();
                if (!line.isEmpty() && !line.equals(" ")) {
                    if (line.contains("TOSS-UP")) {
                        bw.write("TOSS UP");
                        bw.write(newLine);
                    } else if (line.contains("Short Answer")) {
                        String[] split = line.split(" ");
                        int i = 1;
                        while (i < split.length) {
                            String curr = split[i];
                            if (curr.trim().length()>0) {
                                if (curr.equals("BIOLOGY") || curr.equals("CHEMISTRY") || curr.equals("PHYSICS") || curr.equals("MATH") || curr.equals("ASTRONOMY")) {
                                    bw.write(curr + newLine);
                                } else if (curr.equals("EARTH") || curr.equals("GENERAL") || curr.equals("COMPUTER")) {
                                    bw.write(curr);
                                    i++;
                                    bw.write(" " + split[i] + newLine);
                                } else if (curr.equals("Short")) {
                                    bw.write(curr.toUpperCase());
                                    i++;
                                    bw.write(" " + split[i].toUpperCase() + newLine);
                                } else {
                                    bw.write(curr + " ");
                                }
                            }
                            i++;
                        }
                        if (line.trim().charAt(line.trim().length()-1) == '?' || line.trim().charAt(line.trim().length()-1) == ':') {
                            bw.write(newLine);
                        }
                    } else if (line.contains("Multiple Choice")) {
                        String[] split = line.split(" ");
                        int i = 1;
                        while (i < split.length) {
                            String curr = split[i];
                            if (curr.trim().length()>0) {
                                if (curr.equals("BIOLOGY") || curr.equals("CHEMISTRY") || curr.equals("PHYSICS") || curr.equals("MATH") || curr.equals("ASTRONOMY") || curr.equals("ENERGY")) {
                                    bw.write(curr + newLine);
                                } else if (curr.equals("EARTH") || curr.equals("GENERAL") || curr.equals("COMPUTER")) {
                                    if (split[i+1].equals("AND")) {
                                        bw.write(curr + " ");
                                        i++;
                                        bw.write(split[i] + " ");
                                        i++;
                                        bw.write(split[i] + newLine);
                                    } else {
                                        bw.write(curr);
                                        i++;
                                        bw.write(" " + split[i] + newLine);
                                    }
                                } else if (curr.equals("Multiple")) {
                                    bw.write(curr.toUpperCase());
                                    i++;
                                    bw.write(" " + split[i].toUpperCase() + newLine);
                                } else {
                                    bw.write(curr + " ");
                                }
                            }
                            i++;
                        }
                        if (line.trim().charAt(line.trim().length()-1) == '?' || line.trim().charAt(line.trim().length()-1) == ':') {
                            bw.write(newLine);
                        }
                    } else if (line.contains("ANSWER:")) {
                        String[] split = line.split(" ");
                        if (line.contains("W)") || line.contains("X)") || line.contains("Y)") || line.contains("Z)")) {
                            boolean letterFound = false;
                            int i = 0;
                            while (!letterFound) {
                                if (split[i].contains("W)") || split[i].contains("X)") || split[i].contains("Y)") || split[i].contains("Z)")) {
                                    String letter = split[i].substring(0, 1).toLowerCase();
                                    bw.write(letter + " OR ");
                                    letterFound = true;
                                }
                                i++;
                            }
                            for (int j = i; j < split.length; j++) {
                                if (split[j].trim().contains("(ACCEPT:") || split[j].trim().equals("or")) {
                                    bw.write("OR ");
                                }
                                bw.write(split[j].toLowerCase() + " ");
                            }
                        } else {
                            for (int i = 1; i < split.length; i++) {
                                if (split[i].trim().length()>0) {
                                    bw.write(split[i].toLowerCase() + " ");
                                }
                            }
                        }
                        bw.write(newLine);
                        bw.write(newLine);
                    } else if (line.trim().length()>0){
                        bw.write(line);
                        bw.write(newLine);
                    } else if (line.contains("_")) {
                        bw.write(newLine);
                    }
                }
            }
        }
        bw.close();
        file.deleteOnExit();
    }
}