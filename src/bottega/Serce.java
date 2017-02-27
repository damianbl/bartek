package bottega;

import javax.management.RuntimeErrorException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Serce {
    private static final String FORMAT_DATY = "yyyy-MM-dd HH:mm";

    private enum TetnoTyp {
        PROSTE(), // bez rozróżnienia na płeć i kondycję
        ZLOZONE() // z rozróżnieniem na płeć i kondycję
    }

    private enum Plec {
        KOBIETA("kobieta"),
        MEZCZYZNA("mężczyzna");

        private String plec;

        Plec(String plec) {
            this.plec = plec;
        }

        public static Plec fromString(String plec) {
            if (MEZCZYZNA.plec.equals(plec)) {
                return MEZCZYZNA;
            } else if (KOBIETA.plec.equals(plec)) {
                return KOBIETA;
            } else {
                throw new RuntimeException("Nieprawidłowa płeć.");
            }
        }
    }

    private enum Kondycja {
        WYCZYNOWA("wyczynowa"),
        SWIETNA("świetna"),
        DOBRA("dobra"),
        PONADPRZECIETNA("ponadprzeciętna"),
        PRZECIETNA("przeciętna"),
        SLABA("słaba"),
        ZLA("zła");

        private String kondycja;

        Kondycja(String kondycja) {
            this.kondycja = kondycja;
        }

        public static Kondycja fromString(String kondycja) {
            if (WYCZYNOWA.kondycja.equals(kondycja)) {
                return WYCZYNOWA;
            } else if (SWIETNA.kondycja.equals(kondycja)) {
                return SWIETNA;
            } else if (DOBRA.kondycja.equals(kondycja)) {
                return DOBRA;
            } else if (PONADPRZECIETNA.kondycja.equals(kondycja)) {
                return PONADPRZECIETNA;
            } else if (PRZECIETNA.kondycja.equals(kondycja)) {
                return PRZECIETNA;
            } else if (SLABA.kondycja.equals(kondycja)) {
                return SLABA;
            } else if (ZLA.kondycja.equals(kondycja)) {
                return ZLA;
            } else {
                throw new RuntimeException("Nieznana kondycja");
            }
        }
    }

    private enum GraniceWieku {
        NIEMOWLE_GRANICA(365 * 24 * 60L, TetnoTyp.PROSTE), // 1 rok

        DIECKO_GRANICA(13 * 365 * 24 * 60L, TetnoTyp.PROSTE), // 13 lat

        MLODZIEZ_GRANICA(18 * 365 * 24 * 60L, TetnoTyp.PROSTE), // 18 lat

        GRANICA_25(25 * 365 * 24 * 60L, TetnoTyp.ZLOZONE), // 25 lat

        GRANICA_35(35 * 365 * 24 * 60L, TetnoTyp.ZLOZONE), // 35 lat

        GRANICA_45(45 * 365 * 24 * 60L, TetnoTyp.ZLOZONE), // 45 lat

        GRANICA_55(55 * 365 * 24 * 60L, TetnoTyp.ZLOZONE), // 55 lat

        GRANICA_65(65 * 365 * 24 * 60L, TetnoTyp.ZLOZONE), // 65 lat

        GRANICA_MAX(Long.MAX_VALUE, TetnoTyp.ZLOZONE); // 65 +

        private Long granica;
        private TetnoTyp typ;

        GraniceWieku(Long granica, TetnoTyp typ) {
            this.granica = granica;
            this.typ = typ;
        }
    }

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATY);

    public static void main(String[] args) {

        Map<Long, Long> czestotliwoscNieDorosly = new TreeMap<>();
        czestotliwoscNieDorosly.put(GraniceWieku.NIEMOWLE_GRANICA.granica, 130L);
        czestotliwoscNieDorosly.put(GraniceWieku.DIECKO_GRANICA.granica, 100L);
        czestotliwoscNieDorosly.put(GraniceWieku.MLODZIEZ_GRANICA.granica, 80L);

        Map<Kondycja, Integer> kondycjaIndeks = new HashMap<>();
        kondycjaIndeks.put(Kondycja.WYCZYNOWA, 0);
        kondycjaIndeks.put(Kondycja.SWIETNA, 1);
        kondycjaIndeks.put(Kondycja.DOBRA, 2);
        kondycjaIndeks.put(Kondycja.PONADPRZECIETNA, 3);
        kondycjaIndeks.put(Kondycja.PRZECIETNA, 4);
        kondycjaIndeks.put(Kondycja.SLABA, 5);
        kondycjaIndeks.put(Kondycja.ZLA, 6);

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

        System.out.println("Jeśli jestes kobieta wpisz kobieta natomiast jeśli jesteś mężczyzną wpisz mężczyzna");

        Plec plecValue = Plec.fromString(skaner.nextLine());

        System.out.println("Jaką masz kondycję: masz do wyboru (wyczynowa, świetna, dobra, ponadprzeciętna, przeciętna, słaba, zła)");

        Kondycja kondycjaValue = Kondycja.fromString(skaner.nextLine());

        System.out.printf("Twoja data urodzenia to: %s \n", dataUrodzenia.format(formatter));

        System.out.println("Jesteś " + plecValue.plec + " i masz " + kondycjaValue.kondycja + " kondycję");

        LocalDateTime now = LocalDateTime.now();
        System.out.println("Aktualna Data " + now.format(formatter));

        long wiekMinuty = ChronoUnit.MINUTES.between(dataUrodzenia, now);

        System.out.println("Przeżyłeś " + wiekMinuty + " minut");

        Long sumaUderzen = wyliczSumaUderzen(
                czestotliwoscNieDorosly,
                kondycjaIndeks,
                wiekIndeks,
                mezczyzniTetno,
                kobietyTetno,
                plecValue,
                kondycjaValue,
                wiekMinuty
        );

        System.out.println("Suma uderzeń wynosi: " + sumaUderzen / 1000 + " tysięcy");
    }

    private static Long wyliczSumaUderzen(Map<Long, Long> czestotliwoscNieDorosly,
                                          Map<bottega.Serce.Kondycja, Integer> kondycjaIndeks,
                                          Map<Long, Integer> wiekIndeks,
                                          int[][] mezczyzniTetno,
                                          int[][] kobietyTetno,
                                          bottega.Serce.Plec plecValue,
                                          bottega.Serce.Kondycja kondycjaValue,
                                          long wiekMinuty) {
        Long poprzendniaGranica = 0L;
        Long sumaUderzen = 0L;

        for (GraniceWieku granica : GraniceWieku.values()) {
            if (granica.granica < wiekMinuty) {
                if (TetnoTyp.PROSTE.equals(granica.typ)) {
                    sumaUderzen += (granica.granica - poprzendniaGranica) * czestotliwoscNieDorosly.get(granica.granica);
                } else {
                    int kondycjaIdx = kondycjaIndeks.get(kondycjaValue);
                    int wiekIdx = wiekIndeks.get(granica.granica);

                    if (Plec.KOBIETA.equals(plecValue)) {
                        sumaUderzen += (granica.granica - poprzendniaGranica) * kobietyTetno[kondycjaIdx][wiekIdx];
                    } else {
                        sumaUderzen += (granica.granica - poprzendniaGranica) * mezczyzniTetno[kondycjaIdx][wiekIdx];
                    }
                }
                poprzendniaGranica = granica.granica;
            } else {
                if (TetnoTyp.PROSTE.equals(granica.typ)) {
                    sumaUderzen += (wiekMinuty - poprzendniaGranica) * czestotliwoscNieDorosly.get(granica.granica);
                } else {
                    int kondycjaIdx = kondycjaIndeks.get(kondycjaValue);
                    int wiekIdx = wiekIndeks.get(granica.granica);

                    if (Plec.KOBIETA.equals(plecValue)) {
                        sumaUderzen += (wiekMinuty - poprzendniaGranica) * kobietyTetno[kondycjaIdx][wiekIdx];
                    } else {
                        sumaUderzen += (wiekMinuty - poprzendniaGranica) * mezczyzniTetno[kondycjaIdx][wiekIdx];
                    }
                }
            }
        }
        return sumaUderzen;
    }
}
