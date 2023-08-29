package utils;

import java.nio.file.Path;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import models.Node;
import models.Street;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ImportData {

    public List<Node> nodesImport() throws IOException {

        String fileName = "src/main/resources/csvFiles/nodes.csv";
        Path myPath = Paths.get(fileName);

        try (BufferedReader br = Files.newBufferedReader(myPath,
                StandardCharsets.UTF_8)) {

            HeaderColumnNameMappingStrategy<Node> mapping
                    = new HeaderColumnNameMappingStrategy<>();

            mapping.setType(Node.class);

            CsvToBean<Node> csvToBean = new CsvToBeanBuilder<Node>(br)
                    .withMappingStrategy(mapping)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<Node> listedItems = csvToBean.parse();


            return listedItems;
        }
    }

    public List<Street> streetsImport() throws IOException {

        String fileName = "src/main/resources/csvFiles/streets.csv";
        Path myPath = Paths.get(fileName);

        try (BufferedReader br = Files.newBufferedReader(myPath,
                StandardCharsets.UTF_8)) {

            HeaderColumnNameMappingStrategy<Street> mapping
                    = new HeaderColumnNameMappingStrategy<>();
            mapping.setType(Street.class);

            CsvToBean<Street> csvToBean = new CsvToBeanBuilder<Street>(br)
                    .withMappingStrategy(mapping)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<Street> listedItems = csvToBean.parse();


            return listedItems;
        }

    }


}
