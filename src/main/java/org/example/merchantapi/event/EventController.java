package org.example.merchantapi.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/analytics")
public class EventController {

    @Autowired
    private EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }


    @GetMapping("/failure-rates")
    public List<Map<String, Object>> getFailureRates() {
        return eventService.getFailureRates();
    }

    @GetMapping("/top-merchant")
    public Map<String, Object> getTopMerchant(){
        return eventService.getTopMerchant();
    }

    @GetMapping("/monthly-active-merchants")
    public Map<String, Long> getMonthlyActiveMerchants(){
        return eventService.getMonthlyActiveMerchants();
    }

    @GetMapping("/product-adoption")
    public Map<String, Long> getProductAdoption(){
        return eventService.getProductAdoption();
    }
}
