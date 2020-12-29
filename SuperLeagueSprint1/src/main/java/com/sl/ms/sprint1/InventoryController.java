package com.sl.ms.sprint1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("summery")
public class InventoryController {

	@Autowired
private InventoryService inventoryService;
	
	/**
	 * 
	 */
	@GetMapping()
	public void reports() {
		inventoryService.generateSummeryRport();
	}
}
