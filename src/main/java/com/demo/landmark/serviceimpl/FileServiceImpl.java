package com.demo.landmark.serviceimpl;

import com.demo.landmark.Model.Result;
import com.demo.landmark.Model.Sample;
import com.demo.landmark.dto.FileDTO;
import com.demo.landmark.dto.SampleResultGrouping;
import com.demo.landmark.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {


    @Autowired
    private PDFParserService pdfParserService;

    @Override
    public boolean readFileInfo(FileDTO fileDTO) {

        if ((fileDTO.getFilePath() != null) && pdfParserService.readPDFFile(fileDTO.getFilePath())) {
            List<String> lines = PDFParserService.lines;
            printLines(lines);
            List<SampleResultGrouping> list = new ArrayList<>();
            SampleResultGrouping sampleResultGrouping = null;
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).equals("LAB REF")) {
                    sampleResultGrouping = new SampleResultGrouping();
                    sampleResultGrouping.setStart(i + 1);

                } else if (lines.get(i).isBlank()) {
                    sampleResultGrouping.setEnd(i - 1);
                    list.add(sampleResultGrouping);
                    sampleResultGrouping = new SampleResultGrouping();
                    sampleResultGrouping.setStart(i + 1);
                }
            }
            List<Sample> samples = getSamples(list, lines);


        }

        return false;
    }

    /*private List<Result> getResults(List<SampleResultGrouping> list, List<String> lines) {
    }*/

    private void printLines(List<String> lines) {
        for (String line : lines) {
            System.out.println(line);
        }
    }

    private List<Sample> getSamples(List<SampleResultGrouping> sampleResultGroupingList, List<String> lines) {
        List<Sample> samples = new ArrayList<>();
        int start, end, i;
        Sample sample;
        String value, matrixType;
        String[] words;
        for (SampleResultGrouping sampleResult : sampleResultGroupingList) {
            sample = new Sample();
            start = sampleResult.getStart();
            end = sampleResult.getEnd();
            i = 0;
            while (start <= end) {
                if (i == 0) {
                    value = lines.get(start).trim().replaceAll(" +"," ");
                    if (value.endsWith("Water")) {
                        int index = value.lastIndexOf(" ", value.lastIndexOf(" ") - 1);
                        sample.setDescription(value.substring(0, index));
                        words = value.split(" ");
                        if (words.length >= 2) {
                            matrixType = words[words.length - 2] + " " + words[words.length - 1];
                            sample.setMatrixType(matrixType);
                            i++;
                        }
                    } else {
                        sample.setDescription(value);
                    }

                } else if (i == 1) {
                    sample.setMatrixType(lines.get(start));
                } else if (i == 2) {
                    sample.setDate(lines.get(start));
                } else if (i == 3) {
                    sample.setTime(lines.get(start));
                } else if (i == 4) {
                    sample.setLabRef(lines.get(start));
                    value = lines.get(start+1);
                    if (!value.contains("Legionella")) {
                        sample.setDescription(sample.getDescription()+" "+value.trim());
                    }
                    break;
                }
                i++;
                start++;
            }
        }
        return samples;
    }
}
