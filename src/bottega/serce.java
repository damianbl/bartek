package bottega;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class serce {
    private enum TetnoWartosc {
        PROSTE(), // bez rozróżnienia na płeć i kondycję
        ZLOZONE() // z rozróżnieniem na płeć i kondycję
    }

    private enum GraniceWieku {
        NIEMOWLE_GRANICA(365 * 24 * 60L, TetnoWartosc.PROSTE), // 1 rok

        DIECKO_GRANICA(13 * 365 * 24 * 60L, TetnoWartosc.PROSTE), // 13 lat

        MLODZIEZ_GRANICA(18 * 365 * 24 * 60L, TetnoWartosc.PROSTE), // 18 lat

        GRANICA_25(25 * 365 * 24 * 60L, TetnoWartosc.ZLOZONE), // 25 lat

        GRANICA_35(35 * 365 * 24 * 60L, TetnoWartosc.ZLOZONE), // 35 lat

        GRANICA_45(45 * 365 * 24 * 60L, TetnoWartosc.ZLOZONE), // 45 lat

        GRANICA_55(55 * 365 * 24 * 60L, TetnoWartosc.ZLOZONE), // 55 lat

        GRANICA_65(65 * 365 * 24 * 60L, TetnoWartosc.ZLOZONE), // 65 lat

        GRANICA_MAX(Long.MAX_VALUE, TetnoWartosc.ZLOZONE); // 65 +

        private Long granica;
        private TetnoWartosc typ;

        private GraniceWieku(Long granica, TetnoWartosc typ) {
            this.granica = granica;
            this.typ = typ;
        }

    }

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {

        Map<Long, Long> czestotliwoscNieDorosly = new TreeMap<>();
        czestotliwoscNieDorosly.put(GraniceWieku.NIEMOWLE_GRANICA.granica, 130L);
        czestotliwoscNieDorosly.put(GraniceWieku.DIECKO_GRANICA.granica, 100L);
        czestotliwoscNieDorosly.put(GraniceWieku.MLODZIEZ_GRANICA.granica, 80L);

        Map<String, Integer> kondycjaIndeks = new HashMap<>();
        kondycjaIndeks.put("wyczynowa", 0);
        kondycjaIndeks.put("świetna", 1);
        kondycjaIndeks.put("dobra", 2);
        kondycjaIndeks.put("ponadprzeciętna", 3);
        kondycjaIndeks.put("przeciętna", 4);
        kondycjaIndeks.put("słaba", 5);
        kondycjaIndeks.put("zła", 6);

        Map<Long, Integer> wiekIndeks = new TreeMap<>();
        wiekIndeks.put(GraniceWieku.GRANICA_25.granica, 0);
        wiekIndeks.put(GraniceWieku.GRANICA_35.granica, 1);
        wiekIndeks.put(GraniceWieku.GRANICA_45.granica, 2);
        wiekIndeks.put(GraniceWieku.GRANICA_55.granica, 3);
        wiekIndeks.put(GraniceWieku.GRANICA_65.granica, 4);
        wiekIndeks.put(GraniceWieku.GRANICA_MAX.granica, 5);

        int[][] mezczyzniTetno = new int[][]{
                {55, 54, 56, 57, 56, 55},
                {61, 61, 62, 63, 61, 61},
                {55, 54, 56, 57, 56, 55},
                {55, 54, 56, 57, 56, 55},
                {55, 54, 56, 57, 56, 55},
                {55, 54, 56, 57, 56, 55},
                {55, 54, 56, 57, 56, 55},
        };

        int[][] kobietyTetno = new int[][]{
                {55, 54, 56, 57, 56, 55},
                {61, 61, 62, 63, 61, 61},
                {55, 54, 56, 57, 56, 55},
                {55, 54, 56, 57, 56, 55},
                {55, 54, 56, 57, 56, 55},
                {55, 54, 56, 57, 56, 55},
                {55, 54, 56, 57, 56, 55},
        };

        System.out.println("Podaj swoją datę urodzin w formacie: yyyy-MM-dd HH:mm");
        System.out.println("Przykład " + LocalDateTime.now().format(formatter));
        Scanner skaner = new Scanner(System.in);
        String line = skaner.nextLine();
        LocalDateTime dataUrodzenia = LocalDateTime.parse(line, formatter);

        System.out.println("Jeśli jestes kobieta wpisz kobieta natomiast jeśli jesteś mężczyzną wpisz mezczyzna");
        String plec = skaner.nextLine();

        System.out.println("Jaką masz kondycję: masz do wyboru (wyczynowa, świetna, dobra, ponadprzeciętna, przeciętna, słaba, zła)");
        String kondycja = skaner.nextLine();

        System.out.printf("Twoja data urodzenia to: %s \n", dataUrodzenia.format(formatter));

        System.out.println("Jesteś " + plec + " i masz " + kondycja + " kondycję");

        LocalDateTime now = LocalDateTime.now();
        System.out.println("Aktualna Data " + now.format(formatter));

        long wiekMinuty = ChronoUnit.MINUTES.between(dataUrodzenia, now);

        System.out.println("Przeżyłeś " + wiekMinuty + " minut");

        Long poprzendniaGranica = 0L;
        Long sumaUderzen = 0L;

        for (GraniceWieku granica : GraniceWieku.values()) {
            if (granica.granica < wiekMinuty) {
                if (TetnoWartosc.PROSTE.equals(granica.typ)) {
                    sumaUderzen += (granica.granica - poprzendniaGranica) * czestotliwoscNieDorosly.get(granica.granica);
                } else {
                    int kondycjaIdx = kondycjaIndeks.get(kondycja);
                    int wiekIdx = wiekIndeks.get(granica.granica);

                    if ("kobieta".equals(plec)) {
                        sumaUderzen += (granica.granica - poprzendniaGranica) * kobietyTetno[kondycjaIdx][wiekIdx];
                    } else {
                        sumaUderzen += (granica.granica - poprzendniaGranica) * mezczyzniTetno[kondycjaIdx][wiekIdx];
                    }
                }
                poprzendniaGranica = granica.granica;
            } else {
                if (TetnoWartosc.PROSTE.equals(granica.typ)) {
                    sumaUderzen += (wiekMinuty - poprzendniaGranica) * czestotliwoscNieDorosly.get(granica.granica);
                } else {
                    int kondycjaIdx = kondycjaIndeks.get(kondycja);
                    int wiekIdx = wiekIndeks.get(granica.granica);

                    if ("kobieta".equals(plec)) {
                        sumaUderzen += (wiekMinuty - poprzendniaGranica) * kobietyTetno[kondycjaIdx][wiekIdx];
                    } else {
                        sumaUderzen += (wiekMinuty - poprzendniaGranica) * mezczyzniTetno[kondycjaIdx][wiekIdx];
                    }
                }
            }
        }

        System.out.println("Suma uderzeń wynosi: " + sumaUderzen);
    }
}
