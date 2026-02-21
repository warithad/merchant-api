package org.example.merchantapi;

import org.example.merchantapi.event.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class CsvDataLoader implements CommandLineRunner {

    private final EventRepository eventRepository;

    public CsvDataLoader(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Path dataFolder = Paths.get("data"); // your CSV folder

        if (!Files.exists(dataFolder)) {
            System.out.println("Data folder not found: " + dataFolder.toAbsolutePath());
            return;
        }

        Files.list(dataFolder)
                .filter(path -> path.toString().endsWith(".csv"))
                .forEach(this::loadCsv);
    }

    private void loadCsv(Path csvPath) {
        try (BufferedReader br = Files.newBufferedReader(csvPath)) {

            String line;
            br.readLine(); // skip header
            List<Event> events = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",", -1); // keep empty strings

                Event event = new Event();

                String id = fields[0].replace("\"", "").trim();
                event.setEventId(!id.isEmpty() ? UUID.fromString(id) : UUID.randomUUID());

                event.setMerchantId(fields[1].replace("\"", "").trim());

                String ts = fields[2].replace("\"", "").trim();
                event.setTimeStamp(LocalDateTime.parse(ts));

                String productStr = fields[3].replace("\"", "").trim();
                event.setProduct(ProductType.valueOf(productStr));

                event.setEventType(fields[4].replace("\"", "").trim());

                String amt = fields[5].replace("\"", "").trim();
                try {
                    event.setAmount(new BigDecimal(amt));
                } catch (NumberFormatException e) {
                    event.setAmount(null); // invalid amount
                }

                //status
                event.setStatus(StatusType.valueOf(fields[6].replace("\"", "").trim()));

                event.setChannel(ChannelType.valueOf(fields[7].replace("\"", "").trim()));

                event.setRegion(fields[8].replace("\"", "").trim());

                String tier = fields[9].replace("\"", "").trim();
                event.setMerchantTier(MerchantTierType.valueOf(tier));

                events.add(event);
            }

            eventRepository.saveAll(events);
            System.out.println("Loaded " + events.size() + " events from " + csvPath.getFileName());

        } catch (Exception e) {
            System.err.println("Error loading CSV " + csvPath.getFileName());
            e.printStackTrace();
        }
    }
}