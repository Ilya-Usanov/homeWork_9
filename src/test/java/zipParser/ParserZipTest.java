package zipParser;

import com.codeborne.pdftest.PDF;
import com.opencsv.CSVReader;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ParserZipTest {

    ClassLoader classLoader = ParserZipTest.class.getClassLoader();
    String zipName = "resources.zip";
    String zipPath = "src/test/resources/";
    String xlsFileName = "example.xls";
    String pdfFileName = "example.pdf";
    String csvFileName = "example.csv";

    @Test
    void zipXlsFileTest() throws Exception {
        InputStream is = classLoader.getResourceAsStream(zipName);
        assert is != null;
        ZipInputStream zip = new ZipInputStream(is);
        ZipFile zfile = new ZipFile(new File(zipPath + zipName));
        ZipEntry entry;
        while ((entry = zip.getNextEntry()) != null) {
            if (entry.getName().equals(xlsFileName)) {
                try (InputStream stream = zfile.getInputStream(entry)) {
                    HSSFWorkbook myExcelBook = new HSSFWorkbook(stream);
                    assertThat(
                            myExcelBook.getSheetAt(0)
                                    .getRow(9)
                                    .getCell(1)
                                    .getStringCellValue()
                    ).contains("Vincenza");
                }
            }

        }
        is.close();
        zip.close();
    }

    @Test
    void zipPdfTest() throws Exception {
        InputStream is = classLoader.getResourceAsStream(zipName);
        assert is != null;
        ZipInputStream zis = new ZipInputStream(is);
        ZipFile zfile = new ZipFile(new File(zipPath + zipName));
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            if (entry.getName().equals(pdfFileName)) {
                try (InputStream stream = zfile.getInputStream(entry)) {
                    PDF pdf = new PDF(stream);
                    assertThat(pdf.text).contains("And more text. And more text. And more text.");
                }
            }

        }
        is.close();
        zis.close();
    }

    @Test
    void zipCsvTest() throws Exception {
        InputStream is = classLoader.getResourceAsStream(zipName);
        assert is != null;
        ZipInputStream zis = new ZipInputStream(is);
        ZipFile zfile = new ZipFile(new File(zipPath + zipName));
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            if (entry.getName().equals(csvFileName)) {
                try (InputStream stream = zfile.getInputStream(entry);
                     CSVReader reader = new CSVReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                    List<String[]> csv = reader.readAll();
                    assertThat(csv).contains(
                            new String[]{"Ford", "Hatchback", "Diesel", "Red", "2.0"},
                            new String[]{"BMW", "Coupe", "Diesel", "Black", "2.0"},
                            new String[]{"Mersedes", "Sedan", "Petrol", "Green", "1.8"},
                            new String[]{"Audi", "Wagon", "Petrol", "White", "3.0"}
                    );
                }
            }
        }
        is.close();
        zis.close();
    }
}
