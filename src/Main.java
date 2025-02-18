import org.json.JSONObject;
import org.json.JSONArray;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
public class Main {

    public enum Konfrontationstyp {
        Individuell, Team, Galaktisch, Multiversal
    }
    public static class Event{
        private int id;
        private String Held;
        private String Antagonist;
        private Konfrontationstyp Konfrontationstyp;
        private String Ort;
        private LocalDate Datum;
        private double GlobalerEinfluss;
    //getters and setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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
    public static List<Event> readEvents(String filePath) throws Exception {
        String content = Files.readString(Paths.get(filePath));
        JSONArray jsonArray = new JSONArray(content);
        List<Event> events = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Event event = new Event();
            event.setId(jsonObject.getInt("id"));
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

    

}
