package au.gestionparcautomobile.aulsh.services.pdfGenerator;

import au.gestionparcautomobile.aulsh.entities.Mission;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class PdfGeneratorServiceImpl implements IPdfGeneratorService {



    public byte[] generateMissionPdf(Mission mission) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        try {
            PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            DeviceRgb headerColor = new DeviceRgb(63, 169, 245);

            // Title
            document.add(new Paragraph("Mission Details")
                    .setFont(font)
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20));

            // Table with 2 columns
            Table table = new Table(new float[]{1, 2});
            table.setWidth(UnitValue.createPercentValue(100));  // Set table width to 100%

            // Adding Header Cells
            table.addHeaderCell(new Cell().add(new Paragraph("Field")).setBackgroundColor(headerColor).setFontColor(new DeviceRgb(255, 255, 255)));
            table.addHeaderCell(new Cell().add(new Paragraph("Value")).setBackgroundColor(headerColor).setFontColor(new DeviceRgb(255, 255, 255)));

            // Adding Data Cells
            table.addCell(new Cell().add(new Paragraph("Reference")));
            table.addCell(new Cell().add(new Paragraph(mission.getReference())));

            table.addCell(new Cell().add(new Paragraph("Responsable")));
            table.addCell(new Cell().add(new Paragraph(mission.getResponsable().getNom() + " " + mission.getResponsable().getPrenom())));

            // Add the table to the document
            document.add(table);

        } catch (Exception e) {
            e.printStackTrace();
        }

        document.close();
        return baos.toByteArray();
    }
}
