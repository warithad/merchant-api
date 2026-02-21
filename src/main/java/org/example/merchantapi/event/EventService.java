package org.example.merchantapi.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Map<String, Object>> getFailureRates() {
        List<Event> events = eventRepository.findAll()
                .stream()
                .filter(e -> e.getStatus() == StatusType.SUCCESS ||e.getStatus() == StatusType.FAILED)
                .toList();

        Map<ProductType, long[]> counts = new HashMap<>();

        for (Event event: events) {
            ProductType product = event.getProduct();
            counts.putIfAbsent(product, new long[]{0, 0});
            long[] tally = counts.get(product);

            if(event.getStatus() == StatusType.FAILED) {
                tally[0]++;
            }
            tally[1]++; // total count of failed + success
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<ProductType, long[]> entry: counts.entrySet()) {
            ProductType product = entry.getKey();
            long[] tally = entry.getValue();

            double rate = (tally[1] == 0) ? 0.0 : (tally[0] * 100.0 / tally[1]);
            rate = Math.round(rate * 100.0 / 100.0);

            Map<String, Object> map = new HashMap<>();
            map.put("product", product.name());
            map.put("failure_rate", rate);
            result.add(map);
        }
        result.sort((a, b) -> Double.compare((double) b.get("failure_rate"), (double) a.get("failure_rate")));
        return result;
    }

    public Map<String, Object> getTopMerchant() {
        List<Event> successEvents  = eventRepository.findAll()
                .stream()
                .filter(e -> e.getStatus() == StatusType.SUCCESS)
                .toList();

        Map<String, Double> merchantTotals = new HashMap<>();
        for (Event event: successEvents) {
            String merchantId = event.getMerchantId();
            double amount = event.getAmount() != null ? event.getAmount().doubleValue() : 0.0;
            merchantTotals.merge(merchantId, amount, Double::sum);
        }

        Map.Entry<String, Double> topEntry = merchantTotals.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        if (topEntry == null) return Collections.emptyMap();
        Map<String, Object> result = new HashMap<>();
        result.put("merchant_id", topEntry.getKey());
        result.put("total_volume", topEntry.getValue());

        return result;
    }


    public Map<String, Long> getMonthlyActiveMerchants() {
        List<Event> events = eventRepository.findByStatus(StatusType.SUCCESS);
        Map<String, Set<String>> monthlyMerchants = new HashMap<>();

        for (Event event : events) {
            String yearMonth = String.format("%d-%02d",
                    event.getTimeStamp().getYear(),
                    event.getTimeStamp().getMonthValue());

            monthlyMerchants
                    .computeIfAbsent(yearMonth, k -> new HashSet<>())
                    .add(event.getMerchantId());
        }

        Map<String, Long> result = new TreeMap<>();
        monthlyMerchants.forEach((month, merchants) ->
                result.put(month, (long) merchants.size())
        );

        return result;
    }

    public Map<String, Long> getProductAdoption() {
        List<Event> events = eventRepository.findAll();

        // Map<Product, Set of unique merchant IDs>
        Map<ProductType, Set<String>> productMerchants = new HashMap<>();

        for (Event event : events) {
            ProductType product = event.getProduct();
            productMerchants
                    .computeIfAbsent(product, k -> new HashSet<>())
                    .add(event.getMerchantId()); // Set ensures uniqueness
        }

        // Convert to Map<Product name, count of unique merchants>
        Map<String, Long> adoptionCounts = new HashMap<>();
        for (Map.Entry<ProductType, Set<String>> entry : productMerchants.entrySet()) {
            adoptionCounts.put(entry.getKey().name(), (long) entry.getValue().size());
        }

        // Count by desc
        return adoptionCounts.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(
                        LinkedHashMap::new,
                        (m, e) -> m.put(e.getKey(), e.getValue()),
                        Map::putAll
                );
        }
}
