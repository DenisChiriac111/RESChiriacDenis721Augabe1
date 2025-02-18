import org.json.JSONObject;
import org.json.JSONArray;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.Scanner;
import java.util.stream.Collectors;



public class Main {

    public enum Konfrontationstyp {
        Individuell, Team, Galaktisch, Multiversal
    }
    public static class Event{
        private int Id;
        private String Held;
        private String Antagonist;
        private Konfrontationstyp Konfrontationstyp;
        private String Ort;
        private LocalDate Datum;
        private double GlobalerEinfluss;
    //getters and setters
        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getHeld() {
            return Held;
        }

        public void setHeld(String held) {
            Held = held;
        }

        public String getAntagonist() {
            return Antagonist;
        }

        public void setAntagonist(String antagonist) {
            Antagonist = antagonist;
        }

        public Konfrontationstyp getKonfrontationstyp() {
            return Konfrontationstyp;
        }

        public void setKonfrontationstyp(Konfrontationstyp konfrontationstyp) {
            Konfrontationstyp = konfrontationstyp;
        }

        public String getOrt() {
            return Ort;
        }

        public void setOrt(String ort) {
            Ort = ort;
        }

        public LocalDate getDatum() {
            return Datum;
        }

        public void setDatum(LocalDate datum) {
            Datum = datum;
        }

        public double getGlobalerEinfluss() {
            return GlobalerEinfluss;
        }

        public void setGlobalerEinfluss(double globalerEinfluss) {
            GlobalerEinfluss = globalerEinfluss;
        }
    }
    /**
     * Reads events from a JSON file.
     *
     * @param filePath the path to the JSON file
     * @return a list of events
     * @throws Exception if an error occurs while reading the file
     */
    public static List<Event> readEvents(String filePath) throws Exception {
        String content = Files.readString(Paths.get(filePath));
        JSONArray jsonArray = new JSONArray(content);
        List<Event> events = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Event event = new Event();
            event.setId(jsonObject.getInt("Id"));
            event.setHeld(jsonObject.getString("Held"));
            event.setAntagonist(jsonObject.getString("Antagonist"));
            event.setKonfrontationstyp(Konfrontationstyp.valueOf(jsonObject.getString("Konfrontationstyp")));
            event.setOrt(jsonObject.getString("Ort"));
            event.setDatum(LocalDate.parse(jsonObject.getString("Datum")));
            event.setGlobalerEinfluss(jsonObject.getDouble("GlobalerEinfluss"));
            events.add(event);
        }
        return events;
    }


    /**
     * Writes events to a JSON file.
     *
     * @param events   the list of events
     * @param threshold the path to the JSON file
     * @throws IOException if an error occurs while writing the file
     */
    public static void displayHeroesWithHigherGlobalInfluence(List<Event> events, double threshold) {
        Set<String> heroes = events.stream()
                .filter(event -> event.getGlobalerEinfluss() > threshold)
                .map(Event::getHeld)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        System.out.println("Heroes with higher global influence than " + threshold + ":");
        heroes.forEach(System.out::println);
    }

    public static void displayGalacticEvents(List<Event> events) {
        events.stream()
                .filter(event -> event.getKonfrontationstyp() == Konfrontationstyp.Galaktisch)
                .sorted(Comparator.comparing(Event::getDatum).reversed())
                .forEach(event -> System.out.println(event.getDatum() + ": " + event.getHeld() + " vs. " + event.getAntagonist() + " - " + event.getOrt()));
    }

    public static void saveEventCounts(List<Event> events, String filePath) throws IOException {
        Map<Konfrontationstyp, Long> eventCounts = events.stream()
                .collect(Collectors.groupingBy(Event::getKonfrontationstyp, TreeMap::new, Collectors.counting()));
        Map<Konfrontationstyp, Double> totalImpact = events.stream()
                .collect(Collectors.groupingBy(Event::getKonfrontationstyp, TreeMap::new, Collectors.summingDouble(Event::getGlobalerEinfluss)));
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            eventCounts.entrySet().stream()
                    .sorted(Map.Entry.<Konfrontationstyp, Long>comparingByValue().reversed()
                            .thenComparing(Map.Entry.comparingByKey()))
                    .forEach(entry -> {
                        Konfrontationstyp konfrontationstyp = entry.getKey();
                        long count = entry.getValue();
                        double impact = totalImpact.get(konfrontationstyp);
                        try {
                            writer.write(konfrontationstyp + "&" + count + "$" + impact);
                            writer.newLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the threshold for global influence: ");
        double threshold = scanner.nextDouble();
        scanner.nextLine(); // consume the newline character
        String jsonFilePath = "marvel_konfrontationen.json";
        System.out.print("Enter the path to save the event counts: ");
        String eventCountsFilePath = "ergebnis_counts.txt";

        try {
            List<Event> events = readEvents(jsonFilePath);
            displayHeroesWithHigherGlobalInfluence(events, threshold);
            displayGalacticEvents(events);
            saveEventCounts(events, eventCountsFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


