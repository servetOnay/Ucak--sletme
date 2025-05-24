// UcakRezervasyonSistemi.java

import java.util.*;
import java.io.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class Ucak {
    String model;
    String marka;
    String seriNo;
    int koltukKapasitesi;

    public Ucak(String model, String marka, String seriNo, int koltukKapasitesi) {
        this.model = model;
        this.marka = marka;
        this.seriNo = seriNo;
        this.koltukKapasitesi = koltukKapasitesi;
    }

    public String toString() {
        return model + ", " + marka + ", Seri No: " + seriNo + ", Koltuk Kapasitesi: " + koltukKapasitesi;
    }
}

class Lokasyon {
    String ulke;
    String sehir;
    String havaalani;
    boolean aktifMi;

    public Lokasyon(String ulke, String sehir, String havaalani, boolean aktifMi) {
        this.ulke = ulke;
        this.sehir = sehir;
        this.havaalani = havaalani;
        this.aktifMi = aktifMi;
    }

    public String toString() {
        return sehir + ", " + ulke + " - " + havaalani + (aktifMi ? " (Aktif)" : " (Pasif)");
    }
}

class Ucus {
    Lokasyon lokasyon;
    String saat;
    Ucak ucak;
    int kalanKoltuk;
    List<Integer> koltukNumaralari;

    public Ucus(Lokasyon lokasyon, String saat, Ucak ucak) {
        this.lokasyon = lokasyon;
        this.saat = saat;
        this.ucak = ucak;
        this.kalanKoltuk = ucak.koltukKapasitesi;
        this.koltukNumaralari = new ArrayList<>();
        for (int i = 1; i <= ucak.koltukKapasitesi; i++) {
            koltukNumaralari.add(i);
        }
    }

    public String toString() {
        return "Lokasyon: " + lokasyon + ", Saat: " + saat + ", Kalan Koltuk: " + kalanKoltuk;
    }
}

class Rezervasyon {
    Ucus ucus;
    String ad;
    String soyad;
    int yas;
    int koltukNo;

    public Rezervasyon(Ucus ucus, String ad, String soyad, int yas, int koltukNo) {
        this.ucus = ucus;
        this.ad = ad;
        this.soyad = soyad;
        this.yas = yas;
        this.koltukNo = koltukNo;
    }

    public String toString() {
        return "Ad: " + ad + " " + soyad + ", Yas: " + yas + ", Koltuk No: " + koltukNo + ", Ucus: [" + ucus + "]";
    }
}

public class UcakRezervasyonSistemi {
    static List<Ucak> ucaklar = new ArrayList<>();
    static List<Lokasyon> lokasyonlar = new ArrayList<>();
    static List<Ucus> ucuslar = new ArrayList<>();
    static List<Rezervasyon> rezervasyonlar = new ArrayList<>();
    static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        veriOlustur();
        menu(scanner);
    }

    static void menu(Scanner scanner) {
        while (true) {
            System.out.println("\n==== Ucak Rezervasyon Sistemi ====");
            System.out.println("1. Uçuşları Listele");
            System.out.println("2. Rezervasyon Yap");
            System.out.println("3. Uçak Ekle");
            System.out.println("4. Lokasyon Ekle");
            System.out.println("5. Uçuş Ekle");
            System.out.println("6. Rezervasyonları Listele");
            System.out.println("7. Çıkış");
            System.out.print("Seçiminiz: ");

            int secim = scanner.nextInt();
            scanner.nextLine();

            switch (secim) {
                case 1: ucuslariListele(); break;
                case 2: rezervasyonYap(scanner); break;
                case 3: ucakEkle(scanner); break;
                case 4: lokasyonEkle(scanner); break;
                case 5: ucusEkle(scanner); break;
                case 6: rezervasyonlariListele(); break;
                case 7: System.out.println("Çıkılıyor..."); return;
                default: System.out.println("Geçersiz seçim!");
            }
        }
    }

    static void veriOlustur() {
        String[][] ornekLokasyonlar = {
                {"Turkiye", "Istanbul", "IST"}, {"Almanya", "Berlin", "BER"}, {"Fransa", "Paris", "CDG"},
                {"Italya", "Roma", "FCO"}, {"Ispanya", "Madrid", "MAD"}, {"Hollanda", "Amsterdam", "AMS"},
                {"ABD", "New York", "JFK"}, {"Japonya", "Tokyo", "NRT"}, {"Kanada", "Toronto", "YYZ"},
                {"Brezilya", "Sao Paulo", "GRU"}
        };

        for (int i = 0; i < 10; i++) {
            Ucak u = new Ucak("Model" + i, "Marka" + i, "SN" + (1000 + i), 50);
            Lokasyon l = new Lokasyon(ornekLokasyonlar[i][0], ornekLokasyonlar[i][1], ornekLokasyonlar[i][2], true);
            Ucus ucus = new Ucus(l, "0" + (8 + i) + ":00", u);
            ucaklar.add(u);
            lokasyonlar.add(l);
            ucuslar.add(ucus);
            saveJson(u, "ucaklar.json");
            saveJson(l, "lokasyonlar.json");
            saveJson(ucus, "ucuslar.json");
        }

        for (int i = 0; i < 10; i++) {
            Ucus u = ucuslar.get(i);
            int koltukNo = u.koltukNumaralari.remove(0);
            Rezervasyon r = new Rezervasyon(u, "Ad" + i, "Soyad" + i, 20 + i, koltukNo);
            rezervasyonlar.add(r);
            u.kalanKoltuk--;
            saveJson(r, "rezervasyonlar.json");
        }
    }

    static void saveJson(Object obj, String filename) {
        try {
            FileWriter writer = new FileWriter(filename, true);
            gson.toJson(obj, writer);
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("JSON yazma hatası (" + filename + "): " + e.getMessage());
        }
    }

    static void ucuslariListele() {
        for (int i = 0; i < ucuslar.size(); i++) {
            System.out.println(i + ". " + ucuslar.get(i));
        }
    }

    static void rezervasyonYap(Scanner scanner) {
        ucuslariListele();
        System.out.print("Rezervasyon yapmak istediğiniz uçuş numarasını seçin: ");
        int secim = scanner.nextInt();
        scanner.nextLine();

        if (secim < 0 || secim >= ucuslar.size()) {
            System.out.println("Geçersiz uçuş numarası.");
            return;
        }

        Ucus secilenUcus = ucuslar.get(secim);

        if (secilenUcus.kalanKoltuk > 0) {
            System.out.print("Adınız: ");
            String ad = scanner.nextLine();
            System.out.print("Soyadınız: ");
            String soyad = scanner.nextLine();
            System.out.print("Yaşınız: ");
            int yas = scanner.nextInt();
            scanner.nextLine();

            int koltukNo = secilenUcus.koltukNumaralari.remove(0);
            Rezervasyon rezervasyon = new Rezervasyon(secilenUcus, ad, soyad, yas, koltukNo);
            rezervasyonlar.add(rezervasyon);
            secilenUcus.kalanKoltuk--;

            saveJson(rezervasyon, "rezervasyonlar.json");
            System.out.println("Rezervasyon başarıyla oluşturuldu! Koltuk No: " + koltukNo);
        } else {
            System.out.println("Bu uçuşta boş koltuk bulunmamaktadır.");
        }
    }

    static void ucakEkle(Scanner scanner) {
        System.out.print("Model: ");
        String model = scanner.nextLine();
        System.out.print("Marka: ");
        String marka = scanner.nextLine();
        System.out.print("Seri No: ");
        String seriNo = scanner.nextLine();
        System.out.print("Koltuk Kapasitesi: ");
        int kapasite = scanner.nextInt();
        scanner.nextLine();

        Ucak ucak = new Ucak(model, marka, seriNo, kapasite);
        ucaklar.add(ucak);
        saveJson(ucak, "ucaklar.json");
        System.out.println("Uçak başarıyla eklendi.");
    }

    static void lokasyonEkle(Scanner scanner) {
        System.out.print("Ülke: ");
        String ulke = scanner.nextLine();
        System.out.print("Şehir: ");
        String sehir = scanner.nextLine();
        System.out.print("Havaalanı: ");
        String havaalani = scanner.nextLine();
        System.out.print("Aktif mi? (true/false): ");
        boolean aktif = scanner.nextBoolean();
        scanner.nextLine();

        Lokasyon lokasyon = new Lokasyon(ulke, sehir, havaalani, aktif);
        lokasyonlar.add(lokasyon);
        saveJson(lokasyon, "lokasyonlar.json");
        System.out.println("Lokasyon başarıyla eklendi.");
    }

    static void ucusEkle(Scanner scanner) {
        if (ucaklar.isEmpty() || lokasyonlar.isEmpty()) {
            System.out.println("Önce en az bir uçak ve lokasyon tanımlamalısınız.");
            return;
        }
        System.out.println("Mevcut Uçaklar:");
        for (int i = 0; i < ucaklar.size(); i++) {
            System.out.println(i + ". " + ucaklar.get(i));
        }
        System.out.print("Uçak numarası seçin: ");
        int ucakIndex = scanner.nextInt();

        System.out.println("Mevcut Lokasyonlar:");
        for (int i = 0; i < lokasyonlar.size(); i++) {
            System.out.println(i + ". " + lokasyonlar.get(i));
        }
        System.out.print("Lokasyon numarası seçin: ");
        int lokasyonIndex = scanner.nextInt();

        scanner.nextLine();
        System.out.print("Uçuş Saati (örn. 14:30): ");
        String saat = scanner.nextLine();

        Ucus ucus = new Ucus(lokasyonlar.get(lokasyonIndex), saat, ucaklar.get(ucakIndex));
        ucuslar.add(ucus);
        saveJson(ucus, "ucuslar.json");
        System.out.println("Uçuş başarıyla eklendi.");
    }

    static void rezervasyonlariListele() {
        if (rezervasyonlar.isEmpty()) {
            System.out.println("Henüz rezervasyon yapılmadı.");
        } else {
            for (Rezervasyon r : rezervasyonlar) {
                System.out.println(r);
            }
        }
    }
}
