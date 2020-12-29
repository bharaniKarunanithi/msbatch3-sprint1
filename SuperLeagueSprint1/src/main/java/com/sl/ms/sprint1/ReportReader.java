package com.sl.ms.sprint1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ReportReader {

	@Value("${REPORT_DIRC}")
	private String path;

	public void processReports(CacheManager cacheService) {

		try (Stream<Path> walk = Files.walk(Paths.get(path))) {

			List<String> result = walk.map(x -> x.toString()).filter(f -> f.endsWith(".csv"))
					.collect(Collectors.toList());

			
			for (String name : result) {
				// printing the name of file in every sub folder
				System.out.println(name);
				String[] split = name.split("_");
				String date = split[1];
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");

				// convert String to LocalDate
				LocalDate localDate = LocalDate.parse(date.substring(0, 8), formatter);
				InputStream inputFS = new FileInputStream(name);

				BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
				List<StockDo> list = new ArrayList();

				list = br.lines().skip(1).map(domainMap).collect(Collectors.toList());
				list.stream().forEach(u -> u.setReportDate(localDate));

				cacheService.getList().addAll(list);
				br.close();
			}

		} catch (IOException e) {
			System.out.println("Place the reports in the path C:\\\\Users\\\\HP\\\\Desktop\\\\Reports");
		}

	}

	private Function<String, StockDo> domainMap = (line) -> {
		StockDo domain = new StockDo();

		String[] records = line.split(",");
		if (records[0] != null)
			domain.setProductID(records[0]);

		if (records[1] != null)
			domain.setProductName(records[1]);

		if (records[2] != null)
			domain.setPrice(Long.parseLong(records[2]));

		if (records[3] != null) {
			domain.setQuantity(Long.parseLong(records[3]));
		}
		;
		return domain;
	};
}