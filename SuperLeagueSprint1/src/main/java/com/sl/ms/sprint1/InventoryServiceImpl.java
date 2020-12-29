package com.sl.ms.sprint1;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl implements InventoryService {

	@Autowired
	private ReportReader reportReader;

	@Autowired
	private CacheManager cacheService;

	@Override
	public void generateSummeryRport() {
		reportReader.processReports(cacheService);
		populateReportDetails(cacheService);

	}

	private void populateReportDetails(CacheManager cacheService) {

		if (!cacheService.getList().isEmpty()) {
			System.out.println("============================================================");
			System.out.println("                      Stock Summary Per Day                 ");
			System.out.println("============================================================");
			System.out.println("    ReportDate    ProductId    ProductName    Price    Quantity");

			Map<LocalDate, List<StockDo>> list = cacheService.getList().stream()
					.collect(Collectors.groupingBy(StockDo::getReportDate));
			list.entrySet().forEach(i -> {
				System.out.println("-----" + i.getKey() + "-------");
				i.getValue().forEach(a -> {
					System.out.println(a.toString());
				});
			});
		}
		System.out.println("===============================================================");
		System.out.println("                      Sales leader board                       ");
		System.out.println("===============================================================");
		List<StockDo> list = cacheService.getList().stream()
				.sorted(Comparator.comparing(StockDo::getReportDate).thenComparing(StockDo::getQuantity))
				.collect(Collectors.toList());
		list.stream().limit(5).forEach(x -> {
			System.out.println(x.toString());
		});

		Long itemsSoldPerDay = cacheService.getList().stream().filter(i -> i.getReportDate().equals(LocalDate.now()))
				.collect(Collectors.summingLong(i -> i.getQuantity()));
		System.out.println("===============================================================");
		System.out.println("             Summary of total items sold per day               ");
		System.out.println("===============================================================");
		System.out.println(LocalDate.now() + "===> " + itemsSoldPerDay);

		Map<Object, Long> monthList = cacheService.getList().stream()
				.collect(Collectors.groupingBy(i -> i.getReportDate(), Collectors.summingLong(i -> i.getQuantity())));
		System.out.println("===============================================================");
		System.out.println("            Summary of total items sold per  month             ");
		System.out.println("===============================================================");
		monthList.entrySet()
				.forEach(i -> System.out.print(i.getKey() + "   " + i.getValue() + System.lineSeparator()));

		Map<Object, Long> particularItemSale = cacheService.getList().stream()
				.collect(Collectors.groupingBy(i -> i.getProductName(), Collectors.summingLong(i -> i.getQuantity())));
		System.out.println("===============================================================");
		System.out.println("     Summary of quantity of sale for one particular item       ");
		System.out.println("===============================================================");
		particularItemSale.entrySet()
				.forEach(i -> System.out.print(i.getKey() + "    " + i.getValue() + System.lineSeparator()));
	}

}
